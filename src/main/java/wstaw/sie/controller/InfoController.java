package wstaw.sie.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wstaw.sie.model.session.SessionBean;

@Controller
public class InfoController {

	@Resource
	SessionBean sessionBean;

	private static Logger logger = Logger.getLogger(InfoController.class);

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String showInfo() {
		return "info";
	}
}