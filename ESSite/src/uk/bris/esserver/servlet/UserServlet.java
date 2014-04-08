package uk.bris.esserver.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Photos;
import uk.bris.esserver.repository.entities.Users;
import uk.bris.esserver.security.ESSecurity;
import uk.bris.esserver.service.PhotosService;
import uk.bris.esserver.service.UserService;

@WebServlet("userregister")
@MultipartConfig(maxFileSize = 16177215)
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(UserServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userSrv = new UserService();
		String id = request.getParameter(EntityNames.ID);
		Users user = userSrv.getUserById(id);
		request.setAttribute("user", user);
		LOGGER.info("User GET Complete");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Postprocess request: gather and validate submitted data and display result in same JSP.
		LOGGER.info("User POST Begin");

		// Prepare messages.
		/*        Map<String, String> messages = new HashMap<String, String>();
        request.setAttribute("messages", messages);
		 */
		String messages = "";
		// use city for password
		String rpass = request.getParameter(EntityNames.RPASS);
		if (rpass == null || rpass.trim().isEmpty()) {
			//messages.put(EntityNames.PASSWORD, "Please enter password");
			messages = messages + "Please enter password and retry";
		} else if (!rpass.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})")) {
			//messages.put(EntityNames.PASSWORD, "please check password policy!");
			messages = messages +" please check password policy and retry! example Testing2$";
		}

		// Get and validate username.
		String firstName = request.getParameter(EntityNames.FIRSTNAME);
		if (firstName == null || firstName.trim().isEmpty() || firstName.length() > 255) {
			//messages.put(EntityNames.FIRSTNAME, "Please enter FIRSTNAME, max 255 characters");
			messages = messages + " Please enter FIRSTNAME, max 255 characters";
		}

		// Get and validate postcode.
		String postcode = request.getParameter(EntityNames.POSTCODE);
		if (postcode == null || postcode.trim().isEmpty() || postcode.length() > 10) {
			//messages.put(EntityNames.POSTCODE, "Please enter PostCode, max 10 characters");
			messages = messages + " Please enter PostCode, max 10 characters";
		}

		// Get and validate remarks.
		String remarks = request.getParameter(EntityNames.REMARKS);
		/*if (remarks != null && remarks.length() > 255) {
            //messages.put(EntityNames.REMARKS, "Please enter Remarks, max 255 characters");
        }*/

		// Get and validate country.
		String remail = request.getParameter(EntityNames.REMAIL);
		if (remail == null || remail.trim().isEmpty() || remail.length() > 255) {
			//messages.put(EntityNames.EMAIL, "Please enter Email, max 255 characters");
			messages = messages + " Please enter Email, max 255 characters";
		}

		// Get and validate country.
		String contact = request.getParameter(EntityNames.CONTACT);
		if (contact == null || contact.trim().isEmpty() || contact.length() > 255) {
			//messages.put(EntityNames.CONTACT, "Please enter CONTACT, max 255 characters");
			messages = messages + " Please enter contact, max 255 characters";
		}

		Part image = request.getPart(EntityNames.FILETOUPLOAD);
		int imageId = 0;
		/*if (image == null) {
            //messages.put(EntityNames.FILETOUPLOAD, "Please select an image file");
        }*/

		UserService  userSrv = new UserService();
		Users usr = userSrv.getUserByEmail(remail);
		if (messages == "" && usr == null) {
			// No validation errors? Do the business job!
			Photos photo = new Photos();
			photo.setImage(IOUtils.toByteArray(image.getInputStream()));
			photo.setCreateDate(new Timestamp(System.currentTimeMillis()));
			photo.setCreatedBy(1);
			photo.setLastModified(new Timestamp(System.currentTimeMillis()));
			photo.setModifiedBy(1);
			photo.setRemarks(remarks);

			PhotosService photosSrv = new PhotosService();
			imageId = photosSrv.addPhoto(photo);

			ESSecurity esSecurity = new ESSecurity();
			// No validation errors? Do the business job!
			Users user = new Users();
			user.setFirstName(firstName);
			String salt = esSecurity.getSalt();
			user.setSalt(salt);
			user.setPostCode(postcode);
			user.setEmail(remail);
			user.setContact(contact);
			user.setRole("U");
			user.setPassword(esSecurity.getSecurePassword(rpass, salt));
			user.setCity("");
			user.setLastName("");
			user.setImageid(imageId);        
			user.setCreateDate(new Timestamp(System.currentTimeMillis()));
			user.setCreatedBy(1);
			user.setLastModified(new Timestamp(System.currentTimeMillis()));
			user.setModifiedBy(1);
			user.setRemarks(remarks);

			userSrv.addUser(user);

			request.setAttribute("messages", "User added successfully!");
			request.setAttribute("loggedin", user.getId()); 
			request.setAttribute("useremail", user.getEmail()); 
			usr = user;
			usr.setPassword("");
			usr.setSalt("");
			HttpSession session = request.getSession();
			session.setAttribute("userObj", usr);
			session.setAttribute("cmessage", "Your account created successfully!");
			//setting session to expiry in 30 mins
			session.setMaxInactiveInterval(30*60);
			Cookie userid = new Cookie("user", new Integer(usr.getId()).toString());
			userid.setMaxAge(30*60);
			response.addCookie(userid);
			request.getRequestDispatcher("/index.jsp").forward(request, response);    		

		}else{
			if(usr != null){
				messages = "email id already in use";
			}
			request.getSession().setAttribute("cmessage", "User registration failed. " + messages);
			request.getRequestDispatcher("/index.jsp").forward(request, response);           
		}
		LOGGER.info("User POST Complete");
	}
}