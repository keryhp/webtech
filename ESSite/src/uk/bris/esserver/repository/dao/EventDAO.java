package uk.bris.esserver.repository.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import uk.bris.esserver.repository.connection.DBConnection;
import uk.bris.esserver.repository.constants.ESSQueries;
import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Event;
import uk.bris.esserver.util.ESSDateUtil;

public class EventDAO implements EntityDAO<Event>{

	private final static Logger LOGGER = Logger.getLogger(Event.class.getName());

	@Override
	public int save(Class<Event> clz, Event entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);
		String query = ESSQueries.INSERT_INTO + clz.getSimpleName() + 
				ESSQueries.OPEN_BRACKET +  
				EntityNames.TITLE + ESSQueries.COMMA + EntityNames.DESCRIPTION + ESSQueries.COMMA + 
				EntityNames.TAGLINE + ESSQueries.COMMA + EntityNames.LOCATION + ESSQueries.COMMA +
				EntityNames.CITY + ESSQueries.COMMA + EntityNames.STARTDATE + ESSQueries.COMMA + 
				EntityNames.PRICE + ESSQueries.COMMA + EntityNames.USERID + ESSQueries.COMMA + 
				EntityNames.IMAGEID + ESSQueries.COMMA + EntityNames.CREATEDATE + ESSQueries.COMMA +  
				EntityNames.CREATEDBY + ESSQueries.COMMA + EntityNames.LASTMODIFIED + ESSQueries.COMMA + 
				EntityNames.MODIFIEDBY + ESSQueries.COMMA + EntityNames.REMARKS +
				ESSQueries.CLOSE_BRACKET + 
				ESSQueries.VALUES + 
				ESSQueries.OPEN_BRACKET + 
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +				
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + 
				ESSQueries.CLOSE_BRACKET;
		LOGGER.info("Event Save: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);		
	}

	@Override
	public int saveOrUpdate(Class<Event> clz, Event entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);		
		//TODO introduce id
		String query = ESSQueries.UPDATE + clz.getSimpleName() + 
				ESSQueries.SET +  
				EntityNames.TITLE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.DESCRIPTION + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.TAGLINE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.LOCATION + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.CITY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.STARTDATE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.PRICE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.USERID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +				
				EntityNames.IMAGEID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +				
				EntityNames.CREATEDATE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CREATEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.LASTMODIFIED + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.MODIFIEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.REMARKS + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK +
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + entity.getId();
		LOGGER.info("Event SaveOrUpdate: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);
	}

	@Override
	public void delete(Class<Event> clz, String id) {
		Map<String, String> queryParams =  new HashMap<String, String>();		
		queryParams.put(new String("1"), id);
		String query = ESSQueries.DELETE_FROM + clz.getSimpleName() + 
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.ORDERBY + EntityNames.STARTDATE;
		LOGGER.info("Event Delete: query:" + query);
		executeUpdate(query, queryParams);
	}

	@Override
	public Event findOne(Class<Event> clz, String id) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), id);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.ORDERBY + EntityNames.STARTDATE;
		LOGGER.info("Event findOne: query:" + query + " queryParams:" + queryParams);
		List<Event> evts = executeQuery(query, queryParams);
		if(evts != null && evts.size() > 0){
			return evts.get(0);
		}else{
			return null;
		}
	}

	public List<Event> findByUserId(Class<Event> clz, String userid) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), userid);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.USERID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.ORDERBY + EntityNames.STARTDATE;
		LOGGER.info("Event findByUserId: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams);
	}
	
	public List<Event> findByDate(Class<Event> clz, String from, String to, String citycode) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), citycode); 
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.CITY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.ORDERBY + EntityNames.STARTDATE;
		LOGGER.info("Event findByDate: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams);
	}

	public List<Event> findByCity(Class<Event> clz, String city) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), city);
		queryParams.put(new String("2"), (new Timestamp(System.currentTimeMillis())).toString());
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.CITY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK +
				ESSQueries.AND +
				EntityNames.STARTDATE + ESSQueries.GREATER_THAN_EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.ORDERBY + EntityNames.STARTDATE;
		LOGGER.info("Event findByCity: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams);
	}

	@Override
	public List<Event> findAll(Class<Event> clz) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), (new Timestamp(System.currentTimeMillis())).toString());
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE +
				EntityNames.STARTDATE + ESSQueries.GREATER_THAN_EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.ORDERBY + EntityNames.STARTDATE;				
		LOGGER.info("Event findAll: query:" + query);
		return executeQuery(query, queryParams);
	}

	@Override
	public List<Event> executeQuery(String query,
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
			List<Event> listOfEvent = new ArrayList<Event>();
			while (rs.next()) {
				Event event = new Event();
				event.setId(rs.getInt(EntityNames.ID));
				event.setTitle(rs.getString(EntityNames.TITLE));
				event.setDescription(rs.getString(EntityNames.DESCRIPTION));
				event.setTagline(rs.getString(EntityNames.TAGLINE));
				event.setLocation(rs.getString(EntityNames.LOCATION));
				event.setCity(rs.getString(EntityNames.CITY));
				event.setStartDate(rs.getTimestamp(EntityNames.STARTDATE));
				event.setPrice(rs.getString(EntityNames.PRICE));
				event.setUserid(rs.getInt(EntityNames.USERID));								
				event.setImageid(rs.getInt(EntityNames.IMAGEID));				
				event.setCreateDate(rs.getTimestamp(EntityNames.CREATEDATE));
				event.setCreatedBy(rs.getInt(EntityNames.CREATEDBY));
				event.setLastModified(rs.getTimestamp(EntityNames.LASTMODIFIED));
				event.setModifiedBy(rs.getInt(EntityNames.MODIFIEDBY));
				event.setRemarks(rs.getString(EntityNames.REMARKS));				
				listOfEvent.add(event);
			}
			ps.close();
			rs.close();
			return listOfEvent;
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

	private Map<String, String> prepareQueryParams(Event event){
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), event.getTitle());
		queryParams.put(new String("2"), event.getDescription());
		queryParams.put(new String("3"), event.getTagline());
		queryParams.put(new String("4"), event.getLocation());
		queryParams.put(new String("5"), event.getCity());
		queryParams.put(new String("6"), event.getStartDate().toString());
		queryParams.put(new String("7"), event.getPrice());
		queryParams.put(new String("8"), new Integer(event.getUserid()).toString());		
		queryParams.put(new String("9"), new Integer(event.getImageid()).toString());
		queryParams.put(new String("10"), event.getCreateDate().toString());
		queryParams.put(new String("11"), new Integer(event.getCreatedBy()).toString());
		queryParams.put(new String("12"), event.getLastModified().toString());
		queryParams.put(new String("13"), new Integer(event.getModifiedBy()).toString());
		queryParams.put(new String("14"), event.getRemarks());		
		return queryParams;
	}
}
