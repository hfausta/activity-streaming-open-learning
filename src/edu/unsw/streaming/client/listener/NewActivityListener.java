package edu.unsw.streaming.client.listener;

import java.util.Date;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;
import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.AssignmentBean;
import edu.unsw.streaming.bean.MaterialBean;
import edu.unsw.streaming.bean.MeetingBean;
import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.client.event.NewActivityEvent;
import edu.unsw.streaming.client.event.NewAssignmentEvent;
import edu.unsw.streaming.client.event.NewCancelMeetingEvent;
import edu.unsw.streaming.client.event.NewCommentEvent;
import edu.unsw.streaming.client.event.NewGroupChatEvent;
import edu.unsw.streaming.client.event.NewMaterialEvent;
import edu.unsw.streaming.client.event.NewMeetingEvent;
import edu.unsw.streaming.client.event.NewThumbsDownEvent;
import edu.unsw.streaming.client.event.NewThumbsUpEvent;
import edu.unsw.streaming.client.panel.ActivityStreamPanel;
import edu.unsw.streaming.client.panel.ActivityStreamPanelIndividual;
import edu.unsw.streaming.client.panel.CalendarPanel;
import edu.unsw.streaming.client.panel.ChatPanel;
import edu.unsw.streaming.client.panel.CommentPanel;

public class NewActivityListener implements RemoteEventListener {

	@Override
	public void apply(Event anEvent) {
		if(anEvent instanceof NewActivityEvent) {
			try {
				onNewActivityEvent((NewActivityEvent)anEvent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(anEvent instanceof NewAssignmentEvent) {
			try {
				onNewAssignmentEvent((NewAssignmentEvent)anEvent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(anEvent instanceof NewCommentEvent) {
			try {
				onNewCommentEvent((NewCommentEvent)anEvent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(anEvent instanceof NewMaterialEvent) {
			try {
				onNewMaterialEvent((NewMaterialEvent)anEvent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(anEvent instanceof NewMeetingEvent) {
			try {
				onNewMeetingEvent((NewMeetingEvent)anEvent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(anEvent instanceof NewThumbsUpEvent) {
			onNewThumbsUpEvent((NewThumbsUpEvent)anEvent);
		} else if(anEvent instanceof NewThumbsDownEvent) {
			onNewThumbsDownEvent((NewThumbsDownEvent)anEvent);
		} else if(anEvent instanceof NewCancelMeetingEvent) {
			onNewCancelMeetingEvent((NewCancelMeetingEvent)anEvent);
		} else if(anEvent instanceof NewGroupChatEvent) {
			onNewGroupChatEvent((NewGroupChatEvent)anEvent);
		}
	}
	
	public void onNewActivityEvent(NewActivityEvent event) throws Exception {
		//Check if the user's group related to the event
		if(StreamED.group.getId().equals(event.getActivity().getParticipantID())) {
			ActivityBean activity = new ActivityBean(event.getActivity().getId(),
					event.getActivity().getUserID(),
					event.getActivity().getParticipantID(),
					event.getActivity().getTitle(),
					event.getActivity().getMessage(),
					event.getActivity().getTimeStamp(),
					event.getActivity().getLastModified());
			//Add the new activity on top
			ActivityStreamPanel.getVPanel().insert(new ActivityStreamPanelIndividual(activity), 1);
		}
	}
	
	//TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
	public void onNewAssignmentEvent(NewAssignmentEvent event) throws Exception {
		//Check if the user's group related to the event
		if(StreamED.group.getId().equals(event.getParticipantID())) {
			AssignmentBean assignment = new AssignmentBean(event.getId(),
					event.getUserID(),
					event.getParticipantID(),
					event.getTitle(),
					event.getMessage(),
					event.getTimeStamp(),
					event.getLastModified(),
					event.getDueDate(),
					event.getFullMark(),
					event.getAttachment(),
					event.getGoogleID());
			//Add the new activity on top
			ActivityStreamPanel.getVPanel().insert(new ActivityStreamPanelIndividual(assignment), 1);
			HTMLPanel panel = (HTMLPanel) CalendarPanel.getVPanel().getWidget(1);
			CalendarPanel.getVPanel().remove(1);
			panel = new HTMLPanel(CalendarPanel.source());
			panel.setStyleName("gwt-Frame");
			//panel.setWidth("100%");
			panel.setPixelSize(600, 400);
			panel.setVisible(true);
			CalendarPanel.getVPanel().insert(panel,1);
		}
	}
	
	public void onNewCommentEvent(NewCommentEvent event) throws Exception {
		boolean found = false;
		int index = 0;
		
		//Get the ActivityStreamPanelIndividual that is related to the comment
		for(int i=0;i<ActivityStreamPanel.getVPanel().getWidgetCount();i++) {
			if(ActivityStreamPanel.getVPanel().getWidget(i) != null && 
					ActivityStreamPanel.getVPanel().getWidget(i) instanceof ActivityStreamPanelIndividual) {
				ActivityStreamPanelIndividual aspi = (ActivityStreamPanelIndividual) ActivityStreamPanel.getVPanel().getWidget(i);
				if(aspi.getActivityID().equals(event.getRelatedActivity())) {
					//If related activity is found
					found = true;
					index = i;
					break;
				}
			}
		}
		
		if(found) {
			ActivityStreamPanelIndividual aspi = (ActivityStreamPanelIndividual) ActivityStreamPanel.getVPanel().getWidget(index);
			//Add new comment to activity stream panel individual
			CommentPanel commentPanel = new CommentPanel(event.getUserBean(), event.getTimeStamp(), event.getMessage());
			aspi.getVPanel().insert(commentPanel, aspi.getVPanel().getWidgetCount()-1);
			//Remove the old activity stream panel individual
			if(!event.getUserBean().getId().equals(StreamED.user.getId())) {
				ActivityStreamPanel.getVPanel().remove(index);
				ActivityStreamPanel.getVPanel().insert(aspi, 1);
			} else {
				ActivityStreamPanel.getVPanel().remove(index);
				ActivityStreamPanel.getVPanel().insert(aspi, index);
			}
		}
	}
	
	public void onNewMaterialEvent(NewMaterialEvent event) throws Exception {
		//Check if the user's group related to the event
		if(StreamED.group.getId().equals(event.getMaterial().getParticipantID())) {
			MaterialBean material = new MaterialBean(event.getMaterial().getId(),
					event.getMaterial().getUserID(),
					event.getMaterial().getParticipantID(),
					event.getMaterial().getTitle(),
					event.getMaterial().getMessage(),
					event.getMaterial().getTimeStamp(),
					event.getMaterial().getLastModified(),
					event.getMaterial().getAttachment());
			//Add the new activity on top
			ActivityStreamPanel.getVPanel().insert(new ActivityStreamPanelIndividual(material), 1);
		}
	}
	
	//TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
	public void onNewMeetingEvent(NewMeetingEvent event) throws Exception {
		//Check if the user's group related to the event
		if(StreamED.group.getId().equals(event.getParticipantID())) {
			MeetingBean meeting = new MeetingBean(event.getId(), event.getUserID(), event.getParticipantID(),
			event.getTitle(), event.getMessage(), event.getTimeStamp(), event.getLastModified(), event.getStartMeetingDate(),
			event.getEndMeetingDate(), event.getActive(), event.getGoogleID());
			//Add the new activity on top
			ActivityStreamPanel.getVPanel().insert(new ActivityStreamPanelIndividual(meeting), 1);
			HTMLPanel panel = (HTMLPanel) CalendarPanel.getVPanel().getWidget(1);
			CalendarPanel.getVPanel().remove(1);
			panel = new HTMLPanel(CalendarPanel.source());
			panel.setStyleName("gwt-Frame");
			//panel.setWidth("100%");
			panel.setPixelSize(600, 400);
			panel.setVisible(true);
			CalendarPanel.getVPanel().insert(panel,1);
		}
	}
	
	public void onNewThumbsUpEvent(NewThumbsUpEvent event) {
		boolean found = false;
		int index = 0;
		
		//Get the ActivityStreamPanelIndividual that is related to the rating
		for(int i=0;i<ActivityStreamPanel.getVPanel().getWidgetCount();i++) {
			if(ActivityStreamPanel.getVPanel().getWidget(i) != null && 
					ActivityStreamPanel.getVPanel().getWidget(i) instanceof ActivityStreamPanelIndividual) {
				ActivityStreamPanelIndividual aspi = (ActivityStreamPanelIndividual) ActivityStreamPanel.getVPanel().getWidget(i);
				if(aspi.getActivityID().equals(event.getRelatedActivity())) {
					//If related activity is found
					found = true;
					index = i;
					break;
				}
			}
		}
		
		if(found) {
			ActivityStreamPanelIndividual aspi = (ActivityStreamPanelIndividual) ActivityStreamPanel.getVPanel().getWidget(index);
			//Adds up the number of likes
			if(aspi.getRatingPanel().getWidgetCount() > 0) {
				aspi.getRatingPanel().remove(0);
				aspi.getRatingPanel().remove(1);
			}
			Integer numLike = 0;
			if(aspi.getNumLikes() == null) {
				numLike = 1;
			} else {
				numLike = aspi.getNumLikes() + 1;
			}
			Integer numDislike = 0;
			if(aspi.getNumDislikes() != null) {
				numDislike = aspi.getNumDislikes();
			}
			Label totalLikes = new Label(numLike + " likes");
			Label totalDislikes = new Label(numDislike + " dislikes");
			aspi.getRatingPanel().add(totalLikes);
			aspi.getRatingPanel().add(totalDislikes);
			//Remove the previous widget and add the new widget
			if(!event.getUser().getId().equals(StreamED.user.getId())) {
				ActivityStreamPanel.getVPanel().remove(index);
				ActivityStreamPanel.getVPanel().insert(aspi, 1);	
			} else {
				ActivityStreamPanel.getVPanel().remove(index);
				ActivityStreamPanel.getVPanel().insert(aspi, index);
			}
		}
	}
	
	public void onNewThumbsDownEvent(NewThumbsDownEvent event) {
		boolean found = false;
		int index = 0;
		
		//Get the ActivityStreamPanelIndividual that is related to the rating
		for(int i=0;i<ActivityStreamPanel.getVPanel().getWidgetCount();i++) {
			if(ActivityStreamPanel.getVPanel().getWidget(i) != null && 
					ActivityStreamPanel.getVPanel().getWidget(i) instanceof ActivityStreamPanelIndividual) {
				ActivityStreamPanelIndividual aspi = (ActivityStreamPanelIndividual) ActivityStreamPanel.getVPanel().getWidget(i);
				if(aspi.getActivityID().equals(event.getRelatedActivity())) {
					//If related activity is found
					found = true;
					index = i;
					break;
				}
			}
		}
		
		if(found) {
			ActivityStreamPanelIndividual aspi = (ActivityStreamPanelIndividual) ActivityStreamPanel.getVPanel().getWidget(index);
			//Adds up the number of likes
			if(aspi.getRatingPanel().getWidgetCount() > 0) {
				aspi.getRatingPanel().remove(0);
				aspi.getRatingPanel().remove(1);
			}
			Integer numLike = 0;
			if(aspi.getNumLikes() != null) {
				numLike = aspi.getNumLikes();
			}
			Integer numDislike = 0;
			if(aspi.getNumDislikes() == null) {
				numDislike = 1;
			} else {
				numDislike = aspi.getNumDislikes() + 1;
			}
			Label totalLikes = new Label(numLike + " likes");
			Label totalDislikes = new Label(numDislike + " dislikes");
			aspi.getRatingPanel().add(totalLikes);
			aspi.getRatingPanel().add(totalDislikes);
			//Remove the previous widget and add the new widget
			if(!event.getUser().getId().equals(StreamED.user.getId())) {
				ActivityStreamPanel.getVPanel().remove(index);
				ActivityStreamPanel.getVPanel().insert(aspi, 1);	
			} else {
				ActivityStreamPanel.getVPanel().remove(index);
				ActivityStreamPanel.getVPanel().insert(aspi, index);
			}
		}
	}
	
	public void onNewCancelMeetingEvent(NewCancelMeetingEvent event) {}

	public void onNewGroupChatEvent(NewGroupChatEvent event) {
		if(StreamED.group.getId().equals(event.getGroupID())) {
			String message = event.getUserName() + " -> " + event.getMessage() + "\n";
			ChatPanel.getChatMessages().setText(ChatPanel.getChatMessages().getText() + message);
		}
	}
}
