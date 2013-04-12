package edu.unsw.streaming.service;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.CommentBean;
import edu.unsw.streaming.bean.GroupBean;

@RemoteServiceRelativePath("activityservice")
public interface ActivityService extends RemoteService {
	List<ActivityBean> viewActivities(int groupID)
			throws IllegalArgumentException;
	
	List<CommentBean> viewComments(Integer activityID) throws IllegalArgumentException;
	
	List<Integer> viewRatings(Integer activityID) throws IllegalArgumentException;

	boolean postActivity(Integer userID, Integer participantID, String title,
			String message) throws IllegalArgumentException, Exception;

	boolean postMaterial(Integer userID, Integer participantID, String title,
			String message, String link) throws IllegalArgumentException, Exception;

	boolean postAssignment(Integer userID, Integer participantID, String title,
			String message, Date due, double mark,String attachment) throws IllegalArgumentException, Exception;

	boolean postComment(Integer userID, String message, Integer relatedActivity)
			throws IllegalArgumentException, Exception;

	boolean thumbsUp(Integer userID, Integer relatedActivity)
			throws IllegalArgumentException, Exception;

	boolean thumbsDown(Integer userID, Integer relatedActivity)
			throws IllegalArgumentException, Exception;
	
	boolean hasRated(Integer userID, Integer relatedActivity) throws IllegalArgumentException, Exception;
	
	public static class Util {
		public static ActivityServiceAsync getInstance() {
			return GWT.create(ActivityService.class);
		}
	}

	void groupChat(GroupBean group, String userName, String message);
}
