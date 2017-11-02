package wstaw.sie.model.wrapper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import wstaw.sie.validator.constraint.DoesntContainWhitespace;
import wstaw.sie.validator.constraint.EqualsLoggedUserPassword;
import wstaw.sie.validator.constraint.FieldMatch;

@FieldMatch(first = "newPassword", second = "passwordConfirmation", message = "Pola \"Nowe hasło\" oraz \"Potwierdź hasło\" powinny być sobie równe")
public class PasswordChangeForm {

	@EqualsLoggedUserPassword (message = "Złe hasło")
	private String oldPassword = null;

	@NotNull
	@Size(min = 8, max = 30, message = "Nieodpowiednia liczba znaków")
	@DoesntContainWhitespace(message = "Hasło nie może zawierać pustych znaków")
	private String newPassword = null;

	// @Size(min=8, max=30, message = "Hasło ma nieodpowiednią liczbę znaków")
	private String passwordConfirmation = null;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public void clear() {
		this.newPassword = null;
		this.oldPassword = null;
		this.passwordConfirmation = null;
	}
	/*
	 * @AssertTrue(message=
	 * "Pola \"Nowe hasło\" oraz \"Potwierdź hasło\" powinny być sobie równe")
	 * private boolean isValid() { return
	 * this.newPassword.equals(this.passwordConfirmation); }
	 */
}
