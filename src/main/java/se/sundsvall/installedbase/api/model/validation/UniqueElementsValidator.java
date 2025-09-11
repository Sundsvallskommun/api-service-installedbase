package se.sundsvall.installedbase.api.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

		var facilitySet = value.stream()
			.map(facility -> new UniqueFacility(Optional.ofNullable(facility.getId()).map(String::trim).map(String::toLowerCase).orElse(null), facility.getBusinessEngagementOrgId()))
			.collect(Collectors.toSet());

		// If the size of the original list is the same as the size of the set, all elements are unique
		return value.size() == facilitySet.size();
	}

	// Helper record to represent unique combination of facility ID and business engagement org ID
	private record UniqueFacility(String id, String businessEngagementOrgId) {
	}
}
