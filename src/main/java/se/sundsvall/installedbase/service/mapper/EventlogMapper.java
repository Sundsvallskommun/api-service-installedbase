package se.sundsvall.installedbase.service.mapper;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.lowerCase;

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
	private static final String SOURCE_TYPE = "Delegation";
	private static final String DELEGATION_ID = "DelegationId";
	private static final String FACILITIES = "Facilities";
	private static final String X_SENT_BY = "X-Sent-By";
	private static final String OWNER = "DelegationOwner";
	private static final String DELEGATED_TO = "DelegatedTo";
	private static final String REQUEST_ID = "RequestId";
	private static final String MESSAGE = "%s facility delegation"; // Inserts the event type (e.g., CREATE, UPDATE, DELETE)

	private EventlogMapper() {}

	/**
	 * Creates an Event object for facility delegation events.
	 * Conditionally includes the Identifier header as metadata if available.
	 *
	 * @param  delegationId the ID of the delegation
	 * @param  owner        the owner of the facility delegation
	 * @param  delegatedTo  the delegate
	 * @param  eventType    the type of event (e.g., CREATE, UPDATE, DELETE)
	 * @return              an Event object populated with the provided details
	 */
	public static Event toEvent(String delegationId, String owner, String delegatedTo, List<String> facilityIds, EventType eventType) {

		final var metadata = new HashMap<>(Map.of(
			DELEGATION_ID, delegationId,
			OWNER, owner,
			DELEGATED_TO, delegatedTo,
			FACILITIES, toReadableString(facilityIds)));

		// As the identifier & request-id might be null, we only conditionally add it to the metadata
		getIdentifierHeaderValue().ifPresent(header -> metadata.put(X_SENT_BY, header));
		getRequestId().ifPresent(id -> metadata.put(REQUEST_ID, id));

		return new Event()
			.expires(OffsetDateTime.now().plusMonths(18))
			.type(eventType)
			.message(MESSAGE.formatted(capitalize(lowerCase(eventType.toString()))))
			.owner(EVENT_OWNER)
			.sourceType(SOURCE_TYPE)
			.metadata(toMetadatas(metadata));
	}

	private static String toReadableString(List<String> facilityIds) {
		return String.join(", ", ofNullable(facilityIds).orElse(emptyList()).stream()
			.sorted()
			.toList());
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

	/**
	 * Retrieves the Request ID if available.
	 *
	 * @return an Optional containing the Request ID
	 */
	private static Optional<String> getRequestId() {
		return ofNullable(RequestId.get());
	}
}
