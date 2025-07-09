package se.sundsvall.installedbase.service.mapper;

import se.sundsvall.installedbase.api.model.facilitydelegation.CreateFacilityDelegation;
import se.sundsvall.installedbase.api.model.facilitydelegation.FacilityDelegation;
import se.sundsvall.installedbase.api.model.facilitydelegation.UpdateFacilityDelegation;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;
import se.sundsvall.installedbase.service.model.DelegationStatus;

public final class EntityMapper {

	private EntityMapper() {}

	/**
	 * Converts a CreateFacilityDelegation to a FacilityDelegationEntity.
	 * 
	 * @param  municipalityId     the ID of the municipality
	 * @param  facilityDelegation the CreateFacilityDelegation object containing delegation details
	 * @param  status             the status of the delegation
	 * @return                    a FacilityDelegationEntity
	 */
	public static FacilityDelegationEntity toEntity(String municipalityId, CreateFacilityDelegation facilityDelegation, DelegationStatus status) {
		return new FacilityDelegationEntity()
			.withMunicipalityId(municipalityId)
			.withOwner(facilityDelegation.getOwner())
			.withDelegatedTo(facilityDelegation.getDelegatedTo())
			.withFacilities(facilityDelegation.getFacilities())
			.withBusinessEngagementOrgId(facilityDelegation.getBusinessEngagementOrgId())
			.withStatus(status);
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
			.withStatus(entity.getStatus().name())
			.withCreated(entity.getCreated())
			.withUpdated(entity.getUpdated())
			.withDeleted(entity.getDeleted());
	}

	/**
	 * Updates a FacilityDelegationEntity for a PUT operation.
	 * Should only updates the facilities, delegatedTo, and businessEngagementOrgId fields.
	 *
	 * @param facilityDelegationEntity the FacilityDelegationEntity to update
	 * @param facilityDelegation       the FacilityDelegation containing the new values
	 */
	public static void updateEntityForPutOperation(FacilityDelegationEntity facilityDelegationEntity, UpdateFacilityDelegation facilityDelegation) {
		// Update the entity with the new values, and only update specific fields
		facilityDelegationEntity
			.withFacilities(facilityDelegation.getFacilities())
			.withDelegatedTo(facilityDelegation.getDelegatedTo())
			.withBusinessEngagementOrgId(facilityDelegation.getBusinessEngagementOrgId());
	}
}
