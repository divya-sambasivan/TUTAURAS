package com.ucsb.cs.rtsystems.dao;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.ucsb.cs.rtsystems.model.Subject;

public class SubjectDao {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private MemcacheService syncCache;
	private Expiration fiveMinutes;
	public static String SUBJECT_KIND = "SUBJECT";
	
	public SubjectDao(){
		syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		fiveMinutes = Expiration.byDeltaSeconds(300);
	}

	public ArrayList<Subject> getAllSubjects(){
		Query q = new Query(SUBJECT_KIND);
		PreparedQuery pq = datastore.prepare(q);
		ArrayList<Subject> subjects;
		String CACHE_KEY = "ALL_SUBJECTS";
		subjects =  (ArrayList<Subject>) syncCache.get(CACHE_KEY);
		
		if(subjects == null){
			subjects = new ArrayList<Subject>();
			for (Entity subjectEntity : pq.asIterable()) {
				Subject subject = new Subject();
				subject.setCode((String)subjectEntity.getKey().getName());
				subject.setName((String)subjectEntity.getProperty("name"));
				subject.setDescription((String)subjectEntity.getProperty("description"));
				subject.setImageUrl((String)subjectEntity.getProperty("imageUrl"));
				subjects.add(subject);
			}
			syncCache.put(CACHE_KEY, subjects, fiveMinutes);
		}
		
		return subjects;
	}
	
	public Subject getSubject(String subjectCode) throws EntityNotFoundException{
		Subject subject = null;
		Entity subjectEntity = datastore.get(KeyFactory.createKey(SUBJECT_KIND, subjectCode));
		if(subjectEntity!=null){
			subject = new Subject();
			subject.setCode((String)subjectEntity.getKey().getName());
			subject.setName((String)subjectEntity.getProperty("name"));
			subject.setDescription((String)subjectEntity.getProperty("description"));
			subject.setImageUrl((String)subjectEntity.getProperty("imageUrl"));
		}
		return subject;
	}
	
	public void updateSubject(Subject subject){
		/*TODO: check if subject with code exists */
		Entity subjectEntity = new Entity(SUBJECT_KIND, subject.getCode());
		subjectEntity.setProperty("name", subject.getName());
		subjectEntity.setProperty("description", subject.getDescription());
		subjectEntity.setProperty("imageUrl", subject.getImageUrl());
		datastore.put(subjectEntity);
	}
	
	public void addSubject(Subject subject){
		/*TODO: check if subject with code already exists */
		Entity subjectEntity = new Entity(SUBJECT_KIND, subject.getCode());
		subjectEntity.setProperty("name", subject.getName());
		subjectEntity.setProperty("description", subject.getDescription());
		subjectEntity.setProperty("imageUrl", subject.getImageUrl());
		datastore.put(subjectEntity);
	}
	
	public void deleteSubject(String subjectCode){
		/*TODO: check if subject with code exists */
		datastore.delete(KeyFactory.createKey(SUBJECT_KIND, subjectCode));
	}
}
