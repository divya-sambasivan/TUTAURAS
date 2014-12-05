package com.ucsb.cs.rtsystems.validation;

import java.util.ArrayList;

import com.ucsb.cs.rtsystems.dao.UserDao;
import com.ucsb.cs.rtsystems.model.LectureInstance;

public class LectureInstanceValidator {
	UserDao userDao;
	LectureInstance lectureInstance;
	long lectureId;
	ArrayList<String> errors;
	
	public LectureInstanceValidator(LectureInstance lectureInstance, long lectureId){
		this.lectureInstance = lectureInstance;
		this.lectureId = lectureId;
		this.userDao = new UserDao();
	}
	
	public ArrayList<String> validate(){
		this.errors = new ArrayList<String>();
		
		if(lectureInstance.getLectureId() != lectureId){
			errors.add("Lecture Instance is under the incorrect lecture");
		}
		
		if(!this.userDao.isUser(lectureInstance.getStudentId())){
			errors.add("Invalid Student Id");
		}
		
		return this.errors;
	}

}
