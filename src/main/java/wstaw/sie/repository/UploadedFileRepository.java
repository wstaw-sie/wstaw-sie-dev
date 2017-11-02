package wstaw.sie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wstaw.sie.model.entity.Parameter;
import wstaw.sie.model.entity.UploadedFile;

@Repository
public interface UploadedFileRepository extends JpaRepository <UploadedFile, Integer>{ 
	
}
