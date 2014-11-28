package com.ucsb.cs.rtsystems.contoller;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.ucsb.cs.rtsystems.dao.LectureDao;
import com.ucsb.cs.rtsystems.exception.BadRequestException;
import com.ucsb.cs.rtsystems.model.Lecture;
import com.ucsb.cs.rtsystems.validation.LectureValidator;

@Path("/lecture/{subject_code}")
public class LectureResource {
	private LectureDao lectureDao;
	private String lectureId;
	
	public LectureResource(){
		lectureDao = new LectureDao();
	}
	
	@GET
	@Path("{lecture_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Lecture getLecture(
			@PathParam("subject_code") String subjectCode,
			@PathParam("lecture_id") long lectureId) throws WebApplicationException{
		Lecture lecture = null;
		try{
			lecture = lectureDao.getLecture(subjectCode, lectureId);
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
		return lecture;
	}
	
	@POST
	@Consumes("application/json")
	public void createLecture(
			@PathParam("subject_code") String subjectCode,
			Lecture lecture) throws BadRequestException{
		LectureValidator lectureValidator = new LectureValidator(lecture, subjectCode);
		ArrayList<String> errors = lectureValidator.validate();
		if (!errors.isEmpty()){
			throw new BadRequestException(errors);
		}
		lectureDao.addLecture(lecture);
	}
	
	@PUT
	@Path("{lecture_id}")
	@Consumes("application/json")
	public void updateLecture(
			@PathParam("subject_code") String subjectCode,
			Lecture lecture) throws WebApplicationException{
		LectureValidator lectureValidator = new LectureValidator(lecture, subjectCode);
		ArrayList<String> errors = lectureValidator.validate();
		if (!errors.isEmpty()){
			throw new BadRequestException(errors);
		}
		try{
			lectureDao.updateLecture(lecture);
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
	}
	
	@DELETE
	@Path("{lecture_id}")
	public void deleteLecture(
			@PathParam("subject_code") String subjectCode,
			@PathParam("lecture_id") long lectureId){
		lectureDao.deleteLecture(lectureId, subjectCode);
	}
	
}
