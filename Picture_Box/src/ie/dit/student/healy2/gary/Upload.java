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

public class Upload extends HttpServlet
{
/**
* 
*/
	private static final long serialVersionUID = 1L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		@SuppressWarnings("deprecation")
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("myFile");
		
		//Check to see if the request contains an entry with the label myFile...
		if (blobKey == null) 
		{
			//...If it does not, redirect to the root of the application
			res.sendRedirect("/");
		}
		else
		{
			//If it does, redirect to the serve servlet
			//System.out.println("Uploaded a file with blobKey: "+blobKey.getKeyString());
			//res.getWriter().println("This is the file: "+ blobKey.getKeyString());
			res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());
		}
	}
}