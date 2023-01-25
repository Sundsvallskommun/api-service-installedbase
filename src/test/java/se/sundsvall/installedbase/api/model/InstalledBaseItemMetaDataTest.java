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

class InstalledBaseItemMetaDataTest {

	@Test
	void testBean() {
		assertThat(InstalledBaseItemMetaData.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var displayName = "displayName";
		final var key = "key";
		final var value = "value";
		final var type = "type";

		final var installedBaseItemMetaData = InstalledBaseItemMetaData.create()
			.withDisplayName(displayName)
			.withKey(key)
			.withType(type)
			.withValue(value);

		assertThat(installedBaseItemMetaData.getDisplayName()).isEqualTo(displayName);
		assertThat(installedBaseItemMetaData.getKey()).isEqualTo(key);
		assertThat(installedBaseItemMetaData.getType()).isEqualTo(type);
		assertThat(installedBaseItemMetaData.getValue()).isEqualTo(value);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(InstalledBaseItemMetaData.create()).hasAllNullFieldsOrProperties();
		assertThat(new InstalledBaseItemMetaData()).hasAllNullFieldsOrProperties();
	}
}
