package edu.unsw.streaming.bean;

import java.io.Serializable;
import java.util.Date;

import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *represents the posting of Lecture Material or notes. It is an implementation of an Activity
 */

@SuppressWarnings("serial")
public class MaterialBean extends ActivityBean implements Serializable {

	private String attachment;
	public MaterialBean() {
		super();
	}
	public MaterialBean(Integer id, Integer userID, Integer participantID,
			String title, String message, Date timeStamp,Date lastModified, String attachment) throws Exception {
		super(id, userID, participantID, title, message, timeStamp,lastModified);
		this.attachment = attachment;
		Validate.notNull(attachment,"Material must have attachment");
		// TODO Auto-generated constructor stub
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) throws Exception {
		Validate.notNull(attachment,"Material must have attachment");
		this.attachment = attachment;
	}

}
