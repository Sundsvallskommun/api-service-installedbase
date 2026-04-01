package se.sundsvall.installedbase.api.model;

import java.time.LocalDate;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class InstalledBaseTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(InstalledBase.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var company = "Sundsvall Energi AB";
		final var customerId = "104327";
		final var type = "Fjärrvärme";
		final var facilityId = "735999109270751042";
		final var placementId = "5263";
		final var careOf = "Agatha Malm";
		final var street = "Storgatan 9";
		final var postCode = "85230";
		final var city = "Sundsvall";
		final var propertyDesignation = "Södermalm 1:27";
		final var dateFrom = LocalDate.of(2019, 1, 1);
		final var dateTo = LocalDate.of(2022, 12, 31);
		final var dateLatestModified = LocalDate.of(2022, 6, 15);

		final var installedBase = InstalledBase.create()
			.withCompany(company)
			.withCustomerId(customerId)
			.withType(type)
			.withFacilityId(facilityId)
			.withPlacementId(placementId)
			.withCareOf(careOf)
			.withStreet(street)
			.withPostCode(postCode)
			.withCity(city)
			.withPropertyDesignation(propertyDesignation)
			.withDateFrom(dateFrom)
			.withDateTo(dateTo)
			.withDateLatestModified(dateLatestModified);

		assertThat(installedBase).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(installedBase.getCompany()).isEqualTo(company);
		assertThat(installedBase.getCustomerId()).isEqualTo(customerId);
		assertThat(installedBase.getType()).isEqualTo(type);
		assertThat(installedBase.getFacilityId()).isEqualTo(facilityId);
		assertThat(installedBase.getPlacementId()).isEqualTo(placementId);
		assertThat(installedBase.getCareOf()).isEqualTo(careOf);
		assertThat(installedBase.getStreet()).isEqualTo(street);
		assertThat(installedBase.getPostCode()).isEqualTo(postCode);
		assertThat(installedBase.getCity()).isEqualTo(city);
		assertThat(installedBase.getPropertyDesignation()).isEqualTo(propertyDesignation);
		assertThat(installedBase.getDateFrom()).isEqualTo(dateFrom);
		assertThat(installedBase.getDateTo()).isEqualTo(dateTo);
		assertThat(installedBase.getDateLatestModified()).isEqualTo(dateLatestModified);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(InstalledBase.create()).hasAllNullFieldsOrProperties();
		assertThat(new InstalledBase()).hasAllNullFieldsOrProperties();
	}
}
