package wstaw.sie.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import wstaw.sie.validator.constraint.DoesntContainWhitespace;

/**
 * http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303
 */
public class DoesntContainWhitespaceValidator implements ConstraintValidator<DoesntContainWhitespace, Object>
{
	private static Pattern pattern = Pattern.compile("\\s");
	
    @Override
    public void initialize(final DoesntContainWhitespace constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context)
    {
    	return value instanceof String && !pattern.matcher((String)value).find();
    }
}