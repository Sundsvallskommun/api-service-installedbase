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
import java.util.UUID;
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
		var facilityDelegationId = UUID.randomUUID().toString();
		var owner = UUID.randomUUID().toString();
		var delegatedTo = UUID.randomUUID().toString();
		var requestId = UUID.randomUUID().toString();
		var identifierHeader = "joe001doe; type=adAccount";
		var mockIdentifier = mock(Identifier.class);

		try (var mockedRequestId = mockStatic(RequestId.class);
			var mockedStaticIdentifier = mockStatic(Identifier.class)) {
			mockedRequestId.when(RequestId::get).thenReturn(requestId);
			mockedStaticIdentifier.when(Identifier::get).thenReturn(mockIdentifier);

			when(mockIdentifier.toHeaderValue()).thenReturn(identifierHeader);

			var event = EventlogMapper.toEvent(facilityDelegationId, owner, delegatedTo, eventType);

			assertThat(event.getExpires()).isCloseTo(OffsetDateTime.now().plusMonths(18), within(1, ChronoUnit.SECONDS));
			assertThat(event.getType()).isEqualTo(eventType);
			assertThat(event.getMessage()).isEqualTo(eventType + " facility delegation");
			assertThat(event.getOwner()).isEqualTo("InstalledBase");
			assertThat(event.getHistoryReference()).isEqualTo(requestId);
			assertThat(event.getSourceType()).isEqualTo("FacilityDelegation");
			assertThat(event.getMetadata()).hasSize(4);
			assertThat(event.getMetadata()).extracting(Metadata::getKey, Metadata::getValue)
				.containsExactlyInAnyOrder(
					tuple("FacilityDelegationId", facilityDelegationId),
					tuple("DelegationOwner", owner),
					tuple("DelegatedTo", delegatedTo),
					tuple("X-Sent-By", identifierHeader));
		}

		verify(mockIdentifier).toHeaderValue();
		verifyNoMoreInteractions(mockIdentifier);
	}

	@Test
	void testToEventWithoutIdentifier() {
		var facilityDelegationId = UUID.randomUUID().toString();
		var owner = UUID.randomUUID().toString();
		var delegatedTo = UUID.randomUUID().toString();
		var requestId = UUID.randomUUID().toString();

		try (var mockedRequestId = mockStatic(RequestId.class);
			var mockedIdentifier = mockStatic(Identifier.class)) {
			mockedRequestId.when(RequestId::get).thenReturn(requestId);
			mockedIdentifier.when(Identifier::get).thenReturn(null);

			var event = EventlogMapper.toEvent(facilityDelegationId, owner, delegatedTo, EventType.CREATE);

			assertThat(event.getExpires()).isCloseTo(OffsetDateTime.now().plusMonths(18), within(1, ChronoUnit.SECONDS));
			assertThat(event.getType()).isEqualTo(EventType.CREATE);
			assertThat(event.getMessage()).isEqualTo("CREATE facility delegation");
			assertThat(event.getOwner()).isEqualTo("InstalledBase");
			assertThat(event.getHistoryReference()).isEqualTo(requestId);
			assertThat(event.getSourceType()).isEqualTo("FacilityDelegation");
			assertThat(event.getMetadata()).hasSize(3);
			assertThat(event.getMetadata()).extracting(Metadata::getKey, Metadata::getValue)
				.containsExactlyInAnyOrder(
					tuple("FacilityDelegationId", facilityDelegationId),
					tuple("DelegationOwner", owner),
					tuple("DelegatedTo", delegatedTo));
		}
	}
}
