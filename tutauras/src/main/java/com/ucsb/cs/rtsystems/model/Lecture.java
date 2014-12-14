package com.ucsb.cs.rtsystems.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Lecture {
	long ID;
	double lat;
	double lon;
	String subjectCode;
	String tutor;
	int dayOfWeek;
	int startTimeHour;
	int startTimeMinute;
	int endTimeHour;
	int endTimeMinute;
	
	
	public int getStartTimeHour() {
		return startTimeHour;
	}
	@XmlElement
	public void setStartTimeHour(int startTimeHour) {
		this.startTimeHour = startTimeHour;
	}
	public int getStartTimeMinute() {
		return startTimeMinute;
	}
	@XmlElement
	public void setStartTimeMinute(int startTimeMinute) {
		this.startTimeMinute = startTimeMinute;
	}
	public int getEndTimeHour() {
		return endTimeHour;
	}
	@XmlElement
	public void setEndTimeHour(int endTimeHour) {
		this.endTimeHour = endTimeHour;
	}
	public int getEndTimeMinute() {
		return endTimeMinute;
	}
	@XmlElement
	public void setEndTimeMinute(int endTimeMinute) {
		this.endTimeMinute = endTimeMinute;
	}
	
	public long getID() {
		return ID;
	}
	@XmlElement
	public void setID(long iD) {
		ID = iD;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	@XmlElement
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getTutor() {
		return tutor;
	}
	@XmlElement
	public void setTutor(String tutor) {
		this.tutor = tutor;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	@XmlElement
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public double getLat() {
		return lat;
	}
	@XmlElement
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	@XmlElement
	public void setLon(double lon) {
		this.lon = lon;
	}
}	
