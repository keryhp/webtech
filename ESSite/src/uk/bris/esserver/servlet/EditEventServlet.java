package uk.bris.esserver.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Event;
import uk.bris.esserver.repository.entities.Photos;
import uk.bris.esserver.repository.entities.Users;
import uk.bris.esserver.service.EventService;
import uk.bris.esserver.service.PhotosService;
import uk.bris.esserver.util.ESSDateUtil;

@WebServlet("/editevent")
@MultipartConfig(maxFileSize = 16177215)
public class EditEventServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(EditEventServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EventService eventSrv = new EventService();
		String id = request.getParameter(EntityNames.ID);
		Event event = eventSrv.getEventById(id);
		//request.setAttribute("event", event);
		Map<String, Object> evtAsMap = event.getEvtAsMap();
		JSONObject obj = new JSONObject(evtAsMap);
		response.getWriter().write(obj.toJSONString());
		LOGGER.info("Edit Event GET Complete");
		HttpSession session = request.getSession();
		session.setAttribute("cmessage", null);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Postprocess request: gather and validate submitted data and display result in same JSP.
		LOGGER.info("Edit Event POST Begin");
		HttpSession session = request.getSession();
		Users user = (Users)session.getAttribute("userObj");
		if(user != null){
			// Prepare messages.
			Map<String, String> messages = new HashMap<String, String>();
			request.setAttribute("messages", messages);

			// Get and validate eventcode.
			String city = request.getParameter(EntityNames.ECITY);

			// Get and validate eventname.
			String title = request.getParameter(EntityNames.TITLE);
			if (title == null || title.trim().isEmpty()|| title.length() > 255) {
				messages.put(EntityNames.TITLE, "Please enter Title max 255 characters");
			}

			// Get and validate eventname.
			String tagline = request.getParameter(EntityNames.TAGLINE);
			if (tagline == null || tagline.trim().isEmpty()|| tagline.length() > 255) {
				messages.put(EntityNames.TAGLINE, "Please enter Tagline max 255 characters");
			}

			// Get and validate eventname.
			String description = request.getParameter(EntityNames.DESCRIPTION);
			if (description == null || description.trim().isEmpty() || description.length() > 1024) {
				messages.put(EntityNames.DESCRIPTION, "Please enter Description max 1024 characters");
			}

			// Get and validate eventname.
			String location = request.getParameter(EntityNames.LOCATION);
			if (location == null || location.trim().isEmpty() || location.length() > 255) {
				messages.put(EntityNames.LOCATION, "Please enter Location max 255 characters");
			}

			// Get and validate eventname.
			String startDate = request.getParameter(EntityNames.STARTDATE);
			if (startDate == null || startDate.trim().isEmpty()) {
				messages.put(EntityNames.STARTDATE, "Please enter Start Date");
			}

			// Get and validate eventname.
			String price = request.getParameter(EntityNames.PRICE);
			if (price == null || price.trim().isEmpty() || price.length() > 10 ||!price.matches("\\d+")) {
				messages.put(EntityNames.PRICE, "Please enter price in digits, max 10 digit");
			}

			// Get and validate remarks.
			String remarks = request.getParameter(EntityNames.REMARKS);
			if (remarks != null && remarks.length() > 255) {
				messages.put(EntityNames.REMARKS, "Please enter Remarks, max 255 characters");
			}

			if(messages.isEmpty()){

				String id = request.getParameter(EntityNames.EVENTID);
				EventService eventSrv = new EventService();
				Event event = eventSrv.getEventById(id);

				Part image = request.getPart(EntityNames.FILETOUPLOAD);
				int imageId = 0;
				if (image != null) {
					Photos photo = new Photos();
					photo.setImage(IOUtils.toByteArray(image.getInputStream()));
					photo.setCreateDate(new Timestamp(System.currentTimeMillis()));
					photo.setCreatedBy(user.getId());
					photo.setLastModified(new Timestamp(System.currentTimeMillis()));
					photo.setModifiedBy(user.getId());
					photo.setRemarks(remarks);

					PhotosService photosSrv = new PhotosService();
					imageId = photosSrv.addPhoto(photo);
					event.setImageid(imageId);
				}

				// No validation errors? Do the business job!
				event.setTitle(title);        
				event.setTagline(tagline);
				event.setDescription(description);
				event.setLocation(location);
				event.setCity(city);
				if(startDate == null){
					event.setStartDate(new Timestamp((ESSDateUtil.parseDateString(startDate)).getTime()));				
				}
				event.setPrice(price);
				event.setUserid(user.getId());                
				event.setLastModified(new Timestamp(System.currentTimeMillis()));
				event.setModifiedBy(user.getId());
				event.setRemarks(remarks);

				eventSrv.updateEvent(event);        	
				messages.put("success", "Hello, Event edited successfully!");
				session.setAttribute("cmessage", "Event edited successfully");
				request.getRequestDispatcher("/index.jsp").forward(request, response);			
			}
		}else{
			session.setAttribute("cmessage", "Please login to post event!");
			request.getRequestDispatcher("/index.jsp").forward(request, response);	
		}
		LOGGER.info("Edit Event POST Complete");
	}
}