package se.sundsvall.installedbase.api;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.service.InstalledBaseService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class InstalledBaseResourceFailuresTest {

	@MockBean
	private InstalledBaseService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getinstalledBaseNoParameters() {

		// Call
		webTestClient.get().uri("/installedbase")
			.exchange()
			.expectStatus().isNotFound()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Not Found")
			.jsonPath("$.status").isEqualTo(NOT_FOUND.value())
			.jsonPath("$.detail").isEqualTo("No handler found for GET /installedbase");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getinstalledBaseMissingPartyId() {

		// Parameters
		final var organizationNumber = "5566112233";

		// Call
		webTestClient.get().uri("/installedbase/{organizationNumber}", organizationNumber)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Bad Request")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.detail").isEqualTo("Required request parameter 'partyId' for method parameter type List is not present");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getinstalledBaseInvalidPartyId() {

		// Parameters
		final var partyId = List.of("invalid-party-id");
		final var organizationNumber = "5566112233";

		// Call
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/installedbase/{organizationNumber}")
			.queryParam("partyId", partyId)
			.build(organizationNumber))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getInstalledBase.partyIds[0].<list element>")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getinstalledBaseInvalidOrganizationNumber() {

		// Parameters
		final var partyId = List.of(UUID.randomUUID());
		final var organizationNumber = "invalid-organization-number";

		// Call
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/installedbase/{organizationNumber}")
			.queryParam("partyId", partyId)
			.build(organizationNumber))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getInstalledBase.organizationNumber")
			.jsonPath("$.violations[0].message").isEqualTo("must match the regular expression ^([1235789][\\d][2-9]\\d{7})$");

		verifyNoInteractions(serviceMock);
	}
}
