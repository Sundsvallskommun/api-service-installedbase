package se.sundsvall.installedbase.service.mapper;

import generated.se.sundsvall.datawarehousereader.CustomerEngagement;
import generated.se.sundsvall.datawarehousereader.CustomerEngagementResponse;
import java.util.Collections;
import java.util.List;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;
import se.sundsvall.dept44.problem.Problem;
import se.sundsvall.installedbase.api.model.InstalledBase;
import se.sundsvall.installedbase.api.model.InstalledBaseCustomer;
import se.sundsvall.installedbase.api.model.InstalledBaseItem;
import se.sundsvall.installedbase.api.model.InstalledBaseItemAddress;
import se.sundsvall.installedbase.api.model.InstalledBaseItemMetaData;
import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.api.model.InstalledBases;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class InstalledBaseMapper {

	private static final String ERROR_NO_CUSTOMER_ENGAGEMENTS_FOUND = "No customer engagements matched the search criteria!";

	private InstalledBaseMapper() {}

	public static InstalledBases toInstalledBases(generated.se.sundsvall.datawarehousereader.InstalledBaseResponse response) {
		return new InstalledBases()
			.withInstalledBases(toInstalledBaseList(response))
			.withMetaData(toMetaData(response.getMeta()));
	}

	private static List<InstalledBase> toInstalledBaseList(generated.se.sundsvall.datawarehousereader.InstalledBaseResponse response) {
		return ofNullable(response.getInstalledBase()).orElse(Collections.emptyList()).stream()
			.map(InstalledBaseMapper::toInstalledBase)
			.toList();
	}

	private static InstalledBase toInstalledBase(generated.se.sundsvall.datawarehousereader.InstalledBaseItem item) {
		return InstalledBase.create()
			.withCompany(item.getCompany())
			.withCustomerId(item.getCustomerNumber())
			.withType(item.getType())
			.withFacilityId(item.getFacilityId())
			.withPlacementId(ofNullable(item.getPlacementId()).map(String::valueOf).orElse(null))
			.withCareOf(item.getCareOf())
			.withStreet(item.getStreet())
			.withPostCode(item.getPostCode())
			.withCity(item.getCity())
			.withPropertyDesignation(item.getPropertyDesignation())
			.withDateFrom(item.getDateFrom())
			.withDateTo(item.getDateTo())
			.withDateLatestModified(item.getDateLastModified());
	}

	private static PagingAndSortingMetaData toMetaData(generated.se.sundsvall.datawarehousereader.PagingAndSortingMetaData meta) {
		if (meta == null) {
			return null;
		}
		final var metaData = new PagingAndSortingMetaData();
		ofNullable(meta.getPage()).ifPresent(metaData::setPage);
		ofNullable(meta.getLimit()).ifPresent(metaData::setLimit);
		ofNullable(meta.getCount()).ifPresent(metaData::setCount);
		ofNullable(meta.getTotalRecords()).ifPresent(metaData::setTotalRecords);
		ofNullable(meta.getTotalPages()).ifPresent(metaData::setTotalPages);
		return metaData;
	}

	public static List<CustomerEngagement> toCustomerEngagements(CustomerEngagementResponse response) {
		if (response.getMeta().getCount() == 0) {
			throw Problem.valueOf(NOT_FOUND, ERROR_NO_CUSTOMER_ENGAGEMENTS_FOUND);
		}

		return response.getCustomerEngagements();
	}

	public static InstalledBaseResponse toInstalledBaseResponse(List<InstalledBaseCustomer> installedBaseCustomers) {
		return InstalledBaseResponse.create().withInstalledBaseCustomers(installedBaseCustomers);
	}

	public static InstalledBaseCustomer toInstalledBaseCustomer(CustomerEngagement customerEngagement, generated.se.sundsvall.datawarehousereader.InstalledBaseResponse installedBaseResponse) {
		return InstalledBaseCustomer.create()
			.withCustomerNumber(customerEngagement.getCustomerNumber())
			.withPartyId(customerEngagement.getPartyId())
			.withItems(toInstalledBaseItems(installedBaseResponse));
	}

	private static List<InstalledBaseItem> toInstalledBaseItems(generated.se.sundsvall.datawarehousereader.InstalledBaseResponse response) {
		return ofNullable(response.getInstalledBase()).orElse(Collections.emptyList()).stream()
			.map(InstalledBaseMapper::toInstalledBaseItem)
			.toList();
	}

	private static InstalledBaseItem toInstalledBaseItem(generated.se.sundsvall.datawarehousereader.InstalledBaseItem installedBaseItem) {
		return InstalledBaseItem.create()
			.withAddress(toAddress(installedBaseItem))
			.withFacilityCommitmentEndDate(installedBaseItem.getDateTo())
			.withFacilityCommitmentStartDate(installedBaseItem.getDateFrom())
			.withFacilityId(installedBaseItem.getFacilityId())
			.withLastModifiedDate(installedBaseItem.getDateLastModified())
			.withMetaData(toMetaData(installedBaseItem.getMetaData()))
			.withPlacementId(installedBaseItem.getPlacementId())
			.withType(installedBaseItem.getType());
	}

	private static InstalledBaseItemAddress toAddress(generated.se.sundsvall.datawarehousereader.InstalledBaseItem installedBaseItem) {
		return InstalledBaseItemAddress.create()
			.withCareOf(installedBaseItem.getCareOf())
			.withCity(installedBaseItem.getCity())
			.withPostalCode(installedBaseItem.getPostCode())
			.withStreet(installedBaseItem.getStreet())
			.withPropertyDesignation(installedBaseItem.getPropertyDesignation());
	}

	private static List<InstalledBaseItemMetaData> toMetaData(List<generated.se.sundsvall.datawarehousereader.InstalledBaseItemMetaData> metaData) {
		return ofNullable(metaData).orElse(Collections.emptyList()).stream()
			.map(InstalledBaseMapper::toMetaData)
			.toList();
	}

	private static InstalledBaseItemMetaData toMetaData(generated.se.sundsvall.datawarehousereader.InstalledBaseItemMetaData metaData) {
		return InstalledBaseItemMetaData.create()
			.withDisplayName(metaData.getDisplayName())
			.withKey(metaData.getKey())
			.withType(metaData.getType())
			.withValue(metaData.getValue());
	}
}
