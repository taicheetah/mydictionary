package com.taicheetah.mydictionary.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.taicheetah.mydictionary.entity.User_Word;
import com.taicheetah.mydictionary.entity.User_WordId;

public interface User_WordService {
	
	public Page<User_Word> findAllWordsByUserId(int userId, Pageable pageable);
	
	public Page<User_Word> findAllWordsByUserIdAndWordNameContains(int userId, String keyWord, Pageable pageable);
	
	public Page<User_Word> findAllWordsByUserIdAndRememberFlgAndWordNameContains(int userId, boolean rememberFlg,
			String keyWord, Pageable pageable);

	public Page<User_Word> findAllWordsByUserIdAndDateTimeBetweenAndWordNameContains(int userId, Date startDate,
			Date endDate, String keyWord, Pageable pageable);
	
	public Page<User_Word> findAllWordsByUserIdAndRememberFlgAndDateTimeBetweenAndWordNameContains(int userId,
			boolean rememberFlg, Date startDate, Date endDate, String keyWord, Pageable pageable);
	
	public User_Word findById(User_WordId theUser_WordId);

	public User_Word save(User_Word theUser_Word);
	
	public List<User_Word> saveAll(List<User_Word> theUser_Words);












}
