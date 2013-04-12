package edu.unsw.streaming.client.panel;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

import edu.unsw.streaming.bean.ActivityBean;
import edu.unsw.streaming.bean.AssignmentBean;
import edu.unsw.streaming.bean.CommentBean;
import edu.unsw.streaming.bean.GroupBean;
import edu.unsw.streaming.bean.MaterialBean;
import edu.unsw.streaming.bean.MeetingBean;
import edu.unsw.streaming.bean.UserBean;
import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.service.ActivityServiceAsync;
import edu.unsw.streaming.service.UserServiceAsync;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Container for displaying individual activities (including comment & rating related to the activity)
 * Display excludes scheduled meetings, which will be shown in CalendarPanel
 * Commenting and rating are done here (to be implemented)
 * @author Lianne
 *
 */
public class ActivityStreamPanelIndividual extends Composite {
	private final ActivityServiceAsync activityService = StreamED.activityService;
	private final UserServiceAsync userService = StreamED.userService;
	private Label lblTitle;
	private Label lblContent;
	private Label lblDue;
	private Label lblWeighting;
	private Label lblAttachment;
	private Label lblMeetingTime;
	private VerticalPanel panel;
	private HorizontalPanel optionPanel;
	private HorizontalPanel ratingPanel;
	private SimplePanel commentFormPanel;
	private TextBox commentField;
	private Integer activityID;
	private Integer numLikes;
	private Integer numDislikes;
	private HandlerRegistration likeRegistration;
	private HandlerRegistration dislikeRegistration;

	public ActivityStreamPanelIndividual(final ActivityBean activity) {
		
		panel = new VerticalPanel();
		panel.setStylePrimaryName("maincontent");
		panel.setWidth("100%");
		initWidget(panel);
		
		//Retreive activityID
		activityID = activity.getId();
		
		// to do: get user name
		userService.getUserByID(activity.getUserID(), new AsyncCallback<UserBean>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(UserBean result) {
				final VerticalPanel verticalPanel = new VerticalPanel();
				verticalPanel.setStylePrimaryName("activity");
				verticalPanel.setWidth("100%");
				String activityStr = result.getName();
				
				final HorizontalPanel heading = new HorizontalPanel();
				//heading.setWidth("100%");
				heading.setStylePrimaryName("h2");
				Label user = new Label(result.getName());
				heading.add(user);
				
				if (StreamED.group == null) {
					StreamED.groupService.getGroupByID(activity.getParticipantID(), new AsyncCallback<GroupBean>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(GroupBean result) {
							String[] groups = result.getName().split("_");
							Label group;
							if (groups.length == 1) {
								group = new Label(groups[0]);
							} else {
								group = new Label(groups[1]+" ("+groups[0]+")");
							}
							Image arrow = new Image("images/to.png");
							heading.add(arrow);
							heading.add(group);
						}
					});
				}
				panel.add(heading);
				
				if (activity instanceof AssignmentBean) {
					verticalPanel.setStylePrimaryName("assignment");
					AssignmentBean assignment = (AssignmentBean)activity;
					lblTitle = new Label(assignment.getTitle());
					lblAttachment = new Label("attachment at "+assignment.getAttachment());
					lblContent = new Label(assignment.getMessage());
					lblDue = new Label("due "+assignment.getDueDate());
					lblWeighting = new Label("percentage weighting: "+assignment.getFullMark());
					activityStr += " uploaded an ASSIGNMENT";
				} else if (activity instanceof MaterialBean) {
					verticalPanel.setStylePrimaryName("material");
					MaterialBean material = (MaterialBean)activity;
					activityStr += " uploaded a course MATERIAL";
					lblTitle = new Label(material.getTitle());
					lblAttachment = new Label("attachment at "+material.getAttachment());
					lblContent = new Label(material.getMessage());
				} else if (activity instanceof MeetingBean) {
					verticalPanel.setStylePrimaryName("meeting");
					MeetingBean meeting = (MeetingBean)activity;
					activityStr += " scheduled a MEETING";
					lblTitle = new Label(meeting.getTitle());
					lblContent = new Label(meeting.getMessage());
					lblMeetingTime = new Label(meeting.getMeetingStartDateTime().toString());
				} else if (activity instanceof ActivityBean) {
					verticalPanel.setStylePrimaryName("message");
					activityStr += " posted a MESSAGE";
					lblTitle = new Label(activity.getTitle());
					lblContent = new Label(activity.getMessage());
				}
				Label lblActivity = new Label(activityStr);
				panel.add(lblActivity);
				panel.add(verticalPanel);
				lblTitle.setStylePrimaryName("h3");
				verticalPanel.add(lblTitle);
				verticalPanel.add(lblContent);
				try {
					verticalPanel.add(lblDue);
					verticalPanel.add(lblWeighting);
				} catch (Exception e) {}
				try {
					verticalPanel.add(lblMeetingTime);
				} catch (Exception e) {}
				try {
					verticalPanel.add(lblAttachment);
				} catch (Exception e) {}
				
				optionPanel = new HorizontalPanel();
				//optionPanel.setWidth("100%");
				optionPanel.setStylePrimaryName("info");
				Label time = new Label(activity.getTimeStamp().toString());
				time.setStylePrimaryName("date");
				final Label like = new Label("Like");
				final Label dislike = new Label("Dislike");
				activityService.hasRated(StreamED.user.getId(), activity.getId(), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						//Window.alert("hrmmmmm "+caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							like.setStylePrimaryName("clicked");
							dislike.setStylePrimaryName("clicked");
						} else {
							like.setStylePrimaryName("unclicked");
							dislike.setStylePrimaryName("unclicked");
							
							likeRegistration = like.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									activityService.thumbsUp(StreamED.user.getId(), activity.getId(), new AsyncCallback<Boolean>() {

										@Override
										public void onFailure(Throwable caught) {
											//Window.alert("thumb"+caught.getMessage());
										}

										@Override
										public void onSuccess(Boolean result) {
											if (result) {
												like.setStylePrimaryName("clicked");
												dislike.setStylePrimaryName("clicked");
												likeRegistration.removeHandler();
												dislikeRegistration.removeHandler();
												//hrlike.removeHandler();
												//History.newItem("RefreshStream", true);
											}
										}
										
									});
								}
								
							});
							
							dislikeRegistration = dislike.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									activityService.thumbsDown(StreamED.user.getId(), activity.getId(), new AsyncCallback<Boolean>() {

										@Override
										public void onFailure(Throwable caught) {
											Window.alert("thumbdown"+caught.getMessage());
										}

										@Override
										public void onSuccess(Boolean result) {
											if (result) {
												like.setStylePrimaryName("clicked");
												dislike.setStylePrimaryName("clicked");
												likeRegistration.removeHandler();
												dislikeRegistration.removeHandler();
												//History.newItem("RefreshStream", true);
											}
										}
										
									});
								}
							});
							
						}
					}
					
				});
				optionPanel.add(time);
				optionPanel.add(like);
				optionPanel.add(dislike);
				
				verticalPanel.add(optionPanel);
				
				ratingPanel = new HorizontalPanel();
				ratingPanel.setStylePrimaryName("rating");
				panel.add(ratingPanel);
				
				activityService.viewRatings(activity.getId(), new AsyncCallback<List<Integer>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to get ratings");
					}

					@Override
					public void onSuccess(List<Integer> result) {
						int likes = result.get(0), dislikes = -result.get(1);
						if (likes > 0 || dislikes > 0) {
							Label totalLikes = new Label(likes + " likes");
							Label totalDisLikes = new Label(dislikes + " dislikes");
							ratingPanel.add(totalLikes);
							ratingPanel.add(totalDisLikes);
							numLikes = likes;
							numDislikes = dislikes;
						}
						activityService.viewComments(activity.getId(), new AsyncCallback<List<CommentBean>>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Failed to retrieve comments");
							}

							@Override
							public void onSuccess(List<CommentBean> result) {
								
								if (result.size() > 0){
									for (final CommentBean comment: result) {
										/*final VerticalPanel commentPanel = new VerticalPanel();
										commentPanel.setStylePrimaryName("comment");
										*/
										userService.getUserByID(comment.getUserID(), new AsyncCallback<UserBean>() {
	
											@Override
											public void onFailure(Throwable caught) {
												Window.alert("user "+caught.getMessage());
											}
	
											@Override
											public void onSuccess(UserBean result) {
												/*HorizontalPanel commentheading = new HorizontalPanel();
												
												Label commentor = new Label(result.getName());
												commentor.setStylePrimaryName("commentor");
												Label commentTime = new Label("|"+comment.getTimeStamp());
												commentTime.setStylePrimaryName("date");
												commentheading.add(commentor);
												commentheading.add(commentTime);
												
												Label lblComment = new Label(comment.getMessage());
												commentPanel.add(commentheading);
												commentPanel.add(lblComment);
												*/
												CommentPanel commentPanel = new CommentPanel(result, comment.getTimeStamp().toString(), comment.getMessage());
												panel.add(commentPanel);
												panel.insert(commentPanel, panel.getWidgetIndex(commentFormPanel));
											}
											
										});
									}
								}
								
								// display comment form
								commentFormPanel = new SimplePanel();
								commentFormPanel.setStylePrimaryName("commentform");
								//commentFormPanel.setWidth("100%");
								commentField = new TextBox();
								//commentField.setWatermark("Write a comment...");
								commentField.getElement().setPropertyString("placeholder", "Write a comment...");
								
								commentField.setWidth("98%");
								commentField.addKeyUpHandler(new KeyUpHandler() {

									@Override
									public void onKeyUp(KeyUpEvent event) {
										if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
											String comment = commentField.getText();
											if (comment.length() > 0) {
												activityService.postComment(StreamED.user.getId(), comment, activity.getId(), new AsyncCallback<Boolean>() {

													@Override
													public void onFailure(
															Throwable caught) {
														Window.alert("streampaneli comment "+caught.getMessage());
													}

													@Override
													public void onSuccess(
															Boolean result) {
														if (result) {
															//Clear comment field
															commentField.setText("");
															//History.newItem("RefreshStream", true);
														}
													}
													
												});
											}
										}
									}
									
								});
								commentFormPanel.add(commentField);
								panel.add(commentFormPanel);
							}
							
						});
					}
					
				});
			}
		});
	}
	
	public Integer getActivityID() {
		return activityID;
	}
	
	public Integer getNumLikes() {
		return numLikes;
	}
	
	public Integer getNumDislikes() {
		return numDislikes;
	}

	public VerticalPanel getVPanel() {
		return panel;
	}
	
	public HorizontalPanel getOptionPanel() {
		return optionPanel;
	}
	
	public SimplePanel getCommentFormPanel() {
		return commentFormPanel;
	}
	
	public HorizontalPanel getRatingPanel() {
		return ratingPanel;
	}
	
	public TextBox getCommentField() {
		return commentField;
	}
}
