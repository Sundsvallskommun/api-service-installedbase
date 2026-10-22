package se.sundsvall.installedbase.integration.rabbitmq;

import generated.se.sundsvall.eventlog.Event;

/**
 * A delegation event as published on RabbitMQ: the event in eventlog's format, together with the municipality and log
 * key that eventlog's REST API takes from the path.
 */
public record DelegationEvent(String municipalityId, String logKey, Event event) {
}
