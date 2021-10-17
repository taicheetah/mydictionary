package com.taicheetah.mydictionary.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taicheetah.mydictionary.dao.DefinitionRepository;
import com.taicheetah.mydictionary.dao.WordRepository;
import com.taicheetah.mydictionary.entity.Word;

@Service
public class WordServiceImpl implements WordService {

	private WordRepository wordRepository;
	
	@Autowired
	public WordServiceImpl(WordRepository thewordRepository) {
		wordRepository = thewordRepository;
	}
	
	@Override
	@Transactional
	public Word save(Word theWord) {
		
		Word dbWord = wordRepository.save(theWord);
		
		return dbWord;
		
	}

	@Override
	@Transactional
	public void deleteByWordName(String wordName) {
	
		wordRepository.deleteById(wordName);
	}

	@Override
	@Transactional
	public List<Word> findAll() {
		
		return wordRepository.findAll();
	}

	@Override
	public Word findByWordName(String wordName) {
		Optional<Word> result = wordRepository.findById(wordName);
		
		Word theWord = null;
		
		if(result.isPresent()) {
			theWord = result.get();
		}
		
		return theWord;
	}
	
	

}
