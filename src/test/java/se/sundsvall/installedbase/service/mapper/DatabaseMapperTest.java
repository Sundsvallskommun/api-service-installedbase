package se.sundsvall.installedbase.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.installedbase.TestDataFactory.createDelegation;
import static se.sundsvall.installedbase.TestDataFactory.createDelegationEntity;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseMapperTest {

	@Test
	void toEntity() {
		final var municipalityId = "2281";
		final var delegation = createDelegation();

		final var bean = DatabaseMapper.toDelegationEntity(municipalityId, delegation);

		assertThat(bean).hasNoNullFieldsOrPropertiesExcept("id", "facilities", "created", "updated");
		assertThat(bean.getOwner()).isEqualTo(delegation.getOwner());
		assertThat(bean.getDelegatedTo()).isEqualTo(delegation.getDelegatedTo());
	}

	@Test
	void toDelegation() {
		final var id = UUID.randomUUID().toString();
		final var entity = createDelegationEntity(id);

		final var bean = DatabaseMapper.toDelegation(entity);

		assertThat(bean).hasNoNullFieldsOrProperties();
		assertThat(bean.getId()).isEqualTo(id);
		assertThat(bean.getOwner()).isEqualTo(entity.getOwner());
		assertThat(bean.getDelegatedTo()).isEqualTo(entity.getDelegatedTo());
		assertThat(bean.getMunicipalityId()).isEqualTo(entity.getMunicipalityId());
		assertThat(bean.getCreated()).isEqualTo(entity.getCreated());
		assertThat(bean.getUpdated()).isEqualTo(entity.getUpdated());
		assertThat(bean.getFacilities()).hasSize(2)
			.allSatisfy(facility -> assertThat(facility).hasNoNullFieldsOrPropertiesExcept("id"))
			.satisfiesExactlyInAnyOrder(facility -> {
				assertThat(facility.getBusinessEngagementOrgId()).isEqualTo("5591628140");
				assertThat(facility.getId()).isEqualTo("facility-7");
			}, facility -> {
				assertThat(facility.getBusinessEngagementOrgId()).isEqualTo("5591628140");
				assertThat(facility.getId()).isEqualTo("facility-8");
			});
	}
}
