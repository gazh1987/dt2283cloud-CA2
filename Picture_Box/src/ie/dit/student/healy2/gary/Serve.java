package ie.dit.student.healy2.gary;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;


public class Serve extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	//Create a blobstore service (for serving data)
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	// file Serve.java
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException
	{
		//Blob key is passed to the download handler
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
		
		//Serve the blob key to the user
		blobstoreService.serve(blobKey, res); 
	}
}
