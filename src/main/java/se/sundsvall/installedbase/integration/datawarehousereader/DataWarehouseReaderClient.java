package se.sundsvall.installedbase.integration.datawarehousereader;

import generated.se.sundsvall.datawarehousereader.CustomerEngagementResponse;
import generated.se.sundsvall.datawarehousereader.InstalledBaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.sundsvall.installedbase.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static se.sundsvall.installedbase.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration.CLIENT_ID;

@FeignClient(name = CLIENT_ID, url = "${integration.datawarehousereader.url}", configuration = DataWarehouseReaderConfiguration.class)
public interface DataWarehouseReaderClient {

	@GetMapping(path = "customer/engagements", produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	CustomerEngagementResponse getCustomerEngagement(
		@RequestParam(value = "organizationNumber") String organizationNumber,
		@RequestParam(value = "partyId") List<String> partyIds);

	@GetMapping(path = "installedbase", produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	InstalledBaseResponse getInstalledBase(
		@RequestParam(value = "customerNumber") String customerNumber,
		@RequestParam(value = "company") String company,
		@RequestParam(value = "page") int page,
		@RequestParam(value = "limit") int limit);
}
