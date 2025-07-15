package se.sundsvall.installedbase.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegation;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegationEntity;
import static se.sundsvall.installedbase.service.mapper.EntityMapper.toEntity;
import static se.sundsvall.installedbase.service.mapper.EntityMapper.toFacilityDelegation;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EntityMapperTest {

	@Test
	void testToEntity() {
		var municipalityId = "2281";
		var facilityDelegation = createFacilityDelegation();

		var entity = toEntity(municipalityId, facilityDelegation);

		assertThat(entity.getId()).isNull();
		assertThat(entity.getOwner()).isEqualTo(facilityDelegation.getOwner());
		assertThat(entity.getDelegatedTo()).isEqualTo(facilityDelegation.getDelegatedTo());
		assertThat(entity.getFacilities()).isEqualTo(facilityDelegation.getFacilities());
		assertThat(entity.getBusinessEngagementOrgId()).isEqualTo(facilityDelegation.getBusinessEngagementOrgId());
	}

	@Test
	void testToFacilityDelegation() {
		var id = UUID.randomUUID().toString();
		var facilityDelegationEntity = createFacilityDelegationEntity(id);

		var facilityDelegation = toFacilityDelegation(facilityDelegationEntity);

		assertThat(facilityDelegation.getId()).isEqualTo(id);
		assertThat(facilityDelegation.getOwner()).isEqualTo(facilityDelegationEntity.getOwner());
		assertThat(facilityDelegation.getDelegatedTo()).isEqualTo(facilityDelegationEntity.getDelegatedTo());
		assertThat(facilityDelegation.getFacilities()).isEqualTo(facilityDelegationEntity.getFacilities());
		assertThat(facilityDelegation.getBusinessEngagementOrgId()).isEqualTo(facilityDelegationEntity.getBusinessEngagementOrgId());
		assertThat(facilityDelegation.getMunicipalityId()).isEqualTo(facilityDelegationEntity.getMunicipalityId());
		assertThat(facilityDelegation.getCreated()).isEqualTo(facilityDelegationEntity.getCreated());
		assertThat(facilityDelegation.getUpdated()).isEqualTo(facilityDelegationEntity.getUpdated());
	}
}
