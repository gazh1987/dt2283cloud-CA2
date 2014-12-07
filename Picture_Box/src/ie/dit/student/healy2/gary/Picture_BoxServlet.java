package ie.dit.student.healy2.gary;
	
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

import java.io.IOException;
import java.security.Principal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class Picture_BoxServlet extends HttpServlet 
{
	private String key;
	private BlobKey bkey;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException 
	{
		resp.setContentType("text/plain");
		 
		//Create an implementation of the user service
		UserService userService = UserServiceFactory.getUserService(); 
		
		//Get the name of the current aunthenticated user
		Principal myPrincipal = req.getUserPrincipal();
		
		//Variable for email address initialised to null
		String emailAddress = null; 
		
		//Variable to store the downloads page 
		String downloadsPage = "/download.jsp";
		
		//Get the part of the URL used by the client to call the servlet on the server 
		String thisURL = req.getRequestURI(); 
		
		//Get the login URL
		String loginURL = userService.createLoginURL(thisURL);
		
		//Get the logout URL
		String logoutURL = userService.createLogoutURL(thisURL);
		
		//Set the content type
		resp.setContentType("text/html"); 
		
		//If there is no authenticated user
		if(myPrincipal == null)
		{
			//Print appropriate messages
			resp.getWriter().println("<p>You are not logged in time </p>");
			
			//Link to login
			resp.getWriter().println("<p> you can <a href=\"" + loginURL + "\"> sign in here</a>.</p>");
		}
		
		//If there is an authenticated user logged in 
		if (myPrincipal != null)
		{
			//Get the email address of the current user
			emailAddress = myPrincipal.getName(); 
			
			//print the email
			resp.getWriter().println("<p> You are logged in as (email): " +emailAddress + "</p>"); 
			
			//Link to logout
			resp.getWriter().println("<p> You can <a href=\"" + logoutURL + "\"> sign out</a>.</p>");
			
			//Link to downloads
			resp.getWriter().println ("<p><a href=\"" + downloadsPage + "\"> Upload Image </a></p>"); 
		}
		
		List<BlobInfo> blobList = new LinkedList<BlobInfo>();
		Iterator<BlobInfo> iterator = new BlobInfoFactory().queryBlobInfos();
		
		while(iterator.hasNext())
		{
			blobList.add(iterator.next());
		}
		
		//resp.getWriter().println("<table> <tr> <td> Name </td> <td> Filetype </td> <td> Size </td></tr>");
		resp.getWriter().println("<table>");
		
		//Use this code to delete all blobs
		/*BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		for(BlobInfo blobInfo : blobList)
			blobstoreService.delete(blobInfo.getBlobKey());*/

		for (int i = 1; i < blobList.size(); i ++)
		{
			key = blobList.get(i).getBlobKey().toString();
			bkey = blobList.get(i).getBlobKey();
			key = key.substring(10, key.length() - 1);
			
			ImagesService imagesService = ImagesServiceFactory.getImagesService();
			String imageUrl = imagesService.getServingUrl(bkey);
			
			resp.getWriter().println("<tr><td><img src=\"imageUrl\" height=\"300\" width=\"300\">" 
			+ "<br>FileName: " + blobList.get(i).getFilename() 
			+ "<br>Content Type: " + blobList.get(i).getContentType() 
			+ "<br>Size: " + blobList.get(i).getSize() 
			+ "<br><input onClick=\"location.href='serve?blob-key="+key+"'\" type=\"button\" value=\"Download\" /><br><br></td></tr>");
		}
		
		resp.getWriter().println("</table>");
	}
}

