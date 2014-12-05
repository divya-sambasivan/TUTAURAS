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
import com.ucsb.cs.rtsystems.dao.LectureInstanceDao;
import com.ucsb.cs.rtsystems.exception.BadRequestException;
import com.ucsb.cs.rtsystems.model.LectureInstance;
import com.ucsb.cs.rtsystems.validation.LectureInstanceValidator;
import com.ucsb.cs.rtsystems.validation.LectureValidator;

@Path("/lectureInstance/{lecture_id}")
public class LectureInstanceResource {
	private LectureInstanceDao lectureInstanceDao;
	
	public LectureInstanceResource(){
		lectureInstanceDao = new LectureInstanceDao();
	}
	
	@GET
	@Path("{lecture_instance_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public LectureInstance getLectureInstance(
			@PathParam("lecture_id") long lectureId,
			@PathParam("lecture_instance_id") long lectureInstanceId) throws WebApplicationException{
		LectureInstance lectureInstance = null;
		try{
			lectureInstance = lectureInstanceDao.getLectureInstance(lectureId, lectureInstanceId);
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
		return lectureInstance;
	}
	
	@POST
	@Consumes("application/json")
	public void createLectureInstance(
			@PathParam("lecture_id") long lectureId,
			LectureInstance lectureInstance) throws BadRequestException{
		LectureInstanceValidator lectureInstanceValidator = new LectureInstanceValidator(lectureInstance, lectureId);
		ArrayList<String> errors = lectureInstanceValidator.validate();
		if (!errors.isEmpty()){
			throw new BadRequestException(errors);
		}
		lectureInstanceDao.addLectureInstance(lectureInstance);
	}
	
	@PUT
	@Path("{lecture_instance_id}")
	@Consumes("application/json")
	public void updateLecture(
			@PathParam("lecture_id") long lectureId,
			LectureInstance lectureInstance) throws BadRequestException, WebApplicationException{
		LectureInstanceValidator lectureInstanceValidator = new LectureInstanceValidator(lectureInstance, lectureId);
		ArrayList<String> errors = lectureInstanceValidator.validate();
		if (!errors.isEmpty()){
			throw new BadRequestException(errors);
		}
		try{
			lectureInstanceDao.updateLectureInstance(lectureInstance);
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
	}
	
	@DELETE
	@Path("{lecture_id}")
	public void deleteLectureInstance(
			@PathParam("lecture_id") long lectureId,
			@PathParam("lecture_instance_id") long lectureInstanceId){
		lectureInstanceDao.deleteLectureInstance(lectureInstanceId, lectureId);
	}

}
