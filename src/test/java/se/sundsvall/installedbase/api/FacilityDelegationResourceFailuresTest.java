package se.sundsvall.installedbase.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.installedbase.Application;

@ActiveProfiles("junit")
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
class FacilityDelegationResourceFailuresTest {

	private static final String MUNICIPALITY_ID = "2281";

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void createDelegateInvalidMunicipalityId() {
		var invalidMunicipalityId = "invalid";

		var delegate = createFacilityDelegation();

		webTestClient.post()
			.uri("/{municipalityId}/delegates", invalidMunicipalityId)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegate)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(result -> {
				assertThat(result.getResponseBody()).isNotNull();
				assertThat(result.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("createDelegation.municipalityId", "not a valid municipality ID"));
			});
	}

	@ParameterizedTest
	@MethodSource("invalidFacilitiesProvider")
	void createDelegateInvalidFacility(List<String> facilities, String field, String expectedMessage) {
		var delegate = createFacilityDelegation();
		delegate.setFacilities(facilities);

		webTestClient.post()
			.uri("/{municipalityId}/delegates", MUNICIPALITY_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegate)
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
			Arguments.of(null, "facilities", "facilities must contain at least one facility"),
			Arguments.of(List.of(), "facilities", "facilities must contain at least one facility"),
			Arguments.of(List.of(""), "facilities[0]", "Facility cannot be blank"),
			Arguments.of(List.of(" "), "facilities[0]", "Facility cannot be blank"),
			Arguments.of(List.of("", "facility-1"), "facilities[0]", "Facility cannot be blank"));
	}

	@ParameterizedTest
	@MethodSource("invalidUuidProvider")
	void createDelegateInvalidDelegatedTo(String delegatedTo, String expectedMessage) {
		var delegate = createFacilityDelegation();
		delegate.setDelegatedTo(delegatedTo);

		webTestClient.post()
			.uri("/{municipalityId}/delegates", MUNICIPALITY_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegate)
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

	@ParameterizedTest
	@MethodSource("invalidUuidProvider")
	void createDelegateInvalidOwner(String delegatedTo, String expectedMessage) {
		var delegate = createFacilityDelegation();
		delegate.setOwner(delegatedTo);

		webTestClient.post()
			.uri("/{municipalityId}/delegates", MUNICIPALITY_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegate)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(result -> {
				assertThat(result.getResponseBody()).isNotNull();
				assertThat(result.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("owner", expectedMessage));
			});
	}

	private static Stream<Arguments> invalidUuidProvider() {
		return Stream.of(
			Arguments.of("", "not a valid UUID"),
			Arguments.of(" ", "not a valid UUID"),
			Arguments.of(null, "not a valid UUID"));
	}

	@Test
	void getDelegateByIdInvalidMunicipalityId() {
		var invalidMunicipalityId = "invalid";
		var id = UUID.randomUUID().toString();

		webTestClient.get()
			.uri("/{municipalityId}/delegates/{id}", invalidMunicipalityId, id)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("getDelegationById.municipalityId", "not a valid municipality ID"));
			});
	}

	@Test
	void getDelegateByIdInvalidId() {
		var invalidId = "invalid-id";

		webTestClient.get()
			.uri("/{municipalityId}/delegates/{id}", MUNICIPALITY_ID, invalidId)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("getDelegationById.id", "not a valid UUID"));
			});
	}

	@Test
	void getDelegationsWithInvalidMunicipalityId() {
		var invalidMunicipalityId = "invalid";

		webTestClient.get()
			.uri("/{municipalityId}/delegates", invalidMunicipalityId)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("getDelegations.municipalityId", "not a valid municipality ID"));
			});
	}

	@Test
	void getDelegationsWithInvalidOwner() {
		var invalidOwner = "invalid-owner";

		webTestClient.get()
			.uri("/{municipalityId}/delegates?owner={owner}", MUNICIPALITY_ID, invalidOwner)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("getDelegations.owner", "not a valid UUID"));
			});
	}

	@Test
	void getDelegationsWithInvalidDelegatedTo() {
		var invalidDelegatedTo = "invalid-delegated-to";
		webTestClient.get()
			.uri("/{municipalityId}/delegates?delegatedTo={delegatedTo}", MUNICIPALITY_ID, invalidDelegatedTo)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("getDelegations.delegatedTo", "not a valid UUID"));
			});
	}

	@Test
	void getDelegationsWithNoOwnerOrDelegatedTo() {
		webTestClient.get()
			.uri("/{municipalityId}/delegates", MUNICIPALITY_ID)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(Problem.class)
			.consumeWith(response -> {
				assertThat(response).isNotNull();
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getTitle()).isEqualTo("Invalid search parameters");
				assertThat(response.getResponseBody().getDetail()).isEqualTo("Either owner or delegatedTo must be provided");
			});
	}

	@Test
	void getDelegationsWithInvalidStatus() {
		var owner = UUID.randomUUID().toString();
		var invalidStatus = "invalid-status";

		webTestClient.get()
			.uri("/{municipalityId}/delegates?owner={owner}&status={status}", MUNICIPALITY_ID, owner, invalidStatus)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("getDelegations.status", "invalid delegation status 'invalid-status'. Must be one of 'ACTIVE, DELETED' or empty"));
			});
	}
}
