package edu.unsw.streaming.service;

import java.util.Date;
import java.util.List;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.CommentBean;
import edu.unsw.streaming.bean.GroupBean;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ActivityServiceAsync {
	void viewActivities(int groupID, AsyncCallback<List<ActivityBean>> callback);
	
	void viewComments(Integer activityID, AsyncCallback<List<CommentBean>> callback);
	
	void viewRatings(Integer activityID, AsyncCallback<List<Integer>> callback);

	void postActivity(Integer userID, Integer participantID, String title,
			String message, AsyncCallback<Boolean> callback);

	void postMaterial(Integer userID, Integer participantID, String title,
			String message, String link, AsyncCallback<Boolean> callback);

	void postAssignment(Integer userID, Integer participantID, String title,
			String message, Date due, double mark,String attachment, AsyncCallback<Boolean> callback);

	void postComment(Integer userID, String message, Integer relatedActivity, AsyncCallback<Boolean> callback);

	void thumbsUp(Integer userID, Integer relatedActivity, AsyncCallback<Boolean> callback);

	void thumbsDown(Integer userID, Integer relatedActivity, AsyncCallback<Boolean> callback);
	
	void hasRated(Integer userID, Integer relatedActivity, AsyncCallback<Boolean> callback);
	
	void groupChat(GroupBean group, String userName, String message, AsyncCallback<Void> callback);
}
