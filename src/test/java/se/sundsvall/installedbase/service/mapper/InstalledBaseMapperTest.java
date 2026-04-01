package se.sundsvall.installedbase.service.mapper;

import generated.se.sundsvall.datawarehousereader.CustomerEngagement;
import generated.se.sundsvall.datawarehousereader.CustomerEngagementResponse;
import generated.se.sundsvall.datawarehousereader.CustomerType;
import generated.se.sundsvall.datawarehousereader.InstalledBaseItem;
import generated.se.sundsvall.datawarehousereader.InstalledBaseItemMetaData;
import generated.se.sundsvall.datawarehousereader.InstalledBaseResponse;
import generated.se.sundsvall.datawarehousereader.PagingAndSortingMetaData;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.dept44.problem.ThrowableProblem;
import se.sundsvall.installedbase.api.model.InstalledBase;
import se.sundsvall.installedbase.api.model.InstalledBaseCustomer;
import se.sundsvall.installedbase.api.model.InstalledBaseItemAddress;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class InstalledBaseMapperTest {

	@Test
	void toCustomerEngagements() {
		final var response = createDataWarehouseReaderCustomerEngagementResponse(2);
		final var engagements = InstalledBaseMapper.toCustomerEngagements(response);

		assertThat(engagements).isEqualTo(response.getCustomerEngagements());
	}

	@Test
	void toCustomerEngagementWhenNoCustomerEngagementsReturned() {
		final var response = createDataWarehouseReaderCustomerEngagementResponse(0);
		final var e = assertThrows(ThrowableProblem.class, () -> InstalledBaseMapper.toCustomerEngagements(response));
		assertThat(e).hasMessage("Not Found: No customer engagements matched the search criteria!");
	}

	@Test
	void toInstalledBaseResponse() {
		final var installedBaseCustomers = List.of(InstalledBaseCustomer.create(), InstalledBaseCustomer.create());

		final var installedBaseResponse = InstalledBaseMapper.toInstalledBaseResponse(installedBaseCustomers);

		assertThat(installedBaseResponse).isNotNull();
		assertThat(installedBaseResponse.getInstalledBaseCustomers())
			.hasSize(2)
			.isEqualTo(installedBaseCustomers);
	}

	@Test
	void toInstalledBaseCustomer() {
		final var dataWarehouseReaderResponse = createDataWarehouseReaderInstalledBaseResponse(3);
		final var customerEngagement = new CustomerEngagement()
			.customerNumber("customerNumber")
			.organizationName("organizationName")
			.partyId("partyId");

		final var installedBaseCustomer = InstalledBaseMapper.toInstalledBaseCustomer(customerEngagement, dataWarehouseReaderResponse);

		assertThat(installedBaseCustomer.getCustomerNumber()).isEqualTo("customerNumber");
		assertThat(installedBaseCustomer.getItems())
			.hasSize(3)
			.extracting(
				se.sundsvall.installedbase.api.model.InstalledBaseItem::getFacilityId,
				se.sundsvall.installedbase.api.model.InstalledBaseItem::getPlacementId,
				se.sundsvall.installedbase.api.model.InstalledBaseItem::getType,
				se.sundsvall.installedbase.api.model.InstalledBaseItem::getFacilityCommitmentEndDate,
				se.sundsvall.installedbase.api.model.InstalledBaseItem::getFacilityCommitmentStartDate,
				se.sundsvall.installedbase.api.model.InstalledBaseItem::getLastModifiedDate)
			.containsExactlyInAnyOrder(
				tuple("facilityId0", 0, "type0", now().plusDays(0), now().minusDays(0), now()),
				tuple("facilityId1", 1, "type1", now().plusDays(1), now().minusDays(1), now()),
				tuple("facilityId2", 2, "type2", now().plusDays(2), now().minusDays(2), now()));

		assertThat(installedBaseCustomer.getItems())
			.extracting("address", InstalledBaseItemAddress.class)
			.containsExactlyInAnyOrder(
				InstalledBaseItemAddress.create()
					.withCareOf("careOf0")
					.withCity("city0")
					.withPostalCode("postCode0")
					.withStreet("street0")
					.withPropertyDesignation("propertyDesignation0"),
				InstalledBaseItemAddress.create()
					.withCareOf("careOf1")
					.withCity("city1")
					.withPostalCode("postCode1")
					.withStreet("street1")
					.withPropertyDesignation("propertyDesignation1"),
				InstalledBaseItemAddress.create()
					.withCareOf("careOf2")
					.withCity("city2")
					.withPostalCode("postCode2")
					.withStreet("street2")
					.withPropertyDesignation("propertyDesignation2"));

		assertThat(installedBaseCustomer.getItems())
			.extracting("metaData", List.class)
			.containsExactlyInAnyOrder(
				List.of(se.sundsvall.installedbase.api.model.InstalledBaseItemMetaData.create()
					.withDisplayName("displayName00").withKey("key00").withType("type00").withValue("value00"),
					se.sundsvall.installedbase.api.model.InstalledBaseItemMetaData.create()
						.withDisplayName("displayName01").withKey("key01").withType("type01").withValue("value01")),
				List.of(se.sundsvall.installedbase.api.model.InstalledBaseItemMetaData.create()
					.withDisplayName("displayName10").withKey("key10").withType("type10").withValue("value10"),
					se.sundsvall.installedbase.api.model.InstalledBaseItemMetaData.create()
						.withDisplayName("displayName11").withKey("key11").withType("type11").withValue("value11")),
				List.of(se.sundsvall.installedbase.api.model.InstalledBaseItemMetaData.create()
					.withDisplayName("displayName20").withKey("key20").withType("type20").withValue("value20"),
					se.sundsvall.installedbase.api.model.InstalledBaseItemMetaData.create()
						.withDisplayName("displayName21").withKey("key21").withType("type21").withValue("value21")));
	}

	@Nested
	class ToInstalledBases {

		@Test
		void toInstalledBases_withItems_mapsCorrectly() {
			// given
			final var response = createDataWarehouseReaderInstalledBaseResponse(2);
			response.getMeta()
				.page(1)
				.limit(10)
				.count(2)
				.totalRecords(2L)
				.totalPages(1);

			// when
			final var result = InstalledBaseMapper.toInstalledBases(response);

			// then
			assertThat(result).isNotNull();
			assertThat(result.getMetaData()).isNotNull();
			assertThat(result.getMetaData().getPage()).isEqualTo(1);
			assertThat(result.getMetaData().getLimit()).isEqualTo(10);
			assertThat(result.getMetaData().getCount()).isEqualTo(2);
			assertThat(result.getMetaData().getTotalRecords()).isEqualTo(2L);
			assertThat(result.getMetaData().getTotalPages()).isEqualTo(1);

			assertThat(result.getInstalledBases()).hasSize(2)
				.extracting(
					InstalledBase::getCompany,
					InstalledBase::getCustomerId,
					InstalledBase::getType,
					InstalledBase::getFacilityId,
					InstalledBase::getPlacementId,
					InstalledBase::getCareOf,
					InstalledBase::getStreet,
					InstalledBase::getPostCode,
					InstalledBase::getCity,
					InstalledBase::getPropertyDesignation,
					InstalledBase::getDateFrom,
					InstalledBase::getDateTo,
					InstalledBase::getDateLatestModified)
				.containsExactly(
					tuple("company0", "1110", "type0", "facilityId0", "0", "careOf0", "street0", "postCode0", "city0", "propertyDesignation0", now(), now(), now()),
					tuple("company1", "1110", "type1", "facilityId1", "1", "careOf1", "street1", "postCode1", "city1", "propertyDesignation1", now().minusDays(1), now().plusDays(1), now()));
		}

		@Test
		void toInstalledBases_withNullInstalledBaseList_returnsEmptyList() {
			// given
			final var response = new InstalledBaseResponse()
				.meta(new PagingAndSortingMetaData().count(0))
				.installedBase(null);

			// when
			final var result = InstalledBaseMapper.toInstalledBases(response);

			// then
			assertThat(result).isNotNull();
			assertThat(result.getInstalledBases()).isEmpty();
		}

		@Test
		void toInstalledBases_withNullMeta_returnsNullMetaData() {
			// given
			final var response = new InstalledBaseResponse()
				.meta(null)
				.installedBase(List.of());

			// when
			final var result = InstalledBaseMapper.toInstalledBases(response);

			// then
			assertThat(result).isNotNull();
			assertThat(result.getMetaData()).isNull();
			assertThat(result.getInstalledBases()).isEmpty();
		}

		@Test
		void toInstalledBases_withNullPlacementId_mapsPlacementIdToNull() {
			// given
			final var item = new InstalledBaseItem()
				.company("company")
				.customerNumber("customer")
				.placementId(null);
			final var response = new InstalledBaseResponse()
				.meta(new PagingAndSortingMetaData())
				.installedBase(List.of(item));

			// when
			final var result = InstalledBaseMapper.toInstalledBases(response);

			// then
			assertThat(result.getInstalledBases()).hasSize(1);
			assertThat(result.getInstalledBases().getFirst().getPlacementId()).isNull();
		}
	}

	private CustomerEngagementResponse createDataWarehouseReaderCustomerEngagementResponse(int count) {
		return new CustomerEngagementResponse()
			.meta(new PagingAndSortingMetaData().count(count))
			.customerEngagements(createDataWarehouseReaderCustomerEngagements(count));
	}

	private List<CustomerEngagement> createDataWarehouseReaderCustomerEngagements(int count) {
		List<CustomerEngagement> engagements = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			engagements.add(new CustomerEngagement()
				.customerNumber("111" + i)
				.customerType(CustomerType.PRIVATE)
				.organizationName("organizationName")
				.organizationNumber("organizationNumber")
				.partyId("partyId"));
		}
		return engagements;
	}

	private InstalledBaseResponse createDataWarehouseReaderInstalledBaseResponse(int count) {
		return new InstalledBaseResponse()
			.meta(new PagingAndSortingMetaData().count(count))
			.installedBase(createDataWarehouseReaderInstalledBase(count));
	}

	private List<InstalledBaseItem> createDataWarehouseReaderInstalledBase(int count) {
		List<InstalledBaseItem> installedBase = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			installedBase.add(new InstalledBaseItem()
				.metaData(createDataWarehouseReaderMetaData(i))
				.careOf("careOf" + i)
				.city("city" + i)
				.company("company" + i)
				.customerNumber("1110")
				.dateFrom(now().minusDays(i))
				.dateTo(now().plusDays(i))
				.dateLastModified(now())
				.facilityId("facilityId" + i)
				.placementId(i)
				.postCode("postCode" + i)
				.street("street" + i)
				.propertyDesignation("propertyDesignation" + i)
				.type("type" + i));
		}

		return installedBase;
	}

	private List<InstalledBaseItemMetaData> createDataWarehouseReaderMetaData(int count) {
		return List.of(
			createDataWarehouseReaderInstalledBaseItemMetaData(count, 0),
			createDataWarehouseReaderInstalledBaseItemMetaData(count, 1));
	}

	private InstalledBaseItemMetaData createDataWarehouseReaderInstalledBaseItemMetaData(int item, int position) {
		return new InstalledBaseItemMetaData()
			.displayName("displayName".concat(String.valueOf(item)).concat(String.valueOf(position)))
			.key("key".concat(String.valueOf(item)).concat(String.valueOf(position)))
			.type("type".concat(String.valueOf(item)).concat(String.valueOf(position)))
			.value("value".concat(String.valueOf(item)).concat(String.valueOf(position)));
	}
}
