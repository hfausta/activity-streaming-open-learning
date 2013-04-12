package edu.unsw.streaming.server;

import java.util.Date;
import java.util.List;

import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.AssignmentBean;
import edu.unsw.streaming.bean.CommentBean;
import edu.unsw.streaming.bean.GroupBean;
import edu.unsw.streaming.bean.MaterialBean;
import edu.unsw.streaming.bean.RateBean;
import edu.unsw.streaming.bean.UserBean;
import edu.unsw.streaming.client.event.NewActivityEvent;
import edu.unsw.streaming.client.event.NewAssignmentEvent;
import edu.unsw.streaming.client.event.NewCommentEvent;
import edu.unsw.streaming.client.event.NewGroupChatEvent;
import edu.unsw.streaming.client.event.NewMaterialEvent;
import edu.unsw.streaming.client.event.NewThumbsDownEvent;
import edu.unsw.streaming.client.event.NewThumbsUpEvent;
import edu.unsw.streaming.dao.ActivityDAOImpl;
import edu.unsw.streaming.dao.UserDAOImpl;
import edu.unsw.streaming.service.ActivityService;
/**
 * 
 * @author Charagh Jethnani
 *Business processes or services that a User can perform relating to Activity
 *Note : Added push
 */
@SuppressWarnings("serial")
public class ActivityServiceImpl extends RemoteEventServiceServlet implements ActivityService{

	private static final Domain DOMAIN = DomainFactory.getDomain("Activity_Domain");
	
	@Override
	public List<ActivityBean> viewActivities(int participantID)
			throws IllegalArgumentException {
		List<ActivityBean> activities = null;
		
		try{
			activities = ActivityDAOImpl.retrieveMainStreams(participantID);
		}
		catch (Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		return activities;
	}

	@Override
	public boolean postActivity(Integer userID, Integer participantID,
			String title, String message) throws Exception {
		Boolean ret = false;
		Integer activityID = null;
		ActivityBean activity = new ActivityBean(null, userID, participantID, title, message, null,null);
		try{
			activityID = ActivityDAOImpl.createActivity(activity);
			ret = true;
		}
		catch (Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		
		//Add NewActivityEvent for push notification
		ActivityBean newActivity = ActivityDAOImpl.retrieveActivity(activityID);
		this.addEvent(DOMAIN, new NewActivityEvent(newActivity));
		return ret;
	}

	@Override
	public boolean postMaterial(Integer userID, Integer participantID,
			String title, String message, String link)
			throws Exception {
		Boolean ret = false;
		Integer materialID = null;
		MaterialBean material = new MaterialBean(null, userID, participantID, title, message, null,null,link);
		try{
			materialID = ActivityDAOImpl.createMaterial(material);
			ret = true;
		}
		catch (Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		
		//Add NewActivityEvent for push notification
		MaterialBean newMaterial = (MaterialBean) ActivityDAOImpl.retrieveActivity(materialID);
		this.addEvent(DOMAIN, new NewMaterialEvent(newMaterial));
		
		return ret;
	}

	@Override
	public boolean postAssignment(Integer userID, Integer participantID,
			String title, String message, Date due, double mark,String attachment)
			throws Exception {
		Boolean ret = false;
		Integer assignmentID = null;
		AssignmentBean assignment = new AssignmentBean(null, userID, participantID, title, message, null,null, due, mark,attachment,null);
		try{
			assignmentID = ActivityDAOImpl.createAssignment(assignment);
			ret = true;
		}
		catch (Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		
		//Add NewActivityEvent for push notification
		AssignmentBean newAssignment = (AssignmentBean)ActivityDAOImpl.retrieveActivity(assignmentID); 
		this.addEvent(DOMAIN, new NewAssignmentEvent(newAssignment.getId(), newAssignment.getUserID(), newAssignment.getParticipantID(),
				newAssignment.getTitle(), newAssignment.getMessage(), newAssignment.getTimeStamp(), newAssignment.getLastModified(), 
				newAssignment.getDueDate(), newAssignment.getFullMark(), newAssignment.getAttachment(), newAssignment.getGoogleId()));
		
		return ret;
	}

	@Override
	public boolean postComment(Integer userID, String message,
			Integer relatedActivity) throws Exception {
		Boolean ret = false;
		CommentBean comment = new CommentBean(null, userID, message, null,null, relatedActivity);
		try{
			ActivityDAOImpl.createComment(comment);
			ret = true;
		}
		catch (Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		
		UserBean user = UserDAOImpl.retrieveUserById(userID);
		//Add NewActivityEvent for push notification
		this.addEvent(DOMAIN, new NewCommentEvent(user, message, relatedActivity, comment.getTimeStamp().toString()));
		
		return ret;
	}

	@Override
	public boolean thumbsUp(Integer userID, Integer relatedActivity)
			throws Exception {
		Boolean ret = false;
		RateBean rate = new RateBean(null,userID,null,null,relatedActivity,"t");
		try{
			ActivityDAOImpl.createRate(rate);
			ret = true;
		}
		catch (Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		
		UserBean user = UserDAOImpl.retrieveUserById(userID);
		//Add NewActivityEvent for push notification
		this.addEvent(DOMAIN, new NewThumbsUpEvent(user, relatedActivity));
		
		return ret;
	}

	@Override
	public boolean thumbsDown(Integer userID, Integer relatedActivity)
			throws Exception {
		Boolean ret = false;
		RateBean rate = new RateBean(null,userID,null,null,relatedActivity,"f");
		try{
			ActivityDAOImpl.createRate(rate);
			ret = true;
		}
		catch (Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		
		UserBean user = UserDAOImpl.retrieveUserById(userID);
		//Add NewActivityEvent for push notification
		this.addEvent(DOMAIN, new NewThumbsDownEvent(user, relatedActivity));
				
		return ret;
	}

	@Override
	public List<CommentBean> viewComments(Integer activityID)
			throws IllegalArgumentException {
		try {
			return ActivityDAOImpl.retrieveComments(activityID);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public List<Integer> viewRatings(Integer activityID)
			throws IllegalArgumentException {
		try {
			return ActivityDAOImpl.getRating(activityID);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public boolean hasRated(Integer userID, Integer relatedActivity)
			throws IllegalArgumentException, Exception {
		try {
			String hasrated = ActivityDAOImpl.hasRated(userID, relatedActivity);
			if (hasrated != null && (hasrated.equals("t") || hasrated.equals("f"))) return true;
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return false;
	}

	@Override
	public void groupChat(GroupBean group, String userName, String message) {
		this.addEvent(DOMAIN, new NewGroupChatEvent(group.getId(), userName, message));
	}
}
