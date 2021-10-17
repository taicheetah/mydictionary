package com.taicheetah.mydictionary.service;

import java.util.List;

import com.taicheetah.mydictionary.entity.User;

public interface UserService{

	public List<User> findAll();
	
	public User findById(String theId);
	
	public User save(User theUser);
	
	public void deleteById(String theId);
	
}
