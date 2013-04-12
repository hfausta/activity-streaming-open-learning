package edu.unsw.streaming.bean;

import java.io.Serializable;
import java.util.ArrayList;

import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *Represents a Group or channel of a website. A group may be a subGroup of a main Group.
 */
@SuppressWarnings("serial")
public class GroupBean extends ParticipantBean implements Serializable {
	//if relatedGroup is null, then its a course
	private Integer relatedGroup;
	private ArrayList<BelongBean> members = new ArrayList<BelongBean>();
	private String name;
	
	public GroupBean() {
		super();
	}
	
	public GroupBean(Integer id, String name, Integer relatedGroup) throws Exception {
		super(id);
		this.relatedGroup = relatedGroup;
		this.name = name;
		Validate.notNull(name,"Group must have a name");
		// TODO Auto-generated constructor stub
		
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) throws Exception {
		Validate.notNull(name,"Group must have a name");
		this.name = name;
	}


	public Integer getRelatedGroup() {
		return relatedGroup;
	}

	public void setRelatedGroup(Integer relatedGroup) {
		this.relatedGroup = relatedGroup;
	}

	public void addMember(Integer user, String role) throws Exception {
		this.members.add(new BelongBean(user, role));
	}


	public ArrayList<BelongBean> getMembers() {
		return members;
	}


	public void setMembers(ArrayList<BelongBean> members) {
		this.members = members;
	}
	
	
	
}
