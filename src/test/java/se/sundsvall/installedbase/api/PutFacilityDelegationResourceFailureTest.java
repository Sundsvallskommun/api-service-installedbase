package se.sundsvall.installedbase.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static se.sundsvall.installedbase.TestDataFactory.createUpdateFacilityDelegation;

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
import se.sundsvall.installedbase.service.InstalledBaseService;

@ActiveProfiles("junit")
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
class PutFacilityDelegationResourceFailureTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String VALID_FACILITY_DELEGATION_ID = UUID.randomUUID().toString();
	private static final String VALID_OWNER_ID = UUID.randomUUID().toString();

	@MockitoBean
	private InstalledBaseService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@AfterEach
	void tearDown() {
		verifyNoInteractions(mockService);
	}

	@Test
	void putDelegationInvalidMunicipalityId() {
		var invalidMunicipalityId = "invalid";
		var delegate = createUpdateFacilityDelegation();

		webTestClient.put()
			.uri("/{municipalityId}/delegates/{id}", invalidMunicipalityId, VALID_FACILITY_DELEGATION_ID, VALID_OWNER_ID)
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
			.uri("/{municipalityId}/delegates/{id}", MUNICIPALITY_ID, invalidId, VALID_OWNER_ID)
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
		var delegation = createUpdateFacilityDelegation();
		delegation.setOwner("not-a-valid-uuid");

		webTestClient.put()
			.uri("/{municipalityId}/delegates/{id}", MUNICIPALITY_ID, VALID_FACILITY_DELEGATION_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegation)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.consumeWith(response -> {
				assertThat(response.getResponseBody()).isNotNull();
				assertThat(response.getResponseBody().getViolations())
					.extracting(Violation::getField, Violation::getMessage)
					.containsExactly(tuple("owner", "not a valid UUID"));
			});
	}

	@Test
	void putDelegationInvalidDelegatedTo() {
		var invalidDelegatedTo = "invalid-delegated-to";
		var delegate = createUpdateFacilityDelegation();
		delegate.setDelegatedTo(invalidDelegatedTo);

		webTestClient.put()
			.uri("/{municipalityId}/delegates/{id}", MUNICIPALITY_ID, VALID_FACILITY_DELEGATION_ID, VALID_OWNER_ID)
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
	void putDelegationInvalidInvalidBusinessEngagementOrgId() {
		var invalidBusinessEngagementOrgId = "invalid-org-id";
		var delegate = createUpdateFacilityDelegation();
		delegate.setBusinessEngagementOrgId(invalidBusinessEngagementOrgId);

		webTestClient.put()
			.uri("/{municipalityId}/delegates/{id}", MUNICIPALITY_ID, VALID_FACILITY_DELEGATION_ID, VALID_OWNER_ID)
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
					.containsExactly(tuple("businessEngagementOrgId", "must match the regular expression ^([1235789][\\d][2-9]\\d{7})$"));
			});
	}

	@ParameterizedTest
	@MethodSource("invalidFacilitiesProvider")
	void putDelegationInvalidFacilities(List<String> facilities, String field, String expectedMessage) {
		var delegate = createUpdateFacilityDelegation();
		delegate.setFacilities(facilities);

		webTestClient.put()
			.uri("/{municipalityId}/delegates/{id}", MUNICIPALITY_ID, VALID_FACILITY_DELEGATION_ID)
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
					.containsExactly(tuple(field, expectedMessage));
			});
	}

	private static Stream<Arguments> invalidFacilitiesProvider() {
		return Stream.of(
			Arguments.of(List.of(""), "facilities[0]", "Facility cannot be blank"),
			Arguments.of(List.of(" "), "facilities[0]", "Facility cannot be blank"),
			Arguments.of(List.of("facility-1", "facility-1"), "facilities", "List must contain unique elements"),
			Arguments.of(List.of("", "facility-1"), "facilities[0]", "Facility cannot be blank"));
	}
}
