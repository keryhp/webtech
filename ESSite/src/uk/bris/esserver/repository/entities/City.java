package uk.bris.esserver.repository.entities;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.util.ESSDateUtil;

public class City {

	private int id;	
	private String cityCode;
	private String cityName;
	private String postCode;
	private String country;
	private Timestamp createDate;
	private int createdBy;
	private Timestamp lastModified;
	private int modifiedBy;
	private String remarks;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String citycode) {
		this.cityCode = citycode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getLastModified() {
		return lastModified;
	}
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	public int getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Map<String, Object> getCityAsMap(){
		Map<String, Object> cityData = new HashMap<String, Object>();
		cityData.put(EntityNames.ID, new Integer(this.id).toString());
		cityData.put(EntityNames.CITYCODE, this.cityCode);
		cityData.put(EntityNames.CITYNAME, this.cityName);
		cityData.put(EntityNames.POSTCODE, this.postCode);
		cityData.put(EntityNames.COUNTRY, this.country);
		cityData.put(EntityNames.CREATEDATE, ESSDateUtil.formatTimeStamp(this.createDate));
		cityData.put(EntityNames.CREATEDBY, new Integer(this.createdBy).toString());
		cityData.put(EntityNames.CREATEDATE, ESSDateUtil.formatTimeStamp(this.lastModified));
		cityData.put(EntityNames.CREATEDBY, new Integer(this.modifiedBy).toString());		
		cityData.put(EntityNames.REMARKS, this.remarks);
		return cityData;
	}
}
