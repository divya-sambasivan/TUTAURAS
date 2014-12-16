package com.ucsb.cs.rtsystems.dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.ucsb.cs.rtsystems.model.User;

/**
 * Provides datastore access to the USER Kind
 * @author divya_000
 *
 */
public class UserDao {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();;
	private static String USER_KIND = "USER";
	
	public UserDao(){
	}
	
	/**
	 * Checks if user exists in the datastore
	 * @param ID user id
	 * @return true if user exists in the datastore; false otherwise
	 */
	public boolean isUser(String ID){
		try {
			this.getUser(ID);
			return true;
		} catch (EntityNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * Retrieves a user by the email id specified. If multiple entries exist, only the first one is returned.
	 * @param email
	 * @return User
	 * @throws EntityNotFoundException
	 */
	public User getUserByEmail(String email) throws EntityNotFoundException{
		User user = null;
		Filter emailFilter =
				  new FilterPredicate("email",
				                      FilterOperator.EQUAL,
				                      email);
		Query q = new Query(USER_KIND).setFilter(emailFilter);
		PreparedQuery pq = datastore.prepare(q);
		Entity userEntity = pq.asSingleEntity();
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
	}
	
	/**
	 * Retrieves the specified user; identified by user id
	 * @param ID
	 * @return User
	 * @throws EntityNotFoundException
	 */
	public User getUser(String ID) throws EntityNotFoundException{
		User user = null;
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
	}
	
	/**
	 * Creates a new USER Entity specified by {@user}. 
	 * @param user the new user to create
	 */
	public void addUser(User user){
		Entity userEntity = new Entity(USER_KIND, user.getID());
		userEntity.setProperty("firstName", user.getFirstName());
		userEntity.setProperty("lastName", user.getLastName());
		userEntity.setProperty("email", user.getEmail());
		userEntity.setProperty("pictureUrl", user.getPictureUrl());
		userEntity.setProperty("phoneNumber", user.getPhoneNumber());
		datastore.put(userEntity);
	}
	
	/**
	 * Updates the user specified with fields specified in {@user}
	 * @param user
	 * @throws EntityNotFoundException
	 */
	public void updateUser(User user) throws EntityNotFoundException{
		Entity userEntity = datastore.get(KeyFactory.createKey(USER_KIND, user.getID()));
		userEntity.setProperty("firstName", user.getFirstName());
		userEntity.setProperty("lastName", user.getLastName());
		userEntity.setProperty("email", user.getEmail());
		userEntity.setProperty("pictureUrl", user.getPictureUrl());
		userEntity.setProperty("phoneNumber", user.getPhoneNumber());
		datastore.put(userEntity);
	}
	
	/**
	 * Deletes user specified by {@ID} if found; does nothing otherwise.
	 * @param ID
	 */
	public void deleteUser(long ID){
		datastore.delete(KeyFactory.createKey(USER_KIND, ID));
	}

}
