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
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		@SuppressWarnings("deprecation")
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		
		BlobKey blobKey = blobs.get("myFile");
		String key = blobKey.getKeyString();
		
		//Add info to datastore about uploaded blob;
	    String content = req.getParameter("content");
	    
	    Entity blob = new Entity("BlobKey");
	    
		
	    blob.setProperty("blobKey", key);
	    blob.setProperty("imageStatus", content);
	    
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    datastore.put(blob);

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