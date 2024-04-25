package com.fdm.barbieBank.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.repository.UserRepository;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> userOptional = userRepository.findByUsername(username);
		User user = userOptional.orElseThrow(()
				-> new UsernameNotFoundException("User with username " + username + " not found."));
		return new UserPrincipal(user);

	}

}
