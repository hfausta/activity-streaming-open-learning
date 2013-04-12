package edu.unsw.streaming.test;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.AssignmentBean;
import edu.unsw.streaming.bean.CommentBean;
import edu.unsw.streaming.bean.MaterialBean;
import edu.unsw.streaming.bean.MeetingBean;
import edu.unsw.streaming.bean.RateBean;


public class Test_ActivityBean {

	@Test
	public void test_Inheritance() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<ActivityBean> activities = new ArrayList<ActivityBean>();
		activities.add(new ActivityBean(1, 1, 1, "Knightlife", "Frozen", null,null));
		activities.add(new MeetingBean(1, 1, 1, "meeting", "Let's meet and greet", null,null, sdf.parse("2012-10-01"),sdf.parse("2012-10-01"),"t",null));
		activities.add(new AssignmentBean(1, 1, 1, "Assignment 1", "You'll definitely fail", null,null, new Date(), 10D,"attachment",null));
		activities.add(new MaterialBean(1, 1, 1, "Material", "Lecture 1", null,null, "C:\\BLA"));
		activities.add(new CommentBean(1,1,"message",null,null,1));
		activities.add(new RateBean(1,1,null,null,1,"f"));
		
		if(activities.get(0) instanceof MeetingBean){
			throw new Exception("Error");
		}
		if(activities.get(1) instanceof MeetingBean){
			MeetingBean meeting = (MeetingBean)activities.get(1);
			Assert.assertEquals(sdf.parse("2012-10-01").toString(), meeting.getMeetingStartDateTime().toString());
		}
		if (!(activities.get(2) instanceof AssignmentBean)){
			throw new Exception("AssignmentBean not detected");
		}
	}

}
