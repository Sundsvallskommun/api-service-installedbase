package se.sundsvall.installedbase.service;

import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toCustomerEngagements;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseCustomer;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseResponse;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.api.model.delegate.FacilityDelegation;
import se.sundsvall.installedbase.integration.datawarehousereader.DataWarehouseReaderClient;
import se.sundsvall.installedbase.integration.db.FacilityDelegationRepository;
import se.sundsvall.installedbase.service.mapper.EntityMapper;
import se.sundsvall.installedbase.service.model.DelegationStatus;

@Service
public class InstalledBaseService {

	private static final int DATAWAREHOUSEREADER_PAGE_LIMIT = 100;
	private static final int DATAWAREHOUSEREADER_PAGE = 1;
	private static final String DATAWAREHOUSEREADER_SORTBY_PROPERTY = "facilityId";

	private final DataWarehouseReaderClient dataWarehouseReaderClient;
	private final FacilityDelegationRepository facilityDelegationRepository;

	public InstalledBaseService(DataWarehouseReaderClient dataWarehouseReaderClient, FacilityDelegationRepository facilityDelegationRepository) {
		this.dataWarehouseReaderClient = dataWarehouseReaderClient;
		this.facilityDelegationRepository = facilityDelegationRepository;
	}

	public InstalledBaseResponse getInstalledBase(String municipalityId, String organizationNumber, List<String> partyIds, LocalDate modifiedFrom) {
		final var customerEngagements = toCustomerEngagements(dataWarehouseReaderClient.getCustomerEngagement(municipalityId, organizationNumber, partyIds));

		return toInstalledBaseResponse(customerEngagements.stream()
			.map(engagement -> toInstalledBaseCustomer(engagement,
				getInstalledBase(municipalityId, engagement.getCustomerNumber(), engagement.getOrganizationName(), modifiedFrom, DATAWAREHOUSEREADER_PAGE, DATAWAREHOUSEREADER_PAGE_LIMIT)))
			.toList());
	}

	private generated.se.sundsvall.datawarehousereader.InstalledBaseResponse getInstalledBase(String municipalityId, String customerNumber, String company, LocalDate modifiedFrom, int page, int limit) {
		final var installedBaseResponse = dataWarehouseReaderClient.getInstalledBase(municipalityId, customerNumber, company, modifiedFrom, page, limit, DATAWAREHOUSEREADER_SORTBY_PROPERTY);

		var currentPage = page;

		if (allNotNull(installedBaseResponse, installedBaseResponse.getMeta(), installedBaseResponse.getMeta().getTotalPages()) && (installedBaseResponse.getMeta().getTotalPages() > currentPage)) {
			while (installedBaseResponse.getMeta().getTotalPages() > currentPage) {
				installedBaseResponse.getInstalledBase().addAll(
					dataWarehouseReaderClient.getInstalledBase(municipalityId, customerNumber, company, modifiedFrom, ++currentPage, limit, DATAWAREHOUSEREADER_SORTBY_PROPERTY).getInstalledBase());
			}
		}
		return installedBaseResponse;
	}

	/**
	 * Create a facility delegation.
	 * Throws a Problem with status 409 Conflict if a delegation already exists.
	 * 
	 * @param  municipalityId     municipalityId
	 * @param  facilityDelegation FacilityDelegation object containing delegation details
	 * @return                    id of the delegation
	 *                            delegation
	 */
	@Transactional
	public String createFacilityDelegation(String municipalityId, FacilityDelegation facilityDelegation) {
		// Check if the owner already has delegated to the same partyId
		if (facilityDelegationRepository.findOne(municipalityId, facilityDelegation.getOwner(), facilityDelegation.getDelegatedTo()).isPresent()) {
			throw Problem.builder()
				.withTitle("Facility delegation already exists")
				.withDetail("Owner with partyId: '" + facilityDelegation.getOwner() + "' has already delegated to partyId: '"
					+ facilityDelegation.getDelegatedTo() + "' for municipality: '" + municipalityId + "'. Update existing delegation instead.")
				.withStatus(Status.CONFLICT)
				.build();
		}

		var entity = facilityDelegationRepository.save(EntityMapper.toEntity(municipalityId, facilityDelegation, DelegationStatus.ACTIVE));

		return entity.getId();
	}

	/**
	 * Get delegation by id.
	 * Throws a Problem with status 404 Not Found if the delegation does not exist.
	 * 
	 * @param  municipalityId       municipalityId
	 * @param  facilityDelegationId id of the delegation
	 * @return                      FacilityDelegation object containing delegation details
	 */
	public FacilityDelegation getFacilityDelegation(String municipalityId, String facilityDelegationId) {
		return facilityDelegationRepository.findOne(municipalityId, facilityDelegationId)
			.map(EntityMapper::toDelegate)
			.orElseThrow(() -> Problem.builder()
				.withDetail("Couldn't find delegation for id: " + facilityDelegationId)
				.withStatus(Status.NOT_FOUND)
				.build());
	}
}
