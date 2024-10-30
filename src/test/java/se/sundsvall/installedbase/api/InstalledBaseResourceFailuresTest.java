package se.sundsvall.installedbase.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.service.InstalledBaseService;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.zalando.problem.Status.BAD_REQUEST;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class InstalledBaseResourceFailuresTest {

	private static final String PATH = "/{municipalityId}/installedbase/{organizationNumber}";

	@MockBean
	private InstalledBaseService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getinstalledBaseInvalidMunicipalityId() {

		// Arrange
		final var municipalityId = "invalid";
		final var organizationNumber = "5566112233";
		final var partyId = randomUUID().toString();

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("partyId", List.of(partyId))
			.build(municipalityId, organizationNumber))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getInstalledBase.municipalityId", "not a valid municipality ID"));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getinstalledBaseMissingPartyId() {

		// Arrange
		final var municipalityId = "2281";
		final var organizationNumber = "5566112233";

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.build(municipalityId, organizationNumber))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo(BAD_REQUEST.getReasonPhrase());
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo("Required request parameter 'partyId' for method parameter type List is not present");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getinstalledBaseInvalidPartyId() {

		// Arrange
		final var municipalityId = "2281";
		final var organizationNumber = "5566112233";

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("partyId", List.of("invalid-party-id"))
			.build(municipalityId, organizationNumber))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getInstalledBase.partyIds[0].<list element>", "not a valid UUID"));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getinstalledBaseInvalidOrganizationNumber() {

		// Arrange
		final var municipalityId = "2281";
		final var organizationNumber = "invalid";

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("partyId", List.of(UUID.randomUUID()))
			.build(municipalityId, organizationNumber))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getInstalledBase.organizationNumber", "must match the regular expression ^([1235789][\\d][2-9]\\d{7})$"));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getinstalledBaseInvalidModifiedFromDate() {

		// Arrange
		final var municipalityId = "2281";
		final var organizationNumber = "5566112233";

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("partyId", List.of(UUID.randomUUID()))
			.queryParam("modifiedFrom", "invalid-date-format")
			.build(municipalityId, organizationNumber))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Bad Request");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo("""
			Method parameter 'modifiedFrom': Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate'; \
			Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.Parameter \
			@org.springframework.web.bind.annotation.RequestParam \
			java.time.LocalDate] for value [invalid-date-format]""");

		verifyNoInteractions(serviceMock);
	}
}
