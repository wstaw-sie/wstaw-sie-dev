package wstaw.sie.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import wstaw.sie.model.entity.UploadedFile;
import wstaw.sie.model.entity.User;
import wstaw.sie.model.session.SessionBean;
import wstaw.sie.model.wrapper.IntentionForm;
import wstaw.sie.model.wrapper.PasswordChangeForm;
import wstaw.sie.service.LoginService;
import wstaw.sie.service.ProfileService;
import wstaw.sie.service.UploadedFileService;
import wstaw.sie.util.PasswordlAndRandomUtil;
import wstaw.sie.util.PhotoValidator;

@Controller
public class ProfileController {

	@Value("${photo.path}")
	private String photoPath;

	@Resource
	private LoginService loginService;

	@Resource
	private ProfileService profileService;

	@Resource
	private UploadedFileService uploadedFileService;

	@Resource
	SessionBean sessionBean;

	private static Logger logger = Logger.getLogger(ProfileController.class);
	
	@PostConstruct
	public void restoreUsersPhotosFromDatabase() {
		logger.info("RESTORING FILES");
		uploadedFileService.restoreUsersPhotosFromDatabase();
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String showProfile(Model model) {
		// logger.info("Servlet context path: " + sessionBean.getContextPath());
		clearInfoFlags();
		if (loginService.isUserLoggedIn()) {
			model.addAttribute("intentionForm", new IntentionForm());
			return "profile";
		} else {
			return "redirect:login";
		}
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String updateIntention(@ModelAttribute IntentionForm submittedIntention, Model model) {
		model.addAttribute("intentionForm", new IntentionForm());
		logger.info("Servlet context path: " + sessionBean.getContextPath());
		if (loginService.isUserLoggedIn()) {
			User loggedUser = sessionBean.getLoggedUser();
			loggedUser.setIntention(submittedIntention.getIntention());
			profileService.updateUser(loggedUser);
			return "profile";
		} else {
			return "redirect:login";
		}
	}

	@RequestMapping(value = "/profile-edit", method = RequestMethod.GET)
	public String showProfileEdit(Model model) {
		logger.info("Servlet context path: " + sessionBean.getContextPath());
		if (loginService.isUserLoggedIn()) {
			clearErrors();
			model.addAttribute("passwordChangeForm", new PasswordChangeForm());
			return "profile-edit";
		} else {
			return "redirect:login";
		}
	}

	@RequestMapping(value = "/profile-edit", method = RequestMethod.POST)
	public String changePassword(@Valid PasswordChangeForm passwordChangeForm, BindingResult bindingResult) {
		logger.info("Servlet context path: " + sessionBean.getContextPath());
		// prepareFieldErrorsToMakeValuesNotCleared(bindingResult);
		if (loginService.isUserLoggedIn()) {
			clearErrors();
			clearInfoFlags();
			checkErrors(passwordChangeForm);
			sessionBean.setPhotoChangedSuccessfully(false);
			if (!(bindingResult.hasErrors() || sessionBean.hasErrors())) {
				String newSalt = PasswordlAndRandomUtil.generateSaltHex();
				sessionBean.getLoggedUser().setPassword(
						PasswordlAndRandomUtil.getHashedPasswordAfterMigrationGivenGivenSaltAndPlainPassword(newSalt,
								passwordChangeForm.getNewPassword()));
				sessionBean.getLoggedUser().setSalt(newSalt);
				sessionBean.getLoggedUser().setIsMigrated(Boolean.TRUE);
				profileService.updateUser(sessionBean.getLoggedUser());
				sessionBean.setPasswordChangedSuccessfully(true);
				passwordChangeForm.clear();
			}
			return "profile-edit";
		} else {
			return "redirect:login";
		}
	}

	private void checkErrors(PasswordChangeForm passwordChangeForm) {
		String newPassword = passwordChangeForm.getNewPassword();
		String passwordConfirmation = passwordChangeForm.getPasswordConfirmation();
		if (!passwordConfirmation.equals(newPassword)) {
			sessionBean.setPasswordConfirmationError(true);
		}
	}

	/**
	 * Bad smell - should be replaced with validators.
	 */
	private void clearErrors() {
		sessionBean.setPasswordConfirmationError(false);
		sessionBean.setPhotoError(false);
	}

	private void clearInfoFlags() {
		sessionBean.setPasswordChangedSuccessfully(false);
		sessionBean.setPhotoChangedSuccessfully(false);
	}

	@RequestMapping(value = "/photoUpload", method = RequestMethod.POST)
	public String photoUpload(@RequestParam("photo") MultipartFile myFile, Model model) {
		if (loginService.isUserLoggedIn()) {
			clearErrors();
			clearInfoFlags();
			model.addAttribute("passwordChangeForm", new PasswordChangeForm()); // for future usage
			BufferedImage image;
			try {
				if (PhotoValidator.isFileAPhoto(myFile) && (image = ImageIO.read(myFile.getInputStream())) != null
						&& PhotoValidator.hasImageAcceptableDimensions(image)
						&& PhotoValidator.isSizeAccetable(myFile.getSize())) {
					UploadedFile savedFile = uploadedFileService.savePhoto(myFile, sessionBean);
					sessionBean.getLoggedUser().setPhoto(savedFile);
					profileService.updateUser(sessionBean.getLoggedUser());
					sessionBean.setPhotoChangedSuccessfully(true);
				} else {
					// bindingResult.addError(new FieldError("photo", "photo", "Przesłany plik nie
					// spełnia określonych wymagań"));
					sessionBean.setPhotoError(true);
				}
			} catch (IOException e) {
				e.printStackTrace();
				sessionBean.setPhotoError(true);
				return "profile-edit";
			}
			return "profile-edit";
		} else {
			return "redirect:login";
		}
	}

	@RequestMapping(value = "/photoDelete", method = RequestMethod.POST)
	public String photoDelete(Model model) {

		if (loginService.isUserLoggedIn()) {
			// clearErrors();
			// clearInfoFlags();
			model.addAttribute("passwordChangeForm", new PasswordChangeForm()); // for future usage

			File toDelete = new File(this.photoPath + sessionBean.getLoggedUser().getPhoto().getFilenameWithPath());
			User loggedUser = sessionBean.getLoggedUser();
			if (toDelete.exists()) {
				toDelete.delete();
				logger.info("Image: " + toDelete.getAbsolutePath() + " , has been deleted successfuly");
				if(loggedUser.getPhoto() != null) {
					UploadedFile photo = loggedUser.getPhoto();
					loggedUser.setPhoto(null);
					profileService.updateUser(loggedUser);
					uploadedFileService.delete(photo);
				}
			} else {
				loggedUser.setPhoto(null);
				logger.info("Image: " + toDelete.getAbsolutePath()
						+ " , cannot be deleted (may not exist or other problem occured)");
			}
			return "profile-edit";
		} else {
			return "redirect:login";
		}
	}
	
	
}