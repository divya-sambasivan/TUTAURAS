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
/**
 * Provides datastore access to the SUBJECT Kind
 * @author divya_000
 *
 */
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
	
	/**
	 * Get all subjects in the SUBJECT kind. Caches results for 5 minutes.
	 * @return a list of all subjects
	 */
	public ArrayList<Subject> getAllSubjects(boolean cached){
		Query q = new Query(SUBJECT_KIND);
		PreparedQuery pq = datastore.prepare(q);
		ArrayList<Subject> subjects;
		String CACHE_KEY = "ALL_SUBJECTS";
		subjects =  (ArrayList<Subject>) syncCache.get(CACHE_KEY);
		System.out.println(cached);
		if(subjects == null || cached==false){
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
	
	/**
	 * Returns subject specified; identified by the subject code, it was created with
	 * @param subjectCode
	 * @return specified subject
	 * @throws EntityNotFoundException
	 */
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
	
	/**
	 * Updates the specified subject with fields from the subject object
	 * @param subject the updated subject object
	 */
	public void updateSubject(Subject subject){
		/*TODO: check if subject with code exists */
		Entity subjectEntity = new Entity(SUBJECT_KIND, subject.getCode());
		subjectEntity.setProperty("name", subject.getName());
		subjectEntity.setProperty("description", subject.getDescription());
		subjectEntity.setProperty("imageUrl", subject.getImageUrl());
		datastore.put(subjectEntity);
	}
	
	/**
	 * Creates a new subject
	 * @param subject new subject to be created
	 */
	public void addSubject(Subject subject){
		/*TODO: check if subject with code already exists */
		Entity subjectEntity = new Entity(SUBJECT_KIND, subject.getCode());
		subjectEntity.setProperty("name", subject.getName());
		subjectEntity.setProperty("description", subject.getDescription());
		subjectEntity.setProperty("imageUrl", subject.getImageUrl());
		datastore.put(subjectEntity);
	}
	
	/**
	 * Deletes specified subject, if it exists, identified by the subject code it was created with;
	 * does nothing otherwise
	 * @param subjectCode
	 */
	public void deleteSubject(String subjectCode){
		datastore.delete(KeyFactory.createKey(SUBJECT_KIND, subjectCode));
	}
}
