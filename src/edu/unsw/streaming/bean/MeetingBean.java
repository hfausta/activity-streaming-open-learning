package edu.unsw.streaming.bean;

import java.io.Serializable;
import java.util.Date;

import edu.unsw.streaming.shared.Validate;

@SuppressWarnings("serial")
/**
 * 
 * @author Charagh Jethnani
 *represents a creation of a meeting ( via google hangout or in person). It is an implementation of an Activity
 */
public class MeetingBean extends ActivityBean implements Serializable {

	private Date meetingStartDateTime;
	private Date meetingEndDateTime;
	private String active;
	private String googleId;
	public MeetingBean() {
		super();
	}
	
	public MeetingBean(Integer id, Integer userID, Integer participantID,
			String title, String message, Date timeStamp,Date lastModified, Date startmeetingDate, Date endMeetingDateTime, String active, String googleId) throws Exception {
		super(id, userID, participantID, title, message, timeStamp,lastModified);
		// TODO Auto-generated constructor stub
		Validate.notNull(startmeetingDate,"meeting must have a start date and time");
		//Validate.isAfterDate(meetingDate,"meeting must have future date and time");
		this.meetingStartDateTime = startmeetingDate;
		Validate.notNull(endMeetingDateTime, "meeting must have an end date and time");
		this.meetingEndDateTime = endMeetingDateTime ;
		this.active = active;
		Validate.notNull(active, "Meeting must have an active status");
		Validate.isRate(active, "Active must be either t or f");
		this.googleId = googleId;
	}

	public Date getMeetingStartDateTime() {
		return meetingStartDateTime;
	}

	public void setMeetingStartDateTime(Date meetingDate) throws Exception {
		Validate.notNull(meetingDate,"meeting must have date and time");
		//Validate.isAfterDate(meetingDate,"meeting must have future date and time");
		this.meetingStartDateTime = meetingDate;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) throws Exception {
		Validate.notNull(active, "Meeting must have an active status");
		Validate.isRate(active, "Active must be either t or f");
		this.active = active;
	}

	public Date getMeetingEndDateTime() {
		return meetingEndDateTime;
	}

	public void setMeetingEndDateTime(Date meetingEndDateTime) {
		this.meetingEndDateTime = meetingEndDateTime;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	
}
