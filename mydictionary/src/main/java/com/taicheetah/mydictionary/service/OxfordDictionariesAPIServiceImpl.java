package com.taicheetah.mydictionary.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.ErrorInfo;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.ResponseFromOxfordDictionariesAPI;

@Service
public class OxfordDictionariesAPIServiceImpl implements OxfordDictionariesAPIService {
	
	@Value("${appId}")
	private String appId;
	@Value("${appKey}")
	private String appKey;
	@Value("${URL}")
	private String urlStr;
	@Value("${fields}")
	private String fields;
	
	private static Logger logger = Logger.getLogger(OxfordDictionariesAPIServiceImpl.class.getName());
	
	@Override
	public ResponseFromOxfordDictionariesAPI search(String wordName) throws Exception {
		
		ResponseFromOxfordDictionariesAPI response = null;
		
		HttpURLConnection urlConnection = null;
			
			URL url = new URL(urlStr + wordName + fields);
			urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setRequestProperty("Accept", "application/json");
			urlConnection.setRequestProperty("app_id", appId);
			urlConnection.setRequestProperty("app_key", appKey);
			urlConnection.connect();
			
			int code = urlConnection.getResponseCode();
			
			// in the case of success
			if(code == 200) {
				StringBuilder stringBuilder = retrieveJson(urlConnection.getInputStream());
				
				logger.info(stringBuilder);
				
				// create Json For Mock
				createJsonForMock(wordName, stringBuilder);
				
				ObjectMapper mapper = new ObjectMapper();
				
				response = mapper.readValue(stringBuilder.toString(), ResponseFromOxfordDictionariesAPI.class);
				
			} else if(code == 404){
				// in the case of not finding the word, create response that involve nothing
				response = new ResponseFromOxfordDictionariesAPI();
			} else {
				// in the case of error except for 404, throw error that involve error message
				StringBuilder stringBuilder = retrieveJson(urlConnection.getErrorStream());
				ObjectMapper mapper = new ObjectMapper();
				ErrorInfo errorResponse = mapper.readValue(stringBuilder.toString(), ErrorInfo.class);
				
				throw new RuntimeException(errorResponse.getError());
			}
					

		
		return response;
	}
	
	// json file to stringBuilder
	private StringBuilder retrieveJson(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		
		String line = null;
		while ((line= reader.readLine()) != null) {
			stringBuilder.append(line + "\n");
		}
		reader.close();
		
		return stringBuilder;
	}

	
	
	public void createJsonForMock(String wordName, StringBuilder content) {
		
		try {
			File newJson = new File("src/main/resources/data/" +wordName +".json");

			FileWriter filewriter = new FileWriter(newJson);
			
			filewriter.write(content.toString());
			
			filewriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public ResponseFromOxfordDictionariesAPI searchStub(String wordName) {
		
		ObjectMapper mapper = new ObjectMapper();
		
		ResponseFromOxfordDictionariesAPI response = new ResponseFromOxfordDictionariesAPI();
		try {
			response = mapper.readValue(new File("src/main/resources/data/" +wordName +".json"), ResponseFromOxfordDictionariesAPI.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
}
