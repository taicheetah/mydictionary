package com.taicheetah.mydictionary.service;

import com.taicheetah.mydictionary.dto.oxfordAPI.response.ResponseFromOxfordDictionariesAPI;

public interface OxfordDictionariesAPIService {

	public ResponseFromOxfordDictionariesAPI search(String wordName) throws Exception;
	
	public ResponseFromOxfordDictionariesAPI searchStub(String wordName);
}
