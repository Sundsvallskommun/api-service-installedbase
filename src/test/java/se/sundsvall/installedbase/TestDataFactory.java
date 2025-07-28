package se.sundsvall.installedbase;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import se.sundsvall.installedbase.api.model.delegation.CreateDelegation;
import se.sundsvall.installedbase.api.model.delegation.Delegation;
import se.sundsvall.installedbase.api.model.delegation.Facility;
import se.sundsvall.installedbase.api.model.delegation.UpdateDelegation;
import se.sundsvall.installedbase.integration.db.model.DelegationEntity;
import se.sundsvall.installedbase.integration.db.model.FacilityEntity;

public final class TestDataFactory {

	public static Delegation createDelegationResponse() {
		return new Delegation()
			.withId(UUID.randomUUID().toString())
			.withDelegatedTo(UUID.randomUUID().toString())
			.withOwner(UUID.randomUUID().toString())
			.withFacilities(List.of(
				Facility.create().withId("facility-1").withBusinessEngagementOrgId("5591628136"),
				Facility.create().withId("facility-2").withBusinessEngagementOrgId("5591628136")))
			.withMunicipalityId("2281");
	}

	public static CreateDelegation createDelegation() {
		return new CreateDelegation()
			.withDelegatedTo(UUID.randomUUID().toString())
			.withOwner(UUID.randomUUID().toString())
			.withFacilities(List.of(
				Facility.create().withId("facility-3").withBusinessEngagementOrgId("5591628137"),
				Facility.create().withId("facility-4").withBusinessEngagementOrgId("5591628137")));
	}

	public static UpdateDelegation updateDelegation() {
		return new UpdateDelegation()
			.withDelegatedTo(UUID.randomUUID().toString())
			.withFacilities(List.of(
				Facility.create().withId("facility-9").withBusinessEngagementOrgId("5591628141"),
				Facility.create().withId("facility-10").withBusinessEngagementOrgId("5591628142")));
	}

	public static DelegationEntity createDelegationEntity(String id, String ownerId, String delegateId) {
		return createDelegationEntity(id)
			.withOwner(ownerId)
			.withDelegatedTo(delegateId);
	}

	public static DelegationEntity createDelegationEntity(String id) {
		return new DelegationEntity()
			.withId(id)
			.withMunicipalityId("2281")
			.withOwner(UUID.randomUUID().toString())
			.withDelegatedTo(UUID.randomUUID().toString())
			.withFacilities(Set.of(
				FacilityEntity.create().withFacilityId("facility-7").withBusinessEngagementOrgId("5591628140"),
				FacilityEntity.create().withFacilityId("facility-8").withBusinessEngagementOrgId("5591628140")))
			.withCreated(LocalDateTime.now().minusMinutes(3))
			.withUpdated(LocalDateTime.now().minusMinutes(2));
	}

	public static FacilityEntity createFacilityEntity(String id) {
		return FacilityEntity.create()
			.withFacilityId(id);
	}

	public static Stream<Arguments> invalidUuidProvider() {
		return Stream.concat(invalidUuidWithoutNullProvider(),
			Stream.of(arguments("null uuid", null, "not a valid UUID")));
	}

	public static Stream<Arguments> invalidUuidWithoutNullProvider() {
		return Stream.of(
			arguments("empty uuid", "", "not a valid UUID"),
			arguments("blank uuid", " ", "not a valid UUID"),
			arguments("invalid uuid", "not-a-uuid", "not a valid UUID"));
	}
}
