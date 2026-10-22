package se.sundsvall.installedbase.integration.rabbitmq;

import generated.se.sundsvall.eventlog.Event;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.sundsvall.dept44.rabbitmq.Dept44RabbitPublisher;
import se.sundsvall.dept44.rabbitmq.OutgoingMessage;

/**
 * Publishes delegation events on the service's own exchange. Who consumes them, such as eventlog, is decided by the
 * consumers' bindings, not here.
 */
@Component
public class DelegationEventPublisher {

	private static final String ROUTING_KEY_PREFIX = "delegation.";

	private final Dept44RabbitPublisher publisher;
	private final String exchange;

	public DelegationEventPublisher(final Dept44RabbitPublisher publisher, @Value("${integration.rabbitmq.exchange}") final String exchange) {
		this.publisher = publisher;
		this.exchange = exchange;
	}

	/**
	 * Returns once the broker has taken the event. The routing key carries the event type, such as
	 * {@code delegation.create}.
	 *
	 * @param municipalityId the municipality of the delegation
	 * @param logKey         the log key the event is stored under, the delegation id
	 * @param event          the event
	 */
	public void publish(final String municipalityId, final String logKey, final Event event) {
		publisher.publish(OutgoingMessage.to(exchange, ROUTING_KEY_PREFIX + event.getType().getValue().toLowerCase(Locale.ROOT))
			.withPayload(new DelegationEvent(municipalityId, logKey, event)));
	}
}
