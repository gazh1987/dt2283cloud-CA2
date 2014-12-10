//Student Name: Gary Healy
//Student Number: C12726809

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

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class Picture_BoxServlet extends HttpServlet 
{
	//Declare variables
	private String key;
	private BlobKey bkey; 
	private String imageStatus;
	private String bk;
	private String identity;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException 
	{
		resp.setContentType("text/plain");
		 
		//Create instance of user service 
		UserService userService = UserServiceFactory.getUserService();
		
		//Create instance of Principal
		Principal myPrincipal = req.getUserPrincipal();
		
		//Initialise variables
		String emailAddress = null;  
		String downloadsPage = "/download.jsp"; 
		String thisURL = req.getRequestURI(); 
		String loginURL = userService.createLoginURL(thisURL);
		String logoutURL = userService.createLogoutURL(thisURL);
		resp.setContentType("text/html"); 
		
		//Create a list to store metadata on blobs
		List<BlobInfo> blobList = new LinkedList<BlobInfo>();
		
		//Create an iterator that fetches metadata on blobs from the blobstore
		Iterator<BlobInfo> iterator = new BlobInfoFactory().queryBlobInfos();
		
		//Create an instance of the datastore service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		
		//While the iterator has another element
		while(iterator.hasNext())
		{
			//Add the blob info from the iretator to the bloblist
			blobList.add(iterator.next());
		}
		
		//Add some CSS to the page
		resp.getWriter().println("<head><style>"
				+ "body {background-color: linen;}"
				+ "</style></head>");
		
		//If no one is logged in, e.g. the user is a Guest
		if(myPrincipal == null)
		{
			//Display Welcome message and link to login page (HTML)
			resp.getWriter().println("<h1>Welcome Guest</h1>");
			resp.getWriter().println("<b><p> You can <a href=\"" + loginURL + "\"> sign in here</a>.</p></b><hr>");
			
			//Open a table (HTML)
			resp.getWriter().println("<table>");

			//Loop through each element in the bloblist
			for (int i = 0; i < blobList.size(); i ++)
			{	
				//Get the blobkey at element i in the bloblist, make it a string
				key = blobList.get(i).getBlobKey().toString();
				
				//Extract the eimportant part of the string
				key = key.substring(10, key.length() - 1);
				
				//Get the blobkey at element i in the bloblist,
				bkey = blobList.get(i).getBlobKey();
				
				//Query the datastore to extract all information 
				Query q  = new Query("BlobKey");
				PreparedQuery pq = datastore.prepare(q);
				
				//Loop through each entity froim the datastore
				for (Entity e : pq.asIterable())
				{
					//extract the blobkey from the current entity
					bk = (String) e.getProperty("blobKey");
					
					//compare the blobkey to the key from the bloblist we are currently processing
					if (bk.equals(key))
					{
						//if they match store the imageStatus and idenetity properties from
						//the datastore into variables declared at the start of the class
						imageStatus = (String) e.getProperty("imageStatus");
						identity = (String)e.getProperty("user");
					}
				}
				
				//Create an instance of images service 
				ImagesService imagesService = ImagesServiceFactory.getImagesService();
				
				//Store the URL of the current blob in a string
				@SuppressWarnings("deprecation") 
				String imageUrl = imagesService.getServingUrl(bkey);
		
				//Only display the image and image details of images that are public.
				if (imageStatus.equals("public"))
				{
					resp.getWriter().println("<tr><td><img src=" + imageUrl + " height=\"300\" width=\"300\">" 
							+ "<br><b>FileName: </b>" + blobList.get(i).getFilename() 
							+ "<br><b>Size: </b>" + blobList.get(i).getSize() 
							+ "<br><b>Upload Date: </b>" + blobList.get(i).getCreation() 
							+ "<br><b>Image Status: </b>" + imageStatus
							+ "<br><b>Email of Uploader: </b>" + identity
							+ "<br><input onClick=\"location.href='serve?blob-key="+key+"'\" type=\"button\" value=\"View Larger Image\" /></td></tr>");
				}
			}
			
			//Close the table tag (HTML)
			resp.getWriter().println("</table>");
		}
		
		//If there is a user logged in
		if (myPrincipal != null)
		{
			//Get the email address of the user
			emailAddress = myPrincipal.getName();
			
			//Set the email of the global user, we use this again in the upload class to upload the
			//email of the current logged in user to the datastore along when he/she uploads a blob
			User.email = emailAddress;
			
			//If the logged in user is an admin
			if (emailAddress.equals("gazh1987gae@gmail.com") || emailAddress.equals("mark.deegan@dit.ie"))
			{
				//Welcome messages and links to logout etc
				resp.getWriter().println("<h1>Welcome Administrator</h1>");
				resp.getWriter().println("<b><p>You are logged in as (email): " +emailAddress + "</p>"); 
				resp.getWriter().println("<p>You can <a href=\"" + logoutURL + "\"> sign out here</a>.</p>");
				resp.getWriter().println ("<p><a href=\"" + downloadsPage + "\"> Upload Image </a></p></b>"); 
				resp.getWriter().println("<i><p>If you cannot view your uploaded image, try refreshing the page.</p></i><hr>");
				
				resp.getWriter().println("<table>");

				for (int i = 0; i < blobList.size(); i ++)
				{	
					key = blobList.get(i).getBlobKey().toString();
					key = key.substring(10, key.length() - 1);
					bkey = blobList.get(i).getBlobKey();
					
					Query q  = new Query("BlobKey");
					PreparedQuery pq = datastore.prepare(q);
					for (Entity e : pq.asIterable())
					{
						bk = (String) e.getProperty("blobKey");
						
						if (bk.equals(key))
						{
							imageStatus = (String) e.getProperty("imageStatus");
							identity = (String)e.getProperty("user");
						}
					}
					
					ImagesService imagesService = ImagesServiceFactory.getImagesService();
					@SuppressWarnings("deprecation")
					String imageUrl = imagesService.getServingUrl(bkey);
			
					//Show all information on every blob in the system 
					resp.getWriter().println("<tr><td><img src=" + imageUrl + " height=\"300\" width=\"300\">" 
							+ "<br><b>FileName: </b>" + blobList.get(i).getFilename() 
							+ "<br><b>Size: </b>" + blobList.get(i).getSize() 
							+ "<br><b>Image Status: </b>" + imageStatus
							+ "<br><b>Email of Uploader: </b>" + identity
							+ "<br><b>Upload Date: </b>" + blobList.get(i).getCreation() 
							+ "<br><input onClick=\"location.href='serve?blob-key="+key+"'\" type=\"button\" value=\"View Larger Image\" />"
							+ "<br><input onClick=\"location.href='delete?blob-key="+key+"'\" type=\"button\" value=\"Delete Image\" /></td></tr>");
					
				}
				
				resp.getWriter().println("</table>");
			}
			
			//If the logged in user is a member
			else
			{
				//Welcome messages and logout links etc
				resp.getWriter().println("<h1>Welcome Member</h1>");
				resp.getWriter().println("<b><p> You are logged in as (email): " +emailAddress + "</p>"); 
				resp.getWriter().println("<p> You can <a href=\"" + logoutURL + "\"> sign out here</a>.</p>");
				resp.getWriter().println("<p><a href=\"" + downloadsPage + "\"> Upload Image </a></p></b>");
				resp.getWriter().println("<i><p> If you cannot view your uploaded image, try refreshing the page.</p></i><hr>");
				
				resp.getWriter().println("<table>");

				for (int i = 0; i < blobList.size(); i ++)
				{	
					key = blobList.get(i).getBlobKey().toString();
					key = key.substring(10, key.length() - 1);
					bkey = blobList.get(i).getBlobKey();
					
					Query q  = new Query("BlobKey");
					PreparedQuery pq = datastore.prepare(q);
					for (Entity e : pq.asIterable())
					{
						bk = (String) e.getProperty("blobKey");
						
						if (bk.equals(key))
						{
							imageStatus = (String) e.getProperty("imageStatus");
							identity = (String) e.getProperty("user");
						}
					}
					
					ImagesService imagesService = ImagesServiceFactory.getImagesService();
					@SuppressWarnings("deprecation")
					String imageUrl = imagesService.getServingUrl(bkey);
					
					//if the identity of the miage is the same as the email address of the current logged in user
					//display a button within the image info that allows the user to delete the image he/she uploaded
					if (identity.equals(emailAddress))
					{
						resp.getWriter().println("<tr><td><img src=" + imageUrl + " height=\"300\" width=\"300\">" 
								+ "<br><b>FileName: </b>" + blobList.get(i).getFilename() 
								+ "<br><b>Size: </b>" + blobList.get(i).getSize() 
								+ "<br><b>Image Status: </b>" + imageStatus
								+ "<br><b>Email of Uploader: </b>" + identity
								+ "<br><b>Upload Date: </b>" + blobList.get(i).getCreation() 
								+ "<br><input onClick=\"location.href='serve?blob-key="+key+"'\" type=\"button\" value=\"View Larger Image\" />"
								+ "<br><input onClick=\"location.href='delete?blob-key="+key+"'\" type=\"button\" value=\"Delete Image\" /></td></tr>");
					}
					//or else just display all info about the image without the delete button.
					else
					{
						resp.getWriter().println("<tr><td><img src=" + imageUrl + " height=\"300\" width=\"300\">" 
								+ "<br><b>FileName: </b>" + blobList.get(i).getFilename() 
								+ "<br><b>Size: </b>" + blobList.get(i).getSize() 
								+ "<br><b>Image Status: </b>" + imageStatus
								+ "<br><b>Email of Uploader: </b>" + identity
								+ "<br><b>Upload Date: </b>" + blobList.get(i).getCreation() 
								+ "<br><input onClick=\"location.href='serve?blob-key="+key+"'\" type=\"button\" value=\"View Larger Image\" /></td></tr>");
					}
				}
				
				resp.getWriter().println("</table>");
			}
			
			resp.getWriter().println("</table>");
		}
	}
}

