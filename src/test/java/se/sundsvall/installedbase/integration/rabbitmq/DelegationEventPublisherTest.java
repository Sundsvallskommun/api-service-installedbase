package se.sundsvall.installedbase.integration.rabbitmq;

import generated.se.sundsvall.eventlog.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.dept44.rabbitmq.Dept44RabbitPublisher;
import se.sundsvall.dept44.rabbitmq.OutgoingMessage;
import se.sundsvall.dept44.rabbitmq.RabbitPublishException;

import static generated.se.sundsvall.eventlog.EventType.CREATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DelegationEventPublisherTest {

	private static final String EXCHANGE = "api-fabriken.installedbase";

	@Mock
	private Dept44RabbitPublisher mockPublisher;

	@Captor
	private ArgumentCaptor<OutgoingMessage> messageCaptor;

	@ParameterizedTest
	@EnumSource(generated.se.sundsvall.eventlog.EventType.class)
	void publishesOnTheOwnExchangeWithTheEventTypeInTheRoutingKey(final generated.se.sundsvall.eventlog.EventType type) {
		final var event = new Event().type(type).message("message");

		new DelegationEventPublisher(mockPublisher, EXCHANGE).publish("2281", "delegation-id", event);

		verify(mockPublisher).publish(messageCaptor.capture());
		final var message = messageCaptor.getValue();
		assertThat(message.getExchange()).isEqualTo(EXCHANGE);
		assertThat(message.getRoutingKey()).isEqualTo("delegation." + type.getValue().toLowerCase());
		assertThat(message.getPayload()).isEqualTo(new DelegationEvent("2281", "delegation-id", event));
	}

	@Test
	void aFailedPublishIsPassedOn() {
		final var exception = new RabbitPublishException(RabbitPublishException.Reason.RETURNED, "message-id", "returned");
		doThrow(exception).when(mockPublisher).publish(any());
		final var publisher = new DelegationEventPublisher(mockPublisher, EXCHANGE);
		final var event = new Event().type(CREATE);

		assertThatExceptionOfType(RabbitPublishException.class)
			.isThrownBy(() -> publisher.publish("2281", "delegation-id", event))
			.isSameAs(exception);
	}
}
