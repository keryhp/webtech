package uk.bris.esserver.repository.entities;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.util.ESSDateUtil;

public class Comment {

	private int id;	
	private String review;	
	private int userid;
	private int eventid;	
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
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getEventid() {
		return eventid;
	}
	public void setEventid(int eventid) {
		this.eventid = eventid;
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
	
	public Map<String, Object> getCommentAsMap(String email){
		Map<String, Object> commentData = new HashMap<String, Object>();
		commentData.put(EntityNames.ID, new Integer(this.id).toString());
		if(email != null){
			commentData.put(EntityNames.USERID, email);
		}else{
			commentData.put(EntityNames.USERID, new Integer(this.userid).toString());			
		}
		commentData.put(EntityNames.EVENTID, new Integer(this.eventid).toString());
		commentData.put(EntityNames.REVIEW, this.review);
		commentData.put(EntityNames.CREATEDATE, ESSDateUtil.formatTimeStamp(this.createDate));
		commentData.put(EntityNames.CREATEDBY, new Integer(this.createdBy).toString());
		commentData.put(EntityNames.CREATEDATE, ESSDateUtil.formatTimeStamp(this.lastModified));
		commentData.put(EntityNames.CREATEDBY, new Integer(this.modifiedBy).toString());		
		commentData.put(EntityNames.REMARKS, this.remarks);
		return commentData;
	}
}
