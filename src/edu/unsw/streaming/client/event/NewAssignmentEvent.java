package edu.unsw.streaming.client.event;

import java.util.Date;

import de.novanic.eventservice.client.event.Event;

@SuppressWarnings("serial")
public class NewAssignmentEvent implements Event {
	
	private Date timeStamp, dueDate, lastModified;
	private double fullMark;
	private Integer id, userID, participantID;
	private String attachment, title, message, googleID;
	
	public NewAssignmentEvent() {}
	
	//Push Assignment Event
	public NewAssignmentEvent(Integer id, Integer userID, Integer participantID,
			String title, String message, Date timeStamp, Date lastModified, Date dueDate, Double fullMark, String attachment,String googleID) {
		this.id = id;
		this.userID = userID;
		this.participantID = participantID;
		this.title = title;
		this.message = message;
		this.timeStamp = timeStamp;
		this.lastModified = lastModified;
		this.dueDate = dueDate;
		this.fullMark = fullMark;
		this.attachment = attachment;
		this.googleID = googleID;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public double getFullMark() {
		return fullMark;
	}

	public void setFullMark(double fullMark) {
		this.fullMark = fullMark;
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

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public Integer getParticipantID() {
		return participantID;
	}

	public void setParticipantID(Integer participantID) {
		this.participantID = participantID;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGoogleID() {
		return googleID;
	}

	public void setGoogleID(String googleID) {
		this.googleID = googleID;
	}
}
