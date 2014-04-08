package uk.bris.esserver.repository.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.util.ESSDateUtil;

public class Event {

	private int id;	
	private String title;
	private String description;
	private String tagline;	
	private String location;
	private String city;	
	private Timestamp startDate;
	private String price;	
	private int userid;	
	private int imageid;	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getImageid() {
		return imageid;
	}
	public void setImageid(int imageid) {
		this.imageid = imageid;
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
	
	public Map<String, Object> getEvtAsMap(){
		Map<String, Object> eventData = new HashMap<String, Object>();
		eventData.put(EntityNames.ID, new Integer(this.id).toString());
		eventData.put(EntityNames.CITY, this.city);
		eventData.put(EntityNames.TITLE, this.title);
		eventData.put(EntityNames.DESCRIPTION, this.description);
		eventData.put(EntityNames.LOCATION, this.location);
		eventData.put(EntityNames.TAGLINE, this.tagline);
		eventData.put(EntityNames.STARTDATE, ESSDateUtil.formatter.format(new Date(this.startDate.getTime())));
		eventData.put(EntityNames.PRICE, this.price);
		eventData.put(EntityNames.USERID, new Integer(this.userid).toString());
		eventData.put(EntityNames.IMAGEID, new Integer(this.imageid).toString());
		eventData.put(EntityNames.CREATEDATE, ESSDateUtil.formatTimeStamp(this.createDate));
		eventData.put(EntityNames.CREATEDBY, new Integer(this.createdBy).toString());
		eventData.put(EntityNames.CREATEDATE, ESSDateUtil.formatTimeStamp(this.lastModified));
		eventData.put(EntityNames.CREATEDBY, new Integer(this.modifiedBy).toString());		
		eventData.put(EntityNames.REMARKS, this.remarks);
		return eventData;
	}
}
