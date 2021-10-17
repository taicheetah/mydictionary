package com.taicheetah.mydictionary.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taicheetah.mydictionary.entity.Word;

public interface WordRepository extends JpaRepository<Word, String> {

}
