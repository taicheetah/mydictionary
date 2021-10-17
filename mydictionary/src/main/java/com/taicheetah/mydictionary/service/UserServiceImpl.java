package com.taicheetah.mydictionary.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taicheetah.mydictionary.dao.UserRepository;
import com.taicheetah.mydictionary.entity.User;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository theUserRepository) {
		userRepository = theUserRepository;
	}
	
	@Override
	@Transactional
	public List<User> findAll() {
		
		return userRepository.findAll();
	}

	@Override
	@Transactional
	public User findById(String theId) {
		Optional<User> result = userRepository.findById(theId);
		
		User theUser = null;
		
		if(result.isPresent()) {
			theUser = result.get();
		}
		
		return theUser;
	}

	@Override
	@Transactional
	public User save(User theUser) {

		User dbUser = userRepository.save(theUser);
		
		return dbUser;
	}

	@Override
	@Transactional
	public void deleteById(String theId) {

		userRepository.deleteById(theId);
	}

}
