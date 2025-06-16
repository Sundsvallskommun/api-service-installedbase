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
import static se.sundsvall.installedbase.service.model.DelegationStatus.ACTIVE;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FacilityDelegationEntityTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> LocalDateTime.now().plusDays(new Random().nextInt()), LocalDateTime.class);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new FacilityDelegationEntity()).hasAllNullFieldsOrProperties();
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(FacilityDelegationEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCodeExcluding("facilities"),
			hasValidBeanEqualsExcluding("facilities"),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		var id = UUID.randomUUID().toString();
		var facilites = List.of("facility1", "facility2");
		var businessEngagementOrgId = "org123";
		var delegatedTo = "delegatedTo123";
		var municipalityId = "2281";
		var owner = "owner123";
		var status = ACTIVE;
		var created = LocalDateTime.now();
		var deleted = LocalDateTime.now().plusDays(1);
		var updated = LocalDateTime.now().plusMinutes(2);

		var entity = new FacilityDelegationEntity()
			.withId(id)
			.withFacilities(facilites)
			.withBusinessEngagementOrgId(businessEngagementOrgId)
			.withDelegatedTo(delegatedTo)
			.withMunicipalityId(municipalityId)
			.withOwner(owner)
			.withStatus(status)
			.withCreated(created)
			.withUpdated(updated)
			.withDeleted(deleted);

		assertThat(entity.getId()).isEqualTo(id);
		assertThat(entity.getFacilities()).isEqualTo(facilites);
		assertThat(entity.getBusinessEngagementOrgId()).isEqualTo(businessEngagementOrgId);
		assertThat(entity.getDelegatedTo()).isEqualTo(delegatedTo);
		assertThat(entity.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(entity.getOwner()).isEqualTo(owner);
		assertThat(entity.getStatus()).isEqualTo(status);
		assertThat(entity.getCreated()).isEqualTo(created);
		assertThat(entity.getUpdated()).isEqualTo(updated);
		assertThat(entity.getDeleted()).isEqualTo(deleted);
	}

	@Test
	void testPrePersist() {
		var entity = new FacilityDelegationEntity();
		entity.onCreate();
		assertThat(entity.getCreated()).isNotNull();
		assertThat(entity.getCreated()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
	}

	@Test
	void testPreUpdate() {
		// Create a new entity and set the created time
		var entity = new FacilityDelegationEntity();
		entity.onCreate();

		// Simulate an update by calling onUpdate
		var initialCreated = entity.getCreated();
		entity.onUpdate();
		assertThat(entity.getUpdated()).isNotNull();
		assertThat(entity.getUpdated()).isAfter(initialCreated);
		assertThat(entity.getUpdated()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
	}
}
