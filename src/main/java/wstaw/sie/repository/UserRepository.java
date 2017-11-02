package wstaw.sie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wstaw.sie.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);
	
	public List<User> findByIdGreaterThanEqualAndIdLessThanEqual(Integer lowId, Integer highId);
	
}
