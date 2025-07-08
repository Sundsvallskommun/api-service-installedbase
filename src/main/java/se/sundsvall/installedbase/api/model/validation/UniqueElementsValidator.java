package se.sundsvall.installedbase.api.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;

/**
 * Validator to ensure that a list contains only unique elements.
 */
public class UniqueElementsValidator implements ConstraintValidator<UniqueElements, List<?>> {

	@Override
	public boolean isValid(List<?> value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		// If the size of the HashSet (which does not allow for duplicates) is the same as the original list
		// then the list contains only unique elements.
		return value.size() == new HashSet<>(value).size();
	}
}
