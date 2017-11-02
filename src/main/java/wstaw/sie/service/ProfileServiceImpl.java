package wstaw.sie.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import wstaw.sie.model.entity.Parameter;
import wstaw.sie.model.entity.User;
import wstaw.sie.repository.ParameterRepository;
import wstaw.sie.repository.UserRepository;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Resource
	UserRepository userRepository;

	@Resource
	ParameterRepository parameterRepository;
	
	@Override
	public void updateUser(User user) {
		userRepository.saveAndFlush(user);
	}

}
