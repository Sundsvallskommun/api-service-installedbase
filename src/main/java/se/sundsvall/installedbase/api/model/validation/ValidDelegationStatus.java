package se.sundsvall.installedbase.api.model.validation;

import static se.sundsvall.installedbase.api.model.validation.ValidDelegationStatusConstraintValidator.VALIDATED_VALUE;
import static se.sundsvall.installedbase.api.model.validation.ValidDelegationStatusConstraintValidator.VALID_VALUES;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import se.sundsvall.installedbase.service.model.DelegationStatus;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
	ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE
})
@Constraint(validatedBy = ValidDelegationStatusConstraintValidator.class)
public @interface ValidDelegationStatus {

	String message() default "invalid delegation status '" + VALIDATED_VALUE + "'. Must be one of '" + VALID_VALUES + "' or empty";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	Class<? extends Enum<?>> enumClass() default DelegationStatus.class;
}
