package com.ucsb.cs.rtsystems.dao;

import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.ucsb.cs.rtsystems.model.LectureInstance;

public class LectureInstanceDao {
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();;
	private static String LECTURE_INSTANCE_KIND = "LECTURE_INSTANCE";
	LectureDao lectureDao;
	
	public LectureInstanceDao(){
		lectureDao = new LectureDao();
	}
	
	public LectureInstance getLectureInstance(long lectureCode, long lectureInstanceId) throws EntityNotFoundException{
		LectureInstance lectureInstance = null;
		
		Entity lectureInstanceEntity = datastore.get(KeyFactory.createKey(KeyFactory.createKey(lectureDao.LECTURE_KIND, lectureCode),LECTURE_INSTANCE_KIND, lectureInstanceId));
		if(lectureInstanceEntity!=null){
			lectureInstance = new LectureInstance();
			lectureInstance.setID((long)lectureInstanceEntity.getKey().getId());
			lectureInstance.setLectureId(lectureInstanceEntity.getParent().getId());
			lectureInstance.setStudentId((String)lectureInstanceEntity.getProperty("studentId"));
			lectureInstance.setLectureDate((Date)lectureInstanceEntity.getProperty("lectureDate"));
		}
		return lectureInstance;
	}
	
	public void addLectureInstance(LectureInstance lectureInstance){
		Entity lectureEntity = new Entity(LECTURE_INSTANCE_KIND, KeyFactory.createKey(lectureDao.LECTURE_KIND, lectureInstance.getLectureId()));
		lectureEntity.setProperty("studentId", lectureInstance.getStudentId());
		lectureEntity.setProperty("lectureDate", lectureInstance.getLectureDate());
		datastore.put(lectureEntity);
	}
	
	public void updateLectureInstance(LectureInstance lectureInstance) throws EntityNotFoundException{
		long lectureInstanceId = lectureInstance.getID();
		Entity lectureEntity = datastore.get(KeyFactory.createKey(KeyFactory.createKey(lectureDao.LECTURE_KIND, lectureInstance.getLectureId()),LECTURE_INSTANCE_KIND, lectureInstanceId));
		lectureEntity.setProperty("studentId", lectureInstance.getStudentId());
		lectureEntity.setProperty("lectureDate", lectureInstance.getLectureDate());
		datastore.put(lectureEntity);
	}
	
	public void deleteLectureInstance(long lectureInstanceId, long lectureId){
		datastore.delete(KeyFactory.createKey(KeyFactory.createKey(lectureDao.LECTURE_KIND, lectureId),LECTURE_INSTANCE_KIND, lectureInstanceId));
	}
}
