package se.sundsvall.installedbase.api.model.facilitydelegation;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FacilityDelegationTest {

	private final String id = "id";
	private final List<String> facilities = List.of("facility-1", "facility-2");
	private final String businessEngagementOrgId = "businessEngagementOrgId";
	private final String delegatedTo = UUID.randomUUID().toString();
	private final String owner = UUID.randomUUID().toString();
	private final String municipalityId = "2281";
	private final String status = "active";
	private final LocalDateTime created = now().minusMinutes(2);
	private final LocalDateTime updated = now().minusMinutes(1);
	private final LocalDateTime deleted = now();

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDateTime.class);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new FacilityDelegation()).hasAllNullFieldsOrProperties();
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(FacilityDelegation.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilders() {
		var delegate = new FacilityDelegation()
			.withId(id)
			.withFacilities(facilities)
			.withBusinessEngagementOrgId(businessEngagementOrgId)
			.withDelegatedTo(delegatedTo)
			.withOwner(owner)
			.withMunicipalityId(municipalityId)
			.withStatus(status)
			.withCreated(created)
			.withUpdated(updated)
			.withDeleted(deleted);

		assertThat(delegate.getId()).isEqualTo(id);
		assertThat(delegate.getFacilities()).isEqualTo(facilities);
		assertThat(delegate.getBusinessEngagementOrgId()).isEqualTo(businessEngagementOrgId);
		assertThat(delegate.getDelegatedTo()).isEqualTo(delegatedTo);
		assertThat(delegate.getOwner()).isEqualTo(owner);
		assertThat(delegate.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(delegate.getStatus()).isEqualTo(status);
		assertThat(delegate.getCreated()).isEqualTo(created);
		assertThat(delegate.getUpdated()).isEqualTo(updated);
		assertThat(delegate.getDeleted()).isEqualTo(deleted);
	}

	@Test
	void testSettersAndGetters() {
		var delegate = new FacilityDelegation();
		delegate.setId(id);
		delegate.setFacilities(facilities);
		delegate.setBusinessEngagementOrgId(businessEngagementOrgId);
		delegate.setDelegatedTo(delegatedTo);
		delegate.setOwner(owner);
		delegate.setMunicipalityId(municipalityId);
		delegate.setStatus(status);
		delegate.setCreated(created);
		delegate.setUpdated(updated);
		delegate.setDeleted(deleted);

		assertThat(delegate.getId()).isEqualTo(id);
		assertThat(delegate.getFacilities()).isEqualTo(facilities);
		assertThat(delegate.getBusinessEngagementOrgId()).isEqualTo(businessEngagementOrgId);
		assertThat(delegate.getDelegatedTo()).isEqualTo(delegatedTo);
		assertThat(delegate.getOwner()).isEqualTo(owner);
		assertThat(delegate.getMunicipalityId()).isEqualTo(municipalityId);
		assertThat(delegate.getStatus()).isEqualTo(status);
		assertThat(delegate.getCreated()).isEqualTo(created);
		assertThat(delegate.getUpdated()).isEqualTo(updated);
		assertThat(delegate.getDeleted()).isEqualTo(deleted);
	}
}
