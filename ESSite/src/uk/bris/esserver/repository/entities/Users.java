package uk.bris.esserver.repository.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.bris.esserver.repository.constants.EntityNames;
import uk.bris.esserver.util.ESSDateUtil;

public class Users {

	private int id;	
	private String firstName;
	private String lastName;
	private String city;	
	private String email;
	private String contact;	
	private String role;
	private String postCode;	
	private String salt;
	private String password;		
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getImageid() {
		return imageid;
	}
	public void setImageid(int imageid) {
		this.imageid = imageid;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
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
	
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Map<String, Object> getUserAsMap(){
		Map<String, Object> usrData = new HashMap<String, Object>();
		usrData.put(EntityNames.ID, new Integer(this.id).toString());
		usrData.put(EntityNames.FIRSTNAME, this.firstName);
		usrData.put(EntityNames.LASTNAME, this.lastName);
		usrData.put(EntityNames.CITY, this.city);		
		usrData.put(EntityNames.EMAIL, this.email);
		usrData.put(EntityNames.CONTACT, this.contact);
		usrData.put(EntityNames.POSTCODE, this.postCode);		
		usrData.put(EntityNames.IMAGEID, new Integer(this.imageid).toString());
		usrData.put(EntityNames.CREATEDATE, ESSDateUtil.formatTimeStamp(this.createDate));
		usrData.put(EntityNames.CREATEDBY, new Integer(this.createdBy).toString());
		usrData.put(EntityNames.CREATEDATE, ESSDateUtil.formatTimeStamp(this.lastModified));
		usrData.put(EntityNames.CREATEDBY, new Integer(this.modifiedBy).toString());		
		usrData.put(EntityNames.REMARKS, this.remarks);
		return usrData;
	}

}
