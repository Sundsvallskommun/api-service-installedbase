package se.sundsvall.installedbase.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class InstalledBaseResponseTest {

	@Test
	void testBean() {
		assertThat(InstalledBaseResponse.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var installedBaseCustomers = List.of(InstalledBaseCustomer.create());

		final var installedBaseResponse = InstalledBaseResponse.create()
			.withInstalledBaseCustomers(installedBaseCustomers);

		assertThat(installedBaseResponse).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(installedBaseResponse.getInstalledBaseCustomers()).isEqualTo(installedBaseCustomers);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(InstalledBaseResponse.create()).hasAllNullFieldsOrProperties();
		assertThat(new InstalledBaseResponse()).hasAllNullFieldsOrProperties();
	}
}
