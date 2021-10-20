package com.taicheetah.mydictionary.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ErrorController implements ErrorViewResolver {

	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
		
		ModelAndView mav = new ModelAndView();
		
		// HTTP 404 error
		if (status == HttpStatus.NOT_FOUND) {
			mav.addObject("code", status.value());
			mav.addObject("message", "That page doen't exist or is unavailable.");
		} else {
			mav.addObject("code", status.value());
			mav.addObject("message", status.name());
		} 
		mav.setViewName("error");
		
		return mav;
	}

}
