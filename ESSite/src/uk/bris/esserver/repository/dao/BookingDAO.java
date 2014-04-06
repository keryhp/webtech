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
import uk.bris.esserver.repository.entities.Booking;

public class BookingDAO implements EntityDAO<Booking>{

	private final static Logger LOGGER = Logger.getLogger(Booking.class.getName());

	@Override
	public int save(Class<Booking> clz, Booking entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);
		String query = ESSQueries.INSERT_INTO + clz.getSimpleName() + 
				ESSQueries.OPEN_BRACKET +  
				EntityNames.USERID + ESSQueries.COMMA + EntityNames.EVENTID + ESSQueries.COMMA + 
				EntityNames.CREATEDATE + ESSQueries.COMMA +  
				EntityNames.CREATEDBY + ESSQueries.COMMA + EntityNames.LASTMODIFIED + ESSQueries.COMMA + 
				EntityNames.MODIFIEDBY + ESSQueries.COMMA + EntityNames.REMARKS +
				ESSQueries.CLOSE_BRACKET + 
				ESSQueries.VALUES + 
				ESSQueries.OPEN_BRACKET + 
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + 
				ESSQueries.CLOSE_BRACKET;
		LOGGER.info("Booking Save: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);		
	}

	@Override
	public int saveOrUpdate(Class<Booking> clz, Booking entity) {
		Map<String, String> queryParams =  prepareQueryParams(entity);		
		//TODO introduce id
		String query = ESSQueries.UPDATE + clz.getSimpleName() + 
				ESSQueries.SET +  
				EntityNames.USERID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.EVENTID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CREATEDATE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CREATEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.LASTMODIFIED + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.MODIFIEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.REMARKS + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK +
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + entity.getId();
		LOGGER.info("Booking SaveOrUpdate: query:" + query + " queryParams:" + queryParams);
		return executeUpdate(query, queryParams);
	}

	@Override
	public void delete(Class<Booking> clz, String id) {
		Map<String, String> queryParams =  new HashMap<String, String>();		
		queryParams.put(new String("1"), id);
		String query = ESSQueries.DELETE_FROM + clz.getSimpleName() + 
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("Booking Delete: query:" + query);
		executeUpdate(query, queryParams);
	}

	@Override
	public Booking findOne(Class<Booking> clz, String id) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), id);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("Booking findOne: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams).get(0);
	}

	public List<Booking> findByEventId(Class<Booking> clz, String eventid) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), eventid);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.EVENTID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("Booking findByEventId: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams);
	}
	
	public List<Booking> findByUserId(Class<Booking> clz, String userid) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), userid);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.USERID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("Booking findByUserId: query:" + query + " queryParams:" + queryParams);
		return executeQuery(query, queryParams);
	}
	
	@Override
	public List<Booking> findAll(Class<Booking> clz) {
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName();
		LOGGER.info("Booking findAll: query:" + query);
		return executeQuery(query, null);
	}

	@Override
	public List<Booking> executeQuery(String query,
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
			List<Booking> listOfBooking = new ArrayList<Booking>();
			while (rs.next()) {
				Booking booking = new Booking();
				booking.setId(rs.getInt(EntityNames.ID));
				booking.setUserid(rs.getInt(EntityNames.USERID));
				booking.setEventid(rs.getInt(EntityNames.EVENTID));
				booking.setCreateDate(rs.getTimestamp(EntityNames.CREATEDATE));
				booking.setCreatedBy(rs.getInt(EntityNames.CREATEDBY));
				booking.setLastModified(rs.getTimestamp(EntityNames.LASTMODIFIED));
				booking.setModifiedBy(rs.getInt(EntityNames.MODIFIEDBY));
				booking.setRemarks(rs.getString(EntityNames.REMARKS));				
				listOfBooking.add(booking);
			}
			ps.close();
			rs.close();
			return listOfBooking;
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

	private Map<String, String> prepareQueryParams(Booking booking){
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), new Integer(booking.getUserid()).toString());
		queryParams.put(new String("2"), new Integer(booking.getEventid()).toString());
		queryParams.put(new String("3"), booking.getCreateDate().toString());
		queryParams.put(new String("4"), new Integer(booking.getCreatedBy()).toString());
		queryParams.put(new String("5"), booking.getLastModified().toString());
		queryParams.put(new String("6"), new Integer(booking.getModifiedBy()).toString());
		queryParams.put(new String("7"), booking.getRemarks());		
		return queryParams;
	}
}
