package se.sundsvall.installedbase.service.mapper;

import se.sundsvall.installedbase.api.model.facilitydelegation.FacilityDelegation;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;
import se.sundsvall.installedbase.service.model.DelegationStatus;

public final class EntityMapper {

	private EntityMapper() {}

	/**
	 * Converts a FacilityDelegation to a FacilityDelegationEntity.
	 * 
	 * @param  municipalityId     the municipality ID
	 * @param  facilityDelegation the facilityDelegation to convert
	 * @param  status             the delegation status
	 * @return                    a FacilityDelegationEntity representing the facilityDelegation
	 */
	public static FacilityDelegationEntity toEntity(String municipalityId, FacilityDelegation facilityDelegation, DelegationStatus status) {
		return new FacilityDelegationEntity()
			.withDelegatedTo(facilityDelegation.getDelegatedTo())
			.withStatus(status)
			.withFacilities(facilityDelegation.getFacilities())
			.withBusinessEngagementOrgId(facilityDelegation.getBusinessEngagementOrgId())
			.withOwner(facilityDelegation.getOwner())
			.withMunicipalityId(municipalityId);
	}

	/**
	 * Converts a FacilityDelegationEntity to a FacilityDelegation.
	 * 
	 * @param  entity the FacilityDelegationEntity to convert
	 * @return        a FacilityDelegation representing the entity
	 */
	public static FacilityDelegation toDelegate(FacilityDelegationEntity entity) {
		return new FacilityDelegation()
			.withId(entity.getId())
			.withFacilities(entity.getFacilities())
			.withBusinessEngagementOrgId(entity.getBusinessEngagementOrgId())
			.withDelegatedTo(entity.getDelegatedTo())
			.withMunicipalityId(entity.getMunicipalityId())
			.withOwner(entity.getOwner())
			.withStatus(entity.getStatus().name())
			.withCreated(entity.getCreated())
			.withDeleted(entity.getDeleted());
	}
}
