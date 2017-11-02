package wstaw.sie.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import wstaw.sie.model.entity.UploadedFile;
import wstaw.sie.model.session.SessionBean;

public interface UploadedFileService {

	public void save(UploadedFile uploadedFile);

	public void restoreUsersPhotosFromDatabase();

	public UploadedFile savePhoto(MultipartFile myFile, SessionBean sessionBean) throws FileNotFoundException, IOException;

	public void delete(UploadedFile uploadedFile);

}
