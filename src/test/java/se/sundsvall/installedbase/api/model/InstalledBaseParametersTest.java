package se.sundsvall.installedbase.api.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class InstalledBaseParametersTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(InstalledBaseParameters.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals()));
	}

	@Test
	void testCreatePattern() {
		final var partyId = "898b3634-a2f9-483c-8808-37f3f25cf24e";
		final var organizationIds = List.of("123456789", "987654321");
		final var date = LocalDate.of(2025, 6, 1);
		final var sortBy = "Company";

		final var parameters = InstalledBaseParameters.create()
			.withPartyId(partyId)
			.withOrganizationIds(organizationIds)
			.withDate(date)
			.withSortBy(sortBy);

		assertThat(parameters.getPartyId()).isEqualTo(partyId);
		assertThat(parameters.getOrganizationIds()).isEqualTo(organizationIds);
		assertThat(parameters.getDate()).isEqualTo(date);
		assertThat(parameters.getSortBy()).isEqualTo(sortBy);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		final var parameters = InstalledBaseParameters.create();
		assertThat(parameters.getPartyId()).isNull();
		assertThat(parameters.getOrganizationIds()).isNull();
		assertThat(parameters.getDate()).isNull();
		assertThat(parameters.getSortBy()).isNull();
	}
}
