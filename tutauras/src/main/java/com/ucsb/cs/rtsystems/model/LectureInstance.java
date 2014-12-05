package com.ucsb.cs.rtsystems.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/* TODO: custom serializer for date */

@XmlRootElement
public class LectureInstance {
	long ID;
	long lectureId;
	long studentId;
	Date lectureDate;
	
	public Date getLectureDate() {
		return lectureDate;
	}
	public void setLectureDate(Date lectureDate) {
		this.lectureDate = lectureDate;
	}
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	
	public long getLectureId() {
		return lectureId;
	}
	public void setLectureId(long lectureId) {
		this.lectureId = lectureId;
	}
	public long getStudentId() {
		return studentId;
	}
	
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	

}
