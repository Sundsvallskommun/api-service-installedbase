package se.sundsvall.installedbase.service.mapper;

import static java.util.Optional.ofNullable;
import static org.zalando.problem.Status.NOT_FOUND;

import java.util.Collections;
import java.util.List;

import org.zalando.problem.Problem;

import generated.se.sundsvall.datawarehousereader.CustomerEngagement;
import generated.se.sundsvall.datawarehousereader.CustomerEngagementResponse;
import se.sundsvall.installedbase.api.model.InstalledBaseCustomer;
import se.sundsvall.installedbase.api.model.InstalledBaseItem;
import se.sundsvall.installedbase.api.model.InstalledBaseItemAddress;
import se.sundsvall.installedbase.api.model.InstalledBaseItemMetaData;
import se.sundsvall.installedbase.api.model.InstalledBaseResponse;

public class InstalledBaseMapper {

	private static final String ERROR_NO_CUSTOMER_ENGAGEMENTS_FOUND = "No customer engagements matched the search criteria!";

	private InstalledBaseMapper() {}

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
