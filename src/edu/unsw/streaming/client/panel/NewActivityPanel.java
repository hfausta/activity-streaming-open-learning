package edu.unsw.streaming.client.panel;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.tractionsoftware.gwt.user.client.ui.UTCDateBox;
import com.tractionsoftware.gwt.user.client.ui.UTCDateTimeRangeController;
import com.tractionsoftware.gwt.user.client.ui.UTCTimeBox;

import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.service.ActivityServiceAsync;

/**
 * Container for posting a new activity, which can be an upload of course material/assignment, posting a message, or scheduling a meeting
 * @author Lianne
 *
 */
public class NewActivityPanel extends Composite {
	private final ActivityServiceAsync activityService = StreamED.activityService;

	private TextBox titleField;
	
	public NewActivityPanel() {

		final VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStylePrimaryName("form");
		initWidget(verticalPanel);

		Label title = new Label("New Activity");
		title.setStylePrimaryName("h1");
		verticalPanel.add(title);
		
		HorizontalPanel activityOptionsPanel = new HorizontalPanel();
		verticalPanel.add(activityOptionsPanel);

		final RadioButton rdbtnMessage = new RadioButton("new name", "Message");
		activityOptionsPanel.add(rdbtnMessage);

		final RadioButton rdbtnAssignment = new RadioButton("new name",
				"Assignment");
		activityOptionsPanel.add(rdbtnAssignment);

		final RadioButton rdbtnMaterial = new RadioButton("new name",
				"Material");
		activityOptionsPanel.add(rdbtnMaterial);

		final RadioButton rdbtnMeeting = new RadioButton("new name", "Meeting");
		activityOptionsPanel.add(rdbtnMeeting);

		HorizontalPanel titlePanel = new HorizontalPanel();
		Label lblTitle = new Label("Title");
		titlePanel.add(lblTitle);

		titleField = new TextBox();
		titlePanel.add(titleField);
		verticalPanel.add(titlePanel);

		HorizontalPanel descriptionPanel = new HorizontalPanel();
		Label lblNewLabel = new Label("Description");
		descriptionPanel.add(lblNewLabel);

		final TextArea descriptionField = new TextArea();
		descriptionPanel.add(descriptionField);
		verticalPanel.add(descriptionPanel);
		
		final HorizontalPanel attPanel = new HorizontalPanel();
		final Label attachment = new Label("Attachment");
		final FileUpload fileUpload = new FileUpload();
		attPanel.add(attachment);
		attPanel.add(fileUpload);
		attPanel.setVisible(false);
		verticalPanel.add(attPanel);

		final HorizontalPanel markPanel = new HorizontalPanel();
		final Label mark = new Label("Assignment weight (out of 100)");
		final TextBox markField = new TextBox();
		markPanel.add(mark);
		markPanel.add(markField);
		markPanel.setVisible(false);
		verticalPanel.add(markPanel);

		final HorizontalPanel startPanel = new HorizontalPanel();
		final Label start = new Label("Due Date");
		final UTCDateBox startDate = new UTCDateBox();
		final UTCTimeBox startTime = new UTCTimeBox();
		startDate.addValueChangeHandler(new ValueChangeHandler<Long>(){

			@Override
			public void onValueChange(ValueChangeEvent<Long> event) {
				if(event.getValue() < UTCDateBox.getValueForToday()){
					startDate.setValue(UTCDateBox.getValueForToday());
				}
				
			}
			
		});
		startTime.addValueChangeHandler(new ValueChangeHandler<Long>(){

			@Override
			public void onValueChange(ValueChangeEvent<Long> event) {
				if(event.getValue() < UTCTimeBox.getValueForNextHour()){
					startDate.setValue(UTCTimeBox.getValueForNextHour());
				}
				
			}
			
		});
		/*final DateBox start = new DateBox();
		start.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(
					final ValueChangeEvent<Date> dateValueChangeEvent) {
				if (dateValueChangeEvent.getValue().before(today())) {
					start.setValue(today(), false);
				}
			}
		});
		start.getDatePicker().addShowRangeHandler(
				new ShowRangeHandler<Date>() {
					@Override
					public void onShowRange(
							final ShowRangeEvent<Date> dateShowRangeEvent) {
						final Date today = today();
						Date d = zeroTime(dateShowRangeEvent.getStart());
						while (d.before(today)) {
							start.getDatePicker().setTransientEnabledOnDates(
									false, d);
							d = nextDay(d);
						}
					}
				});*/
		
		final HorizontalPanel endPanel = new HorizontalPanel();
		final Label end = new Label("End Date");
		final UTCDateBox endDate = new UTCDateBox();
		final UTCTimeBox endTime = new UTCTimeBox();
		endDate.addValueChangeHandler(new ValueChangeHandler<Long>(){

			@Override
			public void onValueChange(ValueChangeEvent<Long> event) {
				if(event.getValue() < UTCDateBox.getValueForToday()){
					endDate.setValue(UTCDateBox.getValueForToday());
				}
				
			}
			
		});
		/*final DateBox end = new DateBox();
		end.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(
					final ValueChangeEvent<Date> dateValueChangeEvent) {
				if (dateValueChangeEvent.getValue().before(today())) {
					end.setValue(today(), false);
				}
			}
		});
		end.getDatePicker().addShowRangeHandler(
				new ShowRangeHandler<Date>() {
					@Override
					public void onShowRange(
							final ShowRangeEvent<Date> dateShowRangeEvent) {
						final Date today = today();
						Date d = zeroTime(dateShowRangeEvent.getStart());
						while (d.before(today)) {
							end.getDatePicker().setTransientEnabledOnDates(
									false, d);
							d = nextDay(d);
						}
					}
				});*/
		
		CheckBox allday = new CheckBox("All Day");
	       
        // constructing this will bind all of the events
        new UTCDateTimeRangeController(startDate, startTime, endDate, endTime, allday);
        startDate.setValue(UTCDateBox.getValueForToday(), true);
        startTime.setValue(UTCTimeBox.getValueForNextHour(), true);
        
        startPanel.add(start);
		startPanel.add(startDate);
		startPanel.add(startTime);
		startPanel.add(allday);
		startPanel.setVisible(false);
		verticalPanel.add(startPanel);

		endPanel.add(end);
		endPanel.add(endDate);
		endPanel.add(endTime);
		endPanel.setVisible(false);
		verticalPanel.add(endPanel);
		// dateBox.setFormat(new
		// DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT)));

		//final Label time = new Label("Time");
		
		/*final ListBox hourField = new ListBox();
		for (int i = 1; i < 13; i++)
			hourField.addItem(i + "");
		final ListBox minField = new ListBox();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				minField.addItem("0" + i);
			else
				minField.addItem(i + "");
		}
		final ListBox ampmField = new ListBox();
		ampmField.addItem("am");
		ampmField.addItem("pm");*/
		
		//whenPanel.add(time);
		// whenPanel.add(dateField);
		/*
		timePanel.add(time);
		timePanel.add(hourField);
		timePanel.add(minField);
		timePanel.add(ampmField);
		whenPanel.add(timePanel);*/
		/*HourMinutePicker hourMinutePicker =
			    new HourMinutePicker(PickerFormat._12_HOUR);
		whenPanel.add(hourMinutePicker);*/
		

		Button btnPost = new Button("Post!");
		verticalPanel.setCellHorizontalAlignment(btnPost,
				HasHorizontalAlignment.ALIGN_RIGHT);
		verticalPanel.add(btnPost);

		// verticalPanel.add(new MaterialForm());
		rdbtnMessage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				attPanel.setVisible(false);
				markPanel.setVisible(false);
				startPanel.setVisible(false);
				endPanel.setVisible(false);
			}

		});

		rdbtnAssignment.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				attPanel.setVisible(true);
				markPanel.setVisible(true);
				start.setText("Due Date");
				startPanel.setVisible(true);
				endPanel.setVisible(false);
			}

		});

		rdbtnMaterial.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				attPanel.setVisible(true);
				markPanel.setVisible(false);
				startPanel.setVisible(false);
				endPanel.setVisible(false);
			}

		});

		rdbtnMeeting.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				attPanel.setVisible(false);
				markPanel.setVisible(false);
				start.setText("Start Date");
				startPanel.setVisible(true);
				endPanel.setVisible(true);
			}

		});

		btnPost.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (rdbtnMessage.getValue()) {

					activityService.postActivity(StreamED.user.getId(),
							StreamED.group.getId(), titleField.getText(),
							descriptionField.getText(),
							new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Failed to post message"
											+ caught.getMessage());
								}

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										Window.alert("Message posted");
										titleField.setText("");
										descriptionField.setText("");
										markField.setText("");
										attachment.setText("");
										startDate.setValue(UTCDateBox.getValueForToday(), true);
								        startTime.setValue(UTCTimeBox.getValueForNextHour(), true);
										//History.newItem("RefreshStream", true);
									}
								}

							});
				} else if (rdbtnAssignment.getValue()) {
					Date date = new Date(startDate.getValue()+startTime.getValue()-11*60*60*1000);
					/*String hour = hourField.getValue(hourField
							.getSelectedIndex());
					String minute = minField.getValue(minField
							.getSelectedIndex());
					String ampm = ampmField.getValue(ampmField
							.getSelectedIndex());
							*/

					// Date dueDate = Validate.setDate(date, hour, minute,
					// ampm);
					Window.alert(date.toString());
					activityService.postAssignment(StreamED.user.getId(),
							StreamED.group.getId(), titleField.getText(),
							descriptionField.getText(), date,
							Integer.parseInt(markField.getText()),
							attachment.getText(), new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("failed to post assignment: "
											+ caught.getMessage());
								}

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										Window.alert("Assignment posted");
										titleField.setText("");
										descriptionField.setText("");
										markField.setText("");
										attachment.setText("");
										startDate.setValue(UTCDateBox.getValueForToday(), true);
								        startTime.setValue(UTCTimeBox.getValueForNextHour(), true);
										//History.newItem("RefreshStream", true);
									}
								}

							});

				} else if (rdbtnMaterial.getValue()) {
					activityService.postMaterial(StreamED.user.getId(),
							StreamED.group.getId(), titleField.getText(),
							descriptionField.getText(), attachment.getText(),
							new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("failed to post material: "
											+ caught.getMessage());
								}

								@Override
								public void onSuccess(Boolean result) {
									Window.alert("Material posted");
									titleField.setText("");
									descriptionField.setText("");
									markField.setText("");
									attachment.setText("");
									startDate.setValue(UTCDateBox.getValueForToday(), true);
							        startTime.setValue(UTCTimeBox.getValueForNextHour(), true);
									//History.newItem("RefreshStream", true);
								}

							});
				} else if (rdbtnMeeting.getValue()) {
					Date startDateReal = new Date(startDate.getValue()+startTime.getValue()-10*60*60*1000);
					Date endDateReal = new Date(endDate.getValue()+endTime.getValue()-10*60*60*1000);
					/*String hour = hourField.getValue(hourField
							.getSelectedIndex());
					String minute = minField.getValue(minField
							.getSelectedIndex());
					String ampm = ampmField.getValue(ampmField
							.getSelectedIndex()); 
					*/
					 //Date meetingDateTime = Validate.setDate(date, hour,
					 //minute, ampm);
					StreamED.meetingService.scheduleMeeting(
							StreamED.user.getId(), StreamED.group.getId(),
							titleField.getText(), descriptionField.getText(),
							startDateReal, endDateReal, new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("failed to schedule meeting: "
											+ caught.getMessage());
								}

								@Override
								public void onSuccess(Boolean result) {
									Window.alert("Meeting scheduled");
									titleField.setText("");
									descriptionField.setText("");
									markField.setText("");
									attachment.setText("");
									startDate.setValue(UTCDateBox.getValueForToday(), true);
							        startTime.setValue(UTCTimeBox.getValueForNextHour(), true);
									//History.newItem("RefreshStream", true);
								}

							});
				}
			}

		});

	}

}
