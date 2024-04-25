package com.fdm.barbieBank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	public void changeCurrentPassword(User user, String newPassword) {
		
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		
	}
	
	public boolean checkIfValidCurrentPassword(User user, String currentPassword) {
		
		return passwordEncoder.matches(currentPassword, user.getPassword());
		
	}
	
	public boolean registerNewUser(User user) {

		Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

		if(userOptional.isEmpty()) {

			String password = user.getPassword();
			String encodedPassword = passwordEncoder.encode(password);
			user.setPassword(encodedPassword);
			userRepository.save(user);
			return true;

		}
		else {

			return false;

		}

	}
	
	public User findVerifiedUser(String username) {

		return userRepository.findByUsername(username).get();

	}
	
}
