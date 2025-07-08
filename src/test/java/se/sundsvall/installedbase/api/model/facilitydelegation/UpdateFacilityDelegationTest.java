package se.sundsvall.installedbase.api.model.facilitydelegation;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.util.List;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class UpdateFacilityDelegationTest {
	private final List<String> facilities = List.of("facility-1", "facility-2");
	private final String businessEngagementOrgId = "businessEngagementOrgId";
	private final String delegatedTo = UUID.randomUUID().toString();

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new FacilityDelegationResponse()).hasAllNullFieldsOrProperties();
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(UpdateFacilityDelegation.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilders() {
		var delegate = new UpdateFacilityDelegation()
			.withFacilities(facilities)
			.withBusinessEngagementOrgId(businessEngagementOrgId)
			.withDelegatedTo(delegatedTo);

		assertThat(delegate.getFacilities()).isEqualTo(facilities);
		assertThat(delegate.getBusinessEngagementOrgId()).isEqualTo(businessEngagementOrgId);
		assertThat(delegate.getDelegatedTo()).isEqualTo(delegatedTo);
	}

	@Test
	void testSettersAndGetters() {
		var delegate = new UpdateFacilityDelegation();
		delegate.setFacilities(facilities);
		delegate.setBusinessEngagementOrgId(businessEngagementOrgId);
		delegate.setDelegatedTo(delegatedTo);

		assertThat(delegate.getFacilities()).isEqualTo(facilities);
		assertThat(delegate.getBusinessEngagementOrgId()).isEqualTo(businessEngagementOrgId);
		assertThat(delegate.getDelegatedTo()).isEqualTo(delegatedTo);
	}
}
