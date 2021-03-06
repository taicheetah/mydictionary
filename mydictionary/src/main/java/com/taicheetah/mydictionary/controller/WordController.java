package com.taicheetah.mydictionary.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.taicheetah.mydictionary.dto.form.FilterForm;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.Entry;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.LexicalEntry;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.Pronunciation;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.ResponseFromOxfordDictionariesAPI;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.Result;
import com.taicheetah.mydictionary.dto.oxfordAPI.response.Sense;
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
@Validated
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
	public String listWordsGet(@RequestParam("pageNumber")int pageNumber, @RequestParam("sortAttribute") @Pattern(regexp = "user_WordId.wordName|dateTime") String sortAttribute, 
							@RequestParam("descending")boolean descending, @AuthenticationPrincipal User loginUser, Model theModel) {
		
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
	public String listWordsFilter(@ModelAttribute("filter") @Valid FilterForm theFilter, @AuthenticationPrincipal User loginUser, Model theModel) {
		
		session.setAttribute(SESSION_KEY_FILTER_FORM, theFilter);
		
		// initialise page setting
		Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by(defaultSortAttribute).descending());
		session.setAttribute(SESSION_KEY_PAGEABLE, pageable);
		
		return listWords(loginUser, theModel);
	}
	
	@PostMapping("/update")
	public String listWordsUpdate(@ModelAttribute("user")User theUser, @AuthenticationPrincipal User loginUser, Model theModel) {
		List<User_Word> dbUser_Words = new ArrayList<>();
		
		for(User_Word tempUser_Word: theUser.getUser_Words()) {
			User_Word dbUser_Word = user_wordService.findById(tempUser_Word.getUser_WordId());
			// set rememberFlg from input data
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
		
		// if the user didn't filter by date
		if(StringUtils.isEmpty(theFilter.getFilterDate())) {
			// invoke JPA query
			if(theFilter.getRememberFlg() != null) {
				return user_wordService.findAllWordsByUserIdAndRememberFlgAndWordNameContains(theUser.getUserId(), theFilter.getRememberFlg(), theFilter.getFilterWord(), pageable);
			} else {
				// if the user didn't filter by rememberFlg
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
			// if the user didn't filter by rememberFlg
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
		
		// if the word already exists in DB
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
		
		// if return code is 404
		if(response == null) {
			theModel.addAttribute("errorMessage","There is not '" + wordName + "' in this dictionray.");
			return "word/search-word";
		}
		
		Word theWord = new Word();
		List<Definition> theDefinitions = new ArrayList<>();
		
		// it is used to prevent repeating same category
		List<String> lexicalCategoryList= new ArrayList<>();
			
		// set the wordName in theWord
		theWord.setWordName(response.getWord());
		
		for(Result tempResult : response.getResults()) {
			
			// set the pronunciation in theWord
			// retrieve just one pronunciation because all pronunciations would be same
			if(theWord.getPhoneticSpelling() == null) {
				List<LexicalEntry> lexicalEntries = tempResult.getLexicalEntries();
				for(LexicalEntry tempLexicalEntry : lexicalEntries) {
					if(tempLexicalEntry.getEntries() == null) continue;
					for(Entry tempEntry : tempLexicalEntry.getEntries()) {
						if(tempEntry.getPronunciations() == null) continue;
						for(Pronunciation tempPronunciation : tempEntry.getPronunciations()) {
							theWord.setPhoneticSpelling(tempPronunciation.getPhoneticSpelling());
							theWord.setAudio_url(tempPronunciation.getAudioFile());
							break;
						}
						if(theWord.getPhoneticSpelling() != null) break;
					}
					if(theWord.getPhoneticSpelling() != null) break;
				}
			}
			
			// retrieve the definition by lexical category
			for(LexicalEntry tempLexicalEntry: tempResult.getLexicalEntries()) {
				
				if(tempLexicalEntry.getLexicalCategory() == null ||
						CollectionUtils.isEmpty(tempLexicalEntry.getEntries()) ||
						CollectionUtils.isEmpty(tempLexicalEntry.getEntries().get(0).getSenses()) ||
						CollectionUtils.isEmpty(tempLexicalEntry.getEntries().get(0).getSenses().get(0).getDefinitions())) {
					continue;
				}
				
				String theLexicalCategory = tempLexicalEntry.getLexicalCategory().getText();
				
				// in the case of coming same category again
				if(lexicalCategoryList.contains(theLexicalCategory)) {
					continue;
				}
				
				lexicalCategoryList.add(theLexicalCategory);
		
				DefinitionId theDefinitionId = new DefinitionId(theLexicalCategory);
				
				Sense tempSense = tempLexicalEntry.getEntries().get(0).getSenses().get(0);
				
				// retrieve only first definition
				String theDefinition = tempSense.getDefinitions().get(0);
				
				String theExample = null;
				// if examples exist
				if(CollectionUtils.isNotEmpty(tempSense.getExamples())) {
					// retrieve only first example
					theExample = tempSense.getExamples().get(0).getText();
				}
				theDefinitions.add(new Definition(theDefinitionId,theDefinition,theExample));
			}
		}
		
		// if any definition doesn't exist
		if(theDefinitions.isEmpty()) {
			theModel.addAttribute("errorMessage","There is not '" + wordName + "' in this dictionray.");
			return "word/search-word";
		}
		
		// set the definitions in theWord
		theWord.setDefinitions(theDefinitions);
		
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
		
		// if the user does not register the word yet
		if(user_wordService.findById(new User_WordId(theUser.getUserId(), dbWord.getWordName())) == null) {
			
			User_Word theUser_Word = new User_Word();
			theUser_Word.setUser_WordId(new User_WordId(theUser.getUserId()));
			theUser_Word.setWord(dbWord);
			theUser_Word.setDateTime(new Date());
			theUser_Word.setRememberFlg(false);
			
			user_wordService.save(theUser_Word);
			
			theModel.addAttribute("registerMessage", "registered");
		} else {
			// if the user already registered the word
			theModel.addAttribute("registerMessage", "'"+dbWord.getWordName()+"' is allready registered in your dictionary");
		}

		
		theModel.addAttribute("word", theWord);

		return "word/search-word";
	}

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView onValidationError(Exception ex) {
		ModelAndView mav = new ModelAndView();
    	mav.setStatus(HttpStatus.BAD_REQUEST);
		mav.addObject("code", HttpStatus.BAD_REQUEST.value());
		mav.addObject("message", "Bad request.");
    	mav.setViewName("error");
        return mav;
    }
}
