package se.sundsvall.installedbase.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegation;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegationEntity;
import static se.sundsvall.installedbase.TestDataFactory.createUpdateFacilityDelegation;
import static se.sundsvall.installedbase.service.mapper.EntityMapper.createFacilityDelegationToEntity;
import static se.sundsvall.installedbase.service.mapper.EntityMapper.toFacilityDelegationResponse;
import static se.sundsvall.installedbase.service.mapper.EntityMapper.updateEntityForPutOperation;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;
import se.sundsvall.installedbase.service.model.DelegationStatus;

@ExtendWith(MockitoExtension.class)
class EntityMapperTest {

	@Test
	void testCreateFacilityDelegationToEntity() {
		var municipalityId = "2281";
		var facilityDelegation = createFacilityDelegation();
		var delegationStatus = DelegationStatus.ACTIVE;

		var entity = createFacilityDelegationToEntity(municipalityId, facilityDelegation, delegationStatus);

		assertThat(entity.getId()).isNull();
		assertThat(entity.getOwner()).isEqualTo(facilityDelegation.getOwner());
		assertThat(entity.getDelegatedTo()).isEqualTo(facilityDelegation.getDelegatedTo());
		assertThat(entity.getFacilities()).isEqualTo(facilityDelegation.getFacilities());
		assertThat(entity.getBusinessEngagementOrgId()).isEqualTo(facilityDelegation.getBusinessEngagementOrgId());
		assertThat(entity.getStatus()).isEqualTo(DelegationStatus.ACTIVE);
	}

	@Test
	void testToFacilityDelegationResponse() {
		var id = UUID.randomUUID().toString();
		var facilityDelegationEntity = createFacilityDelegationEntity(id);

		var facilityDelegation = toFacilityDelegationResponse(facilityDelegationEntity);

		assertThat(facilityDelegation.getId()).isEqualTo(id);
		assertThat(facilityDelegation.getOwner()).isEqualTo(facilityDelegationEntity.getOwner());
		assertThat(facilityDelegation.getDelegatedTo()).isEqualTo(facilityDelegationEntity.getDelegatedTo());
		assertThat(facilityDelegation.getFacilities()).isEqualTo(facilityDelegationEntity.getFacilities());
		assertThat(facilityDelegation.getBusinessEngagementOrgId()).isEqualTo(facilityDelegationEntity.getBusinessEngagementOrgId());
		assertThat(facilityDelegation.getMunicipalityId()).isEqualTo(facilityDelegationEntity.getMunicipalityId());
		assertThat(facilityDelegation.getStatus()).isEqualTo(DelegationStatus.ACTIVE.name());
		assertThat(facilityDelegation.getCreated()).isEqualTo(facilityDelegationEntity.getCreated());
		assertThat(facilityDelegation.getDeleted()).isEqualTo(facilityDelegationEntity.getDeleted());
		assertThat(facilityDelegation.getUpdated()).isEqualTo(facilityDelegationEntity.getUpdated());
	}

	@Test
	void testUpdateEntityForPutOperation_shouldOnlyupdateSpecificFields() {
		var facilityDelegation = createUpdateFacilityDelegation();

		// Mock so we can verify that only specific methods are called
		var entityMock = mock(FacilityDelegationEntity.class);

		when(entityMock.withFacilities(facilityDelegation.getFacilities())).thenReturn(entityMock);
		when(entityMock.withDelegatedTo(facilityDelegation.getDelegatedTo())).thenReturn(entityMock);
		when(entityMock.withBusinessEngagementOrgId(facilityDelegation.getBusinessEngagementOrgId())).thenReturn(entityMock);

		updateEntityForPutOperation(entityMock, facilityDelegation);

		// The update method should only update the facilities, delegatedTo, and businessEngagementOrgId fields
		verify(entityMock).withFacilities(facilityDelegation.getFacilities());
		verify(entityMock).withDelegatedTo(facilityDelegation.getDelegatedTo());
		verify(entityMock).withBusinessEngagementOrgId(facilityDelegation.getBusinessEngagementOrgId());

		// Verify that no other methods were called on the entityMock
		verifyNoMoreInteractions(entityMock);
	}

	@Test
	void testUpdateEntityForPutOperation() {
		var entity = createFacilityDelegationEntity(UUID.randomUUID().toString());
		var facilityDelegation = createUpdateFacilityDelegation();

		// original values for later comparison
		var originalId = entity.getId();
		var originalMunicipalityId = entity.getMunicipalityId();
		var originalOwner = entity.getOwner();
		var originalStatus = entity.getStatus();
		var originalCreated = entity.getCreated();
		var originalUpdated = entity.getUpdated();
		var originalDeleted = entity.getDeleted();

		// Update the entity with the new values
		updateEntityForPutOperation(entity, facilityDelegation);

		// Assert the updated values
		assertThat(entity.getFacilities()).isEqualTo(facilityDelegation.getFacilities());
		assertThat(entity.getDelegatedTo()).isEqualTo(facilityDelegation.getDelegatedTo());
		assertThat(entity.getBusinessEngagementOrgId()).isEqualTo(facilityDelegation.getBusinessEngagementOrgId());

		// Assert the "original" values are not changed
		assertThat(entity.getId()).isEqualTo(originalId);
		assertThat(entity.getMunicipalityId()).isEqualTo(originalMunicipalityId);
		assertThat(entity.getOwner()).isEqualTo(originalOwner);
		assertThat(entity.getStatus()).isEqualTo(originalStatus);
		assertThat(entity.getCreated()).isEqualTo(originalCreated);
		assertThat(entity.getUpdated()).isEqualTo(originalUpdated);
		assertThat(entity.getDeleted()).isEqualTo(originalDeleted);
	}
}
