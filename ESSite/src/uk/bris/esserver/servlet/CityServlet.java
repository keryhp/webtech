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
import uk.bris.esserver.repository.entities.City;
import uk.bris.esserver.service.CityService;

@WebServlet("city")
public class CityServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(CityServlet.class.getName());
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Preprocess request: we actually don't need to do any business stuff, so just display JSP.
	    CityService citySrv = new CityService();
	    List<City> lcity = citySrv.getListOfCity();
	    request.setAttribute("lcity", lcity);
	    request.setAttribute("defaultCity", "Bristol");
        //request.getRequestDispatcher("/addcity.jsp").forward(request, response);
        LOGGER.info("City GET Complete");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Postprocess request: gather and validate submitted data and display result in same JSP.
        LOGGER.info("City POST Begin");

        // Prepare messages.
        Map<String, String> messages = new HashMap<String, String>();
        request.setAttribute("messages", messages);

        // Get and validate citycode.
        String citycode = request.getParameter(EntityNames.CITYCODE);
        if (citycode == null || citycode.trim().isEmpty() || citycode.length() > 3) {
            messages.put(EntityNames.CITYCODE, "Please enter CityCode, max 3 characters");
        } else if (!citycode.matches("\\p{Alnum}+")) {
            messages.put(EntityNames.CITYCODE, "Please enter alphanumeric characters only");
        }

        // Get and validate cityname.
        String cityname = request.getParameter(EntityNames.CITYNAME);
        if (cityname == null || cityname.trim().isEmpty() || cityname.length() > 30) {
            messages.put(EntityNames.CITYNAME, "Please enter cityname, max 30 characters");
        }

        // Get and validate postcode.
        String postcode = request.getParameter(EntityNames.POSTCODE);
        if (postcode == null || postcode.trim().isEmpty() || postcode.length() > 10) {
            messages.put(EntityNames.POSTCODE, "Please enter PostCode, max 10 characters");
        }
        
        // Get and validate remarks.
        String remarks = request.getParameter(EntityNames.REMARKS);
        if(remarks != null && remarks.length() > 255){
			messages.put(EntityNames.REMARKS, "Please enter Remarks, max 255 characters");
		}

        // Get and validate country.
        String country = request.getParameter(EntityNames.COUNTRY);
        if (country == null || country.trim().isEmpty() || country.length() > 30) {
            messages.put(EntityNames.COUNTRY, "Please enter Country, max 30 characters");
        }
        if (messages.isEmpty()) {

            // No validation errors? Do the business job!
            City city = new City();
            city.setCityCode(citycode);
            city.setCityName(cityname);
            city.setPostCode(postcode);
            city.setCountry(country);
            city.setCreateDate(new Timestamp(System.currentTimeMillis()));
            city.setCreatedBy(1);
            city.setLastModified(new Timestamp(System.currentTimeMillis()));
            city.setModifiedBy(1);
            city.setRemarks(remarks);
            
            CityService citySrv = new CityService();
            citySrv.addCity(city);
                    	
            messages.put("success", String.format("Hello, City %s %s added successfully!", citycode, cityname));
            request.getRequestDispatcher("/index.jsp").forward(request, response);            
        }

        LOGGER.info("City POST Complete");
    }
  }