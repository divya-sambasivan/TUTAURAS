package com.ucsb.cs.rtsystems.exception;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class BadRequestException extends WebApplicationException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadRequestException(List<String> errors){
		super(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON)
                .entity(new GenericEntity<List<String>>(errors)
                {}).build());
	}

}
