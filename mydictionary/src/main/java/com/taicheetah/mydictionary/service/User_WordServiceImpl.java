package com.taicheetah.mydictionary.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taicheetah.mydictionary.dao.User_WordRepository;
import com.taicheetah.mydictionary.entity.User_Word;
import com.taicheetah.mydictionary.entity.User_WordId;

@Service
public class User_WordServiceImpl implements User_WordService {

	private User_WordRepository user_WordRepository;
	
	@Autowired
	public User_WordServiceImpl(User_WordRepository theUser_WordRepository) {
		user_WordRepository = theUser_WordRepository;
	}
	
	
	@Override
	@Transactional
	public Page<User_Word> findAllWordsByUserId(int userId, Pageable pageable){
		
		return user_WordRepository.findAllWordsByUserId(userId, pageable);
	}
	
	@Override
	@Transactional
	public Page<User_Word> findAllWordsByUserIdAndWordNameContains(int userId, String keyWord, Pageable pageable){
		
		return user_WordRepository.findAllWordsByUserIdAndWordNameContains(userId, keyWord, pageable);
	}
	
	@Override
	@Transactional
	public Page<User_Word> findAllWordsByUserIdAndRememberFlgAndWordNameContains(int userId, boolean rememberFlg, String keyWord, Pageable pageable){
		
		return user_WordRepository.findAllWordsByUserIdAndRememberFlgAndWordNameContains(userId, rememberFlg, keyWord, pageable);
	}
	
	@Override
	@Transactional
	public Page<User_Word> findAllWordsByUserIdAndDateTimeBetweenAndWordNameContains(int userId, Date startDate, Date endDate,String keyWord, Pageable pageable){
		
		return user_WordRepository.findAllWordsByUserIdAndDateTimeBetweenAndWordNameContains(userId, startDate, endDate, keyWord, pageable);
	}
	
	@Override
	@Transactional
	public Page<User_Word> findAllWordsByUserIdAndRememberFlgAndDateTimeBetweenAndWordNameContains(int userId, boolean rememberFlg, Date startDate, Date endDate,String keyWord, Pageable pageable){
		
		return user_WordRepository.findAllWordsByUserIdAndRememberFlgAndDateTimeBetweenAndWordNameContains(userId, rememberFlg, startDate, endDate, keyWord, pageable);
	}
	
	@Override
	@Transactional
	public User_Word save(User_Word theUser_Word){
		return user_WordRepository.save(theUser_Word);
	}
	
	@Override
	@Transactional
	public List<User_Word> saveAll(List<User_Word> theUser_Words){
		return user_WordRepository.saveAll(theUser_Words);
	}
	
	@Override
	@Transactional
	public User_Word findById(User_WordId theUser_WordId){
		Optional<User_Word> result = user_WordRepository.findById(theUser_WordId);
		
		User_Word theUser_Word = null;
		
		if(result.isPresent()) {
			theUser_Word = result.get();
		}
		
		return theUser_Word;
	}
}
