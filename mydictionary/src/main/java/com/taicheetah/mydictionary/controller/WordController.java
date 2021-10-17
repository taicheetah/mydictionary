package com.taicheetah.mydictionary.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taicheetah.mydictionary.dto.form.FilterForm;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.LexicalEntry;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.Pronunciation;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.ResponseFromOxfordDictionariesAPI;
import com.taicheetah.mydictionary.entity.Definition;
import com.taicheetah.mydictionary.entity.DefinitionId;
import com.taicheetah.mydictionary.entity.User;
import com.taicheetah.mydictionary.entity.User_Word;
import com.taicheetah.mydictionary.entity.User_WordId;
import com.taicheetah.mydictionary.entity.Word;
import com.taicheetah.mydictionary.service.OxfordDictionariesAPIService;
import com.taicheetah.mydictionary.service.User_WordService;
import com.taicheetah.mydictionary.service.WordService;

@Controller
@RequestMapping("/word")
public class WordController {
	
	@Value("${stubMode}")
	private String STUB_MODE;
	@Value("${pageSize}")
	private int PAGE_SIZE;
	@Value("${defaultSortBy}")
	private String defaultSortAttribute;
	
	private static final String SESSION_KEY_PAGEABLE = "pageable";
	private static final String SESSION_KEY_FILTER_FORM = "filterForm";

	private WordService wordService;
	private User_WordService user_wordService;
	private OxfordDictionariesAPIService oxfordDictionariesAPIService;
	
	HttpSession session;
	
	@Autowired
	public WordController(WordService theWordService, User_WordService theUser_wordService, 
							OxfordDictionariesAPIService theOxfordDictionariesAPIService,HttpSession thesession) {
		wordService = theWordService;
		user_wordService = theUser_wordService;
		oxfordDictionariesAPIService = theOxfordDictionariesAPIService;
		session = thesession;
	}
	
	@GetMapping("/list")
	public String listWordsGet(@RequestParam("pageNumber")int pageNumber, @RequestParam("sortAttribute")String sortAttribute, @RequestParam("descending")boolean descending,
							@AuthenticationPrincipal User loginUser, Model theModel) {
		
		if(StringUtils.isEmpty(sortAttribute)){
			sortAttribute = defaultSortAttribute;
		}
		
		// for pagination
		Pageable pageable;
		
		if(descending) {
			pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(sortAttribute).descending());
		} else {
			pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(sortAttribute));
		}
		
		session.setAttribute(SESSION_KEY_PAGEABLE, pageable);
		
		return listWords(loginUser, theModel);
	}
	
	@GetMapping("/filter")
	public String listWordsFilter(@ModelAttribute("filter")FilterForm theFilter, @AuthenticationPrincipal User loginUser, Model theModel) {
		
		session.setAttribute(SESSION_KEY_FILTER_FORM, theFilter);
		
		Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by(defaultSortAttribute).descending());
		session.setAttribute(SESSION_KEY_PAGEABLE, pageable);
		
		return listWords(loginUser, theModel);
	}
	
	@PostMapping("/update")
	public String listWordsUpdate(@ModelAttribute("user")User theUser, @AuthenticationPrincipal User loginUser, Model theModel) {
		List<User_Word> dbUser_Words = new ArrayList<>();
		
		for(User_Word tempUser_Word: theUser.getUser_Words()) {
			User_Word dbUser_Word = user_wordService.findById(tempUser_Word.getUser_WordId());
			dbUser_Word.setRememberFlg(tempUser_Word.isRememberFlg());
			dbUser_Words.add(dbUser_Word);
		}
		user_wordService.saveAll(dbUser_Words);
		
		return listWords(loginUser, theModel);
	}
	
	private String listWords(User theUser, Model theModel) {
		
		FilterForm theFilter = (FilterForm) session.getAttribute(SESSION_KEY_FILTER_FORM);
		
		// when first access
		if(theFilter == null) {
			theFilter = new FilterForm("",null,false);
			session.setAttribute(SESSION_KEY_FILTER_FORM, theFilter);
		}
		
		Pageable pageable = (Pageable) session.getAttribute(SESSION_KEY_PAGEABLE);
		
		Page<User_Word> user_WordsPage = retrieveUser_Words(theUser, theFilter, pageable);
		
		theUser.setUser_Words(user_WordsPage.getContent());
		
		theModel.addAttribute("filterForm", theFilter);
		theModel.addAttribute("user", theUser);
		theModel.addAttribute("page", user_WordsPage);
		theModel.addAttribute("sortAttribute", pageable.getSort().iterator().next().getProperty());
		theModel.addAttribute("descending", String.valueOf(pageable.getSort().iterator().next().isDescending()));
			
		return "word/list-words";
	}
	
	private Page<User_Word> retrieveUser_Words(User theUser, FilterForm theFilter, Pageable pageable){
		
		if(StringUtils.isEmpty(theFilter.getFilterDate())) {
			// invoke JPA query
			if(theFilter.getRememberFlg() != null) {
				return user_wordService.findAllWordsByUserIdAndRememberFlgAndWordNameContains(theUser.getUserId(), theFilter.getRememberFlg(), theFilter.getFilterWord(), pageable);
			} else {
				return user_wordService.findAllWordsByUserIdAndWordNameContains(theUser.getUserId(), theFilter.getFilterWord(), pageable);
			}
		}
		
		// get the start of the day from parameter date and user's timezone
		ZonedDateTime zonedStartDate = ZonedDateTime.parse(theFilter.getFilterDate() + " 00:00:00 " + theUser.getTimeZone(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV"));
		
		// get the end of the day
		ZonedDateTime zonedEndDate = zonedStartDate.plusDays(1);
		
		Date startDate = Date.from(zonedStartDate.toInstant());
		Date endDate = Date.from(zonedEndDate.toInstant());
		
		// invoke JPA query
		if(theFilter.getRememberFlg() != null) {
			return user_wordService.findAllWordsByUserIdAndRememberFlgAndDateTimeBetweenAndWordNameContains(theUser.getUserId(), theFilter.getRememberFlg(), startDate, endDate, theFilter.getFilterWord(), pageable);	
		} else {
			return user_wordService.findAllWordsByUserIdAndDateTimeBetweenAndWordNameContains(theUser.getUserId(),startDate,endDate,theFilter.getFilterWord(),pageable );		
		}
	}
	
	@GetMapping("/showSearchWordForm")
	public String showSaveUserForm(Model theModel) {	
		return "word/search-word";
	}
	
	@GetMapping("/searchWord")
	public String searchWord(@RequestParam("wordName") String wordName, Model theModel) {
		
		// retrieve DTO from Oxford dictionaries API
		ResponseFromOxfordDictionariesAPI response = null;
		
		Word dbWord = wordService.findByWordName(wordName);
		
		// when the word exist in DB
		if(dbWord != null) {
			theModel.addAttribute("word",dbWord);
			
			return "word/search-word";
		}
		
		if("ON".equals(STUB_MODE)) {
			response = oxfordDictionariesAPIService.searchStub(wordName);
		} else {
			try {
				response = oxfordDictionariesAPIService.search(wordName);
			} catch (Exception e) {
				// in the case of error except for 404
				
				theModel.addAttribute("errorMessage","Something wrong happend. Try again after a while.");
				theModel.addAttribute("isAPIenabled",true);
				
				return "word/search-word";
			}
		}
		
		
		Word theWord = new Word(wordName);
		if(response != null && response.getResults() != null) {
			
			List<LexicalEntry> lexicalEntries = response.getResults().get(0).getLexicalEntries();
			
			// set the pronunciation in theWord
			Pronunciation thePronunciation = lexicalEntries.get(0).getEntries().get(0).getPronunciations().get(0);
			theWord.setPhoneticSpelling(thePronunciation.getPhoneticSpelling());
			theWord.setAudio_url(thePronunciation.getAudioFile());

			List<Definition> theDefinitions = new ArrayList<>();
			DefinitionId theDefinitionId = null;
			String theLexicalCategory = null;
			String theDefinition = null;
			String theExample = null;
			
			// retrieve the definition by lexical category
			for(LexicalEntry tempLexicalEntry: lexicalEntries) {
				theLexicalCategory = tempLexicalEntry.getLexicalCategory().getText();
				theDefinitionId = new DefinitionId(theLexicalCategory);
				
				// retrieve only first definition
				theDefinition = tempLexicalEntry.getEntries().get(0).getSenses().get(0).getDefinitions().get(0);
				
				// when examples exist
				if(tempLexicalEntry.getEntries().get(0).getSenses().get(0).getExamples() != null) {
					// retrieve only first example
					theExample = tempLexicalEntry.getEntries().get(0).getSenses().get(0).getExamples().get(0).getText();
				}
				theDefinitions.add(new Definition(theDefinitionId,theDefinition,theExample));
			}
			
			// set the definitions in theWord
			theWord.setDefinitions(theDefinitions);
			
		} else if(response != null && response.getResults() == null){
			// when 404
			theModel.addAttribute("errorMessage","There is not '" + wordName + "' in this dictionray.");
			return "word/search-word";
		}
		
		theModel.addAttribute("word",theWord);
		
		return "word/search-word";
	}

	@PostMapping("register")
	public String registerWord(@ModelAttribute("word") Word theWord, Model theModel, @AuthenticationPrincipal User theUser) {	
		
		Word dbWord = wordService.findByWordName(theWord.getWordName());
		if(dbWord == null) {	
			// associate theWord with theDefinition
			for(Definition tempDefinition: theWord.getDefinitions()) {
				tempDefinition.setWord(theWord);
			}
			// save the word
			dbWord = wordService.save(theWord);
		}
		
		// when the user does not register the word yet
		if(user_wordService.findById(new User_WordId(theUser.getUserId(), dbWord.getWordName())) == null) {
			
			User_Word theUser_Word = new User_Word();
			theUser_Word.setUser_WordId(new User_WordId(theUser.getUserId()));
			theUser_Word.setWord(dbWord);
			theUser_Word.setDateTime(new Date());
			theUser_Word.setRememberFlg(false);
			
			user_wordService.save(theUser_Word);
			
			theModel.addAttribute("registerMessage", "registered");
		} else {
			// when the  user already registered the word
			theModel.addAttribute("registerMessage", "'"+dbWord.getWordName()+"' is allready registered in your dictionary");
		}

		
		theModel.addAttribute("word", theWord);

		return "word/search-word";
	}
	
}
