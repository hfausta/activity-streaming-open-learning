package edu.unsw.streaming.bean;

import java.io.Serializable;
import java.util.Date;

import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *represents an addition of a Comment to an Activity. It is an implementation of an Activity
 */

@SuppressWarnings("serial")
public class CommentBean extends ActivityBean implements Serializable {
	private Integer relatedActivity;
	public CommentBean(){}
	public CommentBean(Integer id, Integer userID, String message,Date timeStamp,Date lastModified, Integer relatedActivity) throws Exception {
		super(id,userID,null,null,message,timeStamp,lastModified);
		this.relatedActivity = relatedActivity;
		//Validate.notNull(message, "Comment must have a message");
		Validate.notNull(relatedActivity, "Comment must relate to Activity");
	}
	public Integer getRelatedActivity() {
		return relatedActivity;
	}
	public void setRelatedActivity(Integer relatedActivity) throws Exception {
		Validate.notNull(relatedActivity, "Comment must relate to Activity");
		this.relatedActivity = relatedActivity;
	}
}
