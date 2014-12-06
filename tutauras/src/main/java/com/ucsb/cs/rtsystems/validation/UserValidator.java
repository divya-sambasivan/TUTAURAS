package com.ucsb.cs.rtsystems.validation;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ucsb.cs.rtsystems.model.User;

public class UserValidator {
	
	User user;
	ArrayList<String> errors;
	private Pattern emailPattern;
	private Pattern phonePattern;
	private Matcher emailMatcher;
	private Matcher phoneMatcher;
 
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String PHONE_PATTERN =
			"^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
 
	
	public UserValidator(User user){
		this.user = user;
		emailPattern = Pattern.compile(EMAIL_PATTERN);
		phonePattern = Pattern.compile(PHONE_PATTERN);
	}
	
	public ArrayList<String> validate(){
		this.errors = new ArrayList<String>();
		
		String email = user.getEmail();
		//email Id cannot be null
		if (email == null || email.equals("")){
			errors.add("Email cannot be empty");
		}
		
		//validate email
		else{
			emailMatcher = emailPattern.matcher(email);
			if(!(email == null || email.equals("")) && !emailMatcher.matches()){
				errors.add("Invalid email address");
			}
		}
		
		//validate phone
		String phoneNumber = user.getPhoneNumber();
		if(phoneNumber != null){
			phoneMatcher = phonePattern.matcher(user.getPhoneNumber());
			if(!phoneMatcher.matches()){
				errors.add("Invalid phone number");
			}
		}
		
		return this.errors;
	}

}
