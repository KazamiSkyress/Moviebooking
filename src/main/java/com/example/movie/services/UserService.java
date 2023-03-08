package com.example.movie.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import com.example.movie.entity.UserModel;
import com.example.movie.repository.UserRepository;

/**
 * @author Prince Kumar
 */
@Service
public class UserService implements UserDetailsService {

//	Injeted userRepository bean
	@Autowired
	private UserRepository userRepository;

	/**
	 * Used for validation
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel foundedUser = userRepository.findByUsername(username);
		if (foundedUser == null)
			return null;
		String name = foundedUser.getUsername();
		String password = foundedUser.getPassword();
		return new User(name, password, new ArrayList<>());
	}


}
