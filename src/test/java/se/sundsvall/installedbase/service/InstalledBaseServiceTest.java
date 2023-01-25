package se.sundsvall.installedbase.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import generated.se.sundsvall.datawarehousereader.CustomerEngagement;
import generated.se.sundsvall.datawarehousereader.CustomerEngagementResponse;
import generated.se.sundsvall.datawarehousereader.MetaData;
import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.integration.datawarehousereader.DataWarehouseReaderClient;

@ExtendWith(MockitoExtension.class)
class InstalledBaseServiceTest {

	@Mock
	private DataWarehouseReaderClient clientMock;

	@Mock
	private CustomerEngagementResponse customerEngagementResponseMock;

	@Mock
	private CustomerEngagement customerEngagementMock;

	@Mock
	private MetaData metaDataMock;

	@Mock
	private generated.se.sundsvall.datawarehousereader.InstalledBaseResponse installedBaseResponseMock;

	@Mock
	private generated.se.sundsvall.datawarehousereader.InstalledBaseItem installedBaseItemMock;

	@InjectMocks
	private InstalledBaseService service;

	@Test
	void test() {

		// Setup
		final var partyId = List.of("partyId");
		final var organizationNumber = "5512345678";
		final var customerNumber = "customerNumber";
		final var organizationName = "organizationName";

		// Mock
		when(metaDataMock.getCount()).thenReturn(1);

		when(clientMock.getCustomerEngagement(any(), any())).thenReturn(customerEngagementResponseMock);
		when(customerEngagementResponseMock.getMeta()).thenReturn(metaDataMock);
		when(customerEngagementResponseMock.getCustomerEngagements()).thenReturn(List.of(customerEngagementMock));
		when(customerEngagementMock.getCustomerNumber()).thenReturn(customerNumber);
		when(customerEngagementMock.getOrganizationName()).thenReturn(organizationName);

		when(clientMock.getInstalledBase(customerNumber, organizationName)).thenReturn(installedBaseResponseMock);
		when(installedBaseResponseMock.getInstalledBase()).thenReturn(List.of(installedBaseItemMock));

		// Call
		final var response = service.getInstalledBase(organizationNumber, partyId);

		// Verifications and assertions
		verify(clientMock).getCustomerEngagement("5512345678", partyId);
		verify(clientMock).getInstalledBase(customerNumber, organizationName);

		assertThat(response)
			.hasFieldOrProperty("installedBaseCustomers")
			.extracting(InstalledBaseResponse::getInstalledBaseCustomers).asList().hasSize(1);
	}
}
