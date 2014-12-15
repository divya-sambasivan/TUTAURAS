package com.ucsb.cs.rtsystems.validation;

import java.util.ArrayList;

import com.ucsb.cs.rtsystems.model.Lecture;

public class LectureValidator {
	Lecture lecture;
	String subjectCode;
	ArrayList<String> errors;
	
	public LectureValidator(Lecture lecture, String subjectCode){
		this.lecture = lecture;
		this.subjectCode = subjectCode;
	}
	
	public ArrayList<String> validate(){
		this.errors = new ArrayList<String>();
		
		// day of Week should be between 0 and 6
		int dayOfWeek = lecture.getDayOfWeek();
		if (dayOfWeek < 0 || dayOfWeek > 6){
			errors.add("Invalid value for dayOfWeek. Should be 0-6");
		}
		int startTimeHour = lecture.getStartTimeHour();
		if (startTimeHour < 0 || startTimeHour > 23){
			errors.add("Invalid value for startTimeHour. Should be 0-23");
		}
		int startTimeMinute = lecture.getStartTimeMinute();
		if (startTimeMinute < 0 || startTimeMinute > 59){
			errors.add("Invalid value for startTimeMinute. Should be 0-59");
		}
		int endTimeHour = lecture.getEndTimeHour();
		if (endTimeHour < 0 || endTimeHour > 23){
			errors.add("Invalid value for endTimeHour. Should be 0-23");
		}
		int endTimeMinute = lecture.getEndTimeMinute();
		if (endTimeMinute < 0 || endTimeMinute > 59){
			errors.add("Invalid value for endTimeMinute. Should be 0-59");
		}
		//end time should be after start time
		if((startTimeHour*100+startTimeMinute) > (endTimeHour*100+endTimeMinute)){
			errors.add("The lecture should start after it begins");
		}
		
		if (!lecture.getSubjectCode().equals(subjectCode)){
			errors.add("Lecture is under the incorrect subjectCode");
		}
		
		return errors;
	}

}
