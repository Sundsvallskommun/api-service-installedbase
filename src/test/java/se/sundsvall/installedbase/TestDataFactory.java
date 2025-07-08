package se.sundsvall.installedbase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import se.sundsvall.installedbase.api.model.facilitydelegation.CreateFacilityDelegation;
import se.sundsvall.installedbase.api.model.facilitydelegation.FacilityDelegationResponse;
import se.sundsvall.installedbase.api.model.facilitydelegation.UpdateFacilityDelegation;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;
import se.sundsvall.installedbase.service.model.DelegationStatus;

public final class TestDataFactory {

	public static FacilityDelegationResponse createFacilityDelegationResponse() {
		return new FacilityDelegationResponse()
			.withId(UUID.randomUUID().toString())
			.withDelegatedTo(UUID.randomUUID().toString())
			.withOwner(UUID.randomUUID().toString())
			.withBusinessEngagementOrgId("1234")
			.withFacilities(List.of("facility-1", "facility-2"))
			.withMunicipalityId("2281");
	}

	public static CreateFacilityDelegation createFacilityDelegation() {
		return new CreateFacilityDelegation()
			.withDelegatedTo(UUID.randomUUID().toString())
			.withOwner(UUID.randomUUID().toString())
			.withBusinessEngagementOrgId("2345")
			.withFacilities(List.of("facility-3", "facility-4"));
	}

	public static UpdateFacilityDelegation createUpdateFacilityDelegation() {
		return new UpdateFacilityDelegation()
			.withDelegatedTo(UUID.randomUUID().toString())
			.withBusinessEngagementOrgId("3456")
			.withFacilities(List.of("facility-5", "facility-6"));
	}

	public static FacilityDelegationEntity createFacilityDelegationEntity(String id) {
		return new FacilityDelegationEntity()
			.withId(id)
			.withMunicipalityId("2281")
			.withOwner(UUID.randomUUID().toString())
			.withDelegatedTo(UUID.randomUUID().toString())
			.withFacilities(List.of("facility-7", "facility-8"))
			.withStatus(DelegationStatus.ACTIVE)
			.withCreated(LocalDateTime.now().minusMinutes(3))
			.withUpdated(LocalDateTime.now().minusMinutes(2))
			.withDeleted(LocalDateTime.now().minusMinutes(1))
			.withBusinessEngagementOrgId("4567");
	}
}
