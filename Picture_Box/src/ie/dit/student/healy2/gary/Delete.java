package ie.dit.student.healy2.gary;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;


public class Delete extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	//Create an instance blobstore service 
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException
	{
		//Blob key is passed to the download handler
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
		
		//delete the blob from the blobstore
		blobstoreService.delete(blobKey); 
		
		//redirect to the main page
		res.sendRedirect("/picture_box");
	}
}
