package com.taicheetah.mydictionary.dto.form;

public class FilterForm {

	private String filterWord;
	
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
