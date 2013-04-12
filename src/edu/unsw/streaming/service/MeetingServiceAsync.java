package edu.unsw.streaming.service;

import java.util.Date;
import java.util.List;

import edu.unsw.streaming.bean.MeetingBean;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MeetingServiceAsync {
	void viewMeetings(int userID, AsyncCallback<List<MeetingBean>> callback);
	
	void cancelMeeting(Integer userID, Integer meetingID, AsyncCallback<Boolean> callback);

	void viewScheduledMeetings(int userID, AsyncCallback<List<MeetingBean>> callback);
	
	void scheduleMeeting(Integer userID, Integer participantID,
			String title, String message, Date meetingStartDateTime,
			Date meetingEndDateTime, AsyncCallback<Boolean> callback);

	void generateCalendarHTML(String userName, String email, AsyncCallback<String> callback);
}
