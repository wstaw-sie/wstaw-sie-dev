package wstaw.sie.validator.constraint;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import wstaw.sie.validator.EqualsLoggedUserPasswordValidator;

/**
 * The annotated element must be equal to password of @link wstaw.sie.model.SessionBean before adding salt and hashing.
 */
@Target({ FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EqualsLoggedUserPasswordValidator.class})
public @interface EqualsLoggedUserPassword {

	String message() default "{wstaw.sie.validator.constraint.DoesntContainWhitespace.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Defines several {@link EqualsLoggedUserPasswordValidator} annotations on the same element.
	 *
	 * @see wstaw.sie.validator.constraint.EqualsLoggedUserPasswordValidator
	 */
	@Target({FIELD})
	@Retention(RUNTIME)
	@Documented
	@interface List {

		EqualsLoggedUserPassword[] value();
	}
}
