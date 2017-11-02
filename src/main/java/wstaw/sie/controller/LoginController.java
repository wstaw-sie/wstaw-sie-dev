package wstaw.sie.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.modelmapper.internal.cglib.proxy.CallbackFilter;
import org.pac4j.j2e.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wstaw.sie.model.entity.Credentials;
import wstaw.sie.model.entity.User;
import wstaw.sie.model.wrapper.IntentionForm;
import wstaw.sie.service.FloodService;
import wstaw.sie.service.LoginService;

@Controller
public class LoginController {
	 
  @Resource
  private LoginService loginService;
  @Resource
  private FloodService floodService;
  
  private static Logger logger  = Logger.getLogger(LoginController.class);
  
  @RequestMapping(value="/login", method=RequestMethod.GET)
  public String loginForm(Model model, HttpServletRequest request) {
	  if(loginService.isUserLoggedIn())
	  {
		  return "redirect:profile";
	  }
	  else
		if (floodService.isIPBlocked(request.getRemoteAddr()))
		{
		      logger.info("IP blocked for request: "+request);
		      return "loginBlocked";
		}
	    model.addAttribute("credentials", new Credentials());
	    return "login";
  }
  
  @RequestMapping(value="/login", method=RequestMethod.POST)
  public String checkLogin(@ModelAttribute Credentials credentials, Model model, HttpServletRequest request) {
    User user = null ;
    logger.info("Login request for: "+request+", where login is: "+credentials.getEmail());
    if (floodService.isIPBlocked(request.getRemoteAddr()))
    {
      logger.info("IP blocked for request: "+request);
      return "loginBlocked";
    }
    else if (null != (user = loginService.getUserByLogin(credentials.getEmail())) && loginService.isPasswordOfGivenUserValid(user, credentials.getPasswordPlain()))
    {
      logger.info("User: "+credentials.getEmail()+" entered vaild credentials");
      loginService.login(user);
      return "redirect:profile";
    }
    else
    {
      logger.info("Updating flood table for request: "+request);
      floodService.updateFloodTableInCaseOfBadCredentials(request.getRemoteAddr());
      return "login";
    }

  }
  
  @RequestMapping(value="/logout", method=RequestMethod.GET)
  public String logout(final HttpServletRequest request, final HttpServletResponse response,
          final HttpSession session) {
	  if(loginService.isUserLoggedIn())
	  {
		  //Wiersz dla Marysi!
		  loginService.logut();
		  UserUtils.logout(session);
	  }
      return "redirect:login";
  }
  
  /*
  @RequestMapping(value="/oauth2callback", method=RequestMethod.GET)
  public String loginGooglePlus() {
	  callbackFilter.
	  if(loginService.isUserLoggedIn())
	  {
		  //Wiersz dla Marysi!
		  loginService.logut();
	  }
      return "redirect:login";
  }
  
  http://localhost:8080/boot-ROOT/oauth2callback?client_name=Google2Client&code=4/_XLs38V2bh-SUQpuBKjVp2YtxitGs9_99HXLXCUO_G8#*/
}
