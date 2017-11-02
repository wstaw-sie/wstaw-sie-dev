package wstaw.sie.validator;

import wstaw.sie.model.session.SessionBean;
import wstaw.sie.validator.constraint.FieldMatch;

import org.apache.commons.beanutils.BeanUtils;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303
 */
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>
{
	
	@Resource
	SessionBean sessionBean; //bad smell
	
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation)
    {	
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context)
    {
        try
        {
            final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName);
            boolean isValid = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        	//sessionBean.setPasswordConfirmationError(!isValid); // bad smell
            return isValid;
        }
        catch (final Exception ignore)
        {
            // ignore
        }
        return true;
    }
}