package uk.bris.esserver.repository.entities;

import java.io.InputStream;
import java.sql.Timestamp;

public class Photos {

	private int id;	
	//private InputStream image;
	private byte[] image;
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
/*	public InputStream getImage() {
		return image;
	}
	public void setImage(InputStream image) {
		this.image = image;
	}*/
	
	public Timestamp getCreateDate() {
		return createDate;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
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
}
