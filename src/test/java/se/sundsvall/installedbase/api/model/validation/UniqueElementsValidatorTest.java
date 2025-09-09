package se.sundsvall.installedbase.api.model.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.sundsvall.installedbase.api.model.delegation.Facility;

class UniqueElementsValidatorTest {

	private final UniqueElementsValidator validator = new UniqueElementsValidator();

	private ConstraintValidatorContext constraintValidatorContext;

	@BeforeEach
	void setup() {
		constraintValidatorContext = mock(ConstraintValidatorContext.class);
	}

	@MethodSource("validParameters")
	@ParameterizedTest(name = "{0}")
	void isValid(String testName, List<Facility> value) {
		assertThat(validator.isValid(value, constraintValidatorContext)).isTrue();
	}

	@MethodSource("invalidParameters")
	@ParameterizedTest(name = "{0}")
	void isInvalid(String testName, List<Facility> value) {
		assertThat(validator.isValid(value, constraintValidatorContext)).isFalse();
	}

	public static Stream<Arguments> validParameters() {
		return Stream.of(
			arguments("null list", null),
			arguments("empty list", List.of()),
			arguments("single element", List.of(createFacility("facility1"))),
			arguments("unique ids", List.of(createFacility("facility1"), createFacility("facility2"), createFacility("facility3"))),
			arguments("ids including empty string", List.of(createFacility("facility1"), createFacility(""), createFacility("facility2"))),
			arguments("ids including whitespace string", List.of(createFacility("facility1"), createFacility(" "), createFacility("facility2"))));
	}

	public static Stream<Arguments> invalidParameters() {
		return Stream.of(
			arguments("duplicate strings", List.of(createFacility("facility1"), createFacility("facility2"), createFacility("facility1"))),
			arguments("duplicate strings with leading spaces", List.of(createFacility("facility1"), createFacility("facility2"), createFacility(" facility1"))),
			arguments("duplicate strings with trailing spaces", List.of(createFacility("facility1"), createFacility("facility2"), createFacility("facility1 "))),
			arguments("multiple duplicates", List.of(createFacility("facility1", "orgId1"), createFacility("facility2", "orgId2"), createFacility("facility1", "orgId1"), createFacility("facility2", "orgId2"))),
			arguments("duplicate with different case strings", List.of(createFacility("Facility1"), createFacility("facility1"), createFacility("FACILITY1"))),
			arguments("duplicate empty strings", List.of(createFacility("facility1"), createFacility(""), createFacility(""), createFacility("facility2"))),
			arguments("duplicate whitespace strings", List.of(createFacility("facility1"), createFacility(" "), createFacility("  "), createFacility("facility2"))));
	}

	private static Facility createFacility(String id, String businessEngagementOrgId) {
		return createFacility(id)
			.withBusinessEngagementOrgId(businessEngagementOrgId);
	}

	private static Facility createFacility(String id) {
		return Facility.create()
			.withId(id);
	}
}
