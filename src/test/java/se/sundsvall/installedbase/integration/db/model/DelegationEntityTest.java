package se.sundsvall.installedbase.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.CoreMatchers.allOf;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DelegationEntityTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> LocalDateTime.now().plusDays(new Random().nextInt()), LocalDateTime.class);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new DelegationEntity()).hasAllNullFieldsOrProperties();
		assertThat(DelegationEntity.create()).hasAllNullFieldsOrProperties();
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(DelegationEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCodeExcluding("facilities"),
			hasValidBeanEqualsExcluding("facilities"),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var id = UUID.randomUUID().toString();
		final var facilites = Set.of(
			FacilityEntity.create().withFacilityId("f1"),
			FacilityEntity.create().withFacilityId("f2"));
		final var delegatedTo = "delegatedTo123";
		final var municipalityId = "2281";
		final var owner = "owner123";
		final var created = LocalDateTime.now();
		final var updated = LocalDateTime.now().plusMinutes(2);

		final var bean = DelegationEntity.create()
			.withId(id)
			.withFacilities(facilites)
			.withDelegatedTo(delegatedTo)
			.withMunicipalityId(municipalityId)
			.withOwner(owner)
			.withCreated(created)
			.withUpdated(updated);

		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getId()).isEqualTo(id);
		assertThat(bean.getFacilities()).isEqualTo(facilites);
		assertThat(bean.getDelegatedTo()).isEqualTo(delegatedTo);
		assertThat(bean.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(bean.getOwner()).isEqualTo(owner);
		assertThat(bean.getCreated()).isEqualTo(created);
		assertThat(bean.getUpdated()).isEqualTo(updated);
	}

	@Test
	void testGettersAndSetters() {
		final var id = UUID.randomUUID().toString();
		final var facilites = Set.of(
			FacilityEntity.create().withFacilityId("f1"),
			FacilityEntity.create().withFacilityId("f2"));
		final var delegatedTo = "delegatedTo123";
		final var municipalityId = "2281";
		final var owner = "owner123";
		final var created = LocalDateTime.now();
		final var updated = LocalDateTime.now().plusMinutes(2);

		final var bean = new DelegationEntity();
		bean.setId(id);
		bean.setFacilities(facilites);
		bean.setDelegatedTo(delegatedTo);
		bean.setMunicipalityId(municipalityId);
		bean.setOwner(owner);
		bean.setCreated(created);
		bean.setUpdated(updated);

		assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(bean.getId()).isEqualTo(id);
		assertThat(bean.getFacilities()).isEqualTo(facilites);
		assertThat(bean.getDelegatedTo()).isEqualTo(delegatedTo);
		assertThat(bean.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(bean.getOwner()).isEqualTo(owner);
		assertThat(bean.getCreated()).isEqualTo(created);
		assertThat(bean.getUpdated()).isEqualTo(updated);
	}

	@Test
	void testPrePersist() {
		final var entity = DelegationEntity.create();

		entity.onCreate();

		assertThat(entity).hasAllNullFieldsOrPropertiesExcept("created");
		assertThat(entity.getCreated()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
	}

	@Test
	void testPreUpdate() {
		final var entity = DelegationEntity.create();

		entity.onUpdate();

		assertThat(entity).hasAllNullFieldsOrPropertiesExcept("updated");
		assertThat(entity.getUpdated()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
	}
}
