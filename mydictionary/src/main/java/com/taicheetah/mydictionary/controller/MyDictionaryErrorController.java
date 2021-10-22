package com.taicheetah.mydictionary.controller;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class MyDictionaryErrorController implements ErrorController {

	@RequestMapping
	public ModelAndView error(HttpServletRequest req, ModelAndView mav) {
		
		String statusCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString();
		
		if("404".equals(statusCode)) {
			mav.setStatus(HttpStatus.NOT_FOUND);
			mav.addObject("code", statusCode);
			mav.addObject("message", "That page doen't exist or is unavailable.");
		} else if ("400".equals(statusCode)){
			mav.setStatus(HttpStatus.BAD_REQUEST);
			mav.addObject("code", statusCode);
			mav.addObject("message", "Bad request.");
		} else {
			mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			mav.addObject("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
			mav.addObject("message", "A System error occured. Please let our operation team know.");
		}
		mav.setViewName("error");
		
		return mav;
	}

}
