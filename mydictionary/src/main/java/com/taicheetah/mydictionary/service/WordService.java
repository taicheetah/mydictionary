package com.taicheetah.mydictionary.service;

import java.util.List;

import com.taicheetah.mydictionary.entity.Definition;
import com.taicheetah.mydictionary.entity.Word;

public interface WordService {

	public Word save(Word theWord);
	
	public void deleteByWordName(String wordName);
	
	public List<Word> findAll();
	
	public Word findByWordName(String wordName);
}
