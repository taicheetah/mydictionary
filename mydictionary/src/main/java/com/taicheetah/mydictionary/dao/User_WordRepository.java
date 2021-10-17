package com.taicheetah.mydictionary.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.taicheetah.mydictionary.entity.User_Word;
import com.taicheetah.mydictionary.entity.User_WordId;

public interface User_WordRepository extends JpaRepository<User_Word, User_WordId> {
	
	@Query("SELECT uw FROM User_Word uw WHERE uw.user_WordId.userId = :userId")
	Page<User_Word> findAllWordsByUserId(@Param("userId") String userId, Pageable pageable);
	
	@Query("SELECT uw FROM User_Word uw WHERE uw.user_WordId.userId = :userId AND uw.user_WordId.wordName LIKE %:keyWord%")
	Page<User_Word> findAllWordsByUserIdAndWordNameContains(@Param("userId") String userId, @Param("keyWord")String keyWord, Pageable pageable);	
	
	@Query("SELECT uw FROM User_Word uw WHERE uw.user_WordId.userId = :userId AND uw.rememberFlg = :rememberFlg AND uw.user_WordId.wordName LIKE %:keyWord%")
	Page<User_Word> findAllWordsByUserIdAndRememberFlgAndWordNameContains(@Param("userId") String userId, @Param("rememberFlg")boolean rememberFlg, @Param("keyWord")String keyWord, Pageable pageable);
	
	@Query("SELECT uw FROM User_Word uw WHERE uw.user_WordId.userId = :userId AND uw.dateTime BETWEEN :startDate AND :endDate AND uw.user_WordId.wordName LIKE %:keyWord%")
	Page<User_Word> findAllWordsByUserIdAndDateTimeBetweenAndWordNameContains(@Param("userId") String userId, @Param("startDate")Date startDate, @Param("endDate")Date endDate, @Param("keyWord")String keyWord, Pageable pageable);
	
	@Query("SELECT uw FROM User_Word uw WHERE uw.user_WordId.userId = :userId AND uw.rememberFlg = :rememberFlg AND uw.dateTime BETWEEN :startDate AND :endDate AND uw.user_WordId.wordName LIKE %:keyWord%")
	Page<User_Word> findAllWordsByUserIdAndRememberFlgAndDateTimeBetweenAndWordNameContains(@Param("userId") String userId, @Param("rememberFlg")boolean rememberFlg, @Param("startDate")Date startDate, @Param("endDate")Date endDate, @Param("keyWord")String keyWord, Pageable pageable);

}
