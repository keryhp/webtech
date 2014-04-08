package uk.bris.esserver.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Users;
import uk.bris.esserver.security.ESSecurity;
import uk.bris.esserver.service.UserService;

@WebServlet("loginuser")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userSrv = new UserService();
		String id = request.getParameter(EntityNames.ID);
		Users user = userSrv.getUserById(id);
		request.setAttribute("user", user);
		LOGGER.info("Login GET Complete");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Postprocess request: gather and validate submitted data and display result in same JSP.
		LOGGER.info("Login POST Begin");

		// Prepare messages.
		Map<String, String> messages = new HashMap<String, String>();
		request.setAttribute("messages", messages);

		// use city for password
		String pass = request.getParameter(EntityNames.LPASS);
		if (pass == null || pass.trim().isEmpty()) {
			messages.put(EntityNames.LPASS, "Please enter password");
		} else if (!pass.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})")) {
			messages.put(EntityNames.LPASS, "please check password policy!");
		}
		// Get and validate country.
		String lemail = request.getParameter(EntityNames.LEMAIL);
		if (lemail == null || lemail.trim().isEmpty() || lemail.length() > 255) {
			messages.put("l" + EntityNames.LEMAIL, "Please enter Email, max 255 characters");
		}

		UserService  userSrv = new UserService();
		Users usr = userSrv.getUserByEmail(lemail);
		if (messages.isEmpty() && usr != null) {
			ESSecurity esSecurity = new ESSecurity();
			if(esSecurity.validatePassword(usr, pass)){
				// No validation errors? Do the business job!
				request.setAttribute("messages", "log in successful!");
				request.setAttribute("loggedin", usr.getId());
	    		request.setAttribute("useremail", usr.getEmail()); 
	    	    HttpSession session = request.getSession();
	    	    session.setAttribute("userObj", usr);
	            //setting session to expiry in 30 mins
	            session.setMaxInactiveInterval(30*60);
	            Cookie userid = new Cookie("user", new Integer(usr.getId()).toString());
	            userid.setMaxAge(30*60);
	            response.addCookie(userid);
	    		request.getRequestDispatcher("/index.jsp").forward(request, response);    		
			}
		}else{
			request.getSession().setAttribute("cmessage", "Invalid login! Please retry");		
    		request.getRequestDispatcher("/index.jsp").forward(request, response);    		
		}

		LOGGER.info("Login POST Complete");
	}
}