package com.ucsb.cs.rtsystems.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Subject {
	String code;
	String name;
	String description;

	public String getCode() {
		return code;
	}
	@XmlElement
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}

}
