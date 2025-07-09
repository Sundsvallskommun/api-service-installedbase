package se.sundsvall.installedbase.api.model.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UniqueElementsValidatorTest {

	private final UniqueElementsValidator validator = new UniqueElementsValidator();

	private ConstraintValidatorContext constraintValidatorContext;

	@BeforeEach
	void setup() {
		constraintValidatorContext = mock(ConstraintValidatorContext.class);
	}

	@MethodSource("validParameters")
	@ParameterizedTest(name = "{0}")
	void isValid(String testName, List<?> value) {
		assertThat(validator.isValid(value, constraintValidatorContext)).isTrue();
	}

	@MethodSource("invalidParameters")
	@ParameterizedTest(name = "{0}")
	void isInvalid(String testName, List<?> value) {
		assertThat(validator.isValid(value, constraintValidatorContext)).isFalse();
	}

	public static Stream<Arguments> validParameters() {
		return Stream.of(
			arguments("null list", null),
			arguments("empty list", List.of()),
			arguments("single element", List.of("facility1")),
			arguments("unique strings", List.of("facility1", "facility2", "facility3")),
			arguments("unique integers", List.of(1, 2, 3, 4, 5)),
			arguments("list including empty string", List.of("facility1", "", "facility2")),
			arguments("list including whitespace strings", List.of("facility1", " ", "  ", "facility2")),
			arguments("case sensitive strings", List.of("Facility1", "facility1", "FACILITY1")),
			arguments("mixed types", List.of("string", 123, new BigDecimal("45.67"))));
	}

	public static Stream<Arguments> invalidParameters() {
		return Stream.of(
			arguments("duplicate strings", List.of("facility1", "facility2", "facility1")),
			arguments("multiple duplicates", List.of("facility1", "facility2", "facility1", "facility2")),
			arguments("duplicate integers", List.of(1, 2, 3, 2, 4)),
			arguments("multiple duplicate integers", List.of(1, 1, 2, 3, 2, 4)),
			arguments("duplicate empty strings", List.of("facility1", "", "", "facility2")));
	}
}
