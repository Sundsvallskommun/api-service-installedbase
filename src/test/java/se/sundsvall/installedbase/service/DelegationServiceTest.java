package se.sundsvall.installedbase.service;

import static generated.se.sundsvall.eventlog.EventType.CREATE;
import static generated.se.sundsvall.eventlog.EventType.DELETE;
import static generated.se.sundsvall.eventlog.EventType.UPDATE;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.installedbase.TestDataFactory.createDelegation;
import static se.sundsvall.installedbase.TestDataFactory.createDelegationEntity;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityEntity;
import static se.sundsvall.installedbase.TestDataFactory.updateDelegation;

import generated.se.sundsvall.eventlog.Event;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.installedbase.integration.db.DelegationRepository;
import se.sundsvall.installedbase.integration.db.FacilityRepository;
import se.sundsvall.installedbase.integration.db.model.DelegationEntity;
import se.sundsvall.installedbase.integration.db.model.FacilityEntity;
import se.sundsvall.installedbase.integration.eventlog.EventLogClient;

@ExtendWith(MockitoExtension.class)
class DelegationServiceTest {

	private static final String MUNICIPALITY_ID = "2281";

	@Mock
	private DelegationRepository mockDelegationRepository;

	@Mock
	private FacilityRepository mockFacilityRepository;

	@Mock
	private EventLogClient mockEventLogClient;

	@InjectMocks
	private DelegationService delegationService;

	@Captor
	private ArgumentCaptor<DelegationEntity> delegationEntityCaptor;

	@Captor
	private ArgumentCaptor<Event> eventCaptor;

	@AfterEach
	void verifyNoMoreMockInteractions() {
		verifyNoMoreInteractions(mockDelegationRepository, mockFacilityRepository, mockEventLogClient);
	}

	@Test
	void testCreateDelegationWhenNoFacilitiesExists() {
		final var delegation = createDelegation();
		final var id = UUID.randomUUID().toString();
		final var delegationEntity = createDelegationEntity(id);
		when(mockDelegationRepository.save(any(DelegationEntity.class))).thenReturn(delegationEntity);
		when(mockEventLogClient.createEvent(eq(MUNICIPALITY_ID), eq(id), any())).thenReturn(ResponseEntity.ok().build());

		final var response = delegationService.createDelegation(MUNICIPALITY_ID, delegation);

		verify(mockDelegationRepository).findOne(ArgumentMatchers.<Specification<DelegationEntity>>any());
		verify(mockFacilityRepository).findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-3", "5591628137");
		verify(mockFacilityRepository).findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-4", "5591628137");
		verify(mockDelegationRepository).save(any(DelegationEntity.class));
		verify(mockEventLogClient).createEvent(eq(MUNICIPALITY_ID), eq(id), eventCaptor.capture());

		assertThat(response).isNotNull().isEqualTo(id);
		assertThat(eventCaptor.getValue().getType()).isEqualTo(CREATE);
	}

	@Test
	void testCreateDelegationWhenFacilityExist() {
		final var delegation = createDelegation();
		final var id = UUID.randomUUID().toString();
		final var facilityEntity = createFacilityEntity("facility-4");
		final var delegationEntity = createDelegationEntity(id);

		when(mockFacilityRepository.findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-3", "5591628137")).thenReturn(Optional.empty());
		when(mockFacilityRepository.findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-4", "5591628137")).thenReturn(Optional.of(facilityEntity));
		when(mockDelegationRepository.save(any(DelegationEntity.class))).thenReturn(delegationEntity);
		when(mockEventLogClient.createEvent(eq(MUNICIPALITY_ID), eq(id), any())).thenReturn(ResponseEntity.ok().build());

		final var response = delegationService.createDelegation(MUNICIPALITY_ID, delegation);

		assertThat(response).isNotNull().isEqualTo(id);

		verify(mockDelegationRepository).findOne(ArgumentMatchers.<Specification<DelegationEntity>>any());
		verify(mockDelegationRepository).save(delegationEntityCaptor.capture());
		verify(mockFacilityRepository).findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-3", "5591628137");
		verify(mockFacilityRepository).findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-4", "5591628137");
		verify(mockEventLogClient).createEvent(eq(MUNICIPALITY_ID), eq(id), eventCaptor.capture());

		assertThat(delegationEntityCaptor.getValue().getFacilities()).contains(facilityEntity);
		assertThat(eventCaptor.getValue().getType()).isEqualTo(CREATE);
	}

	@Test
	void testCreateDelegation_shouldThrowProblem() {
		final var delegation = createDelegation();
		final var ownerUuid = UUID.randomUUID().toString();
		final var delegatedToUuid = UUID.randomUUID().toString();
		delegation.setOwner(ownerUuid);
		delegation.setDelegatedTo(delegatedToUuid);
		when(mockDelegationRepository.findOne(ArgumentMatchers.<Specification<DelegationEntity>>any())).thenReturn(Optional.of(DelegationEntity.create()));

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> delegationService.createDelegation(MUNICIPALITY_ID, delegation))
			.withMessage("Delegation already exists: Owner with partyId: '" + ownerUuid + "' has already delegated to partyId: '"
				+ delegatedToUuid + "' for municipality: '" + MUNICIPALITY_ID + "'. Update existing delegation instead.");

		verify(mockDelegationRepository).findOne(ArgumentMatchers.<Specification<DelegationEntity>>any());
		verifyNoInteractions(mockEventLogClient);
	}

	@Test
	void testUpdateDelegationWhenNoFacilitiesExists() {
		final var id = UUID.randomUUID().toString();
		final var delegation = updateDelegation();
		final var delegationEntity = createDelegationEntity(id);

		when(mockDelegationRepository.findOne(ArgumentMatchers.<Specification<DelegationEntity>>any())).thenReturn(Optional.of(delegationEntity));

		delegationService.updateDelegation(MUNICIPALITY_ID, id, delegation);

		verify(mockDelegationRepository).findOne(ArgumentMatchers.<Specification<DelegationEntity>>any());
		verify(mockFacilityRepository).deleteAllInBatch(anyIterable());
		verify(mockFacilityRepository).findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-9", "5591628141");
		verify(mockFacilityRepository).findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-10", "5591628142");
		verify(mockFacilityRepository).findAllByDelegationsIsEmpty();
		verify(mockDelegationRepository).save(delegationEntityCaptor.capture());
		verify(mockEventLogClient).createEvent(eq(MUNICIPALITY_ID), eq(id), eventCaptor.capture());

		assertThat(eventCaptor.getValue().getType()).isEqualTo(UPDATE);
		assertThat(delegationEntityCaptor.getValue().getFacilities())
			.extracting(FacilityEntity::getFacilityId)
			.containsExactlyInAnyOrder("facility-9", "facility-10");
	}

	@Test
	void testUpdateDelegationWhenFacilitYExists() {
		final var id = UUID.randomUUID().toString();
		final var delegation = updateDelegation();
		final var delegationEntity = createDelegationEntity(id);
		final var facilityEntity = createFacilityEntity("facility-9");

		when(mockDelegationRepository.findOne(ArgumentMatchers.<Specification<DelegationEntity>>any())).thenReturn(Optional.of(delegationEntity));
		when(mockFacilityRepository.findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-9", "5591628141")).thenReturn(Optional.of(facilityEntity));
		when(mockFacilityRepository.findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-10", "5591628142")).thenReturn(Optional.empty());

		delegationService.updateDelegation(MUNICIPALITY_ID, id, delegation);

		verify(mockDelegationRepository).findOne(ArgumentMatchers.<Specification<DelegationEntity>>any());
		verify(mockFacilityRepository).findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-9", "5591628141");
		verify(mockFacilityRepository).findByFacilityIdAndBusinessEngagementOrgIdIgnoreCase("facility-10", "5591628142");
		verify(mockDelegationRepository).save(delegationEntityCaptor.capture());
		verify(mockFacilityRepository).findAllByDelegationsIsEmpty();
		verify(mockFacilityRepository).deleteAllInBatch(emptyList());
		verify(mockEventLogClient).createEvent(eq(MUNICIPALITY_ID), eq(id), eventCaptor.capture());

		assertThat(eventCaptor.getValue().getType()).isEqualTo(UPDATE);
		assertThat(delegationEntityCaptor.getValue().getFacilities()).contains(facilityEntity);
	}

	@Test
	void testUpdateDelegationWhenNotFound_shouldThrowProblem() {
		final var id = UUID.randomUUID().toString();
		final var delegation = updateDelegation();

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> delegationService.updateDelegation(MUNICIPALITY_ID, id, delegation))
			.withMessage("Delegation with id: '" + id + "' was not found within municipality '" + MUNICIPALITY_ID + "'.");

		verify(mockDelegationRepository).findOne(ArgumentMatchers.<Specification<DelegationEntity>>any());
		verifyNoInteractions(mockEventLogClient);
	}

	@Test
	void testGetDelegation() {
		final var id = UUID.randomUUID().toString();
		final var delegationEntity = createDelegationEntity(id);
		when(mockDelegationRepository.findOne(ArgumentMatchers.<Specification<DelegationEntity>>any())).thenReturn(Optional.of(delegationEntity));

		final var response = delegationService.getDelegation(MUNICIPALITY_ID, id);

		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(id);

		verify(mockDelegationRepository).findOne(ArgumentMatchers.<Specification<DelegationEntity>>any());
		verifyNoInteractions(mockEventLogClient);
	}

	@Test
	void testGetDelegation_shouldThrowProblem_WhenNotFound() {
		final var id = UUID.randomUUID().toString();
		when(mockDelegationRepository.findOne(ArgumentMatchers.<Specification<DelegationEntity>>any())).thenReturn(Optional.empty());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> delegationService.getDelegation(MUNICIPALITY_ID, id))
			.withMessage("Delegation with id: '" + id + "' was not found within municipality '2281'.");

		verify(mockDelegationRepository).findOne(ArgumentMatchers.<Specification<DelegationEntity>>any());
		verifyNoInteractions(mockEventLogClient);
	}

	// Tests for getDelegations

	@Test
	void testGetDelegations() {
		final var owner = UUID.randomUUID().toString();
		final var delegatedTo = UUID.randomUUID().toString();
		final var delegationEntity = createDelegationEntity(UUID.randomUUID().toString());
		when(mockDelegationRepository.findAll(ArgumentMatchers.<Specification<DelegationEntity>>any()))
			.thenReturn(List.of(delegationEntity));

		final var response = delegationService.getDelegations(MUNICIPALITY_ID, owner, delegatedTo);

		assertThat(response).isNotNull().hasSize(1);
		assertThat(response.getFirst().getId()).isEqualTo(delegationEntity.getId());

		verify(mockDelegationRepository).findAll(ArgumentMatchers.<Specification<DelegationEntity>>any());
		verifyNoInteractions(mockEventLogClient);
	}

	@Test
	void testGetDelegations_shouldReturnEmptyList_whenNoDelegationsFound() {
		final var owner = UUID.randomUUID().toString();
		final var delegatedTo = UUID.randomUUID().toString();
		when(mockDelegationRepository.findAll(ArgumentMatchers.<Specification<DelegationEntity>>any()))
			.thenReturn(List.of());

		final var response = delegationService.getDelegations(MUNICIPALITY_ID, owner, delegatedTo);

		assertThat(response).isNotNull().isEmpty();

		verify(mockDelegationRepository).findAll(ArgumentMatchers.<Specification<DelegationEntity>>any());
		verifyNoInteractions(mockEventLogClient);
	}

	@Test
	void testDeleteDelegation() {
		final var id = UUID.randomUUID().toString();
		final var delegationEntity = createDelegationEntity(id);
		final var orphanFacilities = List.of(FacilityEntity.create(), FacilityEntity.create());
		when(mockDelegationRepository.findOne(ArgumentMatchers.<Specification<DelegationEntity>>any())).thenReturn(Optional.of(delegationEntity));
		when(mockEventLogClient.createEvent(eq(MUNICIPALITY_ID), eq(id), any())).thenReturn(ResponseEntity.ok().build());
		when(mockFacilityRepository.findAllByDelegationsIsEmpty()).thenReturn(orphanFacilities);

		delegationService.deleteDelegation(MUNICIPALITY_ID, id);

		verify(mockDelegationRepository).delete(delegationEntity);
		verify(mockEventLogClient).createEvent(eq(MUNICIPALITY_ID), eq(id), eventCaptor.capture());
		verify(mockFacilityRepository).findAllByDelegationsIsEmpty();
		verify(mockFacilityRepository).deleteAllInBatch(orphanFacilities);
		assertThat(eventCaptor.getValue().getType()).isEqualTo(DELETE);
	}

	@Test
	void testSendEventThrowsException_shouldNotThrowProblem() {
		final var id = UUID.randomUUID().toString();
		final var delegationEntity = createDelegationEntity(id);
		when(mockDelegationRepository.findOne(ArgumentMatchers.<Specification<DelegationEntity>>any())).thenReturn(Optional.of(delegationEntity));
		when(mockEventLogClient.createEvent(eq(MUNICIPALITY_ID), eq(id), any())).thenThrow(new RuntimeException("Exception"));

		// Just testing the delete method as they all call the same client method
		assertThatNoException()
			.isThrownBy(() -> delegationService.deleteDelegation(MUNICIPALITY_ID, id));

		verify(mockDelegationRepository).delete(delegationEntity);
		verify(mockFacilityRepository).findAllByDelegationsIsEmpty();
		verify(mockFacilityRepository).deleteAllInBatch(anyIterable());
		verify(mockEventLogClient).createEvent(eq(MUNICIPALITY_ID), eq(id), eventCaptor.capture());

		assertThat(eventCaptor.getValue().getType()).isEqualTo(DELETE);
	}
}
