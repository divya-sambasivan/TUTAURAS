package com.ucsb.cs.rtsystems.dao;

import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.ucsb.cs.rtsystems.model.Lecture;
import com.ucsb.cs.rtsystems.model.LectureInstance;

/**
 * Provides datastore access to the "LECTURE" Kind
 * @author divya_000
 *
 */
public class LectureDao {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();;
	public static String LECTURE_KIND = "LECTURE";
	SubjectDao subjectDao;
	
	public LectureDao(){
		subjectDao = new SubjectDao();
	}
	
	/**
	 * Retrieves all the lectures created by a given tutor
	 * @param tutorId	user id of the tutor
	 * @return 	a list of lectures created by the tutor specified
	 * @throws EntityNotFoundException
	 */
	public ArrayList<Lecture> getLecturesByTutor(String tutorId) throws EntityNotFoundException{
		ArrayList<Lecture> lectures = new ArrayList<Lecture>();
		Lecture lecture;
		
		Filter tutorFilter =
				  new FilterPredicate("tutor",
				                      FilterOperator.EQUAL,
				                      tutorId);
		Query q = new Query(LECTURE_KIND).setFilter(tutorFilter);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity lectureEntity : pq.asIterable()) {
			lecture = new Lecture();
			lecture.setID((long)lectureEntity.getKey().getId());
			lecture.setSubjectCode(lectureEntity.getParent().getName());
			lecture.setStartTimeHour(((Long)lectureEntity.getProperty("startTimeHour")).intValue());
			lecture.setStartTimeMinute(((Long)lectureEntity.getProperty("startTimeMinute")).intValue());
			lecture.setEndTimeHour(((Long)lectureEntity.getProperty("endTimeHour")).intValue());
			lecture.setEndTimeMinute(((Long)lectureEntity.getProperty("endTimeMinute")).intValue());
			lecture.setDayOfWeek(((Long)lectureEntity.getProperty("dayOfWeek")).intValue());
			lecture.setLat((Double)lectureEntity.getProperty("latitude"));
			lecture.setLon((Double)lectureEntity.getProperty("longitude"));
			lecture.setTutor((String)lectureEntity.getProperty("tutor"));
			lecture.setSubjectCode(lectureEntity.getParent().getName());
			lectures.add(lecture);
		}
		return lectures;
	}
	/**
	 * Retrieves all lectures for a specified subject
	 * @param subjectCode
	 * @return	a list of lectures for the subject specified
	 * @throws EntityNotFoundException
	 */
	
	public ArrayList<Lecture> getAllLectures(String subjectCode) throws EntityNotFoundException{
		ArrayList<Lecture> lectures = new ArrayList<Lecture>();
		Lecture lecture;
		Query q = new Query(LECTURE_KIND).setAncestor(KeyFactory.createKey(subjectDao.SUBJECT_KIND, subjectCode));
		PreparedQuery pq = datastore.prepare(q);
		for (Entity lectureEntity : pq.asIterable()) {
			lecture = new Lecture();
			lecture.setID((long)lectureEntity.getKey().getId());
			lecture.setSubjectCode(lectureEntity.getParent().getName());
			lecture.setStartTimeHour(((Long)lectureEntity.getProperty("startTimeHour")).intValue());
			lecture.setStartTimeMinute(((Long)lectureEntity.getProperty("startTimeMinute")).intValue());
			lecture.setEndTimeHour(((Long)lectureEntity.getProperty("endTimeHour")).intValue());
			lecture.setEndTimeMinute(((Long)lectureEntity.getProperty("endTimeMinute")).intValue());
			lecture.setDayOfWeek(((Long)lectureEntity.getProperty("dayOfWeek")).intValue());
			lecture.setLat((Double)lectureEntity.getProperty("latitude"));
			lecture.setLon((Double)lectureEntity.getProperty("longitude"));
			lecture.setTutor((String)lectureEntity.getProperty("tutor"));
			lecture.setSubjectCode(lectureEntity.getParent().getName());
			lectures.add(lecture);
		}
		return lectures;
	}
	
	/**
	 * Gets the specified lecture
	 * @param subjectCode
	 * @param lectureId
	 * @return
	 * @throws EntityNotFoundException
	 */
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
			lecture.setDayOfWeek(((Long)lectureEntity.getProperty("dayOfWeek")).intValue());
			lecture.setLat((Double)lectureEntity.getProperty("latitude"));
			lecture.setLon((Double)lectureEntity.getProperty("longitude"));
			lecture.setTutor((String)lectureEntity.getProperty("tutor"));
			lecture.setSubjectCode(lectureEntity.getParent().getName());
		}
		return lecture;
	}
	
	/**
	 * Add a new lecture. Persists an object of type Lecture in Entity LECTURE
	 * @param lecture
	 */
	public void addLecture(Lecture lecture){
		Entity lectureEntity = new Entity(LECTURE_KIND, KeyFactory.createKey(SubjectDao.SUBJECT_KIND, lecture.getSubjectCode()));
		lectureEntity.setProperty("startTimeHour", lecture.getStartTimeHour());
		lectureEntity.setProperty("startTimeMinute", lecture.getStartTimeMinute());
		lectureEntity.setProperty("endTimeHour", lecture.getEndTimeHour());
		lectureEntity.setProperty("endTimeMinute", lecture.getEndTimeMinute());
		lectureEntity.setProperty("dayOfWeek", lecture.getDayOfWeek());
		lectureEntity.setProperty("latitude", lecture.getLat());
		lectureEntity.setProperty("longitude", lecture.getLon());
		lectureEntity.setProperty("tutor", lecture.getTutor());
		datastore.put(lectureEntity);
	}
	
	/**
	 * Updates an Entity of type LECTURE, identified and updated with field from the corresponding Lecture object
	 * @param lecture	the object to update along with the updated fields
	 * @throws EntityNotFoundException
	 */
	public void updateLecture(Lecture lecture) throws EntityNotFoundException{
		long lectureId = lecture.getID();
		String subjectCode = lecture.getSubjectCode();
		Entity lectureEntity = datastore.get(KeyFactory.createKey(KeyFactory.createKey(subjectDao.SUBJECT_KIND, subjectCode),LECTURE_KIND, lectureId));
		lectureEntity.setProperty("startTimeHour", lecture.getStartTimeHour());
		lectureEntity.setProperty("startTimeMinute", lecture.getStartTimeMinute());
		lectureEntity.setProperty("endTimeHour", lecture.getEndTimeHour());
		lectureEntity.setProperty("endTimeMinute", lecture.getEndTimeMinute());
		lectureEntity.setProperty("dayOfWeek", lecture.getDayOfWeek());
		lectureEntity.setProperty("latitude", lecture.getLat());
		lectureEntity.setProperty("longitude", lecture.getLon());
		lectureEntity.setProperty("tutor", lecture.getTutor());
		datastore.put(lectureEntity);
	}
	
	/**
	 * Deletes Entity if it exists; does nothing otherwise
	 * @param lectureId
	 * @param subjectCode
	 */
	public void deleteLecture(long lectureId, String subjectCode){
		datastore.delete(KeyFactory.createKey(KeyFactory.createKey(subjectDao.SUBJECT_KIND, subjectCode),LECTURE_KIND, lectureId));
	}
	
}
