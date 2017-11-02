package wstaw.sie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wstaw.sie.model.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository <Group, Integer>{ 
	
}
