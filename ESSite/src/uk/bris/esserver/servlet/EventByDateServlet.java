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
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Event;
import uk.bris.esserver.service.EventService;
import uk.bris.esserver.util.ESSDateUtil;

@WebServlet("/eventbydate")
public class EventByDateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(EventByDateServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EventService eventSrv = new EventService();
		String from = request.getParameter(EntityNames.STARTDATE);
		String to = request.getParameter(EntityNames.ENDDATE);
		String citycode = request.getParameter(EntityNames.CITY);
		List<Event> events = eventSrv.getEventsByDate(from, to, citycode);
		//request.setAttribute("event", event);
		Map<String, Object> evtbyDate = new HashMap<String, Object>();
		//the where clause with from and to doesn't work, using alternative
		for (Event event : events) {
			if(new Timestamp(ESSDateUtil.parseDateString(ESSDateUtil.parser.format(event.getStartDate())).getTime()).after(new Timestamp(ESSDateUtil.parseDateString(from).getTime())) && 
					new Timestamp(ESSDateUtil.parseDateString(ESSDateUtil.parser.format(event.getStartDate())).getTime()).before(new Timestamp(ESSDateUtil.parseDateString(to).getTime())) ){
				evtbyDate.put(new Integer(event.getId()).toString(), event.getEvtAsMap());
			}
		}
		JSONObject obj = new JSONObject(evtbyDate);
		response.getWriter().write(obj.toJSONString());
		request.getSession().removeAttribute("cmessage");
		LOGGER.info("EventByDate GET Complete");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Postprocess request: gather and validate submitted data and display result in same JSP.
		LOGGER.info("EventByDate POST Begin");
		//nothing here
		LOGGER.info("EventByDate POST Complete");
	}
}