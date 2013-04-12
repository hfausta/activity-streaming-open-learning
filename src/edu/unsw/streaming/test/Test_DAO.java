package edu.unsw.streaming.test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.BelongBean;
import edu.unsw.streaming.bean.CommentBean;
import edu.unsw.streaming.bean.GroupBean;
import edu.unsw.streaming.bean.MaterialBean;
import edu.unsw.streaming.bean.MeetingBean;
import edu.unsw.streaming.bean.RateBean;
import edu.unsw.streaming.bean.UserBean;
import edu.unsw.streaming.dao.ActivityDAOImpl;
import edu.unsw.streaming.dao.BelongDAOImpl;
import edu.unsw.streaming.dao.GroupDAOImpl;
import edu.unsw.streaming.dao.UserDAOImpl;


public class Test_DAO {

	@Test
	public void testUser() throws Exception {	
		UserBean charaghJ = new UserBean(null, "Charagh Jethnani", "charagh.jethnani@gmail.com", "123456");
		Integer id = UserDAOImpl.createUser(charaghJ);
		charaghJ.setPassword("23456");
		charaghJ.setId(id);
		UserDAOImpl.updateUser(charaghJ);
		charaghJ = UserDAOImpl.retrieveUser("charagh.jethnani@gmail.com");
		Assert.assertEquals(id,charaghJ.getId());
		Assert.assertEquals("charagh.jethnani@gmail.com",charaghJ.getEmail());
		Assert.assertEquals((Boolean)false,charaghJ.getIsAdmin());
		Assert.assertNotSame("23456",charaghJ.getPassword());
		UserDAOImpl.deleteUser(id);
		Assert.assertEquals(null, UserDAOImpl.retrieveUser("bla@gmail.com"));
		
	}
	
	@Test
	public void testGroup() throws Exception {	
		GroupBean comp9323 = new GroupBean(null, "COMP9323",null);
		Integer id = GroupDAOImpl.createGroup(comp9323);
		comp9323.setId(id);
		comp9323 =GroupDAOImpl.retrieveGroup("COMP9323");
		List<GroupBean> mainGroups = GroupDAOImpl.retrieveMainGroups();
		Assert.assertEquals(comp9323.getName(),mainGroups.get(0).getName());
		Assert.assertEquals(id,comp9323.getId());
		Assert.assertEquals("COMP9323",comp9323.getName());
		GroupBean group1 = new GroupBean(null,"COMP9323_Group 1", id);
		Integer groupId = GroupDAOImpl.createGroup(group1);
		List<GroupBean> subGroups = GroupDAOImpl.retrieveSubGroups(comp9323);
		GroupBean group1byId = GroupDAOImpl.retrieveGroupById(groupId);
		Assert.assertEquals(groupId, group1byId.getId());
		Assert.assertEquals(groupId,subGroups.get(0).getId());
		Assert.assertEquals(subGroups.get(0).getRelatedGroup(), id);
		Assert.assertEquals(subGroups.get(0).getName(), "COMP9323_Group 1"); //ISSUE HOW TO IDENTIFY GROUPS. FOR NOW GROUP NAMES HAVE TO BE UNIQUE
		List<GroupBean> groups = GroupDAOImpl.searchGroupsByName("COMP9323");
		Assert.assertEquals(2,groups.size());
		groups = GroupDAOImpl.searchMainGroupsByName("comp9323");
		Assert.assertEquals(1, groups.size());
		GroupDAOImpl.deleteGroup(groupId);
		GroupDAOImpl.deleteGroup(id);
	}
	@Test
	public void testBelong() throws Exception{
		UserBean charaghJ = new UserBean(null, "Charagh Jethnani", "charagh.jethnani@gmail.com", "123456");
		UserDAOImpl.createUser(charaghJ);
		UserBean rhendieA = new UserBean(null, "Rhendie", "rhendiea@gmail.com", "123456");
		UserDAOImpl.createUser(rhendieA);
		GroupBean comp9323 = new GroupBean(null, "COMP9323",null);
		Integer id = GroupDAOImpl.createGroup(comp9323);
		BelongDAOImpl.joinGroup(new BelongBean(charaghJ.getId(),"lecturer"),id);
		List<Integer> res = BelongDAOImpl.retrieveSubscribedGroups(charaghJ.getId());
		Assert.assertEquals(id, res.get(0));
		BelongDAOImpl.leaveGroup(charaghJ.getId(),id);
		BelongDAOImpl.joinGroup(new BelongBean(rhendieA.getId(),"mentor"),id);
		comp9323 = GroupDAOImpl.retrieveGroup(comp9323.getName());
		Assert.assertEquals(1, comp9323.getMembers().size());
		Assert.assertEquals("mentor", comp9323.getMembers().get(0).getRole());
		GroupDAOImpl.deleteGroup(id);//DUE TO CASCADE DELETES ALL BELONG ENTRIES
		UserDAOImpl.deleteUser(charaghJ.getId());
		UserDAOImpl.deleteUser(rhendieA.getId());
	}
	//CLEAR DATABASE AFTER THIS TEST
	@Test
	public void testActivity() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		UserBean charaghJ = new UserBean(null, "Charagh Jethnani", "charagh.jethnani@gmail.com", "123456");
		UserDAOImpl.createUser(charaghJ);
		UserBean rhendieA = new UserBean(null, "Rhendie", "rhendiea@gmail.com", "123456");
		UserDAOImpl.createUser(rhendieA);
		GroupBean comp9323 = new GroupBean(null, "COMP9323",null);
		Integer id = GroupDAOImpl.createGroup(comp9323);
		BelongDAOImpl.joinGroup(new BelongBean(charaghJ.getId(),"lecturer"),id);
		ActivityBean activity = new ActivityBean(null,charaghJ.getId(),id,"Notice","messages",sdf.parse("2012-01-01"),sdf.parse("2012-01-01"));
		ActivityDAOImpl.createActivity(activity);
		CommentBean comment = new CommentBean(null,charaghJ.getId(),"messages",sdf.parse("2012-01-02"),sdf.parse("2012-01-02"),activity.getId());
		ActivityDAOImpl.createComment(comment);
		activity = ActivityDAOImpl.retrieveActivity(activity.getId());
		Assert.assertEquals(new Timestamp(sdf.parse("2012-01-02").getTime()), activity.getLastModified());
		RateBean rate = new RateBean(null,charaghJ.getId(),null,null,activity.getId(),"f");
		ActivityDAOImpl.createRate(rate);
		MaterialBean material = new MaterialBean(null,charaghJ.getId(),id,"Notice","messages",null,null,"attachment");
		ActivityDAOImpl.createMaterial(material);
		MeetingBean meeting = new MeetingBean(null,charaghJ.getId(),id,"Notice","messages",null,null,sdf.parse("2020-01-01"),sdf.parse("2020-01-01"),"t",null);
		ActivityDAOImpl.createMeeting(meeting);
		List<ActivityBean> mainStreams = ActivityDAOImpl.retrieveMainStreams(id);
		//List<ActivityBean> activities = ActivityDAOImpl.retrieveActivitiesTo(comp9323.getId());
		Assert.assertEquals(3, mainStreams.size());
		List<Integer> getRatingforActivity = ActivityDAOImpl.getRating(activity.getId());
		Assert.assertEquals((Integer)0, getRatingforActivity.get(0));
		Assert.assertEquals((Integer)(-1), getRatingforActivity.get(1));
		List<Integer> getRatingForComment = ActivityDAOImpl.getRating(comment.getId());
		Assert.assertEquals((Integer)0, getRatingForComment.get(0));
		Assert.assertEquals((Integer)(0), getRatingForComment.get(0));
		List<CommentBean> comments = ActivityDAOImpl.retrieveComments(activity.getId());
		Assert.assertEquals( 1, comments.size());
		comments = ActivityDAOImpl.retrieveComments(100);
		Assert.assertEquals(0, comments.size());
		Assert.assertNull(ActivityDAOImpl.hasRated(100, activity.getId()));
		Assert.assertEquals("f", ActivityDAOImpl.hasRated(charaghJ.getId(), activity.getId()));
		ActivityBean retAct = ActivityDAOImpl.retrieveActivity(material.getId());
		if(retAct instanceof MaterialBean){
			MaterialBean mb = (MaterialBean) retAct;
			Assert.assertEquals("attachment", mb.getAttachment());
		}
		List<MeetingBean> meetings = ActivityDAOImpl.retrieveMeetings(id);
		Assert.assertEquals(1, meetings.size());
		meetings = ActivityDAOImpl.retrieveScheduledMeetings(id);
		Assert.assertEquals(1, meetings.size());
		meeting.setActive("f");
		ActivityDAOImpl.updateMeeting(meeting);
		meetings = ActivityDAOImpl.retrieveScheduledMeetings(id);
		Assert.assertEquals(0, meetings.size());
		meetings = ActivityDAOImpl.retrieveMeetings(id);
		Assert.assertEquals(1, meetings.size());
		Assert.assertEquals(3,ActivityDAOImpl.retrieveLastModifiedMainStreams(id, 10).size());
	}
}
