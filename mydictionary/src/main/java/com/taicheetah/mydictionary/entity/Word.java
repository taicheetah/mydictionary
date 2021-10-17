package com.taicheetah.mydictionary.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="words")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Word {

	@Id
	@Column(name="word_name")
	private String wordName;
	
	@Column(name="phonetic_spelling")
	private String phoneticSpelling;
	
	@Column(name="audio_url")
	private String audio_url;
	
	@OneToMany(mappedBy="word",fetch = FetchType.EAGER, cascade= {CascadeType.ALL})
	private List<Definition> definitions;
	
	
	public Word() {
		
	}
	

	public Word(String wordName) {
		this.wordName = wordName;
	}


	public Word(String wordName, String phoneticSpelling, String audio_url, List<Definition> definitions) {
		this.wordName = wordName;
		this.phoneticSpelling = phoneticSpelling;
		this.audio_url = audio_url;
		this.definitions = definitions;
	}



	public String getWordName() {
		return wordName;
	}

	public void setWordName(String wordName) {
		this.wordName = wordName;
	}

	public String getPhoneticSpelling() {
		return phoneticSpelling;
	}

	public void setPhoneticSpelling(String phoneticSpelling) {
		this.phoneticSpelling = phoneticSpelling;
	}

	public String getAudio_url() {
		return audio_url;
	}

	public void setAudio_url(String audio_url) {
		this.audio_url = audio_url;
	}

	public List<Definition> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<Definition> definitions) {
		this.definitions = definitions;
	}
	
	// add convenience methods for bi-directional relationship
	
	public void add(Definition tempDefinition) {
		
		if(definitions == null) {
			definitions = new ArrayList<>();
		}
		
		definitions.add(tempDefinition);
		
		tempDefinition.setWord(this);
	}

	@Override
	public String toString() {
		return "Word [wordName=" + wordName + ", phoneticSpelling=" + phoneticSpelling + ", audio_url=" + audio_url
				+ "]";
	}


	
}
