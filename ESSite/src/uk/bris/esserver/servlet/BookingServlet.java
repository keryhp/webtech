package uk.bris.esserver.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Booking;
import uk.bris.esserver.service.BookingService;

@WebServlet("booking")
public class BookingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(BookingServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BookingService bookingSrv = new BookingService();
		String id = request.getParameter(EntityNames.ID);
		List<Booking> bookings = bookingSrv.getBookingByUserId(id);
		request.setAttribute("bookings", bookings);
		LOGGER.info("Booking GET Complete");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Postprocess request: gather and validate submitted data and display result in same JSP.
		LOGGER.info("Booking POST Begin");

		// Prepare messages.
		Map<String, String> messages = new HashMap<String, String>();
		request.setAttribute("messages", messages);

		// Get and validate remarks.
		String remarks = request.getParameter(EntityNames.REMARKS);
		if(remarks != null && remarks.length() > 255){
			messages.put(EntityNames.REMARKS, "Please enter Remarks, max 255 characters");
		}
		
		String eventid = request.getParameter(EntityNames.EVENTID);
		//String userid = request.getParameter(EntityNames.USERID);

		if (messages.isEmpty()) {
			// No validation errors? Do the business job!
			Booking booking = new Booking();
			booking.setEventid(new Integer(eventid).intValue());
			booking.setEventid(1);//TODO change to actual userid        
			booking.setCreateDate(new Timestamp(System.currentTimeMillis()));
			booking.setCreatedBy(1);
			booking.setLastModified(new Timestamp(System.currentTimeMillis()));
			booking.setModifiedBy(1);
			booking.setRemarks(remarks);

			BookingService bookingSrv = new BookingService();
			bookingSrv.addBooking(booking);			
			messages.put("success", "Hello, Booking added successfully!");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}

		LOGGER.info("Booking POST Complete");
	}
}