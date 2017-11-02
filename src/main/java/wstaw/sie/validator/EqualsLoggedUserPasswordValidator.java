package wstaw.sie.validator;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import wstaw.sie.model.session.SessionBean;
import wstaw.sie.util.PasswordlAndRandomUtil;
import wstaw.sie.validator.constraint.EqualsLoggedUserPassword;

/**
 * http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303
 */
public class EqualsLoggedUserPasswordValidator implements ConstraintValidator<EqualsLoggedUserPassword, Object>
{
	@Resource
	SessionBean sessionBean;
	
    @Override
    public void initialize(final EqualsLoggedUserPassword constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context)
    {
    	return sessionBean.getLoggedUser() != null ? PasswordlAndRandomUtil.isPasswordOfGivenUserValid(sessionBean.getLoggedUser(), (String)value) : false;
    }
}