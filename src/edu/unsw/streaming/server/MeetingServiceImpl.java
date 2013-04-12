package edu.unsw.streaming.server;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.CommentBean;
import edu.unsw.streaming.bean.MeetingBean;
import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.client.event.NewCancelMeetingEvent;
import edu.unsw.streaming.client.event.NewMeetingEvent;
import edu.unsw.streaming.dao.ActivityDAOImpl;
import edu.unsw.streaming.dao.BelongDAOImpl;
import edu.unsw.streaming.google.GoogleAccess;
import edu.unsw.streaming.service.MeetingService;
/**
 * 
 * @author Charagh Jethnani
 *Business processes or services that a User can perform relating to Meetings
 *Note : Added push
 */
@SuppressWarnings("serial")
public class MeetingServiceImpl extends RemoteEventServiceServlet implements MeetingService {

	private static final Domain DOMAIN = DomainFactory.getDomain("Activity_Domain");
	
	@Override
	public List<MeetingBean> viewMeetings(int userID)
			throws IllegalArgumentException {
		List<Integer> subscribedGroups = null;
		try{
			subscribedGroups = BelongDAOImpl.retrieveSubscribedGroups(userID);
		}
		catch (Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
		List<MeetingBean> ret = new ArrayList<MeetingBean>();
		try{
			for(Integer groupID : subscribedGroups){
				ret.addAll(ActivityDAOImpl.retrieveMeetings(groupID));
			}
		}
		catch (Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
		
		return ret;
	}
	
	@Override
	public List<MeetingBean> viewScheduledMeetings(int userID)
			throws IllegalArgumentException {
		List<Integer> subscribedGroups = null;
		try{
			subscribedGroups = BelongDAOImpl.retrieveSubscribedGroups(userID);
		}
		catch (Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
		List<MeetingBean> ret = new ArrayList<MeetingBean>();
		try{
			for(Integer groupID : subscribedGroups){
				ret.addAll(ActivityDAOImpl.retrieveScheduledMeetings(groupID));
			}
		}
		catch (Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
		
		return ret;
	}

	@Override
	public boolean scheduleMeeting(Integer userID, Integer participantID,
			String title, String message, Date meetingStartDateTime,Date meetingEndDateTime) throws Exception {
		MeetingBean meeting = null;
		boolean ret = false;
		Integer meetingID = null;
		try {
			meeting = new MeetingBean(null, userID, participantID, title, message,null,null, meetingStartDateTime,meetingEndDateTime, "t",null);
			meetingID = ActivityDAOImpl.createMeeting(meeting);
			ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException(e.getMessage());
		}
		
		//Add NewActivityEvent for push notification
		MeetingBean newMeeting = (MeetingBean) ActivityDAOImpl.retrieveActivity(meetingID);
		this.addEvent(DOMAIN, new NewMeetingEvent(newMeeting.getId(), newMeeting.getUserID(), newMeeting.getParticipantID(),
				newMeeting.getTitle(), newMeeting.getMessage(), newMeeting.getTimeStamp(), newMeeting.getLastModified(), newMeeting.getMeetingStartDateTime(), 
				newMeeting.getMeetingEndDateTime(), newMeeting.getActive(), newMeeting.getGoogleId()));
		
		return ret;
	}

	@Override
	public boolean cancelMeeting(Integer userID, Integer meetingID)
			throws IllegalArgumentException {
		MeetingBean meeting = null;
		boolean ret = false;
		try{
			ActivityBean activity = ActivityDAOImpl.retrieveActivity(meetingID);
			if(activity instanceof MeetingBean){
				meeting = (MeetingBean) activity;
				meeting.setActive("f");
				ActivityDAOImpl.updateMeeting(meeting);
				//remove from google calendar
				CommentBean comment = new CommentBean(null,userID,"This meeting has been cancelled",null,null,meeting.getId());
				ActivityDAOImpl.createComment(comment);
				ret = true;
			}
		}
		catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		
		//Add NewActivityEvent for push notification
		this.addEvent(DOMAIN, new NewCancelMeetingEvent(userID, meetingID));
		
		return ret;
	}

	@Override
	public String generateCalendarHTML(String userName, String email) throws MalformedURLException {	
		System.out.println("1 = " + userName + " " + email);
		//System.out.println("2 = " + GoogleAccess.getCalendarFrame(groupName));
		//System.out.println("3 = " + GoogleAccess.getCalendarFrame("COMP9323"));
		return GoogleAccess.getCalendarSourceByUser(userName, email);
	}
	
}
