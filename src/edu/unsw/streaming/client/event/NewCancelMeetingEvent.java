package edu.unsw.streaming.client.event;

import de.novanic.eventservice.client.event.Event;

@SuppressWarnings("serial")
public class NewCancelMeetingEvent implements Event {

	private Integer userID, meetingID;
	
	public NewCancelMeetingEvent() {}
	
	//Push Cancel Meeting Event
	public NewCancelMeetingEvent(Integer userID, Integer meetingID) {
		this.userID = userID;
		this.meetingID = meetingID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public Integer getMeetingID() {
		return meetingID;
	}

	public void setMeetingID(Integer meetingID) {
		this.meetingID = meetingID;
	}
	
}