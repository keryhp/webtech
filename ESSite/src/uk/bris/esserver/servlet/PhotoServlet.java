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

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Photos;
import uk.bris.esserver.service.PhotosService;

@WebServlet("photo")
@MultipartConfig(maxFileSize = 16177215)
public class PhotoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(PhotoServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Preprocess request: we actually don't need to do any business stuff, so just display JSP.
		PhotosService photosSrv = new PhotosService();
		//	    List<Photos> lphotos = photosSrv.getListOfPhotos();
		//	    request.setAttribute("lphotos", lphotos);
		String id = request.getParameter(EntityNames.ID);
		Photos photo = photosSrv.getPhotoById(id);
		if(photo != null){
			byte[] photoBytes = photo.getImage();
			response.setContentType("image/png");
			response.setContentLength(photoBytes.length);
			response.setHeader("Content-Disposition", "inline;filename=\"" + id + "\"");
			response.getOutputStream().write(photoBytes);
		}
		//request.getRequestDispatcher("/addphoto.jsp").forward(request, response);
		LOGGER.info("Photo GET Complete");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Postprocess request: gather and validate submitted data and display result in same JSP.
		LOGGER.info("Photos POST Begin");

		// Prepare messages.
		Map<String, String> messages = new HashMap<String, String>();
		request.setAttribute("messages", messages);

		// Get and validate remarks.
		String remarks = request.getParameter(EntityNames.REMARKS);
		if (remarks != null && remarks.length() > 255) {
			messages.put(EntityNames.REMARKS, "Please enter Remarks, max 255 characters");
		}
		if (messages.isEmpty()) {
			Part image = request.getPart(EntityNames.FILETOUPLOAD);
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
				photosSrv.addPhoto(photo);

				messages.put("success", String.format("Hello, Image added successfully!"));
				request.getRequestDispatcher("/index.jsp").forward(request, response);			
			}
		}
		LOGGER.info("Photos POST Complete");
	}
}