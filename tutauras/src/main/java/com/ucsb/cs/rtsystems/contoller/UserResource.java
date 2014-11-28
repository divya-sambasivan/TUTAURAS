package com.ucsb.cs.rtsystems.contoller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ucsb.cs.rtsystems.dao.UserDao;
import com.ucsb.cs.rtsystems.model.User;

@Path("/user")
public class UserResource {
private UserDao userDao;
	
	public UserResource(){
		userDao = new UserDao();
	}
	
	
	@GET
	@Path("{user_code}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(
			@PathParam("user_code") String userCode){
		User user = userDao.getUser(userCode);
		return user;
	}
	
	@POST
	@Consumes("application/json")
	public void createUser(User user){
		userDao.addUser(user);
	}
	
	@PUT
	@Consumes("application/json")
	public void updateUser(User user){
		userDao.updateUser(user);
	}
	
	@DELETE
	@Path("{user_code}")
	public void deleteUser(@PathParam("user_code") String userCode){
		userDao.deleteUser(userCode);
	}
}
