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
import uk.bris.esserver.repository.entities.City;

public class CityDAO implements EntityDAO<City>{

	private final static Logger LOGGER = Logger.getLogger(City.class.getName());

	@Override
	public int save(Class<City> clz, City entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);
		String query = ESSQueries.INSERT_INTO + clz.getSimpleName() + 
				ESSQueries.OPEN_BRACKET +  
				EntityNames.CITYCODE + ESSQueries.COMMA + EntityNames.CITYNAME + ESSQueries.COMMA + 
				EntityNames.POSTCODE + ESSQueries.COMMA + EntityNames.COUNTRY + ESSQueries.COMMA +
				EntityNames.CREATEDATE + ESSQueries.COMMA + EntityNames.CREATEDBY + ESSQueries.COMMA +
				EntityNames.LASTMODIFIED + ESSQueries.COMMA + EntityNames.MODIFIEDBY + ESSQueries.COMMA +
				EntityNames.REMARKS +
				ESSQueries.CLOSE_BRACKET + 
				ESSQueries.VALUES + 
				ESSQueries.OPEN_BRACKET + 
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK +
				ESSQueries.CLOSE_BRACKET;
		LOGGER.info("City Save: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);		
	}

	@Override
	public int saveOrUpdate(Class<City> clz, City entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);		
		//TODO introduce id
		String query = ESSQueries.UPDATE + clz.getSimpleName() + 
				ESSQueries.SET +  
				EntityNames.CITYCODE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CITYNAME + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.POSTCODE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.COUNTRY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.CREATEDATE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CREATEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.LASTMODIFIED + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.MODIFIEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.REMARKS + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK +
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + entity.getId();
		LOGGER.info("City SaveOrUpdate: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);
	}

	@Override
	public void delete(Class<City> clz, String id) {
		Map<String, String> queryParams =  new HashMap<String, String>();		
		queryParams.put(new String("1"), id);
		String query = ESSQueries.DELETE_FROM + clz.getSimpleName() + 
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("City Delete: query:" + query);
		executeUpdate(query, queryParams);
	}

	@Override
	public City findOne(Class<City> clz, String id) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), id);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("City findOne: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams).get(0);
	}

	@Override
	public List<City> findAll(Class<City> clz) {
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName();
		LOGGER.info("City findAll: query:" + query);
		return executeQuery(query, null);
	}

	@Override
	public List<City> executeQuery(String query,
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
			List<City> listOfCity = new ArrayList<City>();
			while (rs.next()) {
				City city = new City();
				city.setId(rs.getInt(EntityNames.ID));
				city.setCityCode(rs.getString(EntityNames.CITYCODE));
				city.setCityName(rs.getString(EntityNames.CITYNAME));
				city.setPostCode(rs.getString(EntityNames.POSTCODE));
				city.setCountry(rs.getString(EntityNames.COUNTRY));
				city.setCreateDate(rs.getTimestamp(EntityNames.CREATEDATE));
				city.setCreatedBy(rs.getInt(EntityNames.CREATEDBY));
				city.setLastModified(rs.getTimestamp(EntityNames.LASTMODIFIED));
				city.setModifiedBy(rs.getInt(EntityNames.MODIFIEDBY));
				city.setRemarks(rs.getString(EntityNames.REMARKS));				
				listOfCity.add(city);
			}
			ps.close();
			rs.close();
			return listOfCity;
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
	
	private Map<String, String> prepareQueryParams(City city){
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), city.getCityCode());
		queryParams.put(new String("2"), city.getCityName());
		queryParams.put(new String("3"), city.getPostCode());
		queryParams.put(new String("4"), city.getCountry());
		queryParams.put(new String("5"), city.getCreateDate().toString());
		queryParams.put(new String("6"), new Integer(city.getCreatedBy()).toString());
		queryParams.put(new String("7"), city.getLastModified().toString());
		queryParams.put(new String("8"), new Integer(city.getModifiedBy()).toString());
		queryParams.put(new String("9"), city.getRemarks());		
		return queryParams;
	}
}
