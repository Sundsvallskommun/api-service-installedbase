package se.sundsvall.installedbase.service;

import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toCustomerEngagements;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseCustomer;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.integration.datawarehousereader.DataWarehouseReaderClient;

@Service
public class InstalledBaseService {

	@Autowired
	private DataWarehouseReaderClient dataWarehouseReaderClient;

	public InstalledBaseResponse getInstalledBase(String organizationNumber, List<String> partyIds) {
		final var customerEngagements = toCustomerEngagements(dataWarehouseReaderClient.getCustomerEngagement(organizationNumber, partyIds));

		return toInstalledBaseResponse(customerEngagements.stream()
			.map(engagement -> toInstalledBaseCustomer(engagement, dataWarehouseReaderClient.getInstalledBase(engagement.getCustomerNumber(), engagement.getOrganizationName())))
			.toList());
	}
}
