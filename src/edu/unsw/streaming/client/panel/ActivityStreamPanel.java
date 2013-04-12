package edu.unsw.streaming.client.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.AssignmentBean;
import edu.unsw.streaming.bean.MaterialBean;
import edu.unsw.streaming.bean.MeetingBean;
import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.service.ActivityServiceAsync;
import edu.unsw.streaming.service.GroupServiceAsync;

/**
 * @author Lianne Container to display all activities related to user
 * 
 */
public class ActivityStreamPanel extends Composite {
	private final ActivityServiceAsync activityService = StreamED.activityService;
	private static VerticalPanel verticalPanel;
	private final GroupServiceAsync groupService = StreamED.groupService;
	private Label assignmentLbl = new Label("Assignments");
	private Label meetingLbl = new Label("Meetings");
	private Label messageLbl = new Label("Messages");
	private Label materialLbl = new Label("Material");
	private Label allLbl = new Label("All");

	/**
	 * constructor for displaying all activities related to user
	 * 
	 * @param participants
	 */
	public ActivityStreamPanel(List<Integer> participants) {

		verticalPanel = new VerticalPanel();
		verticalPanel.setWidth("100%");
		initWidget(verticalPanel);

		final Label title = new Label("Activity Stream Feed");
		title.setStylePrimaryName("h1");
		verticalPanel.add(title);
		
		final HorizontalPanel sortPanel = new HorizontalPanel();
		sortPanel.setWidth("100%");
		sortPanel.setStylePrimaryName("menu");
		Label filterLbl = new Label("Filter by: ");
		filterLbl.setStylePrimaryName("none");
		sortPanel.add(filterLbl);
		allLbl.setStylePrimaryName("current");
		sortPanel.add(allLbl);
		sortPanel.add(messageLbl);
		sortPanel.add(assignmentLbl);
		sortPanel.add(materialLbl);
		sortPanel.add(meetingLbl);
		verticalPanel.add(sortPanel);
		groupService.getNewsFeed(StreamED.user.getId(),
				new AsyncCallback<List<ActivityBean>>() {

					@Override
					public void onFailure(Throwable caught) {
						// Window.alert("main "+caught.getMessage());
					}

					@Override
					public void onSuccess(final List<ActivityBean> result) {
						// TODO Auto-generated method stub
						for (ActivityBean activity : result) {
							verticalPanel
									.add(new ActivityStreamPanelIndividual(
											activity));
						}
						
						assignmentLbl.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								verticalPanel.clear();
								verticalPanel.add(title);
								verticalPanel.add(sortPanel);
								assignmentLbl.setStylePrimaryName("current");
								allLbl.setStylePrimaryName("gwt-Label");
								materialLbl.setStylePrimaryName("gwt-Label");
								meetingLbl.setStylePrimaryName("gwt-Label");
								messageLbl.setStylePrimaryName("gwt-Label");
								List<ActivityBean> assignments = sortByAssignment(result);
								for (ActivityBean activity : assignments) {
									verticalPanel
											.add(new ActivityStreamPanelIndividual(
													activity));
								}
							}
						});

						meetingLbl.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								verticalPanel.clear();
								verticalPanel.add(title);
								verticalPanel.add(sortPanel);
								meetingLbl.setStylePrimaryName("current");
								allLbl.setStylePrimaryName("gwt-Label");
								assignmentLbl.setStylePrimaryName("gwt-Label");
								materialLbl.setStylePrimaryName("gwt-Label");
								messageLbl.setStylePrimaryName("gwt-Label");
								List<ActivityBean> meetings = sortByMeeting(result);
								for (ActivityBean activity : meetings) {
									verticalPanel
											.add(new ActivityStreamPanelIndividual(
													activity));
								}

							}
						});

						materialLbl.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								verticalPanel.clear();
								verticalPanel.add(title);
								verticalPanel.add(sortPanel);
								materialLbl.setStylePrimaryName("current");
								allLbl.setStylePrimaryName("gwt-Label");
								assignmentLbl.setStylePrimaryName("gwt-Label");
								meetingLbl.setStylePrimaryName("gwt-Label");
								messageLbl.setStylePrimaryName("gwt-Label");
								List<ActivityBean> materials = sortByMaterial(result);
								for (ActivityBean activity : materials) {
									verticalPanel
											.add(new ActivityStreamPanelIndividual(
													activity));
								}

							}
						});

						messageLbl.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								verticalPanel.clear();
								verticalPanel.add(title);
								verticalPanel.add(sortPanel);
								messageLbl.setStylePrimaryName("current");
								allLbl.setStylePrimaryName("gwt-Label");
								assignmentLbl.setStylePrimaryName("gwt-Label");
								materialLbl.setStylePrimaryName("gwt-Label");
								meetingLbl.setStylePrimaryName("gwt-Label");
								List<ActivityBean> messages = sortByMessage(result);
								for (ActivityBean activity : messages) {
									verticalPanel
											.add(new ActivityStreamPanelIndividual(
													activity));
								}

							}
						});
						
						allLbl.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								verticalPanel.clear();
								verticalPanel.add(title);
								verticalPanel.add(sortPanel);
								allLbl.setStylePrimaryName("current");
								messageLbl.setStylePrimaryName("gwt-Label");
								assignmentLbl.setStylePrimaryName("gwt-Label");
								materialLbl.setStylePrimaryName("gwt-Label");
								meetingLbl.setStylePrimaryName("gwt-Label");
								for (ActivityBean activity : result) {
									verticalPanel
											.add(new ActivityStreamPanelIndividual(
													activity));
								}

							}
						});
					}

				});
		/*
		 * for (int p: participants) { activityService.viewActivities(p, new
		 * AsyncCallback<List<ActivityBean>>() {
		 * 
		 * @Override public void onFailure(Throwable caught) {
		 * //Window.alert("main "+caught.getMessage()); }
		 * 
		 * @Override public void onSuccess(List<ActivityBean> result) { for
		 * (ActivityBean activity: result) { verticalPanel.add(new
		 * ActivityStreamPanelIndividual(activity)); } }
		 * 
		 * }); }
		 */
	}

	/**
	 * constructor for displaying all activities related to a particular group
	 * the user has clicked
	 */
	public ActivityStreamPanel() {

		verticalPanel = new VerticalPanel();
		verticalPanel.setWidth("100%");
		initWidget(verticalPanel);

		final Label title = new Label("Activity Stream Feed");
		title.setStylePrimaryName("h1");
		verticalPanel.add(title);
		
		final HorizontalPanel sortPanel = new HorizontalPanel();
		sortPanel.setWidth("100%");
		sortPanel.setStylePrimaryName("menu");
		Label filterLbl = new Label("Filter by: ");
		filterLbl.setStylePrimaryName("none");
		sortPanel.add(filterLbl);
		allLbl.setStylePrimaryName("current");
		sortPanel.add(allLbl);
		sortPanel.add(messageLbl);
		sortPanel.add(assignmentLbl);
		sortPanel.add(materialLbl);
		sortPanel.add(meetingLbl);
		verticalPanel.add(sortPanel);
		
		activityService.viewActivities(StreamED.group.getId(),
				new AsyncCallback<List<ActivityBean>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("activitystreampanel viewactivities"
								+ caught.getMessage());
					}

					@Override
					public void onSuccess(final List<ActivityBean> result) {
						for (ActivityBean activity : result) {
							verticalPanel
									.add(new ActivityStreamPanelIndividual(
											activity));
						}

						assignmentLbl.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								verticalPanel.clear();
								verticalPanel.add(title);
								verticalPanel.add(sortPanel);
								assignmentLbl.setStylePrimaryName("current");
								allLbl.setStylePrimaryName("gwt-Label");
								materialLbl.setStylePrimaryName("gwt-Label");
								meetingLbl.setStylePrimaryName("gwt-Label");
								messageLbl.setStylePrimaryName("gwt-Label");
								List<ActivityBean> assignments = sortByAssignment(result);
								Window.alert(assignments.size()+"");
								for (ActivityBean activity : assignments) {
									verticalPanel
											.add(new ActivityStreamPanelIndividual(
													activity));
								}
							}
						});

						meetingLbl.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								verticalPanel.clear();
								verticalPanel.add(title);
								verticalPanel.add(sortPanel);
								meetingLbl.setStylePrimaryName("current");
								allLbl.setStylePrimaryName("gwt-Label");
								assignmentLbl.setStylePrimaryName("gwt-Label");
								materialLbl.setStylePrimaryName("gwt-Label");
								messageLbl.setStylePrimaryName("gwt-Label");
								List<ActivityBean> meetings = sortByMeeting(result);
								Window.alert(meetings.size()+"");
								for (ActivityBean activity : meetings) {
									verticalPanel
											.add(new ActivityStreamPanelIndividual(
													activity));
								}

							}
						});

						materialLbl.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								verticalPanel.clear();
								verticalPanel.add(title);
								verticalPanel.add(sortPanel);
								materialLbl.setStylePrimaryName("current");
								allLbl.setStylePrimaryName("gwt-Label");
								assignmentLbl.setStylePrimaryName("gwt-Label");
								meetingLbl.setStylePrimaryName("gwt-Label");
								messageLbl.setStylePrimaryName("gwt-Label");
								List<ActivityBean> materials = sortByMaterial(result);
								for (ActivityBean activity : materials) {
									verticalPanel
											.add(new ActivityStreamPanelIndividual(
													activity));
								}

							}
						});

						messageLbl.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								verticalPanel.clear();
								verticalPanel.add(title);
								verticalPanel.add(sortPanel);
								messageLbl.setStylePrimaryName("current");
								allLbl.setStylePrimaryName("gwt-Label");
								assignmentLbl.setStylePrimaryName("gwt-Label");
								materialLbl.setStylePrimaryName("gwt-Label");
								meetingLbl.setStylePrimaryName("gwt-Label");
								List<ActivityBean> messages = sortByMessage(result);
								Window.alert(messages.size()+"");
								for (ActivityBean activity : messages) {
									verticalPanel
											.add(new ActivityStreamPanelIndividual(
													activity));
								}

							}
						});
						
						allLbl.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								verticalPanel.clear();
								verticalPanel.add(title);
								verticalPanel.add(sortPanel);
								allLbl.setStylePrimaryName("current");
								messageLbl.setStylePrimaryName("gwt-Label");
								assignmentLbl.setStylePrimaryName("gwt-Label");
								materialLbl.setStylePrimaryName("gwt-Label");
								meetingLbl.setStylePrimaryName("gwt-Label");
								for (ActivityBean activity : result) {
									verticalPanel
											.add(new ActivityStreamPanelIndividual(
													activity));
								}

							}
						});
					}

				});

	}

	public static VerticalPanel getVPanel() {
		return verticalPanel;
	}

	public List<ActivityBean> sortByAssignment(List<ActivityBean> activities) {
		List<ActivityBean> assignments = new ArrayList<ActivityBean>();
		if (activities != null) {
			for (ActivityBean activity : activities) {
				if (activity instanceof AssignmentBean) {
					assignments.add(activity);
				}
			}
		}
		return assignments;

	}

	public List<ActivityBean> sortByMeeting(List<ActivityBean> activities) {
		List<ActivityBean> meetings = new ArrayList<ActivityBean>();
		if (activities != null) {
			for (ActivityBean activity : activities) {
				if (activity instanceof MeetingBean) {
					meetings.add(activity);
				}
			}
		}
		return meetings;

	}

	public List<ActivityBean> sortByMaterial(List<ActivityBean> activities) {
		List<ActivityBean> materials = new ArrayList<ActivityBean>();
		if (activities != null) {
			for (ActivityBean activity : activities) {
				if (activity instanceof MaterialBean) {
					materials.add(activity);
				}
			}
		}
		return materials;

	}

	public List<ActivityBean> sortByMessage(List<ActivityBean> activities) {
		List<ActivityBean> messages = new ArrayList<ActivityBean>();
		if (activities != null) {
			for (ActivityBean activity : activities) {
				if (!(activity instanceof MaterialBean)
						&& !(activity instanceof MeetingBean)
						&& !(activity instanceof AssignmentBean)) {
					messages.add(activity);
				}
			}
		}
		return messages;

	}
}