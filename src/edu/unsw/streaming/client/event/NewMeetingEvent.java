package edu.unsw.streaming.client.event;

import java.util.Date;

import de.novanic.eventservice.client.event.Event;

@SuppressWarnings("serial")
public class NewMeetingEvent implements Event {

	private Date startMeetingDate, endMeetingDate, timeStamp, lastModified;
	private Integer id, userID, participantID;
	private String active, googleID, message, title;
	
	public NewMeetingEvent() {}
	
	//Push Meeting Event
	public NewMeetingEvent(Integer id, Integer userID, Integer participantID,
			String title, String message, Date timeStamp,Date lastModified, Date startMeetingDate, Date endMeetingDate, String active, String googleID) {
		this.id = id;
		this.userID = userID;
		this.participantID = participantID;
		this.title = title;
		this.message = message;
		this.timeStamp = timeStamp;
		this.lastModified = lastModified;
		this.startMeetingDate = startMeetingDate;
		this.endMeetingDate = endMeetingDate;
		this.active = active;
		this.googleID = googleID;
	}

	public Date getStartMeetingDate() {
		return startMeetingDate;
	}

	public void setStartMeetingDate(Date startMeetingDate) {
		this.startMeetingDate = startMeetingDate;
	}

	public Date getEndMeetingDate() {
		return endMeetingDate;
	}

	public void setEndMeetingDate(Date endMeetingDate) {
		this.endMeetingDate = endMeetingDate;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getGoogleID() {
		return googleID;
	}

	public void setGoogleID(String googleID) {
		this.googleID = googleID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
