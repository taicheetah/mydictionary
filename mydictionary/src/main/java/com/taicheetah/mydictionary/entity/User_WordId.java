package com.taicheetah.mydictionary.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class User_WordId implements Serializable{
	
	@Column(name="user_id")
	private int userId;

	@Column(name="word_name")
	private String wordName;
	
	
	public User_WordId() {
		
	}
	
	public User_WordId(int userId) {
		this.userId = userId;
	}

	public User_WordId(int userId, String wordName) {
		this.userId = userId;
		this.wordName = wordName;
	}

	
	public long getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getWordName() {
		return wordName;
	}

	public void setWordName(String wordName) {
		this.wordName = wordName;
	}

	
	@Override
	public String toString() {
		return "User_WordId [userId=" + userId + ", wordName=" + wordName + "]";
	}

		
}
