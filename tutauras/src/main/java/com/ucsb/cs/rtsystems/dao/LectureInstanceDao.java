package com.ucsb.cs.rtsystems.dao;

import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.search.SortExpression;
import com.ucsb.cs.rtsystems.model.LectureInstance;
/**
 * Provides datastore access to the "LECTURE_INSTANCE" Kind
 * @author divya_000
 *
 */
public class LectureInstanceDao {
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();;
	private static String LECTURE_INSTANCE_KIND = "LECTURE_INSTANCE";
	LectureDao lectureDao;
	
	public LectureInstanceDao(){
		lectureDao = new LectureDao();
	}
	
	/**
	 * Gets all lecture Instances of a user as a student
	 * @param studentId User ID of student
	 * @return list of LectureInstances for the student specified
	 */
	public ArrayList<LectureInstance> getLectureInstancesByStudent(String studentId){
		ArrayList<LectureInstance> lectureInstances = new ArrayList<LectureInstance>();
		LectureInstance lectureInstance = null;
		Filter studentFilter =
				  new FilterPredicate("studentId",
				                      FilterOperator.EQUAL,
				                      studentId);
		Filter upcomingFilter = 
				new FilterPredicate("lectureDate",
									FilterOperator.GREATER_THAN_OR_EQUAL,
									new Date()
						);
		Filter myUpcomingFilter = CompositeFilterOperator.and(studentFilter, upcomingFilter);
		Query q = new Query(LECTURE_INSTANCE_KIND).setFilter(myUpcomingFilter).addSort("lectureDate", Query.SortDirection.ASCENDING);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity lectureInstanceEntity : pq.asIterable()) {
			lectureInstance = new LectureInstance();
			lectureInstance.setID((long)lectureInstanceEntity.getKey().getId());
			lectureInstance.setLectureId(lectureInstanceEntity.getParent().getId());
			lectureInstance.setSubjectCode(lectureInstanceEntity.getParent().getParent().getName());
			lectureInstance.setStudentId((String)lectureInstanceEntity.getProperty("studentId"));
			lectureInstance.setLectureDate((Date)lectureInstanceEntity.getProperty("lectureDate"));
			lectureInstances.add(lectureInstance);
		}
		return lectureInstances;
	}
	
	/**
	 * Retrieves all lecture instances for a given lecture (specified by subjectCode, lectureId). Optionally, only 
	 * returns upcoming lecture instances, i.e. lecture instances starting from the current date.
	 * @param subjectCode
	 * @param lectureId
	 * @param onlyUpcoming if true, only returns the lecture instances with date after and including the current date
	 * @return
	 * @throws EntityNotFoundException
	 */
	public ArrayList<LectureInstance> getLectureInstances(String subjectCode, long lectureId, boolean onlyUpcoming) throws EntityNotFoundException{
		ArrayList<LectureInstance> lectureInstances = new ArrayList<LectureInstance>();
		LectureInstance lectureInstance = new LectureInstance();
		Query q = new Query(LECTURE_INSTANCE_KIND).setAncestor(new Builder(SubjectDao.SUBJECT_KIND, subjectCode).addChild(LectureDao.LECTURE_KIND, lectureId).getKey());
		if(onlyUpcoming == true){
			Filter upcomingFilter = 
					new FilterPredicate("lectureDate",
										FilterOperator.GREATER_THAN_OR_EQUAL,
										new Date()
							);
			q.setFilter(upcomingFilter);
		}
		PreparedQuery pq = datastore.prepare(q);
		for (Entity lectureInstanceEntity : pq.asIterable()) {
			lectureInstance = new LectureInstance();
			lectureInstance.setID((long)lectureInstanceEntity.getKey().getId());
			lectureInstance.setLectureId(lectureInstanceEntity.getParent().getId());
			lectureInstance.setSubjectCode(lectureInstanceEntity.getParent().getParent().getName());
			lectureInstance.setStudentId((String)lectureInstanceEntity.getProperty("studentId"));
			lectureInstance.setLectureDate((Date)lectureInstanceEntity.getProperty("lectureDate"));
			lectureInstances.add(lectureInstance);
		}
		return lectureInstances;
	}
	
	/**
	 * Retrieves the specified lecture instance, if present. A lecture instance is identified by the 3 tuple (subject code, lecture id,
	 * lecture instance).
	 * @param subjectCode
	 * @param lectureId
	 * @param lectureInstanceId
	 * @return
	 * @throws EntityNotFoundException
	 */
	public LectureInstance getLectureInstance(String subjectCode, long lectureId, long lectureInstanceId) throws EntityNotFoundException{
		LectureInstance lectureInstance = null;
		Key key = new Builder(SubjectDao.SUBJECT_KIND, subjectCode).addChild(LectureDao.LECTURE_KIND, lectureId).addChild(LECTURE_INSTANCE_KIND, lectureInstanceId).getKey();
		Entity lectureInstanceEntity = datastore.get(key);
		if(lectureInstanceEntity!=null){
			lectureInstance = new LectureInstance();
			lectureInstance.setID((long)lectureInstanceEntity.getKey().getId());
			lectureInstance.setLectureId(lectureInstanceEntity.getParent().getId());
			lectureInstance.setSubjectCode(lectureInstanceEntity.getParent().getParent().getName());
			lectureInstance.setStudentId((String)lectureInstanceEntity.getProperty("studentId"));
			lectureInstance.setLectureDate((Date)lectureInstanceEntity.getProperty("lectureDate"));
		}
		return lectureInstance;
	}
	
	/**
	 * Creates a new lecture instance specified
	 * @param lectureInstance
	 */
	public void addLectureInstance(LectureInstance lectureInstance){
		Key key = new Builder(SubjectDao.SUBJECT_KIND, lectureInstance.getSubjectCode()).addChild(LectureDao.LECTURE_KIND, lectureInstance.getLectureId()).getKey();
		System.out.println(new Builder(SubjectDao.SUBJECT_KIND, lectureInstance.getSubjectCode()).addChild(LectureDao.LECTURE_KIND, lectureInstance.getLectureId()));
		Entity lectureEntity = new Entity(LECTURE_INSTANCE_KIND, key);
		lectureEntity.setProperty("studentId", lectureInstance.getStudentId());
		lectureEntity.setProperty("lectureDate", lectureInstance.getLectureDate());
		datastore.put(lectureEntity);
	}
	
	/**
	 * Updates the specified lecture instance with the fields specified
	 * @param lectureInstance
	 * @throws EntityNotFoundException
	 */
	public void updateLectureInstance(LectureInstance lectureInstance) throws EntityNotFoundException{
		Key key = new Builder(SubjectDao.SUBJECT_KIND, lectureInstance.getSubjectCode()).addChild(LectureDao.LECTURE_KIND, lectureInstance.getLectureId()).addChild(LECTURE_INSTANCE_KIND, lectureInstance.getID()).getKey();
		Entity lectureEntity = datastore.get(key);
		lectureEntity.setProperty("studentId", lectureInstance.getStudentId());
		lectureEntity.setProperty("lectureDate", lectureInstance.getLectureDate());
		datastore.put(lectureEntity);
	}
	
	/**
	 * Deletes the specified lecture instance identified by the tuple (subject code, lecture id, lecture instance id) if it exists;
	 * does nothing otherwise
	 * @param subjectCode
	 * @param lectureId
	 * @param lectureInstanceId
	 */
	public void deleteLectureInstance(String subjectCode,  long lectureId, long lectureInstanceId){
		Key key = new Builder(SubjectDao.SUBJECT_KIND, subjectCode).addChild(LectureDao.LECTURE_KIND, lectureId).addChild(LECTURE_INSTANCE_KIND, lectureInstanceId).getKey();
		datastore.delete(key);
	}
}
