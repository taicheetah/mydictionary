package com.taicheetah.mydictionary.dto.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {
	
	@NotBlank(message="UserName cannot be blank.")
	@Size(max=20, message="UserName cannnot be greater than 20.")
	private String username;
	
	@NotBlank(message="Password cannot be blank.")
	@Size(min=6,max=12, message="Password length must be between 6 and 12.")
	@Pattern(regexp ="^[A-Za-z0-9]+$", message="Password must be letters or numbers.")
	private String password;
	
	@NotBlank(message="E-Mail cannot be blank.")
	@Pattern(regexp ="^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]+)+$", message="Please enter valid email address.")
	private String email;
	
	@NotBlank(message="TimeZone cannot be blank.")
	private String timeZone;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	
}
