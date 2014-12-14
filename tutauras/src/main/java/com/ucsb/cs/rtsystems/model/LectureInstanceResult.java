package com.ucsb.cs.rtsystems.model;

public class LectureInstanceResult {
	
	LectureInstance lectureInstance;
	Lecture lecture;
	Subject subject;
	User tutor;
	
	public LectureInstance getLectureInstance() {
		return lectureInstance;
	}
	public void setLectureInstance(LectureInstance lectureInstance) {
		this.lectureInstance = lectureInstance;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
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
