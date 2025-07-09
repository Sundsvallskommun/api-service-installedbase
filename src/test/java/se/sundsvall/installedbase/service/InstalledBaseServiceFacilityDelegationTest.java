package se.sundsvall.installedbase.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegation;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegationEntity;
import static se.sundsvall.installedbase.TestDataFactory.createUpdateFacilityDelegation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.installedbase.integration.db.FacilityDelegationRepository;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;
import se.sundsvall.installedbase.service.model.DelegationStatus;

@ExtendWith(MockitoExtension.class)
class InstalledBaseServiceFacilityDelegationTest {

	private static final String MUNICIPALITY_ID = "2281";

	@Mock
	private FacilityDelegationRepository mockFacilityDelegationRepository;

	@InjectMocks
	private InstalledBaseService installedBaseService;

	@Test
	void testCreateFacilityDelegation() {
		var delegate = createFacilityDelegation();
		var id = UUID.randomUUID().toString();
		var facilityDelegationEntity = createFacilityDelegationEntity(id);
		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any())).thenReturn(Optional.empty());
		when(mockFacilityDelegationRepository.save(any(FacilityDelegationEntity.class))).thenReturn(facilityDelegationEntity);

		var response = installedBaseService.createFacilityDelegation(MUNICIPALITY_ID, delegate);

		assertThat(response).isNotNull().isEqualTo(id);

		verify(mockFacilityDelegationRepository).findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verify(mockFacilityDelegationRepository).save(any(FacilityDelegationEntity.class));
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}

	@Test
	void testCreateFacilityDelegation_shouldThrowProblem() {
		var delegate = createFacilityDelegation();
		var ownerUuid = UUID.randomUUID().toString();
		var delegatedToUuid = UUID.randomUUID().toString();
		delegate.setOwner(ownerUuid);
		delegate.setDelegatedTo(delegatedToUuid);
		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any())).thenReturn(Optional.of(new FacilityDelegationEntity()));

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> installedBaseService.createFacilityDelegation(MUNICIPALITY_ID, delegate))
			.withMessage("Facility delegation already exists: Owner with partyId: '" + ownerUuid + "' has already delegated to partyId: '"
				+ delegatedToUuid + "' for municipality: '" + MUNICIPALITY_ID + "'. Update existing delegation instead.");

		verify(mockFacilityDelegationRepository).findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}

	@Test
	void testGetFacilityDelegation() {
		var id = UUID.randomUUID().toString();
		var facilityDelegationEntity = createFacilityDelegationEntity(id);
		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any())).thenReturn(Optional.of(facilityDelegationEntity));

		var response = installedBaseService.getFacilityDelegation(MUNICIPALITY_ID, id);

		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(id);

		verify(mockFacilityDelegationRepository).findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}

	@Test
	void testGetFacilityDelegation_shouldThrowProblem_WhenNotFound() {
		var id = UUID.randomUUID().toString();
		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any())).thenReturn(Optional.empty());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> installedBaseService.getFacilityDelegation(MUNICIPALITY_ID, id))
			.withMessage("Couldn't find delegation for id: " + id);

		verify(mockFacilityDelegationRepository).findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}

	// Tests for getFacilityDelegations

	@Test
	void testGetFacilityDelegations() {
		var owner = UUID.randomUUID().toString();
		var delegatedTo = UUID.randomUUID().toString();
		var status = DelegationStatus.ACTIVE.name();
		var facilityDelegationEntity = createFacilityDelegationEntity(UUID.randomUUID().toString());
		when(mockFacilityDelegationRepository.findAll(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any()))
			.thenReturn(List.of(facilityDelegationEntity));

		var response = installedBaseService.getFacilityDelegations(MUNICIPALITY_ID, owner, delegatedTo, status);

		assertThat(response).isNotNull().hasSize(1);
		assertThat(response.getFirst().getId()).isEqualTo(facilityDelegationEntity.getId());

		verify(mockFacilityDelegationRepository).findAll(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}

	@Test
	void testGetFacilityDelegations_shouldReturnEmptyList_whenNoDelegationsFound() {
		var owner = UUID.randomUUID().toString();
		var delegatedTo = UUID.randomUUID().toString();
		var status = DelegationStatus.ACTIVE.name();
		when(mockFacilityDelegationRepository.findAll(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any()))
			.thenReturn(List.of());

		var response = installedBaseService.getFacilityDelegations(MUNICIPALITY_ID, owner, delegatedTo, status);

		assertThat(response).isNotNull().isEmpty();

		verify(mockFacilityDelegationRepository).findAll(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}

	@Test
	void testPutFacilityDelegation_shouldUpdateDelegation() {
		var id = UUID.randomUUID().toString();
		var facilityDelegationEntity = createFacilityDelegationEntity(id);
		var updateFacilityDelegation = createUpdateFacilityDelegation();
		updateFacilityDelegation.setOwner(facilityDelegationEntity.getOwner()); // Set the same owner to avoid mismatch

		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any())).thenReturn(Optional.of(facilityDelegationEntity));
		when(mockFacilityDelegationRepository.save(any(FacilityDelegationEntity.class))).thenReturn(facilityDelegationEntity);

		installedBaseService.putFacilityDelegation(MUNICIPALITY_ID, id, updateFacilityDelegation); // Set same

		verify(mockFacilityDelegationRepository).findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verify(mockFacilityDelegationRepository).save(any(FacilityDelegationEntity.class));
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}

	@Test
	void testPutFacilityDelegation_shouldThrowProblem_whenFacilityDelegationNotFoundOrDeleted() {
		var id = UUID.randomUUID().toString();
		var updateFacilityDelegation = createUpdateFacilityDelegation();

		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any())).thenReturn(Optional.empty());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> installedBaseService.putFacilityDelegation(MUNICIPALITY_ID, id, updateFacilityDelegation))
			.withMessage("Facility delegation not found: Couldn't find any active facility delegations for id: " + id);

		verify(mockFacilityDelegationRepository).findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}

	@Test
	void testPutFacilityDelegation_shouldThrowProblem_whenOwnerMismatch() {
		var id = UUID.randomUUID().toString();
		var facilityDelegationEntity = createFacilityDelegationEntity(id);
		var updateFacilityDelegation = createUpdateFacilityDelegation();

		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any())).thenReturn(Optional.of(facilityDelegationEntity));

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> installedBaseService.putFacilityDelegation(MUNICIPALITY_ID, id, updateFacilityDelegation))
			.withMessage("Invalid delegation owner: The owner of the delegation with id: '" + facilityDelegationEntity.getId() + "' is not the same as the one provided in the request.");

		verify(mockFacilityDelegationRepository).findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}
}
