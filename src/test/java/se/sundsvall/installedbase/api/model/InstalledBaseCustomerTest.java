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

class InstalledBaseCustomerTest {

	@Test
	void testBean() {
		assertThat(InstalledBaseCustomer.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var customerNumber = "customerNumber";
		final var items = List.of(InstalledBaseItem.create());
		final var partyId = "partyId";

		final var installedBaseCustomer = InstalledBaseCustomer.create()
			.withCustomerNumber(customerNumber)
			.withItems(items)
			.withPartyId(partyId);

		assertThat(installedBaseCustomer.getCustomerNumber()).isEqualTo(customerNumber);
		assertThat(installedBaseCustomer.getItems()).isEqualTo(items);
		assertThat(installedBaseCustomer.getPartyId()).isEqualTo(partyId);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(InstalledBaseCustomer.create()).hasAllNullFieldsOrProperties();
		assertThat(new InstalledBaseCustomer()).hasAllNullFieldsOrProperties();
	}
}
