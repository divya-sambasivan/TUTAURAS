package com.ucsb.cs.rtsystems.contoller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ucsb.cs.rtsystems.dao.SubjectDao;
import com.ucsb.cs.rtsystems.model.Subject;

@Path("/subject")
public class SubjectResource {
	
	private SubjectDao subjectDao;
	
	public SubjectResource(){
		subjectDao = new SubjectDao();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subject> getSubjects(){
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		long beforeTimestamp = new Date().getTime();
		subjects = subjectDao.getAllSubjects();
		long afterTimestamp = new Date().getTime();
		System.out.println("Time taken to get all subjects "+ (afterTimestamp - beforeTimestamp));
		return subjects;
	}
	
	@GET
	@Path("{subject_code}")
	@Produces(MediaType.APPLICATION_JSON)
	public Subject getSubject(
			@PathParam("subject_code") String subjectCode){
		Subject subject = subjectDao.getSubject(subjectCode);
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
		subjectDao.deleteSubject(subjectCode);
	}
}
