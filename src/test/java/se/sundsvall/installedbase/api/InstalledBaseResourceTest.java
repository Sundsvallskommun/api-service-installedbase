package se.sundsvall.installedbase.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class InstalledBaseResourceTest {

	@MockBean
	private InstalledBaseService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getinstalledBase() {

		// Parameters
		final var partyId = List.of(UUID.randomUUID().toString());
		final var organizationNumber = "5566112233";
		final var customerNumber = "12345";

		// Mock
		final var response = InstalledBaseResponse.create().withInstalledBaseCustomers(List.of(InstalledBaseCustomer.create().withCustomerNumber(customerNumber)));
		when(serviceMock.getInstalledBase(organizationNumber, partyId)).thenReturn(response);

		// Call
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/installedbase/{organizationNumber}")
			.queryParam("partyId", partyId)
			.build(organizationNumber))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(InstalledBaseResponse.class)
			.isEqualTo(response);

		// Verify call to mock
		verify(serviceMock).getInstalledBase(organizationNumber, partyId);
	}
}
