package uk.bris.esserver.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.People;
import uk.bris.esserver.service.PeopleService;

@WebServlet("/test")
public class PeopleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(PeopleServlet.class.getName());
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Preprocess request: we actually don't need to do any business stuff, so just display JSP.
        //request.getRequestDispatcher("/test.jsp").forward(request, response);
        LOGGER.info("TEST GET Complete");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Postprocess request: gather and validate submitted data and display result in same JSP.
        LOGGER.info("TEST POST Begin");

        // Prepare messages.
        Map<String, String> messages = new HashMap<String, String>();
        request.setAttribute("messages", messages);

        // Get and validate forenames.
        String forenames = request.getParameter(EntityNames.FORENAMES);
        if (forenames == null || forenames.trim().isEmpty()) {
            messages.put(EntityNames.FORENAMES, "Please enter forenames");
        } else if (!forenames.matches("\\p{Alnum}+")) {
            messages.put(EntityNames.FORENAMES, "Please enter alphanumeric characters only");
        }

        // Get and validate surname.
        String surname = request.getParameter(EntityNames.SURNAME);
        if (surname == null || surname.trim().isEmpty()) {
            messages.put(EntityNames.SURNAME, "Please enter surname");
        } else if (!surname.matches("\\p{Alnum}+")) {
            messages.put(EntityNames.SURNAME, "Please enter alphanumeric characters only");
        }

        // No validation errors? Do the business job!
        People ppl = new People();
        ppl.setForenames(forenames);
        ppl.setSurname(surname);
        
        PeopleService pplSrv = new PeopleService();
        pplSrv.addPeople(ppl);
        
        if (messages.isEmpty()) {
            messages.put("success", String.format("Hello, your forename is %s and your surname is %s!", forenames, surname));
        }

        request.getRequestDispatcher("/test.jsp").forward(request, response);
        LOGGER.info("TEST POST Complete");
    }
  }