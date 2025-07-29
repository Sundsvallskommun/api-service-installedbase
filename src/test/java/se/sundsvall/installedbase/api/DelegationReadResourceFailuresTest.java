package se.sundsvall.installedbase.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.service.InstalledBaseService;

@ActiveProfiles("junit")
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
class DelegationReadResourceFailuresTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String FACILITY_DELEGATION_ID = UUID.randomUUID().toString();

	@MockitoBean
	private InstalledBaseService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@AfterEach
	void tearDown() {
		verifyNoInteractions(mockService);
	}

	@Test
	void getDelegationByIdInvalidMunicipalityId() {
		final var invalidMunicipalityId = "invalid";

		webTestClient.get()
			.uri("/{municipalityId}/delegations/{id}", invalidMunicipalityId, FACILITY_DELEGATION_ID)
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
	void getDelegationByIdInvalidId() {
		final var invalidId = "invalid-id";

		webTestClient.get()
			.uri("/{municipalityId}/delegations/{id}", MUNICIPALITY_ID, invalidId)
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
		final var invalidMunicipalityId = "invalid";

		webTestClient.get()
			.uri("/{municipalityId}/delegations", invalidMunicipalityId)
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

	@ParameterizedTest(name = "{0}")
	@MethodSource("se.sundsvall.installedbase.TestDataFactory#invalidUuidProvider")
	void getDelegationsWithInvalidOwner() {
		final var invalidOwner = "invalid-owner";

		webTestClient.get()
			.uri("/{municipalityId}/delegations?owner={owner}", MUNICIPALITY_ID, invalidOwner)
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

	@ParameterizedTest(name = "{0}")
	@MethodSource("se.sundsvall.installedbase.TestDataFactory#invalidUuidProvider")
	void getDelegationsWithInvalidDelegatedTo() {
		final var invalidDelegatedTo = "invalid-delegated-to";
		webTestClient.get()
			.uri("/{municipalityId}/delegations?delegatedTo={delegatedTo}", MUNICIPALITY_ID, invalidDelegatedTo)
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
			.uri("/{municipalityId}/delegations", MUNICIPALITY_ID)
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
}
