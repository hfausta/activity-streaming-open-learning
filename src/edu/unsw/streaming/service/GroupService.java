package edu.unsw.streaming.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.GroupBean;

@RemoteServiceRelativePath("groupservice")
public interface GroupService extends RemoteService {
	List<GroupBean> viewGroups(int userID) throws IllegalArgumentException;
	
	int createGroup(String name, int relatedGroup) throws IllegalArgumentException, Exception;
	
	boolean addMembersToGroup(int groupID, List<Integer> userIDList) throws IllegalArgumentException;
	
	boolean removeMembersFromGroup(int groupID, List<Integer> userIDList) throws IllegalArgumentException;
	
	public List<GroupBean> getMainGroups(List<GroupBean> groups);
	
	public List<GroupBean> getSubGroups(List<GroupBean> groups, GroupBean mainGroup);
	
	public GroupBean getGroupByID(int groupID) throws IllegalArgumentException;

	List<ActivityBean> getNewsFeed(int userID) throws IllegalArgumentException;

	boolean createSubGroup(int groupID, String name)
			throws IllegalArgumentException;

	List<ActivityBean> getNewsFeedTop50(int userID)
			throws IllegalArgumentException;

}
