package edu.unsw.streaming.client.event;

import de.novanic.eventservice.client.event.Event;

@SuppressWarnings("serial")
public class NewGroupChatEvent implements Event {

	private Integer groupID;
	private String userName;
	private String message;
	
	public NewGroupChatEvent() {}
	
	public NewGroupChatEvent(Integer groupID, String userName, String message) {
		this.groupID = groupID;
		this.userName = userName;
		this.message = message;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
