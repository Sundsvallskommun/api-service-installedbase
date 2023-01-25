package se.sundsvall.installedbase.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class InstalledBaseItemAddressTest {

	@Test
	void testBean() {
		assertThat(InstalledBaseItemAddress.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var careOf = "careOf";
		final var city = "city";
		final var postalCode = "postalCode";
		final var street = "street";
		final var propertyDesignation = "propertyDesignation";

		final var installedBaseItemAddress = InstalledBaseItemAddress.create()
			.withCareOf(careOf)
			.withCity(city)
			.withPostalCode(postalCode)
			.withStreet(street)
			.withPropertyDesignation(propertyDesignation);

		assertThat(installedBaseItemAddress.getCareOf()).isEqualTo(careOf);
		assertThat(installedBaseItemAddress.getCity()).isEqualTo(city);
		assertThat(installedBaseItemAddress.getPostalCode()).isEqualTo(postalCode);
		assertThat(installedBaseItemAddress.getStreet()).isEqualTo(street);
		assertThat(installedBaseItemAddress.getPropertyDesignation()).isEqualTo(propertyDesignation);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(InstalledBaseItemAddress.create()).hasAllNullFieldsOrProperties();
		assertThat(new InstalledBaseItemAddress()).hasAllNullFieldsOrProperties();
	}
}
