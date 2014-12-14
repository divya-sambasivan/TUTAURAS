package com.ucsb.cs.rtsystems.model;

public class LectureResult {
	Lecture lecture;
	User tutor;
	
	public Lecture getLecture() {
		return lecture;
	}
	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}
	public User getTutor() {
		return tutor;
	}
	public void setTutor(User tutor) {
		this.tutor = tutor;
	}
}
