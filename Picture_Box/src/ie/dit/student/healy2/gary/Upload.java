package ie.dit.student.healy2.gary;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class Upload extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	//Create a new blobstore service
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{	
		//CReate a map that keeps stores all uploaded blobs blobkeys
		@SuppressWarnings("deprecation")
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		
		//Return the blobkey of the specified blob
		BlobKey blobKey = blobs.get("myFile");
		
		//Retun this blopb as a string
		String key = blobKey.getKeyString();
		
		//Get the content info from the form 
	    String content = req.getParameter("content");
	    
	    //Get the email address of the current user
	    String email = User.email;
	    
	    //Create a new Entity
	    Entity blob = new Entity("BlobKey");
	    
	    //Set the entity properties
	    blob.setProperty("blobKey", key);
	    blob.setProperty("imageStatus", content);
	    blob.setProperty("user", email);
	    
	    //Create a new instance of the datastore service
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    
	    //add the entity to the datastore service
	    datastore.put(blob);

	    //Redirect to the main page
		if (blobKey.equals(null)) 
		{
			res.sendRedirect("/picture_box");
		}
		else
		{
			blobs.keySet();
			res.sendRedirect("/picture_box");
		}
	}
}