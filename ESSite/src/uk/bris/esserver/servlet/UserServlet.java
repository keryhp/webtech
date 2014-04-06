package uk.bris.esserver.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Photos;
import uk.bris.esserver.repository.entities.Users;
import uk.bris.esserver.service.PhotosService;
import uk.bris.esserver.service.UserService;

@WebServlet("user")
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
        Map<String, String> messages = new HashMap<String, String>();
        request.setAttribute("messages", messages);

        // Get and validate usercode.
        String city = request.getParameter(EntityNames.CITY);
        if (city == null || city.trim().isEmpty()) {
            messages.put(EntityNames.CITY, "Please select City");
        }

        // Get and validate username.
        String firstName = request.getParameter(EntityNames.FIRSTNAME);
        if (firstName == null || firstName.trim().isEmpty() || firstName.length() > 255) {
            messages.put(EntityNames.FIRSTNAME, "Please enter FIRSTNAME, max 255 characters");
        }

        // Get and validate username.
        String lastName = request.getParameter(EntityNames.LASTNAME);
        if (lastName == null || lastName.trim().isEmpty() || lastName.length() > 255) {
            messages.put(EntityNames.LASTNAME, "Please enter LASTNAME, max 255 characters");
        }

        // Get and validate postcode.
        String postcode = request.getParameter(EntityNames.POSTCODE);
        if (postcode == null || postcode.trim().isEmpty() || postcode.length() > 10) {
            messages.put(EntityNames.POSTCODE, "Please enter PostCode, max 10 characters");
        }
        
        // Get and validate remarks.
        String remarks = request.getParameter(EntityNames.REMARKS);
        if (remarks != null && remarks.length() > 255) {
            messages.put(EntityNames.REMARKS, "Please enter Remarks, max 255 characters");
        }

        // Get and validate country.
        String email = request.getParameter(EntityNames.EMAIL);
        if (email == null || email.trim().isEmpty() || email.length() > 255) {
            messages.put(EntityNames.EMAIL, "Please enter Email, max 255 characters");
        }

        // Get and validate country.
        String contact = request.getParameter(EntityNames.CONTACT);
        if (contact == null || contact.trim().isEmpty() || contact.length() > 255) {
            messages.put(EntityNames.CONTACT, "Please enter CONTACT, max 255 characters");
        }

        Part image = request.getPart(EntityNames.FILETOUPLOAD);
        int imageId = 0;
        if (image == null) {
            messages.put(EntityNames.FILETOUPLOAD, "Please select an image file");
        }else{
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
        }
        
        if (messages.isEmpty()) {
            // No validation errors? Do the business job!
            Users user = new Users();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPostCode(postcode);
            user.setEmail(email);
            user.setContact(contact);
            user.setRole("U");
            user.setCity(city);
            user.setImageid(imageId);        
            user.setCreateDate(new Timestamp(System.currentTimeMillis()));
            user.setCreatedBy(1);
            user.setLastModified(new Timestamp(System.currentTimeMillis()));
            user.setModifiedBy(1);
            user.setRemarks(remarks);
            
            UserService userSrv = new UserService();
            userSrv.addUser(user);
                    	
            messages.put("success", "Hello, User added successfully!");
            request.getRequestDispatcher("/index.jsp").forward(request, response);            
        }

        LOGGER.info("User POST Complete");
    }
  }