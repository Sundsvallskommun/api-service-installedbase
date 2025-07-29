package se.sundsvall.installedbase.api.model.delegation;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DelegationTest {

	private final String id = "id";
	private final List<Facility> facilities = List.of(Facility.create(), Facility.create());
	private final String delegatedTo = UUID.randomUUID().toString();
	private final String owner = UUID.randomUUID().toString();
	private final String municipalityId = "2281";
	private final OffsetDateTime created = now().minusMinutes(2);
	private final OffsetDateTime updated = now().minusMinutes(1);

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Delegation.create()).hasAllNullFieldsOrProperties();
		assertThat(new Delegation()).hasAllNullFieldsOrProperties();
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(Delegation.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilders() {
		final var bean = Delegation.create()
			.withId(id)
			.withFacilities(facilities)
			.withDelegatedTo(delegatedTo)
			.withOwner(owner)
			.withMunicipalityId(municipalityId)
			.withCreated(created)
			.withUpdated(updated);

		assertValues(bean);
	}

	@Test
	void testSettersAndGetters() {
		final var bean = new Delegation();
		bean.setId(id);
		bean.setFacilities(facilities);
		bean.setDelegatedTo(delegatedTo);
		bean.setOwner(owner);
		bean.setMunicipalityId(municipalityId);
		bean.setCreated(created);
		bean.setUpdated(updated);

		assertValues(bean);
	}

	private void assertValues(final Delegation bean) {
		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getId()).isEqualTo(id);
		assertThat(bean.getFacilities()).isEqualTo(facilities);
		assertThat(bean.getDelegatedTo()).isEqualTo(delegatedTo);
		assertThat(bean.getOwner()).isEqualTo(owner);
		assertThat(bean.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(bean.getCreated()).isEqualTo(created);
		assertThat(bean.getUpdated()).isEqualTo(updated);
	}
}
