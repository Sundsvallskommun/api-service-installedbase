package se.sundsvall.installedbase.integration.eventlog;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static se.sundsvall.installedbase.integration.eventlog.configuration.EventlogConfiguration.CLIENT_ID;

import generated.se.sundsvall.eventlog.Event;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.sundsvall.installedbase.integration.eventlog.configuration.EventlogConfiguration;

@FeignClient(name = CLIENT_ID, url = "${integration.eventlog.url}", configuration = EventlogConfiguration.class)
@CircuitBreaker(name = CLIENT_ID)
public interface EventLogClient {

	/**
	 * Create a log event.
	 *
	 * @param municipalityId Municipality ID of the event
	 * @param logKey         UUID to create event for
	 * @param event          the event to create
	 */
	@PostMapping(path = "/{municipalityId}/{logKey}", consumes = APPLICATION_JSON_VALUE, produces = ALL_VALUE)
	ResponseEntity<Void> createEvent(@PathVariable("municipalityId") String municipalityId, @PathVariable("logKey") String logKey, @RequestBody Event event);
}
