package se.sundsvall.installedbase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import se.sundsvall.installedbase.api.model.delegate.FacilityDelegation;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;
import se.sundsvall.installedbase.service.model.DelegationStatus;

public final class TestDataFactory {

	public static FacilityDelegation createFacilityDelegation() {
		return new FacilityDelegation()
			.withId(UUID.randomUUID().toString())
			.withDelegatedTo(UUID.randomUUID().toString())
			.withOwner(UUID.randomUUID().toString())
			.withBusinessEngagementOrgId("businessEngagementOrgId")
			.withFacilities(List.of("facility-1", "facility-2"))
			.withMunicipalityId("2281");
	}

	public static FacilityDelegationEntity createFacilityDelegationEntity(String id) {
		return new FacilityDelegationEntity()
			.withId(id)
			.withStatus(DelegationStatus.ACTIVE)
			.withCreated(LocalDateTime.now().minusMinutes(3))
			.withUpdated(LocalDateTime.now())
			.withMunicipalityId("2281")
			.withBusinessEngagementOrgId("businessEngagementOrgId")
			.withFacilities(List.of("facility-1", "facility-2"));
	}
}
