package edu.unsw.streaming.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.Validate;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.BelongBean;
import edu.unsw.streaming.bean.GroupBean;
import edu.unsw.streaming.dao.ActivityDAOImpl;
import edu.unsw.streaming.dao.BelongDAOImpl;
import edu.unsw.streaming.dao.GroupDAOImpl;
import edu.unsw.streaming.service.GroupService;
/**
 * 
 * @author Charagh Jethnani
 *Business processes or services that a User can perform relating to Group
 */
@SuppressWarnings("serial")
public class GroupServiceImpl extends RemoteServiceServlet implements GroupService {

	@Override
	public List<GroupBean> viewGroups(int userID)
			throws IllegalArgumentException {
		List<Integer> subscribedGroups = null;
		try{
			subscribedGroups = BelongDAOImpl.retrieveSubscribedGroups(userID);
		} catch (Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
		List<GroupBean> ret = new ArrayList<GroupBean>();
		try{
			for(Integer groupID : subscribedGroups){
				ret.add(GroupDAOImpl.retrieveGroupById(groupID));
			}
		} catch (Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
		System.out.println(ret.size() + " groups");
		return ret;
	}

	@Override
	public int createGroup(String name, int relatedGroup)
			throws Exception {
		Validate.notNull(name,"Group name can't be null");
		GroupBean group = new GroupBean(null, name, relatedGroup);
		int ret = 0;
		try{
			ret = GroupDAOImpl.createGroup(group);
		}
		catch (Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
		return ret;
	}

	@Override
	public boolean addMembersToGroup(int groupID, List<Integer> userIDList)
			throws IllegalArgumentException {
		Validate.notNull(userIDList,"Group name can't be null");
		boolean ret = false;
		try{
			for(Integer userID : userIDList){
				BelongDAOImpl.joinGroup(new BelongBean(userID, "student"), groupID);
			}
			ret = true;
		}
		catch(Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
		return ret;
	}

	@Override
	public boolean removeMembersFromGroup(int groupID, List<Integer> userIDList)
			throws IllegalArgumentException {
		Validate.notNull(userIDList,"Group name can't be null");
		boolean ret = false;
		try{
			for(Integer userID : userIDList){
				BelongDAOImpl.leaveGroup(userID,groupID);
			}
			ret = true;
		}
		catch(Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
		return ret;
	}
	
	public List<GroupBean> getMainGroups(List<GroupBean> groups){
		List<GroupBean> mainGroups = new ArrayList<GroupBean>();
		for(GroupBean group : groups){
			if(group.getRelatedGroup() == 0){
				mainGroups.add(group);
			}
		}
		return mainGroups;
	}
	
	public List<GroupBean> getSubGroups(List<GroupBean> groups, GroupBean mainGroup){
		List <GroupBean> subGroups = new ArrayList<GroupBean>();
		for(GroupBean group : groups){
			if(group.getRelatedGroup().equals(mainGroup.getId())){
				subGroups.add(group);
			}
		}
		return subGroups;
	}

	@Override
	public GroupBean getGroupByID(int groupID) throws IllegalArgumentException {
		try {
			return GroupDAOImpl.retrieveGroupById(groupID);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	@Override
	public List<ActivityBean> getNewsFeed(int userID) throws IllegalArgumentException{
		List<GroupBean> groups = viewGroups(userID);
		List<ActivityBean> activities = new ArrayList<ActivityBean>();
		for(GroupBean group : groups){
			try {
				activities.addAll(ActivityDAOImpl.retrieveLastModifiedMainStreams(group.getId()));
			} catch (Exception e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		Collections.sort(activities, new Comparator<ActivityBean>(){
		    public int compare(ActivityBean a1, ActivityBean a2)
		    {
		        return a2.getLastModified().compareTo(a1.getLastModified());
		    } });
		
		return activities;
	}
	
	@Override
	public List<ActivityBean> getNewsFeedTop50(int userID) throws IllegalArgumentException{
		List<GroupBean> groups = viewGroups(userID);
		List<ActivityBean> activities = new ArrayList<ActivityBean>();
		for(GroupBean group : groups){
			try {
				activities.addAll(ActivityDAOImpl.retrieveLastModifiedMainStreams(group.getId(),50));
			} catch (Exception e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		Collections.sort(activities, new Comparator<ActivityBean>(){
		    public int compare(ActivityBean a1, ActivityBean a2)
		    {
		        return a2.getLastModified().compareTo(a1.getLastModified());
		    } });
		List<ActivityBean> ret = new ArrayList<ActivityBean>();
		for(int i =0; (i < activities.size() && i<50);i++){
			ret.add(activities.get(i));
		}
		return ret;
	}
	
	@Override
	public boolean createSubGroup(int groupID, String name) throws IllegalArgumentException{
		boolean ret = false;
		GroupBean mainGroup = null;
		try{
			mainGroup = GroupDAOImpl.retrieveGroupById(groupID);
			if(mainGroup.getRelatedGroup().equals(0)){
				try {
					GroupDAOImpl.createGroup(new GroupBean(null, mainGroup.getName()+"_"+name, mainGroup.getId()));
					ret = true;
				}
				catch (Exception ex){
					throw new IllegalArgumentException(ex.getMessage());
				}
			}
			else{
				throw new IllegalArgumentException("Sub groups can not belong to sub groups");
			}
		}
		catch (Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
		
		return ret;
	}
}
