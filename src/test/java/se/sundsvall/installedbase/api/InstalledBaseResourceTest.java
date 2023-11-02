package se.sundsvall.installedbase.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.api.model.InstalledBaseCustomer;
import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.service.InstalledBaseService;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class InstalledBaseResourceTest {

	@MockBean
	private InstalledBaseService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getinstalledBaseWithoutModifiedFrom() {

		// Arrange
		final var partyId = List.of(UUID.randomUUID().toString());
		final var organizationNumber = "5566112233";
		final var customerNumber = "12345";

		final var expectedResponse = InstalledBaseResponse.create().withInstalledBaseCustomers(List.of(InstalledBaseCustomer.create().withCustomerNumber(customerNumber)));
		when(serviceMock.getInstalledBase(organizationNumber, partyId, null)).thenReturn(expectedResponse);

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/installedbase/{organizationNumber}")
			.queryParam("partyId", partyId)
			.build(organizationNumber))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(InstalledBaseResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isEqualTo(expectedResponse);
		verify(serviceMock).getInstalledBase(organizationNumber, partyId, null);
	}

	@Test
	void getinstalledBaseWithModifiedFrom() {

		// Arrange
		final var partyId = List.of(UUID.randomUUID().toString());
		final var organizationNumber = "5566112233";
		final var customerNumber = "12345";
		final var modifiedFrom = LocalDate.now();

		final var expectedResponse = InstalledBaseResponse.create().withInstalledBaseCustomers(List.of(InstalledBaseCustomer.create().withCustomerNumber(customerNumber)));
		when(serviceMock.getInstalledBase(organizationNumber, partyId, modifiedFrom)).thenReturn(expectedResponse);

		// Act
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/installedbase/{organizationNumber}")
			.queryParam("partyId", partyId)
			.queryParam("modifiedFrom", modifiedFrom)
			.build(organizationNumber))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(InstalledBaseResponse.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isEqualTo(expectedResponse);
		verify(serviceMock).getInstalledBase(organizationNumber, partyId, modifiedFrom);
	}
}
