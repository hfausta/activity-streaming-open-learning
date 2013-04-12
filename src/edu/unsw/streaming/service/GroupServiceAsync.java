package edu.unsw.streaming.service;

import java.util.List;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.GroupBean;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GroupServiceAsync {
	void viewGroups(int userID, AsyncCallback<List<GroupBean>> callback);
	
	void createGroup(String name, int relatedGroup, AsyncCallback<Integer> callback);
	
	void addMembersToGroup(int groupID, List<Integer> userIDList, AsyncCallback<Boolean> callback);
	
	void removeMembersFromGroup(int groupID, List<Integer> userIDList, AsyncCallback<Boolean> callback);
	
	public void getMainGroups(List<GroupBean> groups, AsyncCallback<List<GroupBean>> callback);
	
	public void getSubGroups(List<GroupBean> groups, GroupBean mainGroup, AsyncCallback<List<GroupBean>> callback);
	
	public void getGroupByID(int groupID, AsyncCallback<GroupBean> callback);

	void getNewsFeed(int userID, AsyncCallback<List<ActivityBean>> callback);

	void createSubGroup(int groupID, String name, AsyncCallback<Boolean> callback);

	void getNewsFeedTop50(int userID, AsyncCallback<List<ActivityBean>> callback);

}
