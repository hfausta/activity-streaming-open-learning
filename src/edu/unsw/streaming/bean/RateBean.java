package edu.unsw.streaming.bean;

import java.io.Serializable;
import java.util.Date;

import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *Represents a user's like or dislike of a particular Activity. 
 *It is an extension of a Comment since Comments have related Activities
 */
@SuppressWarnings("serial")
public class RateBean extends CommentBean implements Serializable {
	private String rate;
	public RateBean() {
		super();
	}
	public RateBean(Integer id, Integer userID,Date timeStamp,Date lastModified, Integer relatedActivity, String rate) throws Exception {
		super(id,userID,null,timeStamp,lastModified,relatedActivity);
		Validate.notNull(relatedActivity, "Rate must have relatedActivity");
		Validate.notNull(rate,"Rate can't be null");
		this.rate = rate;
		if(rate != null){
			Validate.isRate(rate, "Invalid Rate");
		}
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) throws Exception {
		Validate.notNull(rate,"Rate can't be null");
		if(rate != null){
			Validate.isRate(rate, "Invalid Rate");
		}
		this.rate = rate;
	}

}
