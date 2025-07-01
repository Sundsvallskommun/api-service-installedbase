package se.sundsvall.installedbase.api.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import se.sundsvall.installedbase.service.model.DelegationStatus;

public class ValidDelegationStatusConstraintValidator implements ConstraintValidator<ValidDelegationStatus, String> {

	private String validValues;

	public static final String VALID_VALUES = "{validValues}";
	public static final String VALIDATED_VALUE = "{validatedValue}";

	@Override
	public void initialize(ValidDelegationStatus constraintAnnotation) {
		// Initialize the valid valus from the enum
		validValues = Arrays.stream(DelegationStatus.values())
			.map(DelegationStatus::name)
			.collect(Collectors.joining(", "));
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// Empty is ok
		if (StringUtils.isEmpty(value)) {
			return true;
		}
		try {
			DelegationStatus.valueOf(value);
			return true;
		} catch (IllegalArgumentException e) {
			context.disableDefaultConstraintViolation();

			context.buildConstraintViolationWithTemplate(
				context.getDefaultConstraintMessageTemplate()
					.replace(VALID_VALUES, validValues)
					.replace(VALIDATED_VALUE, value))
				.addConstraintViolation();

			return false;
		}
	}
}
