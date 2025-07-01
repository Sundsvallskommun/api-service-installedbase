package se.sundsvall.installedbase.api.model.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.installedbase.service.model.DelegationStatus;

@ExtendWith(MockitoExtension.class)
class ValidDelegationStatusConstraintValidatorTest {

	@Mock
	private ConstraintValidatorContext mockContext;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder mockViolationBuilder;

	@Mock
	private ValidDelegationStatus mockAnnotation;

	@InjectMocks
	private ValidDelegationStatusConstraintValidator validator;

	@BeforeEach
	void setUp() {
		validator.initialize(mockAnnotation);
	}

	@ParameterizedTest
	@MethodSource("invalidDelegationStatusProvider")
	void testInvalidValue(String status) {

		when(mockContext.getDefaultConstraintMessageTemplate()).thenReturn("Invalid delegation status: {validatedValue}. Valid values are: {validValues}");
		when(mockContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(mockViolationBuilder);
		when(mockViolationBuilder.addConstraintViolation()).thenReturn(mockContext);

		assertThat(validator.isValid(status, mockContext)).isFalse();

		ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockContext).buildConstraintViolationWithTemplate(messageCaptor.capture());
		verify(mockViolationBuilder).addConstraintViolation();

		String actualMessage = messageCaptor.getValue();
		assertThat(actualMessage).isEqualTo("Invalid delegation status: " + status + ". Valid values are: ACTIVE, DELETED");
	}

	@ParameterizedTest
	@EnumSource(DelegationStatus.class)
	void testDelegationStatusValues(DelegationStatus delegationStatus) {
		var name = delegationStatus.name();
		assertThat(validator.isValid(name, mockContext)).isTrue();
		verifyNoInteractions(mockContext, mockViolationBuilder, mockAnnotation);
	}

	@ParameterizedTest
	@MethodSource("validDelegationStatusProvider")
	void testValidValues(String status) {
		assertThat(validator.isValid(status, mockContext)).isTrue();
		verifyNoInteractions(mockContext, mockViolationBuilder, mockAnnotation);
	}

	public static Stream<Arguments> validDelegationStatusProvider() {
		return Stream.of(
			Arguments.of(""),
			Arguments.of((Object) null));
	}

	public static Stream<Arguments> invalidDelegationStatusProvider() {
		return Stream.of(
			Arguments.of(" "),
			Arguments.of(" ACTIVE "),
			Arguments.of("INVALID_STATUS"));
	}
}
