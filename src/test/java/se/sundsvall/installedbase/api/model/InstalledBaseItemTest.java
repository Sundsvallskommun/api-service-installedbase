package se.sundsvall.installedbase.api.model;

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

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InstalledBaseItemTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(InstalledBaseItem.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var address = InstalledBaseItemAddress.create();
		final var facilityId = "facilityId";
		final var metaData = List.of(InstalledBaseItemMetaData.create());
		final var placementId = 12345;
		final var type = "type";
		final var facilityCommitmentStartDate = LocalDate.now();
		final var facilityCommitmentEndDate = LocalDate.now().plusYears(1);

		final var installedBaseItem = InstalledBaseItem.create()
			.withAddress(address)
			.withFacilityCommitmentEndDate(facilityCommitmentEndDate)
			.withFacilityCommitmentStartDate(facilityCommitmentStartDate)
			.withFacilityId(facilityId)
			.withMetaData(metaData)
			.withPlacementId(placementId)
			.withType(type);

		assertThat(installedBaseItem.getAddress()).isEqualTo(address);
		assertThat(installedBaseItem.getFacilityCommitmentEndDate()).isEqualTo(facilityCommitmentEndDate);
		assertThat(installedBaseItem.getFacilityCommitmentStartDate()).isEqualTo(facilityCommitmentStartDate);
		assertThat(installedBaseItem.getFacilityId()).isEqualTo(facilityId);
		assertThat(installedBaseItem.getMetaData()).isEqualTo(metaData);
		assertThat(installedBaseItem.getPlacementId()).isEqualTo(placementId);
		assertThat(installedBaseItem.getType()).isEqualTo(type);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(InstalledBaseItem.create())
			.hasAllNullFieldsOrPropertiesExcept("placementId")
			.hasFieldOrPropertyWithValue("placementId", 0);

		assertThat(new InstalledBaseItem())
			.hasAllNullFieldsOrPropertiesExcept("placementId")
			.hasFieldOrPropertyWithValue("placementId", 0);
	}
}
