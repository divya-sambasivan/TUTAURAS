package com.ucsb.cs.rtsystems.contoller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.ucsb.cs.rtsystems.dao.SubjectDao;
import com.ucsb.cs.rtsystems.model.Subject;


public class SubjectUpload extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
	}

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
    	System.out.println("called");
    	
    	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        
        String subjectCode = req.getParameter("subjectCode");
        String subjectName = req.getParameter("subjectName");
        String subjectDescription = req.getParameter("subjectDescription");
        BlobKey blobKey = blobs.get("subjectImage");
        ServingUrlOptions servingUrlOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        String subjectImage = imagesService.getServingUrl(servingUrlOptions);
        
        System.out.println(subjectImage);
        
        /*create a subject object*/
        Subject subject = new Subject();
        subject.setCode(subjectCode);
        subject.setName(subjectName);
        subject.setDescription(subjectDescription);
        subject.setImageUrl(subjectImage);
        
        SubjectDao subjectDao = new SubjectDao();
        subjectDao.addSubject(subject);
    }
}
