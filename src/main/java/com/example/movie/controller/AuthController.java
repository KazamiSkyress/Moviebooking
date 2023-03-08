package com.example.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import  com.example.movie.dto.AuthenticationRequest;
import  com.example.movie.dto.AuthenticationResponse;
import  com.example.movie.dto.NewPassword;
import  com.example.movie.entity.UserModel;
import  com.example.movie.exception.UsernameAlreadyExists;
import  com.example.movie.repository.UserRepository;
import  com.example.movie.services.UserModelService;

import io.swagger.annotations.Api;

/**
 * @author 
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api
public class AuthController {

	// Injected UserModelService bean
	@Autowired
	private UserModelService userModelService;

	// Injected UserRespository bean
	@Autowired
	private UserRepository userRepository;

//  Kafka Configuration
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
//	Kafka Topic Name
	private static final String KAFKA_TOPIC = "movies";

	/**
	 * Controller Method to register a new User HTTP Post Request
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/register
	 */
	@PostMapping("/movies/register")
	public ResponseEntity<?> register(@RequestBody UserModel userModel) {
		try {
			UserModel savedUser = userModelService.createUser(userModel);
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (UsernameAlreadyExists e) {
			return new ResponseEntity<>(new AuthenticationResponse("Given userId/email already exists"),
					HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(new AuthenticationResponse("Application has faced an issue"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Controller Method for user login HTTP Post Request
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/login
	 */
	@PostMapping("/movies/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		UserModel checkUser = userRepository.findByUsername(username);
		if (checkUser.getPassword().equals(password)) {
			return new ResponseEntity<>(userModelService.findByUsername(username), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new AuthenticationResponse("Bad Credentials " + username),
					HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * Controller Method for user password reset HTTP Post Request
	 * 
	 * @return ResponseEntity
	 * 
	 *         http://localhost:8082/api/v1.0/tweets/ram/forgot
	 */
	@PutMapping(value = "/movies/{username}/forgot")
	public ResponseEntity<?> changePassword(@PathVariable("username") String username,
			@RequestBody NewPassword newPassword) {
		try {
			return new ResponseEntity<>(
					userModelService.changePassword(username, newPassword.getNewPassword(), newPassword.getContact()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new AuthenticationResponse("Unable to change password"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
