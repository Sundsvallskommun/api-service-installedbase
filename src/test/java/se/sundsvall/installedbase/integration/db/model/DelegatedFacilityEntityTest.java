package se.sundsvall.installedbase.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static se.sundsvall.installedbase.integration.db.model.DelegationStatus.ACTIVE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DelegatedFacilityEntityTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> LocalDateTime.now().plusDays(new Random().nextInt()), LocalDateTime.class);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new DelegatedFacilityEntity()).hasAllNullFieldsOrProperties();
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(DelegatedFacilityEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCodeExcluding("facilities"),
			hasValidBeanEqualsExcluding("facilities"),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		var id = 123;
		var facilites = List.of("facility1", "facility2");
		var businessEngagementOrgId = "org123";
		var delegatedTo = "delegatedTo123";
		var municipalityId = "2281";
		var owner = "owner123";
		var status = ACTIVE;
		var start = LocalDateTime.now();
		var end = LocalDateTime.now().plusDays(1);

		var entity = new DelegatedFacilityEntity()
			.withId(id)
			.withFacilities(facilites)
			.withBusinessEngagementOrgId(businessEngagementOrgId)
			.withDelegatedTo(delegatedTo)
			.withMunicipalityId(municipalityId)
			.withOwner(owner)
			.withStatus(status)
			.withStart(start)
			.withEnd(end);

		assertThat(entity.getId()).isEqualTo(id);
		assertThat(entity.getFacilities()).isEqualTo(facilites);
		assertThat(entity.getBusinessEngagementOrgId()).isEqualTo(businessEngagementOrgId);
		assertThat(entity.getDelegatedTo()).isEqualTo(delegatedTo);
		assertThat(entity.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(entity.getOwner()).isEqualTo(owner);
		assertThat(entity.getStatus()).isEqualTo(status);
		assertThat(entity.getStart()).isEqualTo(start);
		assertThat(entity.getEnd()).isEqualTo(end);
	}
}
