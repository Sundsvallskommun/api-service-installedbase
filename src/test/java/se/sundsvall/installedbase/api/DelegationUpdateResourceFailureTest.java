package se.sundsvall.installedbase.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static se.sundsvall.installedbase.TestDataFactory.updateDelegation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.api.model.delegation.Facility;
import se.sundsvall.installedbase.service.InstalledBaseService;

@ActiveProfiles("junit")
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
class DelegationUpdateResourceFailureTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String ID = UUID.randomUUID().toString();

	@MockitoBean
	private InstalledBaseService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@AfterEach
	void tearDown() {
		verifyNoInteractions(mockService);
	}

	@Test
	void updateDelegationInvalidMunicipalityId() {
		final var invalidMunicipalityId = "invalid";

		final var delegation = updateDelegation();

		webTestClient.patch()
			.uri("/{municipalityId}/delegations/{id}", invalidMunicipalityId, ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegation)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(result -> {
				assertThat(result.getResponseBody()).isNotNull();
				assertThat(result.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("updateDelegation.municipalityId", "not a valid municipality ID"));
			});
	}

	@Test
	void updateDelegationInvalidId() {
		final var invalidId = "invalid";

		final var delegation = updateDelegation();

		webTestClient.patch()
			.uri("/{municipalityId}/delegations/{id}", MUNICIPALITY_ID, invalidId)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegation)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(result -> {
				assertThat(result.getResponseBody()).isNotNull();
				assertThat(result.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("updateDelegation.id", "not a valid UUID"));
			});
	}

	@ParameterizedTest
	@MethodSource("invalidFacilitiesProvider")
	void updateDelegationInvalidFacilities(List<Facility> facilities, String field, String expectedMessage) {
		final var delegation = updateDelegation();
		delegation.setFacilities(facilities);

		webTestClient.patch()
			.uri("/{municipalityId}/delegations/{id}", MUNICIPALITY_ID, ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegation)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(result -> {
				assertThat(result).isNotNull();
				assertThat(result.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple(field, expectedMessage));
			});
	}

	private static Stream<Arguments> invalidFacilitiesProvider() {
		return Stream.of(
			Arguments.of(List.of(
				Facility.create().withId("123")), "facilities[0].businessEngagementOrgId", "must match the regular expression ^([1235789][\\d][2-9]\\d{7})$"),
			Arguments.of(List.of(
				Facility.create().withBusinessEngagementOrgId("5591628135")), "facilities[0].id", "Facility id cannot be blank"),
			Arguments.of(List.of(
				Facility.create().withBusinessEngagementOrgId("5591628135").withId("")), "facilities[0].id", "Facility id cannot be blank"),
			Arguments.of(List.of(
				Facility.create().withBusinessEngagementOrgId("5591628135").withId(" ")), "facilities[0].id", "Facility id cannot be blank"),
			Arguments.of(List.of(
				Facility.create().withBusinessEngagementOrgId("5591628135").withId("facility-1"),
				Facility.create().withBusinessEngagementOrgId("5591628135").withId("Facility-1")), "facilities", "List must contain unique elements"),
			Arguments.of(List.of(
				Facility.create().withBusinessEngagementOrgId("5591628135").withId(""),
				Facility.create().withBusinessEngagementOrgId("5591628135").withId("facility-1")), "facilities[0].id", "Facility id cannot be blank"));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("se.sundsvall.installedbase.TestDataFactory#invalidUuidWithoutNullProvider")
	void updateDelegationInvalidDelegatedTo(String testName, String delegatedTo, String expectedMessage) {
		final var delegation = updateDelegation();
		delegation.setDelegatedTo(delegatedTo);

		webTestClient.patch()
			.uri("/{municipalityId}/delegations/{id}", MUNICIPALITY_ID, ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegation)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(result -> {
				assertThat(result.getResponseBody()).isNotNull();
				assertThat(result.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("delegatedTo", expectedMessage));
			});
	}

	@Test
	void updateDelegateInvalidBusinessEngagementOrgId() {
		final var invalidBusinessEngagementOrgId = "invalid-org-id";
		final var delegatation = updateDelegation();
		delegatation.getFacilities().getFirst().setBusinessEngagementOrgId(invalidBusinessEngagementOrgId);

		webTestClient.patch()
			.uri("/{municipalityId}/delegations/{id}", MUNICIPALITY_ID, ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegatation)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(result -> {
				assertThat(result.getResponseBody()).isNotNull();
				assertThat(result.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("facilities[0].businessEngagementOrgId", "must match the regular expression ^([1235789][\\d][2-9]\\d{7})$"));
			});
	}
}
