package se.sundsvall.installedbase.api;

import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.dept44.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.problem.violations.Violation;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.service.InstalledBaseService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@ActiveProfiles("junit")
@AutoConfigureWebTestClient
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
class DelegationDeleteResourceFailureTest {

	private static final String BASE_URL = "/{municipalityId}/delegations/{id}";
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
	void deleteByIdInvalidMunicipalityId() {
		final var invalidMunicipalityId = "invalid";

		webTestClient.delete()
			.uri(BASE_URL, invalidMunicipalityId, FACILITY_DELEGATION_ID)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::field, Violation::message)
					.containsExactly(tuple("deleteDelegation.municipalityId", "not a valid municipality ID"));
			});
	}

	@Test
	void testDeleteByIdInvalidId() {
		final var invalidId = "invalid";

		webTestClient.delete()
			.uri(BASE_URL, "2281", invalidId)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::field, Violation::message)
					.containsExactly(tuple("deleteDelegation.id", "not a valid UUID"));
			});
	}
}
