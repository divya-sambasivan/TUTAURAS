package com.ucsb.cs.rtsystems.model;

import javax.xml.bind.annotation.XmlElement;

public class User {
	long ID;
	String firstName;
	String lastName;
	String email;
	String pictureUrl;
	String phoneNumber;
	
	
	public long getID() {
		return ID;
	}
	@XmlElement
	public void setID(long iD) {
		ID = iD;
	}
	public String getFirstName() {
		return firstName;
	}
	@XmlElement
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	@XmlElement
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	@XmlElement
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	@XmlElement
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	@XmlElement
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
