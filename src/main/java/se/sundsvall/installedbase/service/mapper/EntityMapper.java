package se.sundsvall.installedbase.service.mapper;

import se.sundsvall.installedbase.api.model.facilitydelegation.CreateFacilityDelegation;
import se.sundsvall.installedbase.api.model.facilitydelegation.FacilityDelegation;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;

public final class EntityMapper {

	private EntityMapper() {}

	/**
	 * Converts a CreateFacilityDelegation to a FacilityDelegationEntity.
	 * 
	 * @param  municipalityId     the ID of the municipality
	 * @param  facilityDelegation the CreateFacilityDelegation object containing delegation details
	 * @return                    a FacilityDelegationEntity
	 */
	public static FacilityDelegationEntity toEntity(String municipalityId, CreateFacilityDelegation facilityDelegation) {
		return new FacilityDelegationEntity()
			.withMunicipalityId(municipalityId)
			.withOwner(facilityDelegation.getOwner())
			.withDelegatedTo(facilityDelegation.getDelegatedTo())
			.withFacilities(facilityDelegation.getFacilities())
			.withBusinessEngagementOrgId(facilityDelegation.getBusinessEngagementOrgId());
	}

	/**
	 * Converts a FacilityDelegationEntity to a FacilityDelegation.
	 *
	 * @param  entity the FacilityDelegationEntity to convert
	 * @return        a FacilityDelegation representing the entity
	 */
	public static FacilityDelegation toFacilityDelegation(FacilityDelegationEntity entity) {
		return new FacilityDelegation()
			.withId(entity.getId())
			.withFacilities(entity.getFacilities())
			.withBusinessEngagementOrgId(entity.getBusinessEngagementOrgId())
			.withDelegatedTo(entity.getDelegatedTo())
			.withMunicipalityId(entity.getMunicipalityId())
			.withOwner(entity.getOwner())
			.withCreated(entity.getCreated())
			.withUpdated(entity.getUpdated());
	}
}
