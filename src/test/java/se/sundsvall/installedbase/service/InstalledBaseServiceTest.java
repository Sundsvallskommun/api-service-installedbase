package se.sundsvall.installedbase.service;

import generated.se.sundsvall.datawarehousereader.CustomerEngagement;
import generated.se.sundsvall.datawarehousereader.CustomerEngagementResponse;
import generated.se.sundsvall.datawarehousereader.InstalledBaseItem;
import generated.se.sundsvall.datawarehousereader.InstalledBaseResponse;
import generated.se.sundsvall.datawarehousereader.PagingAndSortingMetaData;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.installedbase.api.model.InstalledBaseParameters;
import se.sundsvall.installedbase.integration.datawarehousereader.DataWarehouseReaderClient;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstalledBaseServiceTest {

	@Mock
	private DataWarehouseReaderClient clientMock;

	@Mock
	private CustomerEngagementResponse customerEngagementResponseMock;

	@Mock
	private CustomerEngagement customerEngagementMock;

	@Mock
	private PagingAndSortingMetaData customerEngagementMetaDataMock;

	@Mock
	private generated.se.sundsvall.datawarehousereader.InstalledBaseResponse installedBaseResponseMock;

	@Mock
	private generated.se.sundsvall.datawarehousereader.InstalledBaseItem installedBaseItemMock;

	@Mock
	private PagingAndSortingMetaData installedBaseMetaDataMock;

	@InjectMocks
	private InstalledBaseService service;

	@Test
	void getInstalledBaseByPartyId_delegatesToClientAndMaps() {
		// given
		final var municipalityId = "2281";
		final var partyId = "898B3634-A2F9-483C-8808-37F3F25CF24E";
		final var organizationIds = List.of("123456789", "987654321");
		final var date = LocalDate.of(2025, 6, 1);
		final var sortBy = "Company";
		final var page = 1;
		final var limit = 15;

		final var parameters = InstalledBaseParameters.create()
			.withPartyId(partyId)
			.withOrganizationIds(organizationIds)
			.withDate(date)
			.withSortBy(sortBy);
		parameters.setPage(page);
		parameters.setLimit(limit);

		final var clientResponse = new InstalledBaseResponse()
			.meta(new PagingAndSortingMetaData().page(1).limit(15).count(1).totalRecords(1L).totalPages(1))
			.installedBase(List.of(new InstalledBaseItem().company("TestCompany").customerNumber("123").facilityId("fac1")));

		when(clientMock.getInstalledBaseByPartyId(any(), any(), any(), any(), any(), any(), any())).thenReturn(clientResponse);

		// when
		final var result = service.getInstalledBaseByPartyId(municipalityId, parameters);

		// then
		verify(clientMock).getInstalledBaseByPartyId(municipalityId, partyId, "123456789,987654321", date, sortBy, page, limit);
		assertThat(result).isNotNull();
		assertThat(result.getInstalledBaseList()).hasSize(1);
		assertThat(result.getInstalledBaseList().getFirst().getCompany()).isEqualTo("TestCompany");
		assertThat(result.getMetaData()).isNotNull();
		assertThat(result.getMetaData().getTotalRecords()).isEqualTo(1L);
	}

	@ParameterizedTest
	@ValueSource(strings = "2020-06-01")
	@NullSource
	void test(String modifiedFromDate) {

		// Arrange
		final var municipalityId = "municipalityId";
		final var partyId = List.of("partyId");
		final var organizationNumber = "5512345678";
		final var customerNumber = "customerNumber";
		final var organizationName = "organizationName";
		final var modifiedFrom = ofNullable(modifiedFromDate).map(LocalDate::parse).orElse(null);
		final var page = 1;
		final var limit = 100;
		final var sortBy = "facilityId";

		// Mock
		when(customerEngagementMetaDataMock.getCount()).thenReturn(1);

		when(clientMock.getCustomerEngagement(any(), any(), any())).thenReturn(customerEngagementResponseMock);
		when(customerEngagementResponseMock.getMeta()).thenReturn(customerEngagementMetaDataMock);
		when(customerEngagementResponseMock.getCustomerEngagements()).thenReturn(List.of(customerEngagementMock));
		when(customerEngagementMock.getCustomerNumber()).thenReturn(customerNumber);
		when(customerEngagementMock.getOrganizationName()).thenReturn(organizationName);

		when(clientMock.getInstalledBase(any(), any(), any(), any(), anyInt(), anyInt(), any())).thenReturn(installedBaseResponseMock);
		when(installedBaseResponseMock.getInstalledBase()).thenReturn(List.of(installedBaseItemMock));
		when(installedBaseResponseMock.getMeta()).thenReturn(installedBaseMetaDataMock);
		when(installedBaseMetaDataMock.getTotalPages()).thenReturn(1);

		// Call
		final var response = service.getInstalledBase(municipalityId, organizationNumber, partyId, modifiedFrom);

		// Verifications and assertions
		verify(clientMock).getCustomerEngagement(municipalityId, "5512345678", partyId);
		verify(clientMock).getInstalledBase(municipalityId, customerNumber, organizationName, modifiedFrom, page, limit, sortBy);

		assertThat(response)
			.hasFieldOrProperty("installedBaseCustomers")
			.extracting(se.sundsvall.installedbase.api.model.InstalledBaseResponse::getInstalledBaseCustomers).asInstanceOf(LIST).hasSize(1);
	}
}
