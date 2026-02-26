package se.sundsvall.installedbase.api.model.validation;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.sundsvall.dept44.problem.ThrowableProblem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class ValidatorUtilTest {

	@ParameterizedTest
	@MethodSource("validDelegationParameters")
	void testValidDelegationParameters(String owner, String delegatedTo) {
		assertDoesNotThrow(() -> ValidatorUtil.validateDelegationParameters(owner, delegatedTo));
	}

	@ParameterizedTest
	@MethodSource("invalidDelegationParameters")
	void testInvalidDelegationParameters(String owner, String delegatedTo) {
		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> ValidatorUtil.validateDelegationParameters(owner, delegatedTo))
			.withMessage("Invalid search parameters: Either owner or delegatedTo must be provided")
			.satisfies(problem -> assertThat(problem.getStatus()).isEqualTo(BAD_REQUEST));
	}

	public static Stream<Arguments> validDelegationParameters() {
		return Stream.of(
			Arguments.of("owner", null),
			Arguments.of(null, "delegatedTo"),
			Arguments.of("owner", "delegatedTo"));
	}

	public static Stream<Arguments> invalidDelegationParameters() {
		return Stream.of(
			Arguments.of(null, null),
			Arguments.of("", ""),
			Arguments.of(" ", " "));
	}
}
