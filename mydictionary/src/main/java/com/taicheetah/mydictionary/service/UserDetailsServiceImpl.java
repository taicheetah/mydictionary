package com.taicheetah.mydictionary.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taicheetah.mydictionary.dao.UserRepository;
import com.taicheetah.mydictionary.entity.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepository;
	
	@Autowired
	public UserDetailsServiceImpl(UserRepository theUserRepository) {
		userRepository = theUserRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String theEmail) throws UsernameNotFoundException {
		Optional<User> result = userRepository.findByEmail(theEmail);
		
		User theUser = null;
		if(result.isPresent()) {
			theUser = result.get();
		} else {
			throw new UsernameNotFoundException("Did not find Email - " + theEmail);
		}
		
		return theUser;
	}

}
