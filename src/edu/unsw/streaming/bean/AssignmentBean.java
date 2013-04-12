package edu.unsw.streaming.bean;

import java.io.Serializable;
import java.util.Date;

import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *represents the creation of an Assignment in the website. It is an implementation of an Activity
 */
@SuppressWarnings("serial")
public class AssignmentBean extends ActivityBean implements Serializable {

	private Date dueDate;
	private double fullMark;
	private String attachment;
	private String googleId;
	public AssignmentBean() {
		super();
	}
	
	public AssignmentBean(Integer id, Integer userID, Integer participantID,
			String title, String message, Date timeStamp,Date lastModified, Date dueDate, Double fullMark, String attachment,String googleId) throws Exception {
		super(id, userID, participantID, title, message, timeStamp,lastModified);
		// TODO Auto-generated constructor stub
		this.dueDate = dueDate;
		this.fullMark = fullMark;
		this.attachment = attachment;
		Validate.notNull(attachment, "Attachment must not be null");
		Validate.notNull(dueDate,"Assignment must have a due date");
		Validate.notNull(fullMark,"Assignment must have a full mark");
		//Validate.isAfterDate(dueDate, "Due date has to be after current date");
		Validate.isMoreThanZero(fullMark, "Value for full mark must be more than zero");
		this.googleId = googleId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) throws Exception {
		Validate.notNull(dueDate,"Assignment must have a due date");
		//Validate.isAfterDate(dueDate, "Due date has to be after current date");
		this.dueDate = dueDate;
	}

	public double getFullMark() {
		return fullMark;
	}

	public void setFullMark(double fullMark) throws Exception {
		Validate.notNull(fullMark,"Assignment must have a full mark");
		Validate.isMoreThanZero(fullMark, "Value for full mark must be more than zero");
		this.fullMark = fullMark;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	
}
