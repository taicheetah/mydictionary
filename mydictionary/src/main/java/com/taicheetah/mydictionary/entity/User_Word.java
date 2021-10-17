package com.taicheetah.mydictionary.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="users_words")
public class User_Word {

	@EmbeddedId
	private User_WordId user_WordId;
	
	@Column(name="datetime")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date dateTime;
	
	@Column(name="remember_flag")
	private boolean rememberFlg;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade= {CascadeType.DETACH, CascadeType.MERGE, 
			CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="word_name")
	@MapsId("wordName")
	private Word word;
	
	public User_Word() {
		
	}
	
	public User_Word(User_WordId user_WordId, Date dateTime, boolean rememberFlg, Word word) {
		this.user_WordId = user_WordId;
		this.dateTime = dateTime;
		this.rememberFlg = rememberFlg;
		this.word = word;
	}

	public User_WordId getUser_WordId() {
		return user_WordId;
	}

	public void setUser_WordId(User_WordId user_WordId) {
		this.user_WordId = user_WordId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getViewDateTime(String timeZone) {
		ZonedDateTime zdt = ZonedDateTime.ofInstant(this.dateTime.toInstant(),ZoneId.of(timeZone));
		return zdt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public boolean isRememberFlg() {
		return rememberFlg;
	}

	public void setRememberFlg(boolean rememberFlg) {
		this.rememberFlg = rememberFlg;
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "User_Word [userWordId=" + user_WordId + ", dateTime=" + dateTime + ", rememberFlg=" + rememberFlg
				+ "]";
	}
	
}
