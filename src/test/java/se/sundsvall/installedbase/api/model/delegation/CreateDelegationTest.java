package se.sundsvall.installedbase.api.model.delegation;

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

class CreateDelegationTest {
	private final List<Facility> facilities = List.of(Facility.create(), Facility.create());
	private final String delegatedTo = UUID.randomUUID().toString();
	private final String owner = UUID.randomUUID().toString();

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new CreateDelegation()).hasAllNullFieldsOrProperties();
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(CreateDelegation.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilders() {
		final var bean = new CreateDelegation()
			.withFacilities(facilities)
			.withDelegatedTo(delegatedTo)
			.withOwner(owner);

		assertValues(bean);
	}

	@Test
	void testSettersAndGetters() {
		final var bean = new CreateDelegation();
		bean.setFacilities(facilities);
		bean.setDelegatedTo(delegatedTo);
		bean.setOwner(owner);

		assertValues(bean);
	}

	private void assertValues(final CreateDelegation bean) {
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getFacilities()).isEqualTo(facilities);
		assertThat(bean.getDelegatedTo()).isEqualTo(delegatedTo);
		assertThat(bean.getOwner()).isEqualTo(owner);
	}
}
