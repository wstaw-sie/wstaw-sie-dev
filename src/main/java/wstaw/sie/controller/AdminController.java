package wstaw.sie.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import drawer.com.bean.Mail;
import drawer.com.bean.Pair;
import drawer.com.logic.Draw;
import wstaw.sie.model.entity.Group;
import wstaw.sie.model.entity.User;
import wstaw.sie.model.session.SessionBean;
import wstaw.sie.model.wrapper.AdminForm;
import wstaw.sie.model.wrapper.AdminPasswordChangeForm;
import wstaw.sie.repository.UserRepository;
import wstaw.sie.service.LoginService;
import wstaw.sie.service.ProfileService;
import wstaw.sie.util.FilenameExtensionExtractor;
import wstaw.sie.util.PasswordlAndRandomUtil;

@Controller
public class AdminController {

	private static final int PASSWORD_LENGTH = 5;

	@Value("${mail.user}")
	private String mailUser;
	@Value("${mail.pass}")
	private String mailPass;
	@Value("${mail.host}")
	private String mailHost;
	@Value("${application.url}")
	private String applicationURL;
	
	@Resource
	private LoginService loginService;

	@Resource
	private ProfileService profileService;

	@Resource
	private UserRepository userRepository;

	@Resource
	SessionBean sessionBean;
	
	@Autowired
	private Draw draw;

	private static Logger logger = Logger.getLogger(AdminController.class);

	
	 @RequestMapping(value = "/draw", method = RequestMethod.GET)
	public String draw(Model model) {	
		if (loginService.isUserLoggedIn() && loginService.isAdminOrSuperAdmin()) {
			logger.info("AdminController - method play execution started");
			List<Pair> drawnUsers = draw.play();
			logger.info("AdminController - method play execution finished");
			model.addAttribute("drawnUsers", drawnUsers);
			return "draw";
		}
		else {
			return "redirect:login";
		}
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String reset(Model model) {
		logger.info("Servlet context path: " + sessionBean.getContextPath());
		if (loginService.isUserLoggedIn() && loginService.isAdminOrSuperAdmin()) {
			List<User> findAll = userRepository.findAll();
			model.addAttribute("users", findAll);
			return "admin";
		} else {
			return "redirect:login";
		}
	}

	@RequestMapping(value = "/admin", method = RequestMethod.POST)
	public String reset(@Valid AdminForm adminForm, Model model, BindingResult bindingResult) {
		logger.info("Servlet context path: " + sessionBean.getContextPath());
		AdminPasswordChangeForm passwordChangedForm = new AdminPasswordChangeForm();
		if (loginService.isUserLoggedIn() && loginService.isAdminOrSuperAdmin()) {
			if (adminForm.getReset_ile().equals("single")) {
				User user = userRepository.getOne(adminForm.getId_resetowanego());				
				resetPasswordForUser(adminForm, user, passwordChangedForm);
				model.addAttribute("passwordChangedForm", passwordChangedForm);
				return "adminPasswordResetResult";
			} else if(adminForm.getReset_ile().equals("multi")){
				List<User> users = userRepository.findByIdGreaterThanEqualAndIdLessThanEqual(adminForm.getId_resetowanego_od(), adminForm.getId_resetowanego_do());
				for (User user : users) {
					resetPasswordForUser(adminForm, user, passwordChangedForm);
				}
				model.addAttribute("passwordChangedForm", passwordChangedForm);
				return "adminPasswordResetResult";
			}
		}
		return "redirect:login";
	}


	@RequestMapping(value = "/uploadCSV", method = RequestMethod.POST)
	public String uploadCSV(@RequestParam("file") MultipartFile file,
			Model model) {
		if (loginService.isUserLoggedIn() && loginService.isAdminOrSuperAdmin()) {
			BufferedReader reader = null;
			boolean isUserListValid = true;
			List<String> incorrectLinesInFile = new ArrayList<String>();
			List<User> users = new ArrayList<User>();
			String fileExtension = FilenameExtensionExtractor.extractFileExtension(file.getOriginalFilename());
		
			if (!file.isEmpty() && fileExtension.equals("csv")) {
				try {
					reader = new BufferedReader(new InputStreamReader(
							file.getInputStream()));
					String line = "";
					while ((line = reader.readLine()) != null) {
						try {
							User user = createUserFromCSVLine(line);
							users.add(user);
						} catch (Exception e1) {
							incorrectLinesInFile.add(line);
							isUserListValid = false;
							e1.printStackTrace();
						}
					}

				} catch (IOException e) {
					isUserListValid = false;
					e.printStackTrace();
				}
			}else{
				isUserListValid = false;
			}

			if (isUserListValid) {
				try {
					users = userRepository.save(users);
				} catch (Exception e) {
					isUserListValid = false;
					e.printStackTrace();
				}
			}

			model.addAttribute("isUserListValid", isUserListValid);
			model.addAttribute("users", users);
			model.addAttribute("incorrectLinesInFile", incorrectLinesInFile);
			return "uploadCSV";
		} else {
			return "redirect:login";
		}

	}

	private User createUserFromCSVLine(String line) throws Exception {
		String[] parts = line.split(";");
		if (parts.length != 5)
			throw new Exception("IncorrectLineFormat");

		Group userGroup = new Group();
		userGroup.setId(Integer.parseInt(parts[4]));

		String newSalt = PasswordlAndRandomUtil.generateSaltHex();

		User user = new User();
		user.setFirstName(parts[0]);
		user.setLastName(parts[1]);
		user.setEmail(parts[2]);
		user.setPassword(PasswordlAndRandomUtil
				.getHashedPasswordAfterMigrationGivenGivenSaltAndPlainPassword(
						newSalt, parts[3]));
		user.setSalt(newSalt);
		user.setIsMigrated(true);
		user.setGroupOfUser(userGroup);
		return user;
	}
		
	private void resetPasswordForUser(AdminForm adminForm, User user, AdminPasswordChangeForm passwordChangedForm) {
		String newPassword = PasswordlAndRandomUtil.getRandomStringBase64(PASSWORD_LENGTH);
		String newSalt = PasswordlAndRandomUtil.generateSaltHex();
		user.setPassword(PasswordlAndRandomUtil.getHashedPasswordAfterMigrationGivenGivenSaltAndPlainPassword(newSalt,
				newPassword));
		user.setSalt(newSalt);
		user.setIsMigrated(Boolean.TRUE);
		if (adminForm.getMail_notification()) {
			String sendEmailWithNewPassword = sendEmailWithNewPassword(user, newPassword);
			if (sendEmailWithNewPassword.equals(Mail.SEND_SUCCESS)) {
				profileService.updateUser(user);
				passwordChangedForm.getResult().addPositiveResult(user,newPassword);
			} else {
				passwordChangedForm.getResult().addNegativeResult(user);
			}
		} else {
			profileService.updateUser(user);
			passwordChangedForm.getResult().addPositiveResult(user,newPassword);
		}
	}

	private String sendEmailWithNewPassword(User user, String password) {
		Mail mail = new Mail(mailUser, mailPass, mailHost, applicationURL);
		String status = mail.sendMailWithNewPassword(user, password);
		return status;
	}
}
