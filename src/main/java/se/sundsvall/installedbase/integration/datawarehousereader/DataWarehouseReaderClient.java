package se.sundsvall.installedbase.integration.datawarehousereader;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static se.sundsvall.installedbase.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration.CLIENT_ID;

import generated.se.sundsvall.datawarehousereader.CustomerEngagementResponse;
import generated.se.sundsvall.datawarehousereader.InstalledBaseResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDate;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import se.sundsvall.installedbase.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration;

@FeignClient(name = CLIENT_ID, url = "${integration.datawarehousereader.url}", configuration = DataWarehouseReaderConfiguration.class)
@CircuitBreaker(name = CLIENT_ID)
public interface DataWarehouseReaderClient {

	@GetMapping(path = "/{municipalityId}/customer/engagements", produces = {
		APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	})
	CustomerEngagementResponse getCustomerEngagement(
		@PathVariable("municipalityId") String municipalityId,
		@RequestParam(value = "organizationNumber") String organizationNumber,
		@RequestParam(value = "partyId") List<String> partyIds);

	@GetMapping(path = "/{municipalityId}/installedbase", produces = {
		APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	})
	InstalledBaseResponse getInstalledBase(
		@PathVariable("municipalityId") String municipalityId,
		@RequestParam(value = "customerNumber") String customerNumber,
		@RequestParam(value = "company") String company,
		@RequestParam(value = "lastModifiedDateFrom") @DateTimeFormat(iso = ISO.DATE) LocalDate modifiedFrom,
		@RequestParam(value = "page") int page,
		@RequestParam(value = "limit") int limit,
		@RequestParam(value = "sortBy") String sortBy);
}
