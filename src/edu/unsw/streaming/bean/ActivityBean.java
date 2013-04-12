package edu.unsw.streaming.bean;

import java.io.Serializable;
import java.util.Date;

import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *represents an Activity Stream in the website 
 */
@SuppressWarnings("serial")
public class ActivityBean implements Serializable {
	protected Integer id;
	protected Integer userID;
	protected Integer participantID;
	protected String title;
	protected String message;
	protected Date timeStamp;
	protected Date lastModified;
	public ActivityBean() {}
	
	public ActivityBean(Integer id, Integer userID, Integer participantID,
			String title, String message,Date timeStamp,Date lastModified) throws Exception {
		this.id = id;
		this.message = message;
		this.participantID = participantID;
		this.title = title;
		this.userID = userID;
		if(timeStamp == null){
			this.timeStamp = new Date();
		}
		else{
			this.timeStamp = timeStamp;
		}
		if(lastModified == null){
			this.lastModified = timeStamp;
		}
		else{
			this.lastModified = lastModified;
		}
		//Validate.notNull(message,"Activity must have a message");
		//Validate.notNull(participantID,"Activity must have a recipient");
		//Validate.notNull(title,"Activity must have a title");
		//Validate.notNull(userID,"Activity must have a sender");
		
	}
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) throws Exception {
		Validate.notNull(userID,"Activity must have a sender");
		this.userID = userID;
	}
	public Integer getParticipantID() {
		return participantID;
	}
	public void setParticipantID(Integer participantID) throws Exception {
		Validate.notNull(participantID,"Activity must have a recipient");
		this.participantID = participantID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) throws Exception {
		Validate.notNull(title,"Activity must have a title");
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) throws Exception {
		Validate.notNull(message,"Activity must have a message");
		this.message = message;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(Date timeStamp) {
		if(timeStamp == null){
			this.timeStamp = new Date();
		}
		else{
			this.timeStamp = timeStamp;
		}
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	
}
