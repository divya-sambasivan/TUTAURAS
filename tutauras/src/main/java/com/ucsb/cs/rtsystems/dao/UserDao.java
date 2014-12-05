package com.ucsb.cs.rtsystems.dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.ucsb.cs.rtsystems.model.User;

public class UserDao {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();;
	private static String USER_KIND = "USER";
	
	public UserDao(){
	}
	
	public boolean isUser(long ID){
		try {
			this.getUser(ID);
			return true;
		} catch (EntityNotFoundException e) {
			return false;
		}
	}
	
	public User getUser(long ID) throws EntityNotFoundException{
		User user = null;
		Entity userEntity = datastore.get(KeyFactory.createKey(USER_KIND, ID));
		if(userEntity!=null){
			user = new User();
			user.setID((long)userEntity.getKey().getId());
			user.setFirstName((String)userEntity.getProperty("firstName"));
			user.setLastName((String)userEntity.getProperty("lastName"));
			user.setEmail((String)userEntity.getProperty("email"));
			user.setPictureUrl((String)userEntity.getProperty("pictureUrl"));
			user.setPhoneNumber((String)userEntity.getProperty("phoneNumber"));
		}
		return user;
	}
	
	public void addUser(User user){
		Entity userEntity = new Entity(USER_KIND);
		userEntity.setProperty("firstName", user.getFirstName());
		userEntity.setProperty("lastName", user.getLastName());
		userEntity.setProperty("email", user.getEmail());
		userEntity.setProperty("pictureUrl", user.getPictureUrl());
		userEntity.setProperty("phoneNumber", user.getPhoneNumber());
		datastore.put(userEntity);
	}
	
	public void updateUser(User user) throws EntityNotFoundException{
		Entity userEntity = datastore.get(KeyFactory.createKey(USER_KIND, user.getID()));
		userEntity.setProperty("firstName", user.getFirstName());
		userEntity.setProperty("lastName", user.getLastName());
		userEntity.setProperty("email", user.getEmail());
		userEntity.setProperty("pictureUrl", user.getPictureUrl());
		userEntity.setProperty("phoneNumber", user.getPhoneNumber());
		datastore.put(userEntity);
	}
	
	public void deleteUser(long ID){
		datastore.delete(KeyFactory.createKey(USER_KIND, ID));
	}

}
