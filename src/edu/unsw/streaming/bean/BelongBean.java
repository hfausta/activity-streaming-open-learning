package edu.unsw.streaming.bean;

import java.io.Serializable;

import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *represents the Role of a user in a particular Group
 */
@SuppressWarnings("serial")
public class BelongBean implements Serializable {

	private Integer userID;
	private String role;
	
	public BelongBean() {}
	
	public BelongBean(Integer userID, String role) throws Exception {
		this.userID = userID;
		this.role = role;
		Validate.notNull(userID,"Assignment must have a due date");
		Validate.notNull(role,"Assignment must have a full mark");
		Validate.isRole(role,"Role is invalid");
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) throws Exception {
		Validate.notNull(userID,"Assignment must have a due date");
		this.userID = userID;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) throws Exception {
		Validate.notNull(role,"Assignment must have a full mark");
		Validate.isRole(role,"Role is invalid");
		this.role = role;
	}
	
	
}
