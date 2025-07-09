package se.sundsvall.installedbase.service;

import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withDelegatedTo;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withId;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withMunicipalityId;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withOwner;
import static se.sundsvall.installedbase.service.mapper.EntityMapper.toEntity;
import static se.sundsvall.installedbase.service.mapper.EntityMapper.updateEntityForPutOperation;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toCustomerEngagements;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseCustomer;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseResponse;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.api.model.facilitydelegation.CreateFacilityDelegation;
import se.sundsvall.installedbase.api.model.facilitydelegation.FacilityDelegation;
import se.sundsvall.installedbase.api.model.facilitydelegation.UpdateFacilityDelegation;
import se.sundsvall.installedbase.integration.datawarehousereader.DataWarehouseReaderClient;
import se.sundsvall.installedbase.integration.db.FacilityDelegationRepository;
import se.sundsvall.installedbase.service.mapper.EntityMapper;

@Service
@Transactional
public class InstalledBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InstalledBaseService.class);

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
	 * Create a facility delegation. Throws a Problem with status 409 Conflict if a delegation already exists.
	 *
	 * @param  municipalityId     municipalityId
	 * @param  facilityDelegation FacilityDelegation object containing delegation details
	 * @return                    id of the delegation delegation
	 */
	public String createFacilityDelegation(String municipalityId, CreateFacilityDelegation facilityDelegation) {
		LOGGER.info("Create facility delegation for owner: {}, delegatedTo: {}", facilityDelegation.getOwner(), facilityDelegation.getDelegatedTo());

		// Check if the owner already has a facility delegated to the same partyId
		if (facilityDelegationRepository.findOne(
			withMunicipalityId(municipalityId)
				.and(withOwner(facilityDelegation.getOwner()))
				.and(withDelegatedTo(facilityDelegation.getDelegatedTo())))
			.isPresent()) {
			throw Problem.builder()
				.withTitle("Facility delegation already exists")
				.withDetail("Owner with partyId: '" + facilityDelegation.getOwner() + "' has already delegated to partyId: '"
					+ facilityDelegation.getDelegatedTo() + "' for municipality: '" + municipalityId + "'. Update existing delegation instead.")
				.withStatus(Status.CONFLICT)
				.build();
		}

		var entity = facilityDelegationRepository.save(toEntity(municipalityId, facilityDelegation));

		return entity.getId();
	}

	/**
	 * Get delegation by municipality and facilityDelegationId. Throws a Problem with status 404 Not Found if the
	 * delegation. does not exist.
	 *
	 * @param  municipalityId       municipalityId
	 * @param  facilityDelegationId id of the delegation
	 * @return                      FacilityDelegation object containing delegation details
	 */
	public FacilityDelegation getFacilityDelegation(String municipalityId, String facilityDelegationId) {
		LOGGER.info("Get facility delegation with id: {}", facilityDelegationId);

		return facilityDelegationRepository.findOne(withMunicipalityId(municipalityId)
			.and(withId(facilityDelegationId)))
			.map(EntityMapper::toFacilityDelegation)
			.orElseThrow(() -> Problem.builder()
				.withDetail("Couldn't find delegation for id: " + facilityDelegationId)
				.withStatus(Status.NOT_FOUND)
				.build());
	}

	/**
	 * ' Get facility delegations by owner and/or delegatedTo.
	 *
	 * @param  municipalityId municipalityId
	 * @param  owner          owner of the delegation
	 * @param  delegatedTo    the party to which the facility is delegated
	 * @return                List of FacilityDelegation objects containing delegation details
	 */
	public List<FacilityDelegation> getFacilityDelegations(String municipalityId, String owner, String delegatedTo) {
		LOGGER.info("Get facility delegations for owner: {} and delegatedTo: {}", owner, delegatedTo);
		return facilityDelegationRepository.findAll(
			withMunicipalityId(municipalityId)
				.and(withOwner(owner))
				.and(withDelegatedTo(delegatedTo)))
			.stream()
			.map(EntityMapper::toFacilityDelegation)
			.toList();
	}

	/**
	 * Update a facility delegation. Owner of the delegation must match the one provided in the request otherwise a Problem
	 * with status 400 Bad Request is thrown. If owner needs to be changed, a new delegation should be created instead.
	 *
	 * @param municipalityId       municipalityId
	 * @param facilityDelegationId id of the facility delegation to be updated
	 * @param facilityDelegation   FacilityDelegation object containing delegation details to be updated
	 */
	public void putFacilityDelegation(String municipalityId, String facilityDelegationId, UpdateFacilityDelegation facilityDelegation) {
		LOGGER.info("Updating facility delegation with id: {}", facilityDelegationId);

		// Check that we have an active delegation to update, inactive delegations cannot be updated
		var facilityDelegationEntity = facilityDelegationRepository.findOne(
			withMunicipalityId(municipalityId)
				.and(withId(facilityDelegationId)))
			.orElseThrow(() -> Problem.builder()
				.withTitle("Facility delegation not found")
				.withDetail("Couldn't find any active facility delegations for id: " + facilityDelegationId)
				.withStatus(Status.NOT_FOUND)
				.build());

		// Check that the owner of the delegation is the same as the one provided in the request.
		// Don't want these two in the same problem/error message, hence the separate checks
		if (!facilityDelegationEntity.getOwner().equals(facilityDelegation.getOwner())) {
			throw Problem.builder()
				.withTitle("Invalid delegation owner")
				.withDetail("The owner of the delegation with id: '" + facilityDelegationId + "' is not the same as the one provided in the request.")
				.withStatus(Status.BAD_REQUEST)
				.build();
		}

		updateEntityForPutOperation(facilityDelegationEntity, facilityDelegation);

		facilityDelegationRepository.save(facilityDelegationEntity);
	}

	/**
	 * Deletes a facility delegation by municipalityId and id.
	 * 
	 * @param municipalityId municipalityId
	 * @param id             id of the facility delegation to be deleted
	 */
	public void deleteFacilityDelegation(String municipalityId, String id) {
		facilityDelegationRepository.deleteByMunicipalityIdAndId(municipalityId, id);
	}
}
