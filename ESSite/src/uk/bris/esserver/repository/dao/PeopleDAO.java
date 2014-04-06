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
import uk.bris.esserver.repository.entities.People;

public class PeopleDAO implements EntityDAO<People>{

	private final static Logger LOGGER = Logger.getLogger(People.class.getName());

	@Override
	public int save(Class<People> clz, People entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);
		String query = ESSQueries.INSERT_INTO + clz.getSimpleName() + 
				ESSQueries.OPEN_BRACKET +  EntityNames.FORENAMES + ESSQueries.COMMA + EntityNames.SURNAME + ESSQueries.CLOSE_BRACKET + 
				ESSQueries.VALUES + ESSQueries.OPEN_BRACKET + ESSQueries.QUESTION_MARK + 
				ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.CLOSE_BRACKET;
		LOGGER.info("People Save: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);		
	}

	@Override
	public int saveOrUpdate(Class<People> clz, People entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);		
		String query = ESSQueries.UPDATE + clz.getSimpleName() + 
				ESSQueries.SET +  EntityNames.FORENAMES + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + 
				ESSQueries.COMMA + EntityNames.SURNAME + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + 
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + entity.getId();
		LOGGER.info("People SaveOrUpdate: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);
	}

	@Override
	public void delete(Class<People> clz, String id) {
		Map<String, String> queryParams =  new HashMap<String, String>();		
		queryParams.put(new String("1"), id);
		String query = ESSQueries.DELETE_FROM + clz.getSimpleName() + 
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("People Delete: query:" + query);
		executeUpdate(query, queryParams);
	}

	@Override
	public People findOne(Class<People> clz, String id) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), id);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("People findOne: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams).get(0);
	}

	@Override
	public List<People> findAll(Class<People> clz) {
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName();
		LOGGER.info("People findOne: query:" + query);
		return executeQuery(query, null);
	}

	@Override
	public List<People> executeQuery(String query,
			Map<String, String> queryParams) {
		//		Statement st = con.createStatement();
		//		ResultSet rs = st.executeQuery("select forenames, surname from people");
		//		use prepared statement instead of statement		
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
			List<People> listOfPeople = new ArrayList<People>();
			while (rs.next()) {
				People ppl = new People();
				ppl.setId(rs.getInt(EntityNames.ID));
				ppl.setForenames(rs.getString(EntityNames.FORENAMES));
				ppl.setSurname(rs.getString(EntityNames.SURNAME));
				listOfPeople.add(ppl);
			}
			ps.close();
			rs.close();
			return listOfPeople;
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
	
	private Map<String, String> prepareQueryParams(People ppl){
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), ppl.getForenames());
		queryParams.put(new String("2"), ppl.getSurname());
		return queryParams;
	}
}
