package com.taicheetah.mydictionary.dto.form;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
	
	public static Map<String, String> getAllZoneIdsAndItsOffSet() {

	        Map<String, String> result = new HashMap<>();

	        LocalDateTime localDateTime = LocalDateTime.now();

	        for (String zoneId : ZoneId.getAvailableZoneIds()) {

	            ZoneId id = ZoneId.of(zoneId);

	            // LocalDateTime -> ZonedDateTime
	            ZonedDateTime zonedDateTime = localDateTime.atZone(id);

	            // ZonedDateTime -> ZoneOffset
	            ZoneOffset zoneOffset = zonedDateTime.getOffset();

	            //replace Z to +00:00
	            String offset = zoneOffset.getId().replaceAll("Z", "+00:00");
	            offset = String.format("(UTC%s)", offset);

	            result.put(id.toString(), offset);

	        }
	        
	        // sort by value, descending order
	        Map<String, String> sortedMap = new LinkedHashMap<>();result.entrySet().stream()
	                .sorted(Map.Entry.<String, String>comparingByValue().reversed())
	                .forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));

	        return sortedMap;

	    }
}
