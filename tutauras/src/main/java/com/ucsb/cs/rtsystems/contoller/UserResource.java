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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.ucsb.cs.rtsystems.dao.UserDao;
import com.ucsb.cs.rtsystems.exception.BadRequestException;
import com.ucsb.cs.rtsystems.model.User;
import com.ucsb.cs.rtsystems.validation.UserValidator;

@Path("/user")
public class UserResource {
private UserDao userDao;
	
	public UserResource(){
		userDao = new UserDao();
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserByEmail(
			@QueryParam("email") String email) throws WebApplicationException{
		try{
			User user = userDao.getUserByEmail(email);
			return user;
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
	}
	
	@GET
	@Path("{user_code}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(
			@PathParam("user_code") String userCode) throws WebApplicationException{
		try{
			User user = userDao.getUser(userCode);
			return user;
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
	}
	
	@POST
	@Consumes("application/json")
	public void createUser(User user) throws BadRequestException{
		UserValidator userValidator = new UserValidator(user);
		ArrayList<String> errors = userValidator.validate();
		if (!errors.isEmpty()){
			throw new BadRequestException(errors);
		}
		userDao.addUser(user);
	}
	
	@PUT
	@Path("{user_code}")
	@Consumes("application/json")
	public void updateUser(User user) throws WebApplicationException{
		try{
			userDao.updateUser(user);
		}catch(EntityNotFoundException e){
			throw new WebApplicationException(404);
		}
	}
	
	@DELETE
	@Path("{user_code}")
	public void deleteUser(@PathParam("user_code") long userCode){
		userDao.deleteUser(userCode);
	}
	
}
