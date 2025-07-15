package se.sundsvall.installedbase.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegation;

import java.util.List;
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
class CreateFacilityDelegationResourceFailureTest {

	private static final String MUNICIPALITY_ID = "2281";

	@MockitoBean
	private InstalledBaseService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@AfterEach
	void tearDown() {
		verifyNoInteractions(mockService);
	}

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
					.containsExactly(tuple("createFacilityDelegation.municipalityId", "not a valid municipality ID"));
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
			Arguments.of(List.of("facility-1", "facility-1"), "facilities", "List must contain unique elements"),
			Arguments.of(List.of("", "facility-1"), "facilities[0]", "Facility cannot be blank"));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("se.sundsvall.installedbase.TestDataFactory#invalidUuidProvider")
	void createDelegateInvalidDelegatedTo(String testName, String delegatedTo, String expectedMessage) {
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

	@ParameterizedTest(name = "{0}")
	@MethodSource("se.sundsvall.installedbase.TestDataFactory#invalidUuidProvider")
	void createDelegateInvalidOwner(String testName, String delegatedTo, String expectedMessage) {
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

	@Test
	void createDelegateInvalidBusinessEngagementOrgId() {
		var invalidBusinessEngagementOrgId = "invalid-org-id";
		var delegate = createFacilityDelegation();
		delegate.setBusinessEngagementOrgId(invalidBusinessEngagementOrgId);

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
					.containsExactly(tuple("businessEngagementOrgId", "must match the regular expression ^([1235789][\\d][2-9]\\d{7})$"));
			});
	}
}
