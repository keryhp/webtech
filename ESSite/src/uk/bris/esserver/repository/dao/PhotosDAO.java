package uk.bris.esserver.repository.dao;

import java.sql.Blob;
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

import javax.sql.rowset.serial.SerialBlob;

import uk.bris.esserver.repository.connection.DBConnection;
import uk.bris.esserver.repository.constants.ESSQueries;
import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.repository.entities.Photos;

public class PhotosDAO implements EntityDAO<Photos>{

	private final static Logger LOGGER = Logger.getLogger(Photos.class.getName());

	@Override
	public int save(Class<Photos> clz, Photos entity) {
		Map<String, Object> queryParams =  prepareQueryParams(entity);
		String query = ESSQueries.INSERT_INTO + clz.getSimpleName() + 
				ESSQueries.OPEN_BRACKET +  
				EntityNames.IMAGE + ESSQueries.COMMA + EntityNames.CREATEDATE + ESSQueries.COMMA + 
				EntityNames.CREATEDBY + ESSQueries.COMMA + EntityNames.LASTMODIFIED + ESSQueries.COMMA + 
				EntityNames.MODIFIEDBY + ESSQueries.COMMA + EntityNames.REMARKS +
				ESSQueries.CLOSE_BRACKET + 
				ESSQueries.VALUES + 
				ESSQueries.OPEN_BRACKET + 
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				ESSQueries.QUESTION_MARK + ESSQueries.COMMA + ESSQueries.QUESTION_MARK +
				ESSQueries.CLOSE_BRACKET;
		LOGGER.info("Photos Save: query:" + query + " queryParams:" + queryParams);
		return executeUpdatePhoto(query, queryParams);		
	}

	@Override
	public int saveOrUpdate(Class<Photos> clz, Photos entity) {
		Map<String, Object> queryParams =  prepareQueryParams(entity);		
		//TODO introduce id
		String query = ESSQueries.UPDATE + clz.getSimpleName() + 
				ESSQueries.SET +  
				EntityNames.IMAGE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CREATEDATE + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.CREATEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.LASTMODIFIED + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA + 
				EntityNames.MODIFIEDBY + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK + ESSQueries.COMMA +
				EntityNames.REMARKS + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK +
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + entity.getId();
		LOGGER.info("City SaveOrUpdate: query:" + query + " queryParams:" + queryParams);
		return executeUpdatePhoto(query, queryParams);
	}

	@Override
	public void delete(Class<Photos> clz, String id) {
		Map<String, String> queryParams =  new HashMap<String, String>();		
		queryParams.put(new String("1"), id);
		String query = ESSQueries.DELETE_FROM + clz.getSimpleName() + 
				ESSQueries.WHERE +  EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("Photos Delete: query:" + query);
		executeUpdate(query, queryParams);
	}

	@Override
	public Photos findOne(Class<Photos> clz, String id) {
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(new String("1"), id);
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName() + ESSQueries.WHERE + 
				EntityNames.ID + ESSQueries.EQUAL_TO + ESSQueries.QUESTION_MARK;
		LOGGER.info("Photos findOne: query:" + query + " queryParams:" + queryParams);
		List<Photos> photos = executeQuery(query, queryParams);
		if(photos.size() > 0){
			return photos.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<Photos> findAll(Class<Photos> clz) {
		String query = ESSQueries.SELECT_ALL_FROM + clz.getSimpleName();
		LOGGER.info("Photos findAll: query:" + query);
		return executeQuery(query, null);
	}

	@Override
	public List<Photos> executeQuery(String query,
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
			List<Photos> listOfPhotos = new ArrayList<Photos>();
			while (rs.next()) {
				Photos photo = new Photos();
				Blob b = rs.getBlob(EntityNames.IMAGE);
				/*InputStream is = null;
				if(b!=null){
					is = b.getBinaryStream();
				}
				photo.setImage(is);*/
				int blobLength = (int) b.length();  
				byte[] blobAsBytes = b.getBytes(1, blobLength);
				//release the blob and free up memory. (since JDBC 4.0)
				b.free();
				photo.setId(rs.getInt(EntityNames.ID));
				photo.setImage(blobAsBytes);
				photo.setCreateDate(rs.getTimestamp(EntityNames.CREATEDATE));
				photo.setCreatedBy(rs.getInt(EntityNames.CREATEDBY));
				photo.setLastModified(rs.getTimestamp(EntityNames.LASTMODIFIED));
				photo.setModifiedBy(rs.getInt(EntityNames.MODIFIEDBY));
				photo.setRemarks(rs.getString(EntityNames.REMARKS));				
				listOfPhotos.add(photo);
			}
			ps.close();
			rs.close();
			return listOfPhotos;
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
			int success = ps.executeUpdate();
			con.commit();
			ps.close();
			return success;
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

	public int executeUpdatePhoto(String query,
			Map<String, Object> queryParams) {
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
					if(key != 1){
						String value = (String)queryParams.get(keyVal);
						ps.setString(key, value);
					}else{
						byte[] value = (byte[])queryParams.get(keyVal);
						Blob b = new SerialBlob(value);						
						ps.setBlob(key, b);						
					}
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

	private Map<String, Object> prepareQueryParams(Photos photo){
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(new String("1"), photo.getImage());
		queryParams.put(new String("2"), photo.getCreateDate().toString());
		queryParams.put(new String("3"), new Integer(photo.getCreatedBy()).toString());
		queryParams.put(new String("4"), photo.getLastModified().toString());
		queryParams.put(new String("5"), new Integer(photo.getModifiedBy()).toString());
		queryParams.put(new String("6"), photo.getRemarks());		
		return queryParams;
	}
}
