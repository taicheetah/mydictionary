package com.taicheetah.mydictionary.dto.form;

import javax.validation.constraints.Pattern;

public class FilterForm {

	private String filterWord;
	
	@Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$|^$")
	private String filterDate;
	
	private Boolean rememberFlg;
	
	public FilterForm() {
		
	}

	public FilterForm(String filterWord, String filterDate, Boolean rememberFlg) {
		this.filterWord = filterWord;
		this.filterDate = filterDate;
		this.rememberFlg = rememberFlg;
	}

	public String getFilterWord() {
		return filterWord;
	}

	public void setFilterWord(String filterWord) {
		this.filterWord = filterWord;
	}

	public String getFilterDate() {
		return filterDate;
	}

	public void setFilterDate(String filterDate) {
		this.filterDate = filterDate;
	}

	public Boolean getRememberFlg() {
		return rememberFlg;
	}

	public void setRememberFlg(Boolean rememberFlg) {
		this.rememberFlg = rememberFlg;
	}
	
	
}
