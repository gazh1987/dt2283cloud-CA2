package ie.dit.student.healy2.gary;
	
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

import java.io.IOException;
import java.security.Principal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
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
		 
		UserService userService = UserServiceFactory.getUserService(); 
		Principal myPrincipal = req.getUserPrincipal();
		String emailAddress = null;  
		String downloadsPage = "/download.jsp"; 
		String thisURL = req.getRequestURI(); 
		String loginURL = userService.createLoginURL(thisURL);
		String logoutURL = userService.createLogoutURL(thisURL);
		resp.setContentType("text/html"); 
		
		if(myPrincipal == null)
		{
			resp.getWriter().println("<p>Welcome Guest</p>");
			resp.getWriter().println("<p> You can <a href=\"" + loginURL + "\"> sign in here</a>.</p>");
		}

		if (myPrincipal != null)
		{
			emailAddress = myPrincipal.getName();

			//Admin
			if (emailAddress.equals("gazh1987gae@gmail.com") || emailAddress.equals("mark.deegan@dit.ie"))
			{
				resp.getWriter().println("<p>Welcome Administrator</p>");
				resp.getWriter().println("<p> You are logged in as (email): " +emailAddress + "</p>"); 
				resp.getWriter().println("<p> You can <a href=\"" + logoutURL + "\"> sign out here</a>.</p>");
				resp.getWriter().println ("<p><a href=\"" + downloadsPage + "\"> Upload Image </a></p>"); 
			}
			
			//Member
			else if (emailAddress.equals("c12726809@mydit.ie") || emailAddress.equals("C12726809@mydit.ie") || 
					emailAddress.equals("gary.healy2@student.dit.ie"))
			{
				resp.getWriter().println("<p>Welcome Member</p>");
				resp.getWriter().println("<p> You are logged in as (email): " +emailAddress + "</p>"); 
				resp.getWriter().println("<p> You can <a href=\"" + logoutURL + "\"> sign out here</a>.</p>");
				resp.getWriter().println ("<p><a href=\"" + downloadsPage + "\"> Upload Image </a></p>");
			}
			
			//Guest 
			else
			{
				resp.getWriter().println("<p>Sorry you are not permitted to sign in with this email address</p>");
				resp.getWriter().println("<p> You are logged in as (email): " +emailAddress + "</p>");
				resp.getWriter().println("<p> You can try <a href=\"" + loginURL + "\"> sign in again here</a>.</p>");
			}
		}
		
		List<BlobInfo> blobList = new LinkedList<BlobInfo>();
		Iterator<BlobInfo> iterator = new BlobInfoFactory().queryBlobInfos();
		
		while(iterator.hasNext())
		{
			blobList.add(iterator.next());
		}
		
		resp.getWriter().println("<table>");

		for (int i = 0; i < blobList.size(); i ++)
		{	
			key = blobList.get(i).getBlobKey().toString();
			key = key.substring(10, key.length() - 1);
			
			bkey = blobList.get(i).getBlobKey();

			ImagesService imagesService = ImagesServiceFactory.getImagesService();
			@SuppressWarnings("deprecation")
			String imageUrl = imagesService.getServingUrl(bkey);
	
			resp.getWriter().println("<tr><td><img src=" + imageUrl + " height=\"300\" width=\"300\">" 
					+ "<br>FileName: " + blobList.get(i).getFilename() 
					+ "<br>Content Type: " + blobList.get(i).getContentType()
					+ "<br>Size: " + blobList.get(i).getSize() 
					+ "<br><input onClick=\"location.href='serve?blob-key="+key+"'\" type=\"button\" value=\"View Larger Image\" />"
					+ "<br><input onClick=\"location.href='delete?blob-key="+key+"'\" type=\"button\" value=\"Delete Image\" /></td></tr>");
			
		}
		
		resp.getWriter().println("</table>");
	}
}

