package com.taicheetah.mydictionary.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="definitions")
public class Definition {
	
	@EmbeddedId
	private DefinitionId definitionId;
	
	@Column(name="definition")
	private String definition;
	
	@Column(name="example")
	private String example;
	
	@ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, 
						CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="word_name")
	@MapsId("wordName")
	private Word word;

	public Definition() {
		
	}
	
	
	public Definition(DefinitionId definitionId, String definition, String example) {
		this.definitionId = definitionId;
		this.definition = definition;
		this.example = example;
	}



	public Definition(DefinitionId definitionId, String definition, String example, Word word) {
		this.definitionId = definitionId;
		this.definition = definition;
		this.example = example;
		this.word = word;
	}

	public DefinitionId getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(DefinitionId definitionId) {
		this.definitionId = definitionId;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "Definition [definitionId=" + definitionId + ", definition=" + definition + ", example=" + example + "]";
	}
	
}
