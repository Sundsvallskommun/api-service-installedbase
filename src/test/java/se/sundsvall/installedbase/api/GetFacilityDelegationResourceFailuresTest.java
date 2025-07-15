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
class GetFacilityDelegationResourceFailuresTest {

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
	void getDelegateByIdInvalidMunicipalityId() {
		var invalidMunicipalityId = "invalid";

		webTestClient.get()
			.uri("/{municipalityId}/delegates/{id}", invalidMunicipalityId, FACILITY_DELEGATION_ID)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("getFacilityDelegationById.municipalityId", "not a valid municipality ID"));
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
					.containsExactly(tuple("getFacilityDelegationById.id", "not a valid UUID"));
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
					.containsExactly(tuple("getFacilityDelegations.municipalityId", "not a valid municipality ID"));
			});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("se.sundsvall.installedbase.TestDataFactory#invalidUuidProvider")
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
					.containsExactly(tuple("getFacilityDelegations.owner", "not a valid UUID"));
			});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("se.sundsvall.installedbase.TestDataFactory#invalidUuidProvider")
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
					.containsExactly(tuple("getFacilityDelegations.delegatedTo", "not a valid UUID"));
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
}
