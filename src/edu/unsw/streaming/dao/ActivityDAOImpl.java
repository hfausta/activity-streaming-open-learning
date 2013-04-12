package edu.unsw.streaming.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.unsw.streaming.bean.*;
import edu.unsw.streaming.shared.Validate;
import edu.unsw.streaming.google.GoogleAccess;
/**
 * 
 * @author Charagh Jethnani
 *ActivityDAOImpl used to access (retrieve and store) Activity information from the database
 */
public class ActivityDAOImpl {
	/**
	 * Function that creates an Activity and stores it into the database
	 * @param activity
	 * @return Integer representing the ID of the Activity
	 * @throws Exception
	 */
	public static Integer createActivity(ActivityBean activity) throws Exception{
		Connection conn = null;
		Integer ret = null;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(activity, "Activity can't be null");
			Validate.notNull(activity.getMessage(),"Activity must have message");
			Validate.notNull(activity.getParticipantID(),"Activity must have recipient");
			Validate.notNull(activity.getUserID(),"Activity must have sender");
			Validate.notNull(activity.getTitle(),"Activity must have title");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("INSERT INTO Activity(userId,participantId,title,message,timeposted,lastmodified) VALUES (?,?,?,?,?,?) returning id;");
					stmt.setInt(1, activity.getUserID());
					stmt.setInt(2,activity.getParticipantID());
					stmt.setString(3, activity.getTitle());
					stmt.setString(4, activity.getMessage());
					stmt.setTimestamp(5, new Timestamp(activity.getTimeStamp().getTime()));
					stmt.setTimestamp(6,new Timestamp(activity.getTimeStamp().getTime()));
					rs = stmt.executeQuery();
					rs.next();
					ret = rs.getInt(1);
					activity.setId(ret);
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return ret;		
	}
	/**
	 * Function that creates a Comment and stores it into the database
	 * @param comment
	 * @return an Integer representing it's ID
	 * @throws Exception
	 */
	public static Integer createComment(CommentBean comment) throws Exception{
		Connection conn = null;
		Integer ret = null;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(comment, "Comment can't be null");
			Validate.notNull(comment.getUserID(), "Comment must have a sender");
			Validate.notNull(comment.getMessage(), "Comment must have a message");
			Validate.notNull(comment.getRelatedActivity(), "Comment must have related activity");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("INSERT INTO Activity(userId,message,timeposted,relatedActivity,lastmodified) VALUES (?,?,?,?,?) returning id;");
					stmt.setInt(1, comment.getUserID());
					stmt.setString(2, comment.getMessage());
					stmt.setTimestamp(3, new Timestamp(comment.getTimeStamp().getTime()));
					stmt.setInt(4, comment.getRelatedActivity());
					stmt.setTimestamp(5, new Timestamp(comment.getTimeStamp().getTime()));
					rs = stmt.executeQuery();
					rs.next();
					ret = rs.getInt(1);
					comment.setId(ret);
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
					ActivityBean activity = retrieveActivity(comment.getRelatedActivity());
					activity.setLastModified(comment.getTimeStamp());
					updateLastModified(activity);
				}
			}
		}
		return ret;		
	}
	/**
	 * Function that creates an Material and stores it into the database
	 * @param material
	 * @return an Integer representing it's ID
	 * @throws Exception
	 */
	public static Integer createMaterial(MaterialBean material) throws Exception{
		Connection conn = null;
		Integer ret = null;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(material, "Material can't be null");
			Validate.notNull(material.getMessage(),"material must have message");
			Validate.notNull(material.getParticipantID(),"material must have recipient");
			Validate.notNull(material.getUserID(),"material must have sender");
			Validate.notNull(material.getTitle(),"material must have title");
			Validate.notNull(material.getAttachment(),"material must have attachment");
			
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("INSERT INTO Activity(userId,participantId,title,message,timeposted,attachment,lastmodified) VALUES (?,?,?,?,?,?,?) returning id;");
					stmt.setInt(1, material.getUserID());
					stmt.setInt(2,material.getParticipantID());
					stmt.setString(3, material.getTitle());
					stmt.setString(4, material.getMessage());
					stmt.setTimestamp(5, new Timestamp(material.getTimeStamp().getTime()));
					stmt.setString(6, material.getAttachment());
					stmt.setTimestamp(7, new Timestamp(material.getTimeStamp().getTime()));
					rs = stmt.executeQuery();
					rs.next();
					ret = rs.getInt(1);
					material.setId(ret);
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return ret;		
	}
	/**
	 * Function that creates an Meeting and stores it into the database
	 * @param meeting
	 * @return an Integer representing it's ID
	 * @throws Exception
	 */	
	public static Integer createMeeting(MeetingBean meeting) throws Exception{
		Connection conn = null;
		Integer ret = null;
		try {
			Validate.notNull(meeting, "meeting can't be null");
			Validate.notNull(meeting, "meeting can't be null");
			Validate.notNull(meeting.getMessage(),"meeting must have message");
			Validate.notNull(meeting.getParticipantID(),"meeting must have recipient");
			Validate.notNull(meeting.getUserID(),"meeting must have sender");
			Validate.notNull(meeting.getTitle(),"meeting must have title");
			Validate.notNull(meeting.getMeetingStartDateTime(),"meeting must have appointed start date/time");
			Validate.notNull(meeting.getMeetingEndDateTime(),"meeting must have appointed end date/time");
			
			//Add Meeting to Google Calendar
			GroupBean currGroup = GroupDAOImpl.retrieveGroupById(meeting.getParticipantID());
			ArrayList<BelongBean> members = currGroup.getMembers();
			ArrayList<String> emails = new ArrayList<String>();
			for (int i = 0; i < members.size(); i++) {
				emails.add(UserDAOImpl.retrieveUserById(members.get(i).getUserID()).getEmail());
			}
			
			meeting.setGoogleId(GoogleAccess.createEvent(currGroup.getName(), meeting.getTitle(), meeting.getMessage(), meeting.getMeetingStartDateTime(), 
					"Online Meeting", meeting.getMeetingEndDateTime(), emails));
			conn = JDBC.getConnection();
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("INSERT INTO Activity(userId,participantId,title,message,timeposted,meetingStartDateTime,meetingEndDateTime,active,lastmodified,googleId) VALUES (?,?,?,?,?,?,?,?,?,?) returning id;");
					stmt.setInt(1, meeting.getUserID());
					stmt.setInt(2,meeting.getParticipantID());
					stmt.setString(3, meeting.getTitle());
					stmt.setString(4, meeting.getMessage());
					stmt.setTimestamp(5, new Timestamp(meeting.getTimeStamp().getTime()));
					stmt.setTimestamp(6, new Timestamp(meeting.getMeetingStartDateTime().getTime()));
					stmt.setTimestamp(7, new Timestamp(meeting.getMeetingEndDateTime().getTime()));
					stmt.setString(8,meeting.getActive());
					stmt.setTimestamp(9,new Timestamp(meeting.getTimeStamp().getTime()));
					if(meeting.getGoogleId()==null){
						stmt.setNull(10,java.sql.Types.VARCHAR);
					}
					else{
						stmt.setString(10, meeting.getGoogleId());
					}
					rs = stmt.executeQuery();
					rs.next();
					ret = rs.getInt(1);
					meeting.setId(ret);
					
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return ret;		
	}
	/**
	 * Function that creates a Rate and stores it into the database
	 * @param rate
	 * @return an Integer representing it's ID
	 * @throws Exception
	 */
	public static Integer createRate(RateBean rate) throws Exception{
		Connection conn = null;
		Integer ret = null;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(rate, "Rate can't be null");
			Validate.notNull(rate.getUserID(), "rate must have a sender");
			Validate.notNull(rate.getRate(), "rate must have a rating");
			Validate.notNull(rate.getRelatedActivity(), "rate must have related activity");
			Validate.isRate(rate.getRate(), "rate value must be either 't' or 'f'");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("INSERT INTO Activity(userId,timeposted,relatedActivity,rate,lastmodified) VALUES (?,?,?,?,?) returning id;");
					stmt.setInt(1, rate.getUserID());
					stmt.setTimestamp(2, new Timestamp(rate.getTimeStamp().getTime()));
					stmt.setInt(3, rate.getRelatedActivity());
					stmt.setString(4, rate.getRate());
					stmt.setTimestamp(5, new Timestamp(rate.getTimeStamp().getTime()));
					rs = stmt.executeQuery();
					rs.next();
					ret = rs.getInt(1);
					rate.setId(ret);
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
					ActivityBean activity = retrieveActivity(rate.getRelatedActivity());
					activity.setLastModified(rate.getTimeStamp());
					updateLastModified(activity);
				}
			}
		}
		return ret;		
	}
	/**
	 * Function that creates an Assignment and stores it into the database
	 * @param assignment
	 * @return an Integer representing it's ID
	 * @throws Exception
	 */
	public static Integer createAssignment(AssignmentBean assignment) throws Exception{
		Connection conn = null;
		Integer ret = null;
		try {
			Validate.notNull(assignment, "assignment can't be null");
			Validate.notNull(assignment.getMessage(),"assignment must have message");
			Validate.notNull(assignment.getParticipantID(),"assignment must have recipient");
			Validate.notNull(assignment.getUserID(),"assignment must have sender");
			Validate.notNull(assignment.getTitle(),"assignment must have title");
			Validate.notNull(assignment.getDueDate(), "assignment must have due date");
			Validate.notNull(assignment.getFullMark(), "assignment must have full mark");
			//Add Assignment to Google Calendar
			GroupBean currGroup = GroupDAOImpl.retrieveGroupById(assignment.getParticipantID());
			ArrayList<BelongBean> members = currGroup.getMembers();
			ArrayList<String> emails = new ArrayList<String>();
			for (int i = 0; i < members.size(); i++) {
				emails.add(UserDAOImpl.retrieveUserById(members.get(i).getUserID()).getEmail());
			}
			
			String fullMessage = "Assignment Mark = " + assignment.getFullMark() + "\n" +
								 "Description = " + assignment.getMessage() + "\n" +
								 "Attachment = " + assignment.getAttachment() + "\n" +
								 "Last Modified = " + assignment.getLastModified();
								 
			
			assignment.setGoogleId(GoogleAccess.createEvent(currGroup.getName(), assignment.getTitle(), fullMessage, null, 
					"UNSW Campus", assignment.getDueDate(), emails));
			conn = JDBC.getConnection();
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("INSERT INTO Activity(userId,participantId,title,message,timeposted,fullMark,dueDate,attachment,lastmodified,googleId) VALUES (?,?,?,?,?,?,?,?,?,?) returning id;");
					stmt.setInt(1, assignment.getUserID());
					stmt.setInt(2,assignment.getParticipantID());
					stmt.setString(3, assignment.getTitle());
					stmt.setString(4, assignment.getMessage());
					stmt.setTimestamp(5, new Timestamp(assignment.getTimeStamp().getTime()));
					stmt.setDouble(6, assignment.getFullMark());
					stmt.setTimestamp(7, new Timestamp(assignment.getDueDate().getTime()));
					stmt.setString(8, assignment.getAttachment());
					stmt.setTimestamp(9, new Timestamp(assignment.getTimeStamp().getTime()));
					if(assignment.getGoogleId() == null){
						stmt.setNull(10,java.sql.Types.VARCHAR);
					}
					else{
						stmt.setString(10, assignment.getGoogleId());
					}
					rs = stmt.executeQuery();
					rs.next();
					ret = rs.getInt(1);
					assignment.setId(ret);
					
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return ret;		
	}
	/**
	 * Function that retrieves an activity based on ID
	 * @param activityId
	 * @return returns an Activity that can be instanced as a Material,Meeting, Rate,Comment, or just a regular Activity
	 * @throws Exception
	 */
	public static ActivityBean retrieveActivity(Integer activityId) throws Exception{
		Connection conn = null;
		ActivityBean ret = null;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(activityId,"can't be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("SELECT * FROM Activity WHERE id = ?;");
					stmt.setInt(1, activityId);
					rs = stmt.executeQuery();
					//SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
					while(rs.next()){
						Integer id = rs.getInt("id");
						Integer userId = rs.getInt("userId");
						Integer participantId = rs.getInt("participantId");
						String title = rs.getString("title");
						String message = rs.getString("message");
						Timestamp timeStamp = rs.getTimestamp("timeposted");
						//attachment
						String attachment = rs.getString("attachment");
						//meeting
						Timestamp meetingStartDateTime = rs.getTimestamp("meetingStartDateTime");
						//assignment
						Double fullMark = rs.getDouble("fullMark");
						Timestamp dueDate = rs.getTimestamp("dueDate");
						//comment
						Integer relatedActivity = rs.getInt("relatedActivity");
						//rate
						String rate = rs.getString("rate");
						String active = rs.getString("active");
						Date lastModified = rs.getTimestamp("lastmodified");
						Date meetingEndDateTime = rs.getTime("meetingEndDateTime");
						String googleId = rs.getString("googleId");
						if(attachment==null && meetingStartDateTime==null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate==null && active == null){
							ret = (new ActivityBean(id,userId,participantId,title,message,timeStamp,lastModified));
						}
						else if(attachment!=null && meetingStartDateTime == null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate == null && active == null){
							ret = (new MaterialBean(id, userId, participantId, title, message,timeStamp,lastModified, attachment));
						}
						else if(attachment==null && meetingStartDateTime != null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate == null && active != null){
							ret = (new MeetingBean(id, userId, participantId, title, message, timeStamp,lastModified, meetingStartDateTime,meetingEndDateTime,active,googleId));
							//activities.add(new AssignmentBean(id, userId, participantId, title, message, timeStamp,sdf.parse(dueDate), fullMark));
						}
						else if(attachment!=null && meetingStartDateTime == null && !fullMark.equals(0D) && dueDate!=null && relatedActivity.equals(0) && rate == null && active == null){
							//activities.add(new CommentBean(id, userId,message, timeStamp, relatedActivity));
							ret = (new AssignmentBean(id, userId, participantId, title, message, timeStamp,lastModified,dueDate, fullMark,attachment,googleId));
						}
						else if(attachment==null && meetingStartDateTime == null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate == null && active == null){
							//activities.add(new RateBean(id, userId,timeStamp, relatedActivity, rate));
							ret = (new CommentBean(id, userId,message, timeStamp,lastModified, relatedActivity));
						}
						else if(attachment==null && meetingStartDateTime == null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate != null && active == null){
							//activities.add(new MeetingBean(id, userId, participantId, title, message, timeStamp, sdf.parse(meetingDateTime)));
							ret = (new RateBean(id, userId,timeStamp,lastModified, relatedActivity, rate));
						}	
					}
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return ret;		
	}
	
	/**
	 * retrieves everything but rates and comments
	 * @param recipient
	 * @return
	 * @throws Exception
	 */
	public static List<ActivityBean> retrieveLastModifiedMainStreams(Integer recipient,Integer top) throws Exception {
		Connection conn = null;
		List<ActivityBean> activities = new ArrayList<ActivityBean>();
		try {
			conn = JDBC.getConnection();
			Validate.notNull(recipient,"Recipient cannot be null");
			Validate.notNull(top,"Top cannot be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("SELECT * FROM Activity WHERE participantId = ? and relatedActivity is null ORDER BY lastmodified DESC LIMIT ?;");
					stmt.setInt(1, recipient);
					stmt.setInt(2, top);
					rs = stmt.executeQuery();
					while(rs.next()){
						Integer id = rs.getInt("id");
						Integer userId = rs.getInt("userId");
						Integer participantId = rs.getInt("participantId");
						String title = rs.getString("title");
						String message = rs.getString("message");
						Timestamp timeStamp = rs.getTimestamp("timeposted");
						//attachment
						String attachment = rs.getString("attachment");
						//meeting
						Timestamp meetingStartDateTime = rs.getTimestamp("meetingStartDateTime");
						//assignment
						Double fullMark = rs.getDouble("fullMark");
						Timestamp dueDate = rs.getTimestamp("dueDate");
						//comment
						Integer relatedActivity = rs.getInt("relatedActivity");
						//rate
						String rate = rs.getString("rate");
						String active = rs.getString("active");
						Date lastModified = rs.getTimestamp("lastmodified");
						Date meetingEndDateTime = rs.getTimestamp("meetingEndDateTime");
						String googleId = rs.getString("googleId");
						if(attachment==null && meetingStartDateTime==null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate==null && active == null){
							activities.add(new ActivityBean(id,userId,participantId,title,message,timeStamp,lastModified));
						}
						else if(attachment!=null && meetingStartDateTime == null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate == null && active == null){
							activities.add(new MaterialBean(id, userId, participantId, title, message,timeStamp,lastModified, attachment));
						}
						else if(attachment==null && meetingStartDateTime != null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate == null && active != null){
							activities.add(new MeetingBean(id, userId, participantId, title, message, timeStamp,lastModified, meetingStartDateTime,meetingEndDateTime,active,googleId));
							//activities.add(new AssignmentBean(id, userId, participantId, title, message, timeStamp,sdf.parse(dueDate), fullMark));
						}
						else if(attachment!=null && meetingStartDateTime == null && !fullMark.equals(0D) && dueDate!=null && relatedActivity.equals(0) && rate == null && active == null){
							//activities.add(new CommentBean(id, userId,message, timeStamp, relatedActivity));
							activities.add(new AssignmentBean(id, userId, participantId, title, message, timeStamp,lastModified,dueDate, fullMark,attachment,googleId));
						}	
					}
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return activities;
	}
	
	public static List<ActivityBean> retrieveLastModifiedMainStreams(Integer recipient) throws Exception {
		Connection conn = null;
		List<ActivityBean> activities = new ArrayList<ActivityBean>();
		try {
			conn = JDBC.getConnection();
			Validate.notNull(recipient,"Recipient cannot be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("SELECT * FROM Activity WHERE participantId = ? and relatedActivity is null ORDER BY lastmodified DESC;");
					stmt.setInt(1, recipient);
					rs = stmt.executeQuery();
					while(rs.next()){
						Integer id = rs.getInt("id");
						Integer userId = rs.getInt("userId");
						Integer participantId = rs.getInt("participantId");
						String title = rs.getString("title");
						String message = rs.getString("message");
						Timestamp timeStamp = rs.getTimestamp("timeposted");
						//attachment
						String attachment = rs.getString("attachment");
						//meeting
						Timestamp meetingStartDateTime = rs.getTimestamp("meetingStartDateTime");
						//assignment
						Double fullMark = rs.getDouble("fullMark");
						Timestamp dueDate = rs.getTimestamp("dueDate");
						//comment
						Integer relatedActivity = rs.getInt("relatedActivity");
						//rate
						String rate = rs.getString("rate");
						String active = rs.getString("active");
						Date lastModified = rs.getTimestamp("lastmodified");
						Date meetingEndDateTime = rs.getTimestamp("meetingEndDateTime");
						String googleId = rs.getString("googleId");
						if(attachment==null && meetingStartDateTime==null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate==null && active == null){
							activities.add(new ActivityBean(id,userId,participantId,title,message,timeStamp,lastModified));
						}
						else if(attachment!=null && meetingStartDateTime == null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate == null && active == null){
							activities.add(new MaterialBean(id, userId, participantId, title, message,timeStamp,lastModified, attachment));
						}
						else if(attachment==null && meetingStartDateTime != null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate == null && active != null){
							activities.add(new MeetingBean(id, userId, participantId, title, message, timeStamp,lastModified, meetingStartDateTime,meetingEndDateTime,active,googleId));
							//activities.add(new AssignmentBean(id, userId, participantId, title, message, timeStamp,sdf.parse(dueDate), fullMark));
						}
						else if(attachment!=null && meetingStartDateTime == null && !fullMark.equals(0D) && dueDate!=null && relatedActivity.equals(0) && rate == null && active == null){
							//activities.add(new CommentBean(id, userId,message, timeStamp, relatedActivity));
							activities.add(new AssignmentBean(id, userId, participantId, title, message, timeStamp,lastModified,dueDate, fullMark,attachment,googleId));
						}	
					}
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return activities;
	}
	
	public static List<ActivityBean> retrieveMainStreams(Integer recipient) throws Exception {
		Connection conn = null;
		List<ActivityBean> activities = new ArrayList<ActivityBean>();
		try {
			conn = JDBC.getConnection();
			Validate.notNull(recipient,"Recipient cannot be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("SELECT * FROM Activity WHERE participantId = ? and relatedActivity is null ORDER BY id DESC;");
					stmt.setInt(1, recipient);
					rs = stmt.executeQuery();
					while(rs.next()){
						Integer id = rs.getInt("id");
						Integer userId = rs.getInt("userId");
						Integer participantId = rs.getInt("participantId");
						String title = rs.getString("title");
						String message = rs.getString("message");
						Timestamp timeStamp = rs.getTimestamp("timeposted");
						//attachment
						String attachment = rs.getString("attachment");
						//meeting
						Timestamp meetingStartDateTime = rs.getTimestamp("meetingStartDateTime");
						//assignment
						Double fullMark = rs.getDouble("fullMark");
						Timestamp dueDate = rs.getTimestamp("dueDate");
						//comment
						Integer relatedActivity = rs.getInt("relatedActivity");
						//rate
						String rate = rs.getString("rate");
						String active = rs.getString("active");
						Timestamp lastModified = rs.getTimestamp("lastmodified");
						Timestamp meetingEndDateTime = rs.getTimestamp("meetingEndDateTime");
						String googleId = rs.getString("googleId");
						if(attachment==null && meetingStartDateTime==null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate==null && active == null){
							activities.add(new ActivityBean(id,userId,participantId,title,message,timeStamp,lastModified));
						}
						else if(attachment!=null && meetingStartDateTime == null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate == null && active == null){
							activities.add(new MaterialBean(id, userId, participantId, title, message,timeStamp,lastModified, attachment));
						}
						else if(attachment==null && meetingStartDateTime != null && fullMark.equals(0D) && dueDate==null && relatedActivity.equals(0) && rate == null && active != null){
							activities.add(new MeetingBean(id, userId, participantId, title, message, timeStamp,lastModified, meetingStartDateTime,meetingEndDateTime,active,googleId));
							//activities.add(new AssignmentBean(id, userId, participantId, title, message, timeStamp,sdf.parse(dueDate), fullMark));
						}
						else if(attachment!=null && meetingStartDateTime == null && !fullMark.equals(0D) && dueDate!=null && relatedActivity.equals(0) && rate == null && active == null){
							//activities.add(new CommentBean(id, userId,message, timeStamp, relatedActivity));
							activities.add(new AssignmentBean(id, userId, participantId, title, message, timeStamp,lastModified,dueDate, fullMark,attachment,googleId));
						}	
					}
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return activities;
	}
	/**
	 *  returns a arrayList of integers where get(0) = num of likes, get (1) = num of dislike
	 * @param activityID
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Integer> getRating (Integer activityID) throws Exception {
		Connection conn = null;
		List<ActivityBean> activities = new ArrayList<ActivityBean>();
		try {
			conn = JDBC.getConnection();
			Validate.notNull(activityID,"Recipient cannot be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("SELECT * FROM Activity WHERE relatedActivity = ? and rate is not null;");
					stmt.setInt(1, activityID);
					rs = stmt.executeQuery();
					//SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
					while(rs.next()){
						Integer id = rs.getInt("id");
						Integer userId = rs.getInt("userId");
						Timestamp timeStamp = rs.getTimestamp("timeposted");
						//comment
						Integer relatedActivity = rs.getInt("relatedActivity");
						//rate
						String rate = rs.getString("rate");
						Timestamp lastModified = rs.getTimestamp("lastmodified");
						activities.add(new RateBean(id, userId, timeStamp,lastModified, relatedActivity, rate));
					}
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		Integer like = 0;
		Integer dislike = 0;
		for (int i = 0; i < activities.size(); i++) {
			if (((RateBean)activities.get(i)).getRate().equals("t")){
				like++;
			} else {
				dislike--;
			}
		}
		ArrayList<Integer> ratingSummary = new ArrayList<Integer>();
		ratingSummary.add(like);
		ratingSummary.add(dislike);
		return ratingSummary;
	}
	
	
	/**
	 * Retrieves all the comments for a particular Activity
	 * @param activityID
	 * @return
	 * @throws Exception
	 */
	public static List<CommentBean> retrieveComments(Integer activityID) throws Exception {
		Connection conn = null;
		List<CommentBean> comments = new ArrayList<CommentBean>();
		try {
			conn = JDBC.getConnection();
			Validate.notNull(activityID,"Recipient cannot be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("SELECT * FROM Activity WHERE relatedActivity = ? and rate is null ORDER BY id ASC;");
					stmt.setInt(1, activityID);
					rs = stmt.executeQuery();
					//SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
					while(rs.next()){
						Integer id = rs.getInt("id");
						Integer userId = rs.getInt("userId");
						String message = rs.getString("message");
						Timestamp timeStamp = rs.getTimestamp("timeposted");
						//comment
						Integer relatedActivity = rs.getInt("relatedActivity");
						Timestamp lastModified = rs.getTimestamp("lastmodified");
						comments.add(new CommentBean(id,userId,message,timeStamp,lastModified,relatedActivity));
					}
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return comments;
	}
	/**
	 * determines whether the user has rated on a particular activity. Returns String "t" if true. "f" if false. or null if hasn't rated
	 * @param userID
	 * @param relatedActivity
	 * @return
	 * @throws Exception
	 */
	public static String hasRated (Integer userID,Integer relatedActivity) throws Exception{
		Connection conn = null;
		String ret = null;
		try{
			conn = JDBC.getConnection();
			Validate.notNull(userID, "Can't be null");
			Validate.notNull(relatedActivity, "relatedActivity can't be null");
		}
		catch (Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			try{
				stmt = conn.prepareStatement("Select * from Activity where userId = ? and relatedActivity = ? AND rate IS NOT NULL;");
				stmt.setInt(1, userID);
				stmt.setInt(2, relatedActivity);
				rs = stmt.executeQuery();
				while(rs.next()){
					ret = rs.getString("rate");
				}
			}
			catch(Exception ex){
				throw ex;
				//throw new Exception("Error in sql query");
			}
		}
		finally{
			if(conn!=null){
				rs.close();
				stmt.close();
			}
		}
		return ret;
	}

	public static List<MeetingBean> retrieveMeetings(Integer groupID) throws Exception {
		Connection conn = null;
		List<MeetingBean> meetings = new ArrayList<MeetingBean>();
		try{
			conn = JDBC.getConnection();
			Validate.notNull(groupID, "Can't be null");
		}
		catch (Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			try{
				stmt = conn.prepareStatement("Select * from Activity where participantId = ? and meetingStartDateTime is not null;");
				stmt.setInt(1, groupID);
				rs = stmt.executeQuery();
				while(rs.next()){
					Integer id = rs.getInt("id");
					Integer userId = rs.getInt("userId");
					Integer participantId = rs.getInt("participantId");
					String title = rs.getString("title");
					String message = rs.getString("message");
					Timestamp timeStamp = rs.getTimestamp("timeposted");
					//meeting
					Timestamp meetingStartDateTime = rs.getTimestamp("meetingStartDateTime");
					String active = rs.getString("active");
					Timestamp lastModified = rs.getTimestamp("lastmodified");
					Timestamp meetingEndDateTime = rs.getTimestamp("meetingEndDateTime");
					String googleId = rs.getString("googleId");
					meetings.add(new MeetingBean(id, userId, participantId, title, message, timeStamp,lastModified, meetingStartDateTime,meetingEndDateTime,active,googleId));
				}
			}
			catch(Exception ex){
				throw ex;
				//throw new Exception("Error in sql query");
			}
		}
		finally{
			if(conn!=null){
				rs.close();
				stmt.close();
			}
		}
		return meetings;
	}
	/**
	 * retrieves all of the scheduled meetings for a particular Group
	 * @param groupID
	 * @return
	 * @throws Exception
	 */
	public static List<MeetingBean> retrieveScheduledMeetings(Integer groupID) throws Exception {
		Connection conn = null;
		List<MeetingBean> meetings = new ArrayList<MeetingBean>();
		try{
			conn = JDBC.getConnection();
			Validate.notNull(groupID, "Can't be null");
		}
		catch (Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			try{
				stmt = conn.prepareStatement("Select * from Activity where participantId = ? and meetingStartDateTime is not null and active = 't';");
				stmt.setInt(1, groupID);
				rs = stmt.executeQuery();
				//SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
				while(rs.next()){
					Integer id = rs.getInt("id");
					Integer userId = rs.getInt("userId");
					Integer participantId = rs.getInt("participantId");
					String title = rs.getString("title");
					String message = rs.getString("message");
					Timestamp timeStamp = rs.getTimestamp("timeposted");
					//meeting
					Timestamp meetingStartDateTime = rs.getTimestamp("meetingStartDateTime");
					Timestamp lastModified = rs.getTimestamp("lastmodified");
					Timestamp meetingEndDateTime = rs.getTimestamp("meetingEndDateTime");
					String googleId = rs.getString("googleId");
					if((new Date()).compareTo(meetingStartDateTime)<=0){
						meetings.add(new MeetingBean(id, userId, participantId, title, message, timeStamp,lastModified, meetingStartDateTime,meetingEndDateTime,"t",googleId));
					}
				}
			}
			catch(Exception ex){
				throw ex;
				//throw new Exception("Error in sql query");
			}
		}
		finally{
			if(conn!=null){
				rs.close();
				stmt.close();
			}
		}
		return meetings;
	}
	
	public static boolean updateMeeting(MeetingBean meeting) throws Exception {
		Connection conn = null;
		boolean ret = false;
		try{
			conn = JDBC.getConnection();
			Validate.notNull(meeting, "Can't be null");
			Validate.notNull(meeting.getId(), "Id must not be null");
		}
		catch (Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		try{
			try{
				stmt = conn.prepareStatement("UPDATE Activity SET active = ?, meetingStartDateTime = ?,meetingEndDateTime = ?,lastmodified = ? WHERE id = ? and active is not null and meetingStartDateTime is not null;");
				stmt.setString(1, meeting.getActive());
				stmt.setTimestamp(2, new Timestamp(meeting.getMeetingStartDateTime().getTime()));
				stmt.setTimestamp(3, new Timestamp(meeting.getMeetingEndDateTime().getTime()));
				stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
				stmt.setInt(5,meeting.getId());
				stmt.execute();
				ret = true;
				
			}
			catch(Exception ex){
				throw ex;
				//throw new Exception("Error in sql query");
			}
		}
		finally{
			if(conn!=null){
				stmt.close();
			}
		}
		return ret;
	}
	public static boolean updateLastModified(ActivityBean activity) throws Exception {
		Connection conn = null;
		boolean ret = false;
		try{
			conn = JDBC.getConnection();
			Validate.notNull(activity, "Can't be null");
			Validate.notNull(activity.getId(), "Id must not be null");
		}
		catch (Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		try{
			try{
				stmt = conn.prepareStatement("UPDATE Activity SET lastmodified = ? WHERE id = ?;");
				stmt.setTimestamp(1, new Timestamp(activity.getLastModified().getTime()));
				stmt.setInt(2, activity.getId());
				stmt.execute();
				ret = true;
				
			}
			catch(Exception ex){
				throw ex;
				//throw new Exception("Error in sql query");
			}
		}
		finally{
			if(conn!=null){
				stmt.close();
			}
		}
		return ret;		
	}
}
