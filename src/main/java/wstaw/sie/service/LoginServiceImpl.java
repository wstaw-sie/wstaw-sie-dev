package wstaw.sie.service;

import javax.annotation.Resource;

import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import wstaw.sie.model.dto.UserDTO;
import wstaw.sie.model.entity.Credentials;
import wstaw.sie.model.entity.User;
import wstaw.sie.model.session.SessionBean;
import wstaw.sie.pac4j.MyCallbackFilter;
import wstaw.sie.repository.UserRepository;
import wstaw.sie.util.ModelUtil;
import wstaw.sie.util.PasswordlAndRandomUtil;

@Service
public class LoginServiceImpl implements LoginService {

  @Resource
  UserRepository userRepository;
  
  @Resource
  SessionBean sessionBean;
  
  private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
  
  @Override
  public void login(User user) {
	  sessionBean.setLoggedUser(user);
	  sessionBean.setLogged(true);
  }
  
  @Override
  public void login(CommonProfile profile) {
	  User userLoggedViaOAuth2 = userRepository.findByEmail(profile.getEmail());
	  if(userLoggedViaOAuth2 != null)
	  {
		  sessionBean.setLoggedUser(userLoggedViaOAuth2);
		  sessionBean.setLogged(true);
		  logger.info("User: "+profile.getEmail()+" logged via OAuth2");
		  logger.info(userLoggedViaOAuth2.toString());
	  }
	  else
	  {
		  sessionBean.setOAuth2Error(true);
		  logger.info("User: "+profile.getEmail()+" doesn't have account in system \"Wstaw się!\"");
	  }
  }
  
  @Override
  public void logut() {
  	this.sessionBean.setLoggedUser(null);
  	this.sessionBean.setLogged(false);
  	
  }
  
  public boolean isUserLoggedIn()
  {
	  return sessionBean.isLogged();
  }
  
  public boolean isAdminOrSuperAdmin()
  {
	  return sessionBean.isAdminOrSuperAdmin();
  }

  @Override
  public User getUserByLogin(String login) {
    return userRepository.findByEmail(login);
  }
  
  @Override
  public void updateUser(UserDTO user) {
  	userRepository.saveAndFlush(ModelUtil.userDTO2Entity(user));
  }

  @Override
  public boolean isPasswordOfGivenUserValid(User user, String passwordPlain) {
	  return user.getIsMigrated() ? PasswordlAndRandomUtil.isPasswordOfGivenUserAfterMigrationValid(user, passwordPlain) : PasswordlAndRandomUtil.isPasswordOfGivenUserBeforeMigrationValid(user, passwordPlain);
	}

@Override
public User getUserById(Integer id) {
	return userRepository.findOne(id);
}

}
