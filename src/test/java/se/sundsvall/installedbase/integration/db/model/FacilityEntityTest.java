package se.sundsvall.installedbase.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class FacilityEntityTest {

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new FacilityEntity()).hasAllNullFieldsOrPropertiesExcept("delegations")
			.hasFieldOrPropertyWithValue("delegations", emptyList());
		assertThat(FacilityEntity.create()).hasAllNullFieldsOrPropertiesExcept("delegations")
			.hasFieldOrPropertyWithValue("delegations", emptyList());
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(FacilityEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCodeExcluding("delegations"),
			hasValidBeanEqualsExcluding("delegations"),
			hasValidBeanToStringExcluding("delegations")));
	}

	@Test
	void testGettersAndSetters() {
		final var businessEngagementOrgId = "businessEngagementOrgId";
		final var delegations = List.of(DelegationEntity.create());
		final var facilityId = "facilityId";

		final var bean = new FacilityEntity();
		bean.setBusinessEngagementOrgId(businessEngagementOrgId);
		bean.setDelegations(delegations);
		bean.setFacilityId(facilityId);

		assertThat(bean).isNotNull().hasNoNullFieldsOrPropertiesExcept("id");
		assertThat(bean.getBusinessEngagementOrgId()).isEqualTo(businessEngagementOrgId);
		assertThat(bean.getDelegations()).isEqualTo(delegations);
		assertThat(bean.getFacilityId()).isEqualTo(facilityId);
	}

	@Test
	void testBuilderMethods() {
		final var businessEngagementOrgId = "businessEngagementOrgId";
		final var facilityId = "facilityId";

		final var bean = FacilityEntity.create()
			.withBusinessEngagementOrgId(businessEngagementOrgId)
			.withFacilityId(facilityId);

		assertThat(bean).isNotNull().hasNoNullFieldsOrPropertiesExcept("id", "delegations");
		assertThat(bean.getBusinessEngagementOrgId()).isEqualTo(businessEngagementOrgId);
		assertThat(bean.getFacilityId()).isEqualTo(facilityId);
	}
}
