package com.ucsb.cs.rtsystems.contoller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class SubjectUploadProxy extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		String subjectUploadUrl = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/subjectUpload");
		HttpClient httpclient = HttpClients.createDefault();
		HttpEntity entity = MultipartEntityBuilder.create().addTextBody("field1", "value1").addBinaryBody("subjectImage", new File("images/student.jpg"), ContentType.create("application/octet-stream"), "student.jpg").build();
		HttpPost httpPost = new HttpPost(subjectUploadUrl);
		httpPost.setEntity(entity);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity result = response.getEntity();
		System.out.println(result);
	}

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
    	System.out.println("proxy post called");
    	String subjectUploadUrl = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/subjectUpload");
		HttpClient httpclient = HttpClients.createDefault();
		
		/*Handle Multipart content */
		if(ServletFileUpload.isMultipartContent(req)) {
	        // Handle the multi-part data
	        //FileItemFactory factory = new DiskFileItemFactory();
	        ServletFileUpload upload = new ServletFileUpload();
	        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
	       
	        
	        try {
	        	FileItemIterator iterator = upload.getItemIterator(req);
	        	
	        	while(iterator.hasNext()){
	        		FileItemStream item = iterator.next();
	        		InputStream stream = item.openStream();
	        		if(item.isFormField()) {
	                    // Parse non-file field
	                    String name = item.getFieldName();
	                    ByteArrayOutputStream fileBytesStream = new ByteArrayOutputStream();
	                    int len;
	                    byte[] buffer = new byte[8192];
	                    while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
	                    	fileBytesStream.write(buffer, 0, len);
	                    }
	                    String val = fileBytesStream.toString();
	                    multipartEntityBuilder.addTextBody(name, val);
	                } else {
	                    // Parse file-field
	                    String fieldName = item.getFieldName();
	                    System.out.println(fieldName);
	                    String fileName = item.getName();
	                    ByteArrayOutputStream fileBytesStream = new ByteArrayOutputStream();
	                    int len;
	                    byte[] buffer = new byte[8192];
	                    while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
	                    	fileBytesStream.write(buffer, 0, len);
	                    }
	                    byte[] data = fileBytesStream.toByteArray();
	                    System.out.println("file size "+ data.length);
	                    multipartEntityBuilder.addBinaryBody(fieldName, data, ContentType.create("application/octet-stream"), fileName);
	                }
	        	}
	            HttpEntity entity = multipartEntityBuilder.build();
	            HttpPost httpPost = new HttpPost(subjectUploadUrl);
	    		httpPost.setEntity(entity);
	    		res = (HttpServletResponse) httpclient.execute(httpPost);
	        } catch(Exception e) {
	        	System.out.println(e);
	        	System.out.println("Exception Parsing Form");
	            return;
	        }
	    }
    }

}
