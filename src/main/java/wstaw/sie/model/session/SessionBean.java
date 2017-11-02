package wstaw.sie.model.session;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import wstaw.sie.model.Role;
import wstaw.sie.model.entity.User;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component("sessionBean")
public class SessionBean {
	
	@Value("${photo.path}")
	private String photoPath;

	@Value("${url.photo.path}")
	private String urlPhotoPath;
	
	@Autowired
	private ServletContext servletContext;

	private boolean isLogged = false;
	private User loggedUser;

	private boolean passwordChangedSuccessfully;

	private boolean photoChangedSuccessfully;

	private boolean passwordConfirmationError;

	private boolean photoError;
	
	private boolean oAuth2Error;

    public String getContextPath() {
        return servletContext.getContextPath();
    }
	
	public boolean isPasswordConfirmationError() {
		return passwordConfirmationError;
	}

	public void setPasswordConfirmationError(boolean passwordConfirmationError) {
		this.passwordConfirmationError = passwordConfirmationError;
	}

	public boolean isPhotoError() {
		return photoError;
	}

	public void setPhotoError(boolean photoError) {
		this.photoError = photoError;
	}

	public boolean isPhotoChangedSuccessfully() {
		return photoChangedSuccessfully;
	}

	public void setPhotoChangedSuccessfully(boolean photoChangedSuccessfully) {
		this.photoChangedSuccessfully = photoChangedSuccessfully;
	}

	public boolean isPasswordChangedSuccessfully() {
		return passwordChangedSuccessfully;
	}

	public void setPasswordChangedSuccessfully(
			boolean passwordChangedSuccessfully) {
		this.passwordChangedSuccessfully = passwordChangedSuccessfully;
	}

	public SessionBean() {
		// TODO Auto-generated constructor stub
	}

	public boolean isLogged() {
		return isLogged;
	}

	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public boolean isAdminOrSuperAdmin() {
		return null != loggedUser
				&& (Role.ADMIN.equals(loggedUser.getRole()) || Role.SUPERADMIN
						.equals(loggedUser.getRole()));
	}

	public boolean hasErrors() {
		return isPhotoError() || isPasswordConfirmationError();
	}
	
	public boolean getOAuth2ErrorAndResetToFalse() {
		boolean returnVal = oAuth2Error;
		oAuth2Error = false;
		return returnVal;
	}
	
	public boolean isOAuth2Error() {
		return oAuth2Error;
	}

	public void setOAuth2Error(boolean oAuth2Error) {
		this.oAuth2Error = oAuth2Error;
	}

	public String getPath(String path)
	{
		return servletContext.getContextPath()+"/"+path;
	}
	
	public String getPhotoPath(String photoPathInDb)
	{
		return servletContext.getContextPath()+ urlPhotoPath + photoPathInDb;
	}

}
