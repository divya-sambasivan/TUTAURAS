package com.ucsb.cs.rtsystems.dao;

import javax.ws.rs.PathParam;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.ucsb.cs.rtsystems.model.Lecture;

public class LectureDao {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();;
	public static String LECTURE_KIND = "LECTURE";
	SubjectDao subjectDao;
	
	public LectureDao(){
		subjectDao = new SubjectDao();
	}
	
	public Lecture getLecture(String subjectCode, long lectureId) throws EntityNotFoundException{
		Lecture lecture = null;
		
		Entity lectureEntity = datastore.get(KeyFactory.createKey(KeyFactory.createKey(subjectDao.SUBJECT_KIND, subjectCode),LECTURE_KIND, lectureId));
		if(lectureEntity!=null){
			lecture = new Lecture();
			lecture.setID((long)lectureEntity.getKey().getId());
			lecture.setStartTimeHour(((Long)lectureEntity.getProperty("startTimeHour")).intValue());
			lecture.setStartTimeMinute(((Long)lectureEntity.getProperty("startTimeMinute")).intValue());
			lecture.setEndTimeHour(((Long)lectureEntity.getProperty("endTimeHour")).intValue());
			lecture.setEndTimeMinute(((Long)lectureEntity.getProperty("endTimeMinute")).intValue());
			lecture.setLat((Double)lectureEntity.getProperty("latitude"));
			lecture.setLon((Double)lectureEntity.getProperty("longitude"));
			lecture.setSubjectCode(lectureEntity.getParent().getName());
		}
		return lecture;
	}
	
	public void addLecture(Lecture lecture){
		Entity lectureEntity = new Entity(LECTURE_KIND, KeyFactory.createKey(SubjectDao.SUBJECT_KIND, lecture.getSubjectCode()));
		lectureEntity.setProperty("startTimeHour", lecture.getStartTimeHour());
		lectureEntity.setProperty("startTimeMinute", lecture.getStartTimeMinute());
		lectureEntity.setProperty("endTimeHour", lecture.getEndTimeHour());
		lectureEntity.setProperty("endTimeMinute", lecture.getEndTimeMinute());
		lectureEntity.setProperty("latitude", lecture.getLat());
		lectureEntity.setProperty("longitude", lecture.getLon());
		lectureEntity.setProperty("tutor", lecture.getTutor());
		datastore.put(lectureEntity);
	}
	
	public void updateLecture(Lecture lecture) throws EntityNotFoundException{
		long lectureId = lecture.getID();
		String subjectCode = lecture.getSubjectCode();
		Entity lectureEntity = datastore.get(KeyFactory.createKey(KeyFactory.createKey(subjectDao.SUBJECT_KIND, subjectCode),LECTURE_KIND, lectureId));
		lectureEntity.setProperty("startTimeHour", lecture.getStartTimeHour());
		lectureEntity.setProperty("startTimeMinute", lecture.getStartTimeMinute());
		lectureEntity.setProperty("endTimeHour", lecture.getEndTimeHour());
		lectureEntity.setProperty("endTimeMinute", lecture.getEndTimeMinute());
		lectureEntity.setProperty("latitude", lecture.getLat());
		lectureEntity.setProperty("longitude", lecture.getLon());
		lectureEntity.setProperty("tutor", lecture.getTutor());
		datastore.put(lectureEntity);
	}
	
	public void deleteLecture(long lectureId, String subjectCode){
		/*TODO: check if lecture with code exists */
		datastore.delete(KeyFactory.createKey(KeyFactory.createKey(subjectDao.SUBJECT_KIND, subjectCode),LECTURE_KIND, lectureId));
	}
	
}
