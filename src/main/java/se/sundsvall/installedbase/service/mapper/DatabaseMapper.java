package se.sundsvall.installedbase.service.mapper;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import se.sundsvall.installedbase.api.model.delegation.CreateDelegation;
import se.sundsvall.installedbase.api.model.delegation.Delegation;
import se.sundsvall.installedbase.api.model.delegation.Facility;
import se.sundsvall.installedbase.integration.db.model.DelegationEntity;
import se.sundsvall.installedbase.integration.db.model.FacilityEntity;

public final class DatabaseMapper {

	private DatabaseMapper() {}

	/**
	 * Converts a CreateDelegation to a DelegationEntity.
	 *
	 * @param  municipalityId the ID of the municipality
	 * @param  delegation     the Delegation object containing delegation details
	 * @return                a DelegationEntity
	 */
	public static DelegationEntity toDelegationEntity(String municipalityId, CreateDelegation delegation) {
		return new DelegationEntity()
			.withMunicipalityId(municipalityId)
			.withOwner(delegation.getOwner())
			.withDelegatedTo(delegation.getDelegatedTo());
	}

	/**
	 * Converts a Facility to a FacilityEntity
	 *
	 * @param  facility the Facility to be converted
	 * @return          a FacilityEntity representing the facility provided to the method
	 */
	public static FacilityEntity toFacilityEntity(Facility facility) {
		return ofNullable(facility)
			.map(f -> FacilityEntity.create()
				.withBusinessEngagementOrgId(f.getBusinessEngagementOrgId())
				.withFacilityId(f.getId()))
			.orElse(null);
	}

	/**
	 * Converts a DelegationEntity to a Delegation.
	 *
	 * @param  entity the DelegationEntity to convert
	 * @return        a Delegation representing the entity
	 */
	public static Delegation toDelegation(DelegationEntity entity) {
		return Delegation.create()
			.withId(entity.getId())
			.withFacilities(toFacilities(entity.getFacilities()))
			.withDelegatedTo(entity.getDelegatedTo())
			.withMunicipalityId(entity.getMunicipalityId())
			.withOwner(entity.getOwner())
			.withCreated(entity.getCreated())
			.withUpdated(entity.getUpdated());
	}

	/**
	 * Converts a list of FacilityEntity to a list of Facility
	 *
	 * @param  facilities the list of FacilityEntity to be converted
	 * @return            a list of Facility ordered by facility id representing the list provided to the method
	 */
	private static List<Facility> toFacilities(Set<FacilityEntity> facilities) {
		return ofNullable(facilities).orElse(emptySet()).stream()
			.map(DatabaseMapper::toFacility)
			.filter(Objects::nonNull)
			.sorted((o1, o2) -> o1.getId().compareTo(o2.getId()))
			.toList();

	}

	/**
	 * Converts a FacilityEntity to a Facility
	 *
	 * @param  entity the FacilityEntity to be converted
	 * @return        a Facility representing the facility entity provided to the method
	 */
	private static Facility toFacility(FacilityEntity entity) {
		return ofNullable(entity)
			.map(e -> Facility.create()
				.withBusinessEngagementOrgId(e.getBusinessEngagementOrgId())
				.withId(e.getFacilityId()))
			.orElse(null);
	}
}
