package edu.unsw.streaming.client.event;

import de.novanic.eventservice.client.event.Event;
import edu.unsw.streaming.bean.UserBean;

@SuppressWarnings("serial")
public class NewThumbsUpEvent implements Event {

	private Integer relatedActivity;
	private UserBean user;
	
	public NewThumbsUpEvent() {}
	
	//Push Thumbs Up Event
	public NewThumbsUpEvent(UserBean user, Integer relatedActivity) {
		this.user = user;
		this.relatedActivity = relatedActivity;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public Integer getRelatedActivity() {
		return relatedActivity;
	}

	public void setRelatedActivity(Integer relatedActivity) {
		this.relatedActivity = relatedActivity;
	}
	
}
