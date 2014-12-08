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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class Picture_BoxServlet extends HttpServlet 
{
	private String key;
	private BlobKey bkey;
	private Key k;
	private Entity imgStatus; 
	
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
		
		List<BlobInfo> blobList = new LinkedList<BlobInfo>();
		Iterator<BlobInfo> iterator = new BlobInfoFactory().queryBlobInfos();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		while(iterator.hasNext())
		{
			blobList.add(iterator.next());
		}
		
		//Not logged in, Guest
		if(myPrincipal == null)
		{
			resp.getWriter().println("<p>Welcome Guest</p>");
			resp.getWriter().println("<p> You can <a href=\"" + loginURL + "\"> sign in here</a>.</p>");
			
			resp.getWriter().println("<table>");

			for (int i = 0; i < blobList.size(); i ++)
			{	
				key = blobList.get(i).getBlobKey().toString();
				key = key.substring(10, key.length() - 1);
				
				bkey = blobList.get(i).getBlobKey();

				//YOU ARE HERE
				k = KeyFactory.createKey("BlobKey", key);
				resp.getWriter().println(k.toString());
				
				String a = "";
				
				try 
				{
					imgStatus = datastore.get(k);
					a = (String) imgStatus.getProperty("imageStatus");
				} 
				catch (EntityNotFoundException e) 
				{
					resp.getWriter().println("IT BROKE");
					e.printStackTrace();
				}
				
				ImagesService imagesService = ImagesServiceFactory.getImagesService();
				@SuppressWarnings("deprecation")
				String imageUrl = imagesService.getServingUrl(bkey);
		
				resp.getWriter().println("<tr><td><img src=" + imageUrl + " height=\"300\" width=\"300\">" 
						+ "<br>FileName: " + blobList.get(i).getFilename() 
						+ "<br>Size: " + blobList.get(i).getSize() 
						+ "<br>Upload Date: " + blobList.get(i).getCreation() 
						+ "<br>Image Status: " + a
						+ "<br><input onClick=\"location.href='serve?blob-key="+key+"'\" type=\"button\" value=\"View Larger Image\" /></td></tr>");
				
			}
			
			resp.getWriter().println("</table>");
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
				resp.getWriter().println("<p> If you cannot view your uploaded image, try refreshing the page.</p>");
				
				resp.getWriter().println("<table>");

				for (int i = 0; i < blobList.size(); i ++)
				{	
					key = blobList.get(i).getBlobKey().toString();
					key = key.substring(10, key.length() - 1);
					bkey = blobList.get(i).getBlobKey();

					k = KeyFactory.createKey("BlobKey", key);
					resp.getWriter().println("k: " + k.toString());
					
					String a = "";
					try 
					{
						imgStatus = datastore.get(k);
						a = (String) imgStatus.getProperty("imageStatus");
					} 
					catch (EntityNotFoundException e) 
					{
						resp.getWriter().println("Entity not found");
						e.printStackTrace();
					}
					
					ImagesService imagesService = ImagesServiceFactory.getImagesService();
					@SuppressWarnings("deprecation")
					String imageUrl = imagesService.getServingUrl(bkey);
			
					resp.getWriter().println("<tr><td><img src=" + imageUrl + " height=\"300\" width=\"300\">" 
							+ "<br>FileName: " + blobList.get(i).getFilename() 
							+ "<br>Size: " + blobList.get(i).getSize() 
							+ "<br>Image Status: " + a
							+ "<br>Upload Date: " + blobList.get(i).getCreation() 
							+ "<br><input onClick=\"location.href='serve?blob-key="+key+"'\" type=\"button\" value=\"View Larger Image\" />"
							+ "<br><input onClick=\"location.href='delete?blob-key="+key+"'\" type=\"button\" value=\"Delete Image\" /></td></tr>");
					
				}
				
				resp.getWriter().println("</table>");
			}
			
			//Member
			else
			{
				resp.getWriter().println("<p>Welcome Member</p>");
				resp.getWriter().println("<p> You are logged in as (email): " +emailAddress + "</p>"); 
				resp.getWriter().println("<p> You can <a href=\"" + logoutURL + "\"> sign out here</a>.</p>");
				resp.getWriter().println("<p><a href=\"" + downloadsPage + "\"> Upload Image </a></p>");
				resp.getWriter().println("<p> If you cannot view your uploaded image, try refreshing the page.</p>");
				
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
							+ "<br>Size: " + blobList.get(i).getSize() 
							+ "<br>Upload Date:: " + blobList.get(i).getCreation() 
							+ "<br><input onClick=\"location.href='serve?blob-key="+key+"'\" type=\"button\" value=\"View Larger Image\" />"
							+ "<br><input onClick=\"location.href='delete?blob-key="+key+"'\" type=\"button\" value=\"Delete Image\" /></td></tr>");
					
				}
				
				resp.getWriter().println("</table>");
			}
			
			resp.getWriter().println("</table>");
		}
	}
}

