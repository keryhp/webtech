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
import uk.bris.esserver.repository.entities.Users;

public class UserDAO implements EntityDAO<Users>{

	private final static Logger LOGGER = Logger.getLogger(Users.class.getName());

	@Override
	public int save(Class<Users> clz, Users entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);
		String query = ESSQueries.INSERT_INTO + clz.getSimpleName() + 
				ESSQueries.OPEN_BRACKET +  
				EntityNames.FIRSTNAME + ESSQueries.COMMA + EntityNames.LASTNAME + ESSQueries.COMMA + 
				EntityNames.CITY + ESSQueries.COMMA + EntityNames.EMAIL + ESSQueries.COMMA +
				EntityNames.CONTACT + ESSQueries.COMMA + EntityNames.ROLE + ESSQueries.COMMA + 
				EntityNames.POSTCODE + ESSQueries.COMMA + EntityNames.IMAGEID + ESSQueries.COMMA + 
				EntityNames.PASSWORD + ESSQueries.COMMA + EntityNames.SALT + ESSQueries.COMMA + 				
				EntityNames.CREATEDATE + ESSQueries.COMMA +  EntityNames.CREATEDBY + ESSQueries.COMMA + 
				EntityNames.LASTMODIFIED + ESSQueries.COMMA + EntityNames.MODIFIEDBY + ESSQueries.COMMA + 
				EntityNames.REMARKS +
				ESSQueries.CLOSE_BRACKET + 
				ESSQueries.VALUES + 
				ESSQueries.OPEN_BRACKET + 
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +				
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +				
				ESSQueries.QUESTION_MARK +
				ESSQueries.CLOSE_BRACKET;
		LOGGER.info("User Save: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);		
	}

	@Override
	public int saveOrUpdate(Class<Users> clz, Users entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);		
		//TODO introduce id
		String query = ESSQueries.UPDATE + clz.getSimpleName() + 
				ESSQueries.SET +  
				EntityNames.FIRSTNAME + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.LASTNAME + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CITY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.EMAIL + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.CONTACT + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +				
				EntityNames.ROLE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 				
				EntityNames.POSTCODE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.IMAGEID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +	
				EntityNames.PASSWORD + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.SALT + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +				
				EntityNames.CREATEDATE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CREATEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.LASTMODIFIED + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.MODIFIEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.REMARKS + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK +
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + entity.getId();
		LOGGER.info("User SaveOrUpdate: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);
	}

	@Override
	public void delete(Class<Users> clz, String id) {
		Map<String, String> queryParams =  new HashMap<String, String>();		
		queryParams.put(new String("1"), id);
		String query = ESSQueries.DELETE_FROM + clz.getSimpleName() + 
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("User Delete: query:" + query);
		executeUpdate(query, queryParams);
	}

	@Override
	public Users findOne(Class<Users> clz, String id) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), id);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("User findOne: query:" + query + " queryParams:" + queryParams);
		List<Users> usrs = executeQuery(query, queryParams);
		if(usrs.size() == 0){
			return null;
		}else{
			return usrs.get(0);
		}
	}

	public Users findByEmail(Class<Users> clz, String email) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), email);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.EMAIL + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("User findByEmail: query:" + query + " queryParams:" + queryParams);
		List<Users> usrs = executeQuery(query, queryParams);
		if(usrs.size() == 0){
			return null;
		}else{
			return usrs.get(0);
		}
	}

	@Override
	public List<Users> findAll(Class<Users> clz) {
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName();
		LOGGER.info("User findAll: query:" + query);
		return executeQuery(query, null);
	}

	@Override
	public List<Users> executeQuery(String query,
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
			List<Users> listOfUser = new ArrayList<Users>();
			while (rs.next()) {
				Users user = new Users();
				user.setId(rs.getInt(EntityNames.ID));
				user.setFirstName(rs.getString(EntityNames.FIRSTNAME));
				user.setLastName(rs.getString(EntityNames.LASTNAME));
				user.setPostCode(rs.getString(EntityNames.POSTCODE));
				user.setSalt(rs.getString(EntityNames.SALT));
				user.setPassword(rs.getString(EntityNames.PASSWORD));				
				user.setCity(rs.getString(EntityNames.CITY));
				user.setContact(rs.getString(EntityNames.CONTACT));
				user.setRole(rs.getString(EntityNames.ROLE));
				user.setEmail(rs.getString(EntityNames.EMAIL));
				user.setImageid(rs.getInt(EntityNames.IMAGEID));				
				user.setCreateDate(rs.getTimestamp(EntityNames.CREATEDATE));
				user.setCreatedBy(rs.getInt(EntityNames.CREATEDBY));
				user.setLastModified(rs.getTimestamp(EntityNames.LASTMODIFIED));
				user.setModifiedBy(rs.getInt(EntityNames.MODIFIEDBY));
				user.setRemarks(rs.getString(EntityNames.REMARKS));				
				listOfUser.add(user);
			}
			ps.close();
			rs.close();
			return listOfUser;
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

	private Map<String, String> prepareQueryParams(Users user){
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), user.getFirstName());
		queryParams.put(new String("2"), user.getLastName());
		queryParams.put(new String("3"), user.getCity());
		queryParams.put(new String("4"), user.getEmail());
		queryParams.put(new String("5"), user.getContact());
		queryParams.put(new String("6"), user.getRole());
		queryParams.put(new String("7"), user.getPostCode());
		queryParams.put(new String("8"), new Integer(user.getImageid()).toString());
		queryParams.put(new String("9"), user.getPassword());
		queryParams.put(new String("10"), user.getSalt());		
		queryParams.put(new String("11"), user.getCreateDate().toString());
		queryParams.put(new String("12"), new Integer(user.getCreatedBy()).toString());
		queryParams.put(new String("13"), user.getLastModified().toString());
		queryParams.put(new String("14"), new Integer(user.getModifiedBy()).toString());
		queryParams.put(new String("15"), user.getRemarks());		
		return queryParams;
	}
}
