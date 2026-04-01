package se.sundsvall.installedbase.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import se.sundsvall.installedbase.api.model.InstalledBaseParameters;
import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.api.model.InstalledBases;
import se.sundsvall.installedbase.integration.datawarehousereader.DataWarehouseReaderClient;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toCustomerEngagements;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseCustomer;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBaseResponse;
import static se.sundsvall.installedbase.service.mapper.InstalledBaseMapper.toInstalledBases;

@Service
public class InstalledBaseService {

	private static final int DATAWAREHOUSEREADER_PAGE_LIMIT = 100;
	private static final int DATAWAREHOUSEREADER_PAGE = 1;
	private static final String DATAWAREHOUSEREADER_SORTBY_PROPERTY = "facilityId";

	private final DataWarehouseReaderClient dataWarehouseReaderClient;

	public InstalledBaseService(DataWarehouseReaderClient dataWarehouseReaderClient) {
		this.dataWarehouseReaderClient = dataWarehouseReaderClient;
	}

	public InstalledBases getInstalledBaseByPartyId(final String municipalityId, final InstalledBaseParameters parameters) {
		final var organizationIds = ofNullable(parameters.getOrganizationIds())
			.map(ids -> String.join(",", ids))
			.orElse(null);

		return toInstalledBases(dataWarehouseReaderClient.getInstalledBaseByPartyId(municipalityId,
			parameters.getPartyId(),
			organizationIds,
			parameters.getDate(),
			parameters.getSortBy(),
			parameters.getPage(),
			parameters.getLimit()));
	}

	public InstalledBaseResponse getInstalledBase(String municipalityId, String organizationNumber, List<String> partyIds, LocalDate modifiedFrom) {
		final var customerEngagements = toCustomerEngagements(dataWarehouseReaderClient.getCustomerEngagement(municipalityId, organizationNumber, partyIds));

		return toInstalledBaseResponse(customerEngagements.stream()
			.map(engagement -> toInstalledBaseCustomer(engagement,
				getInstalledBase(municipalityId, engagement.getCustomerNumber(), engagement.getOrganizationName(), modifiedFrom, DATAWAREHOUSEREADER_PAGE, DATAWAREHOUSEREADER_PAGE_LIMIT)))
			.toList());
	}

	private generated.se.sundsvall.datawarehousereader.InstalledBaseResponse getInstalledBase(String municipalityId, String customerNumber, String company, LocalDate modifiedFrom, int page, int limit) {
		final var installedBaseResponse = dataWarehouseReaderClient.getInstalledBase(municipalityId, customerNumber, company, modifiedFrom, page, limit, DATAWAREHOUSEREADER_SORTBY_PROPERTY);

		var currentPage = page;

		if (allNotNull(installedBaseResponse, installedBaseResponse.getMeta(), installedBaseResponse.getMeta().getTotalPages()) && (installedBaseResponse.getMeta().getTotalPages() > currentPage)) {
			while (installedBaseResponse.getMeta().getTotalPages() > currentPage) {
				installedBaseResponse.getInstalledBase().addAll(
					dataWarehouseReaderClient.getInstalledBase(municipalityId, customerNumber, company, modifiedFrom, ++currentPage, limit, DATAWAREHOUSEREADER_SORTBY_PROPERTY).getInstalledBase());
			}
		}
		return installedBaseResponse;
	}

}
