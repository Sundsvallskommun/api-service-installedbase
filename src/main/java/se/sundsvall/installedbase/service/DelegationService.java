package se.sundsvall.installedbase.service;

import static generated.se.sundsvall.eventlog.EventType.CREATE;
import static generated.se.sundsvall.eventlog.EventType.DELETE;
import static generated.se.sundsvall.eventlog.EventType.UPDATE;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;
import static se.sundsvall.installedbase.integration.db.specification.DelegationSpecification.withDelegatedTo;
import static se.sundsvall.installedbase.integration.db.specification.DelegationSpecification.withId;
import static se.sundsvall.installedbase.integration.db.specification.DelegationSpecification.withMunicipalityId;
import static se.sundsvall.installedbase.integration.db.specification.DelegationSpecification.withOwner;
import static se.sundsvall.installedbase.service.mapper.DatabaseMapper.toDelegationEntity;
import static se.sundsvall.installedbase.service.mapper.DatabaseMapper.toFacilityEntity;
import static se.sundsvall.installedbase.service.mapper.EventlogMapper.toEvent;

import generated.se.sundsvall.eventlog.EventType;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.installedbase.api.model.delegation.CreateDelegation;
import se.sundsvall.installedbase.api.model.delegation.Delegation;
import se.sundsvall.installedbase.api.model.delegation.Facility;
import se.sundsvall.installedbase.api.model.delegation.UpdateDelegation;
import se.sundsvall.installedbase.integration.db.DelegationRepository;
import se.sundsvall.installedbase.integration.db.FacilityRepository;
import se.sundsvall.installedbase.integration.db.model.DelegationEntity;
import se.sundsvall.installedbase.integration.db.model.FacilityEntity;
import se.sundsvall.installedbase.integration.eventlog.EventLogClient;
import se.sundsvall.installedbase.service.mapper.DatabaseMapper;

@Service
@Transactional
public class DelegationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DelegationService.class);

	private final DelegationRepository delegationRepository;
	private final FacilityRepository facilityRepository;
	private final EventLogClient eventLogClient;

	public DelegationService(DelegationRepository delegationRepository, FacilityRepository facilityRepository, EventLogClient eventLogClient) {
		this.delegationRepository = delegationRepository;
		this.facilityRepository = facilityRepository;
		this.eventLogClient = eventLogClient;
	}

	/**
	 * Create a facility delegation. Throws a Problem with status 409 Conflict if a delegation already exists.
	 *
	 * @param  municipalityId municipalityId
	 * @param  delegation     CreateDelegation object containing delegation details
	 * @return                id of the delegation delegation
	 */
	public String createDelegation(String municipalityId, CreateDelegation delegation) {
		LOGGER.info("Create delegation for owner: {}, delegatedTo: {}", delegation.getOwner(), delegation.getDelegatedTo());

		// Check if the owner already has a facility delegated to the same partyId
		if (delegationRepository.findOne(
			withMunicipalityId(municipalityId)
				.and(withOwner(delegation.getOwner()))
				.and(withDelegatedTo(delegation.getDelegatedTo())))
			.isPresent()) {
			throw Problem.builder()
				.withTitle("Delegation already exists")
				.withDetail("Owner with partyId: '" + delegation.getOwner() + "' has already delegated to partyId: '"
					+ delegation.getDelegatedTo() + "' for municipality: '" + municipalityId + "'. Update existing delegation instead.")
				.withStatus(Status.CONFLICT)
				.build();
		}

		final var entity = delegationRepository.save(toDelegationEntity(municipalityId, delegation).withFacilities(toFacilities(delegation.getFacilities())));

		final var facilityIds = ofNullable(entity.getFacilities()).orElse(emptySet()).stream().map(FacilityEntity::getFacilityId).toList();
		sendEvent(municipalityId, entity, facilityIds, CREATE);

		return entity.getId();
	}

	/**
	 * Finds and reuses existing facility entity if found in database, else creates a new entity
	 *
	 * @param  delegation CreateDelegation object containing delegation details
	 * @return            list of FacilityEntity with existing entities for those existing in db and new entities for those
	 *                    who doesn't exist
	 */
	private Set<FacilityEntity> toFacilities(List<Facility> facilities) {
		return ofNullable(facilities).orElse(emptyList()).stream()
			.map(facility -> facilityRepository.findByFacilityIdIgnoreCase(facility.getId()).orElse(
				toFacilityEntity(facility)))
			.filter(Objects::nonNull)
			.collect(toCollection(HashSet::new));
	}

	/**
	 * Update an existing delegation. Throws a Problem with status 404 Not Found if the delegation does not exist.
	 *
	 * @param municipalityId
	 * @param id             id of the delegation to update
	 * @param delegation     UpdateDelegation object containing updated properties
	 */
	public void updateDelegation(String municipalityId, String id, UpdateDelegation delegation) {
		LOGGER.info("Update delegation with id: {}", id);

		final var entity = delegationRepository.findOne(withMunicipalityId(municipalityId)
			.and(withId(id)))
			.orElseThrow(() -> Problem.builder()
				.withDetail("Delegation with id: '" + id + "' was not found within municipality '" + municipalityId + "'.")
				.withStatus(Status.NOT_FOUND)
				.build());

		ofNullable(delegation.getDelegatedTo()).ifPresent(entity::setDelegatedTo);
		ofNullable(delegation.getFacilities()).ifPresent(facilities -> entity.setFacilities(toFacilities(delegation.getFacilities())));
		delegationRepository.save(entity);
		facilityRepository.deleteAllInBatch(facilityRepository.findAllByDelegationsIsEmpty()); // Clean up orphan facilities that has no connection to any delegation

		final var facilityIds = ofNullable(entity.getFacilities()).orElse(emptySet()).stream().map(FacilityEntity::getFacilityId).toList();
		sendEvent(municipalityId, entity, facilityIds, UPDATE);
	}

	/**
	 * Get delegation by municipality and id. Throws a Problem with status 404 Not Found if the delegation does not exist.
	 *
	 * @param  municipalityId municipalityId
	 * @param  id             id of the delegation
	 * @return                Delegation object containing delegation details
	 */
	public Delegation getDelegation(String municipalityId, String id) {
		LOGGER.info("Get delegation with id: {}", id);

		return delegationRepository.findOne(withMunicipalityId(municipalityId)
			.and(withId(id)))
			.map(DatabaseMapper::toDelegation)
			.orElseThrow(() -> Problem.builder()
				.withDetail("Delegation with id: '" + id + "' was not found within municipality '" + municipalityId + "'.")
				.withStatus(Status.NOT_FOUND)
				.build());
	}

	/**
	 * ' Get delegations by owner and/or delegatedTo.
	 *
	 * @param  municipalityId municipalityId
	 * @param  owner          owner of the delegation
	 * @param  delegatedTo    the party to which the facility is delegated
	 * @return                List of Delegation objects containing delegation details
	 */
	public List<Delegation> getDelegations(String municipalityId, String owner, String delegatedTo) {
		LOGGER.info("Get facility delegations for owner: {} and delegatedTo: {}", owner, delegatedTo);
		return delegationRepository.findAll(
			withMunicipalityId(municipalityId)
				.and(withOwner(owner))
				.and(withDelegatedTo(delegatedTo)))
			.stream()
			.map(DatabaseMapper::toDelegation)
			.toList();
	}

	/**
	 * Deletes a delegation by municipalityId and id.
	 *
	 * @param municipalityId municipalityId
	 * @param id             id of the delegation to be deleted
	 */
	public void deleteDelegation(String municipalityId, String id) {
		delegationRepository.findOne(withMunicipalityId(municipalityId)
			.and(withId(id)))
			.ifPresentOrElse(entity -> {
				LOGGER.info("Deleting delegation with id: {}", id);

				final var facilityIds = ofNullable(entity.getFacilities()).orElse(emptySet()).stream().map(FacilityEntity::getFacilityId).toList(); // Needs to be fetched before deletion to be visible in the event log

				delegationRepository.delete(entity.withFacilities(null));
				facilityRepository.deleteAllInBatch(facilityRepository.findAllByDelegationsIsEmpty()); // Clean up orphan facilities that has no connection to any delegation
				sendEvent(municipalityId, entity, facilityIds, DELETE);
			}, () -> LOGGER.info("Couldn't delete delegation with id: {} within municipality: {} as it does not exist", id, municipalityId));
	}

	/**
	 * Creates and sends an event for a facility delegation operation.
	 *
	 * @param municipalityId municipalityId
	 * @param entity         DelegationEntity containing delegation details
	 * @param eventType      EventType representing the type of event (CREATE, UPDATE, DELETE)
	 */
	private void sendEvent(String municipalityId, DelegationEntity entity, List<String> facilityIds, EventType eventType) {
		LOGGER.info("Creating event for delegation with id: {}", entity.getId());
		try {
			eventLogClient.createEvent(municipalityId, entity.getId(),
				toEvent(entity.getId(),
					entity.getOwner(),
					entity.getDelegatedTo(),
					facilityIds,
					eventType));
		} catch (final Exception e) {
			LOGGER.warn("Failed to send event for delegation with id: {}", entity.getId(), e);
			LOGGER.info("Delegation content: {}", entity);
		}
	}
}
