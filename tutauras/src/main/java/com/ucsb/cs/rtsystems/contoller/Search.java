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
import com.ucsb.cs.rtsystems.dao.SubjectDao;
import com.ucsb.cs.rtsystems.dao.UserDao;
import com.ucsb.cs.rtsystems.model.Lecture;
import com.ucsb.cs.rtsystems.model.LectureInstance;
import com.ucsb.cs.rtsystems.model.LectureInstanceResult;
import com.ucsb.cs.rtsystems.model.LectureResult;
import com.ucsb.cs.rtsystems.model.Subject;
import com.ucsb.cs.rtsystems.model.User;

@Path("/search")
public class Search {
	private LectureInstanceDao lectureInstanceDao;
	private UserDao userDao;
	private LectureDao lectureDao;
	private SubjectDao subjectDao;
	
	public Search(){
		this.lectureInstanceDao = new LectureInstanceDao();
		this.lectureDao = new LectureDao();
		this.userDao = new UserDao();
		this.subjectDao = new SubjectDao();
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
			lectureInstances = lectureInstanceDao.getLectureInstancesByStudent(student.getID());
			for(LectureInstance lectureInstance: lectureInstances){
				lectureInstanceResult = new LectureInstanceResult();
				lectureInstanceResult.setLectureInstance(lectureInstance);
				Lecture lecture;
				User tutor;
				try{
					lecture = lectureDao.getLecture(lectureInstance.getSubjectCode(), lectureInstance.getLectureId());
					tutor = userDao.getUser(lecture.getTutor());
				}catch(EntityNotFoundException e){
					lecture = null;
					tutor = null;
					continue;
				}
				lectureInstanceResult.setLecture(lecture);
				lectureInstanceResult.setTutor(tutor);
				lectureInstanceResults.add(lectureInstanceResult);
			}
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
		return lectureInstanceResults;
	}
	
	@GET
	@Path("lectureInstanceForTutor")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<LectureInstanceResult> getLectureInstancesForTutor(
			@QueryParam("tutorEmail") String tutorEmail) throws WebApplicationException{
		LectureInstanceResult lectureInstanceResult;
		ArrayList<LectureInstance> lectureInstances;
		ArrayList<LectureInstanceResult> lectureInstanceResults = new ArrayList<LectureInstanceResult>();
		try{
			//Get all the lectures for a tutor
			User tutor = userDao.getUserByEmail(tutorEmail);
			ArrayList<Lecture> tutorLectures = lectureDao.getLecturesByTutor(tutor.getID());
			for(Lecture tutorLecture: tutorLectures){
				try{
					lectureInstances = lectureInstanceDao.getLectureInstances(tutorLecture.getSubjectCode(), tutorLecture.getID(), true);
					for(LectureInstance lectureInstance: lectureInstances){
						lectureInstanceResult = new LectureInstanceResult();
						lectureInstanceResult.setLecture(tutorLecture);
						lectureInstanceResult.setLectureInstance(lectureInstance);
						try{
							User student = userDao.getUser(lectureInstance.getStudentId());
							lectureInstanceResult.setStudent(student);
						}catch(EntityNotFoundException e){
							continue;
						}
						lectureInstanceResults.add(lectureInstanceResult);
					}
				}catch(EntityNotFoundException e){
					continue;
				}
			}
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
		return lectureInstanceResults;
	}
	
	@GET
	@Path("lectureForTutor")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<LectureResult> getLecturesForTutor(
			@QueryParam("tutorEmail") String tutorEmail) throws WebApplicationException{
		ArrayList<LectureResult> lectureResults = new ArrayList<LectureResult>();
		ArrayList<Lecture> tutorLectures = null;
		LectureResult lectureResult;
		try{
			//Get all the lectures for a tutor
			User tutor = userDao.getUserByEmail(tutorEmail);
			tutorLectures = lectureDao.getLecturesByTutor(tutor.getID());
			for(Lecture tutorLecture: tutorLectures){
				lectureResult = new LectureResult();
				Subject subject;
				try{
					subject = subjectDao.getSubject(tutorLecture.getSubjectCode());
				}catch(EntityNotFoundException e){
					subject = null;
				}
				lectureResult.setLecture(tutorLecture);
				lectureResult.setSubject(subject);
				lectureResults.add(lectureResult);
			}
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
		return lectureResults;
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
