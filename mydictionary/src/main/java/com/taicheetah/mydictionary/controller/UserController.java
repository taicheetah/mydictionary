package com.taicheetah.mydictionary.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taicheetah.mydictionary.dto.form.UserForm;
import com.taicheetah.mydictionary.entity.User;
import com.taicheetah.mydictionary.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private UserService userService;
	
	@Autowired
	public UserController(UserService theUserService) {
		userService = theUserService;
	}
	
	@GetMapping("/showLoginPage")
	public String showLoginPage() {
		
		return "user/login";
	}
	
	@GetMapping("/showSaveUserForm")
	public String showSaveUserForm(Model theModel) {
		
		UserForm theUserForm = new UserForm();
		
		theModel.addAttribute("user", theUserForm);
		
		return "user/user-form";
		
	}
	
	@PostMapping("/registerUser")
	public String saveUser(@Validated @ModelAttribute("user") UserForm theUserForm, BindingResult result, Model theModel) {
		
		// if involving invalid parameters
		if(result.hasErrors()) {

			List<String> userNameErrorList = new ArrayList<String>();
			List<String> passwordErrorList = new ArrayList<String>();
			List<String> eMailErrorList = new ArrayList<String>();
			List<String> timezoneErrorList = new ArrayList<String>();
			
			// divide by input form
			for(ObjectError error: result.getAllErrors()) {
				
				if(error.getDefaultMessage().startsWith("UserName")) {
					userNameErrorList.add(error.getDefaultMessage());
				} else if(error.getDefaultMessage().startsWith("Password")) {
					passwordErrorList.add(error.getDefaultMessage());
				} else if(error.getDefaultMessage().startsWith("E-Mail")) {
					eMailErrorList.add(error.getDefaultMessage());
				} else if(error.getDefaultMessage().startsWith("TimeZone")) {
					timezoneErrorList.add(error.getDefaultMessage());
				}
			}
			
			theModel.addAttribute("userNameErrorList", userNameErrorList);
			theModel.addAttribute("passwordErrorList", passwordErrorList);
			theModel.addAttribute("eMailErrorList", eMailErrorList);
			theModel.addAttribute("timezoneErrorList", timezoneErrorList);
			
			return "user/user-form";
		}
		
		// in the case of the mail address is already exist in DB
		if(userService.findByEmail(theUserForm.getEmail()) != null){
			List<String> eMailErrorList = new ArrayList<String>();
			eMailErrorList.add("This Email address is already used.");
			theModel.addAttribute("eMailErrorList", eMailErrorList);
			return "user/user-form";
			
		} else {
			
			// encrypt the password
			BCryptPasswordEncoder bEncoder = new BCryptPasswordEncoder();
			String bcPassword = bEncoder.encode(theUserForm.getPassword());
			
			User theUser = new User(theUserForm.getUsername(),
									bcPassword,
									theUserForm.getEmail(),
									theUserForm.getTimeZone(),
									true);
			
			User dbUser = userService.save(theUser);
			theModel.addAttribute("user", dbUser);
			
			return "user/save-completed";
		}
	}
}
