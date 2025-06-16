package se.sundsvall.installedbase.service;

import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static se.sundsvall.installedbase.Constants.DELEGATES_BY_ID_PATH;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withDelegatedTo;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withId;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withMunicipalityId;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withOwner;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toCustomerEngagements;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseCustomer;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseResponse;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
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
	 * @return                    ResponseEntity with status 201 Created and location header pointing to the created
	 *                            delegation
	 */
	@Transactional
	public ResponseEntity<Void> createFacilityDelegation(String municipalityId, FacilityDelegation facilityDelegation) {
		var spec = withMunicipalityId(municipalityId)
			.and(withOwner(facilityDelegation.getOwner()))
			.and(withDelegatedTo(facilityDelegation.getDelegatedTo()));

		// Check if the owner already has delegated to the same partyId
		if (facilityDelegationRepository.findOne(spec).isPresent()) {
			throw Problem.builder()
				.withTitle("Facility delegation already exists")
				.withDetail("Owner with partyId: '" + facilityDelegation.getOwner() + "' has already delegated to partyId: '"
					+ facilityDelegation.getDelegatedTo() + "' for municipality: '" + municipalityId + "'. Update existing delegation instead.")
				.withStatus(Status.CONFLICT)
				.build();
		}

		var entity = facilityDelegationRepository.save(EntityMapper.toEntity(municipalityId, facilityDelegation, DelegationStatus.ACTIVE));

		var uri = fromPath(DELEGATES_BY_ID_PATH)
			.buildAndExpand(entity.getMunicipalityId(), entity.getId())
			.toUri();

		return created(uri)
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
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
		var spec = withMunicipalityId(municipalityId)
			.and(withId(facilityDelegationId));

		var facilityDelegationEntity = facilityDelegationRepository.findOne(spec);

		return facilityDelegationEntity
			.map(EntityMapper::toDelegate)
			.orElseThrow(() -> Problem.builder()
				.withDetail("Couldn't find delegation for id: " + facilityDelegationId)
				.withStatus(Status.NOT_FOUND)
				.build());
	}
}
