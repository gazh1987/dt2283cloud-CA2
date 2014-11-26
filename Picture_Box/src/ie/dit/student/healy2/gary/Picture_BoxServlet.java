package ie.dit.student.healy2.gary;
	
import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class Picture_BoxServlet extends HttpServlet 
{
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
			resp.getWriter().println("<p>You are not logged in time </p>");
			resp.getWriter().println("<p> you can <a href=\"" + loginURL + "\"> sign in here</a>.</p>");
		}
		
		if (myPrincipal != null)
		{
			emailAddress = myPrincipal.getName(); 
			resp.getWriter().println("<p> You are logged in as (email): " +emailAddress + "</p>"); 
			resp.getWriter().println("<p> You can <a href=\"" + logoutURL + "\"> sign out</a>.</p>");
			resp.getWriter().println ("<p><a href=\"" + downloadsPage + "\"> Downloads</p>"); 
		}
	}
}

