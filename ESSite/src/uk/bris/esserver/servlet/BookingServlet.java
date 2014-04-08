package uk.bris.esserver.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Booking;
import uk.bris.esserver.repository.entities.Event;
import uk.bris.esserver.repository.entities.Users;
import uk.bris.esserver.service.BookingService;
import uk.bris.esserver.service.EventService;
import uk.bris.esserver.util.ESSDateUtil;

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
		HttpSession session = request.getSession();
		Users user = (Users)session.getAttribute("userObj");
		if(user != null){

			// Prepare messages.
			Map<String, String> messages = new HashMap<String, String>();
			request.setAttribute("messages", messages);

			// Get and validate remarks.
			String remarks = request.getParameter(EntityNames.REMARKS);
			if(remarks != null && remarks.length() > 255){
				messages.put(EntityNames.REMARKS, "Please enter Remarks, max 255 characters");
			}

			String eventid = request.getParameter(EntityNames.EVENTID);

			if (messages.isEmpty()) {
				// No validation errors? Do the business job!
				Booking booking = new Booking();
				booking.setUserid(user.getId());
				booking.setEventid(new Integer(eventid).intValue());
				booking.setCreateDate(new Timestamp(System.currentTimeMillis()));
				booking.setCreatedBy(user.getId());
				booking.setLastModified(new Timestamp(System.currentTimeMillis()));
				booking.setModifiedBy(user.getId());
				booking.setRemarks(remarks);

				BookingService bookingSrv = new BookingService();
				bookingSrv.addBooking(booking);			
				List<Booking> bkgs = bookingSrv.getBookingByUserId(new Integer(user.getId()).toString());
				EventService evtSrv = new EventService();
				Map<String, Object> evtMap = new HashMap<String, Object>();
				List<Map<String, Object>> elist = new ArrayList<Map<String, Object>>();
				List<String> evtsInList = new ArrayList<String>();
				for (Booking booking2 : bkgs) {
					Event evt = evtSrv.getEventById(new Integer(booking2.getEventid()).toString());
					evt.getEvtAsMap().put(EntityNames.CREATEDATE, ESSDateUtil.formatDate(booking2.getCreateDate())); //replace evt creation date with booking date for use in javascript				
					if(!evtsInList.contains(new Integer(evt.getId()).toString())){
						elist.add(evt.getEvtAsMap());
						evtsInList.add(new Integer(evt.getId()).toString());
					}
				}
				evtMap.put(EntityNames.EVENTS, elist);
				JSONObject obj = new JSONObject(evtMap);
				response.getWriter().write(obj.toJSONString());
				messages.put("success", "Hello, Booking added successfully!");
				//request.getRequestDispatcher("/index.jsp").forward(request, response);
			}
		}else{
			session.setAttribute("cmessage", "Please login to book an event!");			
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		LOGGER.info("Booking POST Complete");
	}
}