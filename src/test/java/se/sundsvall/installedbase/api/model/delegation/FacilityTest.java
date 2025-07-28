package se.sundsvall.installedbase.api.model.delegation;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class FacilityTest {

	private final String id = "id";
	private final String businessEngagementOrgId = "businessEngagementOrgId";

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Facility.create()).hasAllNullFieldsOrProperties();
		assertThat(new Facility()).hasAllNullFieldsOrProperties();
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(Facility.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilders() {
		final var bean = Facility.create()
			.withBusinessEngagementOrgId(businessEngagementOrgId)
			.withId(id);

		assertValues(bean);
	}

	@Test
	void testSettersAndGetters() {
		final var bean = new Facility();
		bean.setId(id);
		bean.setBusinessEngagementOrgId(businessEngagementOrgId);

		assertValues(bean);
	}

	private void assertValues(final Facility bean) {
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getId()).isEqualTo(id);
		assertThat(bean.getBusinessEngagementOrgId()).isEqualTo(businessEngagementOrgId);
	}
}
