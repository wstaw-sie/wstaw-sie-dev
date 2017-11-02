package wstaw.sie.util;

import org.modelmapper.ModelMapper;

import wstaw.sie.model.dto.UserDTO;
import wstaw.sie.model.entity.User;

public class ModelUtil {

	private static ModelMapper modelMapper = new ModelMapper();
	
	public static UserDTO userEntity2DTO(User user)
	{
		return modelMapper.map(user, UserDTO.class);
	}
	
	public static User userDTO2Entity(UserDTO userDTO)
	{
		User retUser = modelMapper.map(userDTO, User.class);
		return retUser;
	}
}
