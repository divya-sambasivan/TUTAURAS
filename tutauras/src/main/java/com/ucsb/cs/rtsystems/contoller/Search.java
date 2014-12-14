package com.ucsb.cs.rtsystems.contoller;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.ucsb.cs.rtsystems.dao.LectureDao;
import com.ucsb.cs.rtsystems.dao.LectureInstanceDao;
import com.ucsb.cs.rtsystems.dao.UserDao;
import com.ucsb.cs.rtsystems.model.Lecture;
import com.ucsb.cs.rtsystems.model.LectureInstance;
import com.ucsb.cs.rtsystems.model.LectureInstanceResult;
import com.ucsb.cs.rtsystems.model.LectureResult;
import com.ucsb.cs.rtsystems.model.User;

@Path("/search")
public class Search {
	private LectureInstanceDao lectureInstanceDao;
	private UserDao userDao;
	private LectureDao lectureDao;
	
	public Search(){
		this.lectureInstanceDao = new LectureInstanceDao();
		this.lectureDao = new LectureDao();
		this.userDao = new UserDao();
	}
		
	@GET
	@Path("lectureInstance")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<LectureInstanceResult> getLectureInstances(
			@QueryParam("email") String email) throws WebApplicationException{
		LectureInstanceResult lectureInstanceResult;
		ArrayList<LectureInstance> lectureInstances;
		ArrayList<LectureInstanceResult> lectureInstanceResults = new ArrayList<LectureInstanceResult>();
		try{
			User student = userDao.getUserByEmail(email);
			System.out.println(student);
			lectureInstances = lectureInstanceDao.getLectureInstancesByStudent(student.getID());
			System.out.println(lectureInstances);
			for(LectureInstance lectureInstance: lectureInstances){
				lectureInstanceResult = new LectureInstanceResult();
				lectureInstanceResult.setLectureInstance(lectureInstance);
				Lecture lecture = lectureDao.getLecture(lectureInstance.getSubjectCode(), lectureInstance.getLectureId());
				lectureInstanceResult.setLecture(lecture);
				User tutor = userDao.getUser(lecture.getTutor());
				lectureInstanceResult.setTutor(tutor);
				lectureInstanceResults.add(lectureInstanceResult);
			}
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
		return lectureInstanceResults;
	}
	
	@GET
	@Path("lecture")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<LectureResult> getLectures(
			@QueryParam("subject_code") String subjectCode) throws WebApplicationException{
		LectureResult lectureResult;
		ArrayList<Lecture> lectures;
		ArrayList<LectureResult> lectureResults = new ArrayList<LectureResult>();
		User tutor;
		try{
			lectures = lectureDao.getAllLectures(subjectCode);
			for(Lecture lecture:lectures){
				lectureResult = new LectureResult();
				tutor = userDao.getUser(lecture.getTutor());
				lectureResult.setLecture(lecture);
				lectureResult.setTutor(tutor);
				lectureResults.add(lectureResult);
			}
			
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
		return lectureResults;
	}

}
