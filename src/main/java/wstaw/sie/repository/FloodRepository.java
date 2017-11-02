package wstaw.sie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import wstaw.sie.model.entity.Flood;
import wstaw.sie.model.entity.User;

@Repository
public interface FloodRepository extends JpaRepository <Flood, String>{ 
	
	@Query("SELECT f FROM Flood f WHERE f.ip = ?1")
    Flood findByIp(String ip);
	
}
