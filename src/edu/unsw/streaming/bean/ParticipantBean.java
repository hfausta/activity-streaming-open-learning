package edu.unsw.streaming.bean;

import java.io.Serializable;
/**
 * 
 * @author Charagh Jethnani
 *Participant refers to either a User or Group. Since Activity Streams can be received to a User or Group,
 * a Generalisation or inheritance Class was required
 */
@SuppressWarnings("serial")
public class ParticipantBean implements Serializable {
	protected Integer id;
	
	public ParticipantBean() {
		
	}
	
	public ParticipantBean(Integer id) throws Exception {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
