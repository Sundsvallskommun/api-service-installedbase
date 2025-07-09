package se.sundsvall.installedbase.service.mapper;

import static java.util.Optional.ofNullable;

import generated.se.sundsvall.eventlog.Event;
import generated.se.sundsvall.eventlog.EventType;
import generated.se.sundsvall.eventlog.Metadata;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import se.sundsvall.dept44.requestid.RequestId;
import se.sundsvall.dept44.support.Identifier;

public final class EventlogMapper {

	private static final String EVENT_OWNER = "InstalledBase";
	private static final String SOURCE_TYPE = "FacilityDelegation";
	private static final String FACILITY_DELEGATION_ID = "FacilityDelegationId";
	private static final String X_SENT_BY = "X-Sent-By";
	private static final String OWNER = "DelegationOwner";
	private static final String DELEGATED_TO = "DelegatedTo";
	private static final String MESSAGE = "%s facility delegation"; // Inserts the event type (e.g., CREATED, UPDATED, DELETED)

	private EventlogMapper() {}

	/**
	 * Creates an Event object for facility delegation events.
	 * Conditionally includes the Identifier header as metadata if available.
	 * 
	 * @param  facilityDelegationId the ID of the facility delegation
	 * @param  owner                the owner of the facility delegation
	 * @param  delegatedTo          the delegate
	 * @param  eventType            the type of event (e.g., CREATED, UPDATED, DELETED)
	 * @return                      an Event object populated with the provided details
	 */
	public static Event toEvent(String facilityDelegationId, String owner, String delegatedTo, EventType eventType) {

		var metadata = new HashMap<>(Map.of(
			FACILITY_DELEGATION_ID, facilityDelegationId,
			OWNER, owner,
			DELEGATED_TO, delegatedTo));

		// As the identifier might be null, we only conditionally add it to the metadata
		getIdentifierHeaderValue().ifPresent(header -> metadata.put(X_SENT_BY, header));

		return new Event()
			.expires(OffsetDateTime.now().plusMonths(18))
			.type(eventType)
			.message(MESSAGE.formatted(eventType.toString()))
			.owner(EVENT_OWNER)
			.historyReference(RequestId.get())
			.sourceType(SOURCE_TYPE)
			.metadata(toMetadatas(metadata));
	}

	/**
	 * Converts a map of metadata into a list of Metadata objects.
	 *
	 * @param  metadata the metadata map
	 * @return          a list of Metadata objects
	 */
	private static List<Metadata> toMetadatas(Map<String, String> metadata) {
		return ofNullable(metadata)
			.orElse(Map.of())
			.entrySet()
			.stream()
			.map(entry -> toMetadata(entry.getKey(), entry.getValue()))
			.toList();
	}

	private static Metadata toMetadata(String key, String value) {
		return new Metadata().key(key).value(value);
	}

	/**
	 * Retrieves the Identifier header value if available.
	 *
	 * @return an Optional containing the identifier header value
	 */
	private static Optional<String> getIdentifierHeaderValue() {
		return ofNullable(Identifier.get())
			.map(Identifier::toHeaderValue);
	}
}
