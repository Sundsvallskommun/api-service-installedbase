package se.sundsvall.installedbase.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static se.sundsvall.installedbase.TestDataFactory.createUpdateFacilityDelegation;

import java.util.UUID;
import org.junit.jupiter.api.Test;
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
class PutFacilityDelegationResourceFailureTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String VALID_FACILITY_DELEGATION_ID = UUID.randomUUID().toString();
	private static final String VALIDE_OWNER_ID = UUID.randomUUID().toString();

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void putDelegationInvalidMunicipalityId() {
		var invalidMunicipalityId = "invalid";
		var delegate = createUpdateFacilityDelegation();

		webTestClient.put()
			.uri("/{municipalityId}/delegates/{id}/{owner}", invalidMunicipalityId, VALID_FACILITY_DELEGATION_ID, VALIDE_OWNER_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegate)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("putFacilityDelegation.municipalityId", "not a valid municipality ID"));
			});
	}

	@Test
	void putDelegationInvalidId() {
		var invalidId = "invalid-id";
		var delegate = createUpdateFacilityDelegation();

		webTestClient.put()
			.uri("/{municipalityId}/delegates/{id}/{owner}", MUNICIPALITY_ID, invalidId, VALIDE_OWNER_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegate)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("putFacilityDelegation.id", "not a valid UUID"));
			});
	}

	@Test
	void putDelegationInvalidOwner() {
		var invalidOwner = "invalid-owner";
		var delegate = createUpdateFacilityDelegation();

		webTestClient.put()
			.uri("/{municipalityId}/delegates/{id}/{owner}", MUNICIPALITY_ID, VALID_FACILITY_DELEGATION_ID, invalidOwner)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegate)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("putFacilityDelegation.owner", "not a valid UUID"));
			});
	}

	@Test
	void putDelegationInvalidDelegatedTo() {
		var invalidDelegatedTo = "invalid-delegated-to";
		var delegate = createUpdateFacilityDelegation();
		delegate.setDelegatedTo(invalidDelegatedTo);

		webTestClient.put()
			.uri("/{municipalityId}/delegates/{id}/{owner}", MUNICIPALITY_ID, VALID_FACILITY_DELEGATION_ID, VALIDE_OWNER_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegate)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("delegatedTo", "not a valid UUID"));
			});
	}

	@Test
	void putDelegationNonExistingDelegation() {
		var delegate = createUpdateFacilityDelegation();

		webTestClient.put()
			.uri("/{municipalityId}/delegates/{id}/{owner}", MUNICIPALITY_ID, VALID_FACILITY_DELEGATION_ID, VALIDE_OWNER_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegate)
			.exchange()
			.expectStatus().isNotFound()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(Problem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getTitle()).isEqualTo("Facility delegation not found");
				assertThat(response.getResponseBody().getDetail()).isEqualTo("Couldn't find any active facility delegations for id: " + VALID_FACILITY_DELEGATION_ID);
			});
	}
}
