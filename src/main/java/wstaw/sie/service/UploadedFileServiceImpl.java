package wstaw.sie.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import wstaw.sie.model.entity.Group;
import wstaw.sie.model.entity.Parameter;
import wstaw.sie.model.entity.UploadedFile;
import wstaw.sie.model.session.SessionBean;
import wstaw.sie.repository.GroupRepository;
import wstaw.sie.repository.ParameterRepository;
import wstaw.sie.repository.UploadedFileRepository;
import wstaw.sie.util.FilenameExtensionExtractor;
import wstaw.sie.util.PasswordlAndRandomUtil;
import wstaw.sie.util.StringUtil;

@Service
public class UploadedFileServiceImpl implements UploadedFileService {

	@Resource
	UploadedFileRepository uploadedFileRepository;

	@Resource
	ParameterRepository parameterRepository;
	
	@Resource
	GroupRepository groupRepository;
	
	@Value("${photo.path}")
	private String photoPath;
	
	private static Logger logger = Logger.getLogger(UploadedFileServiceImpl.class);

	@Override
	public void save(UploadedFile user) {
		uploadedFileRepository.saveAndFlush(user);
	}

	@Override
	public void restoreUsersPhotosFromDatabase() {
		Parameter parameter = parameterRepository.findByName("STORE_FILES_IN_DB");
		if (parameter != null && "y".equalsIgnoreCase(parameter.getParamValue())) {
			logger.info("... IN PROGRESS ...");
			for(Group group: groupRepository.findAll()) {
				createFoldersIfDontExist( this.photoPath + "/users/" + group.getId());
			}
			for(UploadedFile uf: uploadedFileRepository.findAll()) {
				try {
					FileOutputStream fos = new FileOutputStream(this.photoPath + uf.getFilenameWithPath(), false);
					fos.write(uf.getBytes());
					fos.flush();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * @return UploadedFile entity of saved file.
	 */
	@Override
	public UploadedFile savePhoto(MultipartFile myFile, SessionBean sessionBean) throws FileNotFoundException, IOException {
		String fileName = getPhotoFilename(myFile, sessionBean);
		String dbPathToFolder = createPathToUsersFolder(sessionBean);
		String fullPathString = this.photoPath + dbPathToFolder;
		File directories = createFoldersIfDontExist(fullPathString);
		String filePath = fullPathString + "/" + fileName;
		FileOutputStream fos = new FileOutputStream(filePath, false);
		fos.write(myFile.getBytes());
		logger.info("Image: " + directories.getAbsolutePath() + " , has been saved successfuly");
		String relativePathToStore = dbPathToFolder + "/" + fileName;
		UploadedFile uploadedFile = new UploadedFile();
		uploadedFile.setBytes(myFile.getBytes());
		uploadedFile.setFilenameWithPath(relativePathToStore);
		this.save(uploadedFile);
		return uploadedFile;
	}

	private String createPathToUsersFolder(SessionBean sessionBean) {
		return "/users/" + sessionBean.getLoggedUser().getGroupOfUser().getId();
	}

	private File createFoldersIfDontExist(String fullPathString) {
		File directories = new File(fullPathString);
		if (!directories.exists()) {
			directories.mkdirs();
		}
		return directories;
	}

	private String getPhotoFilename(MultipartFile myFile, SessionBean sessionBean) {
		String randomPrefixOfPhoto = PasswordlAndRandomUtil.getRandomStringHexEncoded(16);
		return randomPrefixOfPhoto + "_" + sessionBean.getLoggedUser().getId() + "_"
				+ StringUtils.replaceEachRepeatedly(
						sessionBean.getLoggedUser().getLastName() + sessionBean.getLoggedUser().getFirstName() + "."
								+ FilenameExtensionExtractor.extractFileExtension(myFile.getOriginalFilename()),
						StringUtil.TO_REPLACE, StringUtil.REPLACEMENT);
	}
	
	@Override
	public void delete(UploadedFile uploadedFile) {
		uploadedFileRepository.delete(uploadedFile);
	}

}
