package se.sundsvall.installedbase.api;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.api.model.InstalledBase;
import se.sundsvall.installedbase.api.model.InstalledBaseCustomer;
import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.api.model.InstalledBases;
import se.sundsvall.installedbase.service.InstalledBaseService;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@AutoConfigureWebTestClient
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class InstalledBaseResourceTest {

	private static final String PATH = "/{municipalityId}/installedbase/{organizationNumber}";
	private static final String PARTY_PATH = "/{municipalityId}/installedbase";

	@MockitoBean
	private InstalledBaseService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getinstalledBaseWithoutModifiedFrom() {

		// Arrange
		final var municipalityId = "2281";
		final var partyId = List.of(randomUUID().toString());
		final var organizationNumber = "5566112233";
		final var customerNumber = "12345";

		final var expectedResponse = InstalledBaseResponse.create().withInstalledBaseCustomers(List.of(InstalledBaseCustomer.create().withCustomerNumber(customerNumber)));
		when(serviceMock.getInstalledBase(any(), any(), any(), any())).thenReturn(expectedResponse);

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("partyId", partyId)
			.build(municipalityId, organizationNumber))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(InstalledBaseResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isEqualTo(expectedResponse);
		verify(serviceMock).getInstalledBase(municipalityId, organizationNumber, partyId, null);
	}

	@Test
	void getinstalledBaseWithModifiedFrom() {

		// Arrange
		final var municipalityId = "2281";
		final var partyId = List.of(randomUUID().toString());
		final var organizationNumber = "5566112233";
		final var customerNumber = "12345";
		final var modifiedFrom = LocalDate.now();

		final var expectedResponse = InstalledBaseResponse.create().withInstalledBaseCustomers(List.of(InstalledBaseCustomer.create().withCustomerNumber(customerNumber)));
		when(serviceMock.getInstalledBase(any(), any(), any(), any())).thenReturn(expectedResponse);

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("partyId", partyId)
			.queryParam("modifiedFrom", modifiedFrom)
			.build(municipalityId, organizationNumber))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(InstalledBaseResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isEqualTo(expectedResponse);
		verify(serviceMock).getInstalledBase(municipalityId, organizationNumber, partyId, modifiedFrom);
	}

	@Test
	void getInstalledBaseByPartyId_withAllParameters() {

		// Arrange
		final var municipalityId = "2281";
		final var partyId = randomUUID().toString();
		final var date = LocalDate.of(2025, 6, 1);

		final var expectedResponse = new InstalledBases()
			.withInstalledBases(List.of(InstalledBase.create().withCompany("TestCompany")));
		when(serviceMock.getInstalledBaseByPartyId(any(), any())).thenReturn(expectedResponse);

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PARTY_PATH)
			.queryParam("partyId", partyId)
			.queryParam("organizationIds", "123456789")
			.queryParam("date", date)
			.queryParam("sortBy", "Company")
			.queryParam("page", 1)
			.queryParam("limit", 15)
			.build(municipalityId))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(InstalledBases.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getInstalledBases()).hasSize(1);
		assertThat(response.getInstalledBases().getFirst().getCompany()).isEqualTo("TestCompany");
		verify(serviceMock).getInstalledBaseByPartyId(eq(municipalityId), any());
	}

	@Test
	void getInstalledBaseByPartyId_withOnlyRequiredParameters() {

		// Arrange
		final var municipalityId = "2281";
		final var partyId = randomUUID().toString();

		final var expectedResponse = new InstalledBases()
			.withInstalledBases(List.of());
		when(serviceMock.getInstalledBaseByPartyId(any(), any())).thenReturn(expectedResponse);

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PARTY_PATH)
			.queryParam("partyId", partyId)
			.build(municipalityId))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(InstalledBases.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getInstalledBases()).isEmpty();
		verify(serviceMock).getInstalledBaseByPartyId(eq(municipalityId), any());
	}

	@Test
	void getInstalledBaseByPartyId_invalidPartyId() {

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PARTY_PATH)
			.queryParam("partyId", "not-a-valid-uuid")
			.build("2281"))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInstalledBaseByPartyId_missingPartyId() {

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PARTY_PATH)
			.build("2281"))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInstalledBaseByPartyId_invalidMunicipalityId() {

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PARTY_PATH)
			.queryParam("partyId", randomUUID().toString())
			.build("invalid"))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody()
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull();
		verifyNoInteractions(serviceMock);
	}
}
