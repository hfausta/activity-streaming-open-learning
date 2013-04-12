package edu.unsw.streaming.client.event;

import de.novanic.eventservice.client.event.Event;
import edu.unsw.streaming.bean.UserBean;

@SuppressWarnings("serial")
public class NewCommentEvent implements Event {
	
	private Integer relatedActivity;
	private String message, timeStamp;
	private UserBean user;
	
	public NewCommentEvent() {}
	
	//Push Comment Event
	public NewCommentEvent(UserBean user, String message, Integer relatedActivity, String timeStamp) {
		this.user = user;
		this.message = message;
		this.relatedActivity = relatedActivity;
		this.timeStamp = timeStamp;
	}

	public UserBean getUserBean() {
		return user;
	}

	public void setUserBean(UserBean user) {
		this.user = user;
	}

	public Integer getRelatedActivity() {
		return relatedActivity;
	}

	public void setRelatedActivity(Integer relatedActivity) {
		this.relatedActivity = relatedActivity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
