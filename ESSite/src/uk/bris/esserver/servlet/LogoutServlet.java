package uk.bris.esserver.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("Logout GET Complete");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Postprocess request: gather and validate submitted data and display result in same JSP.
		LOGGER.info("Logout POST Begin");
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("JSESSIONID")){
					LOGGER.info("JSESSIONID="+cookie.getValue());
					break;
				}
			}
		}
		//invalidate the session if exists
		HttpSession session = request.getSession(false);
		LOGGER.info("Logout User="+session.getAttribute("userObj"));
		if(session != null){
			session.invalidate();
		}
		request.getRequestDispatcher("/index.jsp").forward(request, response);    		
		LOGGER.info("Logout POST Complete");
	}
}