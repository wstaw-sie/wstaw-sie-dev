package wstaw.sie.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wstaw.sie.model.entity.Group;
import wstaw.sie.model.entity.User;
import wstaw.sie.model.session.SessionBean;
import wstaw.sie.service.LoginService;
import wstaw.sie.service.ProfileService;

@Controller
public class PrayingPartnerController {

	@Resource
	private LoginService loginService;

	@Resource
	private ProfileService profileService;

	@Resource
	SessionBean sessionBean;

	private static Logger logger = Logger.getLogger(ProfileController.class);

	@RequestMapping(value = "/praying", method = RequestMethod.GET)
	public String showProfile(Model model) {
		logger.info("Servlet context path: " + sessionBean.getContextPath());
		if (loginService.isUserLoggedIn()) {
			User logged = sessionBean.getLoggedUser();
			if (logged.getPrayFor() != null) {
				User prayingPartner = loginService.getUserById(logged.getPrayFor());
				model.addAttribute("prayingPartner", prayingPartner);

				return "praying";
			} else {
				Group groupOfLogged = logged.getGroupOfUser();
				model.addAttribute("prayingGroup", groupOfLogged);
				return "praying-group";
			}
		} else {
			return "redirect:login";
		}
	}

}
