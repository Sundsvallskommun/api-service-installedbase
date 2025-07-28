package se.sundsvall.installedbase.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import generated.se.sundsvall.eventlog.EventType;
import generated.se.sundsvall.eventlog.Metadata;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.dept44.requestid.RequestId;
import se.sundsvall.dept44.support.Identifier;

@ExtendWith(MockitoExtension.class)
class EventlogMapperTest {

	@EnumSource(EventType.class)
	@ParameterizedTest
	void testToEvent(EventType eventType) {
		final var delegationId = UUID.randomUUID().toString();
		final var owner = UUID.randomUUID().toString();
		final var delegatedTo = UUID.randomUUID().toString();
		final var facilityIds = List.of("facility-1", "facility-2");
		final var requestId = UUID.randomUUID().toString();
		final var identifierHeader = "joe001doe; type=adAccount";
		final var mockIdentifier = mock(Identifier.class);

		try (var mockedRequestId = mockStatic(RequestId.class);
			var mockedStaticIdentifier = mockStatic(Identifier.class)) {
			mockedRequestId.when(RequestId::get).thenReturn(requestId);
			mockedStaticIdentifier.when(Identifier::get).thenReturn(mockIdentifier);

			when(mockIdentifier.toHeaderValue()).thenReturn(identifierHeader);

			final var event = EventlogMapper.toEvent(delegationId, owner, delegatedTo, facilityIds, eventType);

			assertThat(event.getExpires()).isCloseTo(OffsetDateTime.now().plusMonths(18), within(1, ChronoUnit.SECONDS));
			assertThat(event.getType()).isEqualTo(eventType);
			assertThat(event.getMessage()).isEqualTo(StringUtils.capitalize(eventType.name().toLowerCase()) + " facility delegation");
			assertThat(event.getOwner()).isEqualTo("InstalledBase");
			assertThat(event.getHistoryReference()).isNull();
			assertThat(event.getSourceType()).isEqualTo("Delegation");
			assertThat(event.getMetadata()).hasSize(6);
			assertThat(event.getMetadata()).extracting(Metadata::getKey, Metadata::getValue)
				.containsExactlyInAnyOrder(
					tuple("DelegationId", delegationId),
					tuple("DelegationOwner", owner),
					tuple("DelegatedTo", delegatedTo),
					tuple("Facilities", String.join(", ", facilityIds)),
					tuple("X-Sent-By", identifierHeader),
					tuple("RequestId", requestId));
		}

		verify(mockIdentifier).toHeaderValue();
		verifyNoMoreInteractions(mockIdentifier);
	}

	@Test
	void testToEventWithoutIdentifier() {
		final var delegationId = UUID.randomUUID().toString();
		final var owner = UUID.randomUUID().toString();
		final var delegatedTo = UUID.randomUUID().toString();
		final var requestId = UUID.randomUUID().toString();

		try (var mockedRequestId = mockStatic(RequestId.class);
			var mockedIdentifier = mockStatic(Identifier.class)) {
			mockedRequestId.when(RequestId::get).thenReturn(requestId);
			mockedIdentifier.when(Identifier::get).thenReturn(null);

			final var event = EventlogMapper.toEvent(delegationId, owner, delegatedTo, null, EventType.CREATE);

			assertThat(event.getExpires()).isCloseTo(OffsetDateTime.now().plusMonths(18), within(1, ChronoUnit.SECONDS));
			assertThat(event.getType()).isEqualTo(EventType.CREATE);
			assertThat(event.getMessage()).isEqualTo("Create facility delegation");
			assertThat(event.getOwner()).isEqualTo("InstalledBase");
			assertThat(event.getHistoryReference()).isNull();
			assertThat(event.getSourceType()).isEqualTo("Delegation");
			assertThat(event.getMetadata()).hasSize(5);
			assertThat(event.getMetadata()).extracting(Metadata::getKey, Metadata::getValue)
				.containsExactlyInAnyOrder(
					tuple("DelegationId", delegationId),
					tuple("DelegationOwner", owner),
					tuple("DelegatedTo", delegatedTo),
					tuple("Facilities", ""),
					tuple("RequestId", requestId));
		}
	}
}
