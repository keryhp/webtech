package uk.bris.esserver.repository.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import uk.bris.esserver.repository.connection.DBConnection;
import uk.bris.esserver.repository.constants.ESSQueries;
import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Comment;

public class CommentDAO implements EntityDAO<Comment>{

	private final static Logger LOGGER = Logger.getLogger(Comment.class.getName());

	@Override
	public int save(Class<Comment> clz, Comment entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);
		String query = ESSQueries.INSERT_INTO + clz.getSimpleName() + 
				ESSQueries.OPEN_BRACKET +  
				EntityNames.USERID + ESSQueries.COMMA + EntityNames.EVENTID + ESSQueries.COMMA + 
				EntityNames.REVIEW + ESSQueries.COMMA + EntityNames.CREATEDATE + ESSQueries.COMMA +  
				EntityNames.CREATEDBY + ESSQueries.COMMA + EntityNames.LASTMODIFIED + ESSQueries.COMMA + 
				EntityNames.MODIFIEDBY + ESSQueries.COMMA + EntityNames.REMARKS +
				ESSQueries.CLOSE_BRACKET + 
				ESSQueries.VALUES + 
				ESSQueries.OPEN_BRACKET + 
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + 
				ESSQueries.CLOSE_BRACKET;
		LOGGER.info("Comment Save: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);		
	}

	@Override
	public int saveOrUpdate(Class<Comment> clz, Comment entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);		
		//TODO introduce id
		String query = ESSQueries.UPDATE + clz.getSimpleName() + 
				ESSQueries.SET +  
				EntityNames.USERID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.EVENTID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.REVIEW + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CREATEDATE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CREATEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.LASTMODIFIED + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.MODIFIEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.REMARKS + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK +
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + entity.getId();
		LOGGER.info("Comment SaveOrUpdate: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);
	}

	@Override
	public void delete(Class<Comment> clz, String id) {
		Map<String, String> queryParams =  new HashMap<String, String>();		
		queryParams.put(new String("1"), id);
		String query = ESSQueries.DELETE_FROM + clz.getSimpleName() + 
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("Comment Delete: query:" + query);
		executeUpdate(query, queryParams);
	}

	@Override
	public Comment findOne(Class<Comment> clz, String id) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), id);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("Comment findOne: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams).get(0);
	}

	public List<Comment> findByEventId(Class<Comment> clz, String eventid) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), eventid);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.EVENTID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("Comment findByEventId: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams);
	}
	
	public List<Comment> findByUserId(Class<Comment> clz, String userid) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), userid);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.USERID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("Comment findByUserId: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams);
	}

	@Override
	public List<Comment> findAll(Class<Comment> clz) {
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName();
		LOGGER.info("Comment findAll: query:" + query);
		return executeQuery(query, null);
	}

	@Override
	public List<Comment> executeQuery(String query,
			Map<String, String> queryParams) {
		Connection con = null;
		try {
			DBConnection dbcon = new DBConnection(false);
			con = dbcon.getDBConnection();
			PreparedStatement ps = con.prepareStatement(query);
			if(queryParams != null){
				Iterator<String> it = queryParams.keySet().iterator();
				while(it.hasNext()) {
					String keyVal = it.next();
					int key = Integer.parseInt(keyVal);
					String value = queryParams.get(keyVal);
					ps.setString(key, value);
				}
			}
			ResultSet rs = ps.executeQuery();
			List<Comment> listOfComment = new ArrayList<Comment>();
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setId(rs.getInt(EntityNames.ID));
				comment.setUserid(rs.getInt(EntityNames.USERID));
				comment.setEventid(rs.getInt(EntityNames.EVENTID));
				comment.setReview(rs.getString(EntityNames.REVIEW));
				comment.setCreateDate(rs.getTimestamp(EntityNames.CREATEDATE));
				comment.setCreatedBy(rs.getInt(EntityNames.CREATEDBY));
				comment.setLastModified(rs.getTimestamp(EntityNames.LASTMODIFIED));
				comment.setModifiedBy(rs.getInt(EntityNames.MODIFIEDBY));
				comment.setRemarks(rs.getString(EntityNames.REMARKS));				
				listOfComment.add(comment);
			}
			ps.close();
			rs.close();
			return listOfComment;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

				}
			}
		}

		return null;
	}

	@Override
	public int executeUpdate(String query,
			Map<String, String> queryParams) {
		Connection con = null;
		try {
			DBConnection dbcon = new DBConnection(false);
			con = dbcon.getDBConnection();
			PreparedStatement ps = con.prepareStatement(query, new String[] { "ID"});
			if(queryParams != null){
				Iterator<String> it = queryParams.keySet().iterator();
				while(it.hasNext()) {
					String keyVal = it.next();
					int key = Integer.parseInt(keyVal);
					String value = queryParams.get(keyVal);
					ps.setString(key, value);
				}
			}
			int insertId = ps.executeUpdate();
			ResultSet generatedKeys = ps.getGeneratedKeys();
	        if (generatedKeys != null && generatedKeys.next()) {
	        	insertId = generatedKeys.getInt(1);
		        generatedKeys.close();
	        }
			con.commit();
			ps.close();
			return insertId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

				}
			}
		}

		return 0;
	}

	private Map<String, String> prepareQueryParams(Comment comment){
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), new Integer(comment.getUserid()).toString());
		queryParams.put(new String("2"), new Integer(comment.getEventid()).toString());
		queryParams.put(new String("3"), comment.getReview());
		queryParams.put(new String("4"), comment.getCreateDate().toString());
		queryParams.put(new String("5"), new Integer(comment.getCreatedBy()).toString());
		queryParams.put(new String("6"), comment.getLastModified().toString());
		queryParams.put(new String("7"), new Integer(comment.getModifiedBy()).toString());
		queryParams.put(new String("8"), comment.getRemarks());		
		return queryParams;
	}
}
