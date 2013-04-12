package edu.unsw.streaming.service;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.unsw.streaming.bean.MeetingBean;

@RemoteServiceRelativePath("meetingservice")
public interface MeetingService extends RemoteService {
	List<MeetingBean> viewMeetings(int userID) throws IllegalArgumentException;
	
	boolean cancelMeeting(Integer userID, Integer meetingID) throws IllegalArgumentException;

	List<MeetingBean> viewScheduledMeetings(int userID)
			throws IllegalArgumentException;
	
	public static class Util {
		public static MeetingServiceAsync getInstance() {
			return GWT.create(MeetingService.class);
		}
	}

	boolean scheduleMeeting(Integer userID, Integer participantID,
			String title, String message, Date meetingStartDateTime,
			Date meetingEndDateTime) throws IllegalArgumentException, Exception;

	String generateCalendarHTML(String userName, String email) throws Exception;
}
