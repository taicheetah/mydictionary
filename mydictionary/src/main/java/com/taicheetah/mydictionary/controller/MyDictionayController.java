package com.taicheetah.mydictionary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyDictionayController {

	@GetMapping("/")
	public String index() {
		return "redirect:/word/showSearchWordForm";
	}
}
