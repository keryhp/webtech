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

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Comment;
import uk.bris.esserver.repository.entities.Users;
import uk.bris.esserver.service.CommentService;

@WebServlet("comment")
public class CommentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(CommentServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommentService commentSrv = new CommentService();
		String id = request.getParameter(EntityNames.ID);
		List<Map<String,Object>> comments = commentSrv.getCommentByEventId(id);
		request.setAttribute("comments", comments);
		LOGGER.info("Comment GET Complete");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Postprocess request: gather and validate submitted data and display result in same JSP.
		LOGGER.info("Comment POST Begin");
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
			//String userid = request.getParameter(EntityNames.USERID);

			// Get and validate review
			String review = request.getParameter(EntityNames.REVIEW);
			if (review == null || review.trim().isEmpty() || review.length() > 1024) {
				messages.put(EntityNames.REVIEW, "Please enter comment, maximum 1024 characters");
			}

			if (messages.isEmpty()) {
				// No validation errors? Do the business job!
				Comment comment = new Comment();
				comment.setUserid(user.getId());
				comment.setReview(review);
				comment.setEventid(new Integer(eventid).intValue());
				comment.setCreateDate(new Timestamp(System.currentTimeMillis()));
				comment.setCreatedBy(user.getId());
				comment.setLastModified(new Timestamp(System.currentTimeMillis()));
				comment.setModifiedBy(user.getId());
				comment.setRemarks(remarks);

				CommentService commentSrv = new CommentService();
				commentSrv.addComment(comment);
				messages.put("success", "Hello, Comment added successfully!");
				request.getRequestDispatcher("/index.jsp").forward(request, response);
			}
		}else{
			session.setAttribute("cmessage", "Please login to post a comment!");			
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		LOGGER.info("Comment POST Complete");
	}
}