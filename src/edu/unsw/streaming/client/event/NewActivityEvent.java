package edu.unsw.streaming.client.event;

import de.novanic.eventservice.client.event.Event;
import edu.unsw.streaming.bean.ActivityBean;

@SuppressWarnings("serial")
public class NewActivityEvent implements Event {

	private ActivityBean activity;
	
	public NewActivityEvent() {}
	
	//Push Activity Event
	public NewActivityEvent(ActivityBean activity) {
		this.activity = activity;
	}

	public ActivityBean getActivity() {
		return activity;
	}

	public void setActivity(ActivityBean activity) {
		this.activity = activity;
	}
	
}
