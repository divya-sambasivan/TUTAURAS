package com.ucsb.cs.rtsystems.dao;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.ucsb.cs.rtsystems.model.Subject;

public class SubjectDao {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	public static String SUBJECT_KIND = "SUBJECT";
	
	public SubjectDao(){
	}

	public ArrayList<Subject> getAllSubjects(){
		Query q = new Query(SUBJECT_KIND);
		PreparedQuery pq = datastore.prepare(q);
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		
		for (Entity subjectEntity : pq.asIterable()) {
			Subject subject = new Subject();
			subject.setCode((String)subjectEntity.getKey().getName());
			subject.setName((String)subjectEntity.getProperty("name"));
			subject.setDescription((String)subjectEntity.getProperty("description"));
			subjects.add(subject);
		}
		return subjects;
	}
	
	public Subject getSubject(String subjectCode){
		Subject subject = null;
		try {
			Entity subjectEntity = datastore.get(KeyFactory.createKey(SUBJECT_KIND, subjectCode));
			if(subjectEntity!=null){
				subject = new Subject();
				subject.setCode((String)subjectEntity.getKey().getName());
				subject.setName((String)subjectEntity.getProperty("name"));
				subject.setDescription((String)subjectEntity.getProperty("description"));
			}
			return subject;
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateSubject(Subject subject){
		/*TODO: check if subject with code exists */
		Entity subjectEntity = new Entity(SUBJECT_KIND, subject.getCode());
		subjectEntity.setProperty("name", subject.getName());
		subjectEntity.setProperty("description", subject.getDescription());
		datastore.put(subjectEntity);
	}
	
	public void addSubject(Subject subject){
		/*TODO: check if subject with code already exists */
		Entity subjectEntity = new Entity(SUBJECT_KIND, subject.getCode());
		subjectEntity.setProperty("name", subject.getName());
		subjectEntity.setProperty("description", subject.getDescription());
		datastore.put(subjectEntity);
	}
	
	public void deleteSubject(String subjectCode){
		/*TODO: check if subject with code exists */
		datastore.delete(KeyFactory.createKey(SUBJECT_KIND, subjectCode));
	}
}
