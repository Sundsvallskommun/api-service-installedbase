package se.sundsvall.installedbase.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegation;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegationEntity;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.installedbase.integration.db.FacilityDelegationRepository;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;

@ExtendWith(MockitoExtension.class)
class InstalledBaseServiceFacilityDelegationTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String DELEGATES_PATH = "/delegates";

	@Mock
	private FacilityDelegationRepository mockFacilityDelegationRepository;

	@InjectMocks
	private InstalledBaseService installedBaseService;

	@Test
	void testCreateFacilityDelegation() {
		var delegate = createFacilityDelegation();
		var uuid = UUID.randomUUID().toString();
		var facilityDelegationEntity = createFacilityDelegationEntity(uuid);
		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any()))
			.thenReturn(Optional.empty());
		when(mockFacilityDelegationRepository.save(any(FacilityDelegationEntity.class))).thenReturn(facilityDelegationEntity);

		var response = installedBaseService.createFacilityDelegation(MUNICIPALITY_ID, delegate);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("/" + MUNICIPALITY_ID + DELEGATES_PATH + "/" + uuid));
		assertThat(response.getHeaders().get(HttpHeaders.CONTENT_TYPE)).containsExactly(MediaType.ALL_VALUE);

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
		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any()))
			.thenReturn(Optional.of(new FacilityDelegationEntity()));

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> installedBaseService.createFacilityDelegation(MUNICIPALITY_ID, delegate))
			.withMessage("Facility delegation already exists: Owner with partyId: '" + ownerUuid + "' has already delegated to partyId: '"
				+ delegatedToUuid + "' for municipality: '" + MUNICIPALITY_ID + "'. Update existing delegation instead.");
	}

	@Test
	void testGetFacilityDelegation() {
		var uuid = UUID.randomUUID().toString();
		var facilityDelegationEntity = createFacilityDelegationEntity(uuid);
		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any()))
			.thenReturn(Optional.of(facilityDelegationEntity));

		var response = installedBaseService.getFacilityDelegation(MUNICIPALITY_ID, uuid);

		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(uuid);

		verify(mockFacilityDelegationRepository).findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}

	@Test
	void testGetFacilityDelegation_shouldThrowProblem_WhenNotFound() {
		var uuid = UUID.randomUUID().toString();
		when(mockFacilityDelegationRepository.findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any()))
			.thenReturn(Optional.empty());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> installedBaseService.getFacilityDelegation(MUNICIPALITY_ID, uuid))
			.withMessage("Couldn't find delegation for id: " + uuid);

		verify(mockFacilityDelegationRepository).findOne(ArgumentMatchers.<Specification<FacilityDelegationEntity>>any());
		verifyNoMoreInteractions(mockFacilityDelegationRepository);
	}
}
