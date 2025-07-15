package se.sundsvall.installedbase;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import se.sundsvall.installedbase.api.model.facilitydelegation.CreateFacilityDelegation;
import se.sundsvall.installedbase.api.model.facilitydelegation.FacilityDelegation;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;

public final class TestDataFactory {

	public static FacilityDelegation createFacilityDelegationResponse() {
		return new FacilityDelegation()
			.withId(UUID.randomUUID().toString())
			.withDelegatedTo(UUID.randomUUID().toString())
			.withOwner(UUID.randomUUID().toString())
			.withBusinessEngagementOrgId("5591628136")
			.withFacilities(List.of("facility-1", "facility-2"))
			.withMunicipalityId("2281");
	}

	public static CreateFacilityDelegation createFacilityDelegation() {
		return new CreateFacilityDelegation()
			.withDelegatedTo(UUID.randomUUID().toString())
			.withOwner(UUID.randomUUID().toString())
			.withBusinessEngagementOrgId("5591628137")
			.withFacilities(List.of("facility-3", "facility-4"));
	}

	public static FacilityDelegationEntity createFacilityDelegationEntity(String id) {
		return new FacilityDelegationEntity()
			.withId(id)
			.withMunicipalityId("2281")
			.withOwner(UUID.randomUUID().toString())
			.withDelegatedTo(UUID.randomUUID().toString())
			.withFacilities(List.of("facility-7", "facility-8"))
			.withCreated(LocalDateTime.now().minusMinutes(3))
			.withUpdated(LocalDateTime.now().minusMinutes(2))
			.withBusinessEngagementOrgId("5591628140");
	}

	public static Stream<Arguments> invalidUuidProvider() {
		return Stream.of(
			arguments("empty uuid", "", "not a valid UUID"),
			arguments("blank uuid", " ", "not a valid UUID"),
			arguments("null uuid", null, "not a valid UUID"),
			arguments("invalid uuid", "not-a-uuid", "not a valid UUID"));
	}
}
