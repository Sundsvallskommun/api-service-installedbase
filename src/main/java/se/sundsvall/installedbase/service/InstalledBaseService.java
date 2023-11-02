package se.sundsvall.installedbase.service;

import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toCustomerEngagements;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseCustomer;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseResponse;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.integration.datawarehousereader.DataWarehouseReaderClient;

@Service
public class InstalledBaseService {

	private static final int DATAWAREHOUSEREADER_INSTALLEDBASE_PAGE_LIMIT  = 100;
	private static final int DATAWAREHOUSEREADER_INSTALLEDBASE_PAGE = 1;

	@Autowired
	private DataWarehouseReaderClient dataWarehouseReaderClient;

	public InstalledBaseResponse getInstalledBase(String organizationNumber, List<String> partyIds, LocalDate modifiedFrom) {
		final var customerEngagements = toCustomerEngagements(dataWarehouseReaderClient.getCustomerEngagement(organizationNumber, partyIds));

		return toInstalledBaseResponse(customerEngagements.stream()
			.map(engagement -> toInstalledBaseCustomer(engagement, getInstalledBase(engagement.getCustomerNumber(), engagement.getOrganizationName(), modifiedFrom,
				DATAWAREHOUSEREADER_INSTALLEDBASE_PAGE, DATAWAREHOUSEREADER_INSTALLEDBASE_PAGE_LIMIT)))
			.toList());
	}

	private generated.se.sundsvall.datawarehousereader.InstalledBaseResponse getInstalledBase(String customerNumber, String company, LocalDate modifiedFrom, int page, int limit) {
		var installedBaseResponse = dataWarehouseReaderClient.getInstalledBase(customerNumber, company, modifiedFrom, page, limit);

		var currentPage = page;

		if (allNotNull(installedBaseResponse, installedBaseResponse.getMeta(), installedBaseResponse.getMeta().getTotalPages()) && installedBaseResponse.getMeta().getTotalPages() > currentPage) {
			while (installedBaseResponse.getMeta().getTotalPages() > currentPage) {
				installedBaseResponse.getInstalledBase().addAll(dataWarehouseReaderClient.getInstalledBase(customerNumber, company, modifiedFrom, ++currentPage, limit).getInstalledBase());
			}
		}
		return installedBaseResponse;
	}
}
