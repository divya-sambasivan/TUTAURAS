package com.ucsb.cs.rtsystems.contoller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.ucsb.cs.rtsystems.dao.LectureDao;
import com.ucsb.cs.rtsystems.dao.SubjectDao;
import com.ucsb.cs.rtsystems.model.Lecture;
import com.ucsb.cs.rtsystems.model.Subject;

@Path("/subject")
public class SubjectResource {
	
	private SubjectDao subjectDao;
	private LectureDao lectureDao;
	private Queue queue;
	
	public SubjectResource(){
		subjectDao = new SubjectDao();
		lectureDao = new LectureDao();
		queue = QueueFactory.getDefaultQueue();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subject> getSubjects(
			@DefaultValue("true") @QueryParam("cached") boolean cached){
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		long beforeTimestamp = new Date().getTime();
		subjects = subjectDao.getAllSubjects(cached);
		long afterTimestamp = new Date().getTime();
		System.out.println("Time taken to get all subjects "+ (afterTimestamp - beforeTimestamp));
		return subjects;
	}
	
	@GET
	@Path("{subject_code}")
	@Produces(MediaType.APPLICATION_JSON)
	public Subject getSubject(
			@PathParam("subject_code") String subjectCode){
		Subject subject;
		try {
			subject = subjectDao.getSubject(subjectCode);
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(404);
		}
		return subject;
	}
	
	@POST
	@Consumes("application/json")
	public void createSubject(Subject subject){
		subjectDao.addSubject(subject);
	}
	
	@PUT
	@Consumes("application/json")
	public void updateSubject(Subject subject){
		subjectDao.updateSubject(subject);
	}
	
	@DELETE
	@Path("{subject_code}")
	public void deleteSubject(@PathParam("subject_code") String subjectCode){
		/* delete all child instances*/
		try {
			ArrayList<Lecture> childLectures = lectureDao.getAllLectures(subjectCode);
			for(Lecture childLecture : childLectures){
				TaskOptions to=TaskOptions.Builder.withUrl("/rest/lecture/"+subjectCode+"/"+childLecture.getID()).method(TaskOptions.Method.DELETE);
				queue.add(to);
			}
		} catch (EntityNotFoundException e) {
			// no children
		}
		subjectDao.deleteSubject(subjectCode);
	}
}
