package uk.bris.esserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.bris.esserver.repository.dao.CommentDAO;
import uk.bris.esserver.repository.entities.Comment;
import uk.bris.esserver.repository.entities.Users;

public class CommentService {

	CommentDAO cdao = new CommentDAO();
	public List<Comment> getListOfComment(){
		List<Comment> listOfComment = cdao.findAll(Comment.class);
		return listOfComment;
	}

	public Comment getCommentById(String id){
		Comment comment = cdao.findOne(Comment.class, id);
		return comment;
	}

	public List<Map<String, Object>> getCommentByEventId(String eventid){
		List<Comment> comments = cdao.findByEventId(Comment.class, eventid);
		List<Map<String, Object>> allCommentAsMap = new ArrayList<Map<String,Object>>();
		UserService usrService = new UserService();
		for (Comment comment : comments) {
			Users usr = usrService.getUserById(new Integer(comment.getUserid()).toString());
			allCommentAsMap.add(comment.getCommentAsMap(usr.getEmail()));
		}
		return allCommentAsMap;
	}

	public List<Comment> getCommentByUserId(String userid){
		List<Comment> comments = cdao.findByUserId(Comment.class, userid);
		return comments;
	}

	public int addComment(Comment comment){
		return cdao.save(Comment.class, comment);
	}
}
