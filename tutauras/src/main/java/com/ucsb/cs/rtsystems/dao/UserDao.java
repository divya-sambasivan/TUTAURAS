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
	
	public User getUser(String ID){
		User user = null;
		try {
			Entity userEntity = datastore.get(KeyFactory.createKey(USER_KIND, ID));
			if(userEntity!=null){
				user = new User();
				user.setID((String)userEntity.getKey().getName());
				user.setFirstName((String)userEntity.getProperty("firstName"));
				user.setLastName((String)userEntity.getProperty("lastName"));
				user.setEmail((String)userEntity.getProperty("email"));
				user.setPictureUrl((String)userEntity.getProperty("pictureUrl"));
				user.setPhoneNumber((String)userEntity.getProperty("phoneNumber"));
			}
			return user;
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void addUser(User user){
		/*TODO: check if user with code already exists */
		Entity userEntity = new Entity(USER_KIND, user.getID());
		userEntity.setProperty("firstName", user.getFirstName());
		userEntity.setProperty("lastName", user.getLastName());
		userEntity.setProperty("email", user.getEmail());
		userEntity.setProperty("pictureUrl", user.getPictureUrl());
		userEntity.setProperty("phoneNumber", user.getPhoneNumber());
		datastore.put(userEntity);
	}
	
	public void updateUser(User user){
		/*TODO: check if user with code exists */
		Entity userEntity = new Entity(USER_KIND, user.getID());
		userEntity.setProperty("firstName", user.getFirstName());
		userEntity.setProperty("lastName", user.getLastName());
		userEntity.setProperty("email", user.getEmail());
		userEntity.setProperty("pictureUrl", user.getPictureUrl());
		userEntity.setProperty("phoneNumber", user.getPhoneNumber());
		datastore.put(userEntity);
	}
	
	public void deleteUser(String ID){
		/*TODO: check if subject with code exists */
		datastore.delete(KeyFactory.createKey(USER_KIND, ID));
	}

}
