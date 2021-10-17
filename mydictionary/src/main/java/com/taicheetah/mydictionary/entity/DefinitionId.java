package com.taicheetah.mydictionary.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DefinitionId implements Serializable{

	@Column(name="word_name")
	private String wordName;
	
	@Column(name="lexical_category")
	private String lexicalCategory;
	
	public DefinitionId() {
		
	}
	

	public DefinitionId(String lexicalCategory) {
		this.lexicalCategory = lexicalCategory;
	}


	public DefinitionId(String wordName, String lexicalCategory) {
		this.wordName = wordName;
		this.lexicalCategory = lexicalCategory;
	}


	public String getWordName() {
		return wordName;
	}

	public void setWordName(String wordName) {
		this.wordName = wordName;
	}

	public String getLexicalCategory() {
		return lexicalCategory;
	}

	public void setLexicalCategory(String lexicalCategory) {
		this.lexicalCategory = lexicalCategory;
	}

	
	@Override
	public String toString() {
		return "DefinitionId [wordName=" + wordName + ", lexicalCategory=" + lexicalCategory + "]";
	}

		
}
