package wstaw.sie.service;

import org.pac4j.core.profile.CommonProfile;

import wstaw.sie.model.dto.UserDTO;
import wstaw.sie.model.entity.Credentials;
import wstaw.sie.model.entity.User;

public interface LoginService {

  /**
   * @param login it is email.
   */
  public User getUserByLogin(String login);
  
  public User getUserById(Integer id);
  
  public void updateUser(UserDTO user);
  
  public void login(User user);
  
  public void logut();
  
  public boolean isUserLoggedIn();
  
  public boolean isAdminOrSuperAdmin();
  
  public boolean isPasswordOfGivenUserValid(User user, String passwordPlain);

  public void login(CommonProfile profile);
	
}
