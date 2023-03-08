package com.example.movie.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie.entity.UserModel;
import com.example.movie.exception.UsernameAlreadyExists;
import com.example.movie.repository.UserRepository;

/**
 * @author Bhakti Joshi
 */
@Service
public class UserModelService {

//  injected User Repository bean
	@Autowired
	private UserRepository userRepository;
	
	Logger logger = LoggerFactory.getLogger(UserModelService.class);

	/**
	 * find user by username
	 * 
	 * @return UserModel
	 */
	public UserModel findByUsername(String username) {
		UserModel userModel = userRepository.findByUsername(username);
		UserModel newUserModel = new UserModel(userModel.getUsername(), userModel.getFirstName(),
				userModel.getLastName(), userModel.getEmail(), userModel.getPassword(), userModel.getContactNum());
		return newUserModel;
	}

	/**
	 * create a new user
	 * 
	 * @return UserModel
	 */
	public UserModel createUser(UserModel username) throws UsernameAlreadyExists {
		UserModel foundedUser = userRepository.findByUsername(username.getUsername());
		if (foundedUser != null) {
			logger.error("Username is not available");
			throw new UsernameAlreadyExists("username already exists");
		}
		return userRepository.save(username);
	}


	/**
	 * Method to get all users
	 * 
	 * @return List<UserModel>
	 */
	public List<UserModel> getAllUsers() {
		List<UserModel> userModels = (List<UserModel>) userRepository.findAll();
		List<UserModel> newUserModel = userModels.stream().map(user -> {
			return new UserModel(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), "null",
					"null");
		}).collect(Collectors.toList());
		return newUserModel;
	}
	

	/**
	 * Reset User password
	 * 
	 * @return userModel
	 */
	public UserModel changePassword(String username, String newPassword, String contact) throws Exception {
		UserModel userDetails = userRepository.findByUsername(username);
		if (((String) userDetails.getContactNum()).equalsIgnoreCase(contact)
				&& userDetails.getUsername().equalsIgnoreCase(username)) {
			userDetails.setPassword(newPassword);
			logger.info("Password Updated for --> {}",userDetails);
			return userRepository.save(userDetails);
		} else {
			logger.error("cannot change password");
			throw new Exception("Unable to change password");
		}
	}
	
	// Method to search for like users by username
	public List<UserModel> getUsersByUsername(String username) {
		return userRepository.findAll();
	}

}
