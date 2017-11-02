package wstaw.sie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wstaw.sie.model.entity.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository <Parameter, String>{ 
	
    Parameter findByName(String name);

}
