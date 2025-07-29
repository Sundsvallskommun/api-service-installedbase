package se.sundsvall.installedbase.api.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import se.sundsvall.installedbase.api.model.delegation.Facility;

/**
 * Validator to ensure that a list contains only unique elements.
 */
public class UniqueElementsValidator implements ConstraintValidator<UniqueElements, List<Facility>> {

	@Override
	public boolean isValid(List<Facility> value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		// If the size of the HashSet (which does not allow for duplicates) is the same as the original list
		// then the list contains only unique elements.
		final var facilityIds = value.stream()
			.map(Facility::getId)
			.filter(Objects::nonNull)
			.map(String::toUpperCase) // Use case insentitive comparison
			.map(String::trim) // Remove leading and trailing spaces before comparison
			.toList();
		return facilityIds.size() == new HashSet<>(facilityIds).size();
	}
}
