package edu.unsw.streaming.client.panel;

import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.user.client.ui.Composite;

import edu.unsw.streaming.client.StreamED;


import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;

import edu.unsw.streaming.bean.*;
import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.google.Constants;
import edu.unsw.streaming.service.MeetingServiceAsync;
/**
 * Container to display a calendar (to be implemented)
 * @author Lianne
 *
 */
public class CalendarPanel extends Composite {

	final static VerticalPanel verticalPanel = new VerticalPanel();
	static String source = null;
	public CalendarPanel() {
		verticalPanel.setWidth("100%");
		initWidget(verticalPanel);
		String userName = null;
		String email = null;
		if(StreamED.user != null){
			userName = StreamED.user.getName();
			email = StreamED.user.getEmail();
		}
		StreamED.meetingService.generateCalendarHTML(userName, email, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("failed: "+caught.getMessage());
			}

			@Override
			public void onSuccess(String html) {
				
				if (html != null) {
					
				/*	HTML panel = new HTML(html);
					panel.setSize("210px","550px");
					panel.addStyleName("demo-panel");
					panel.setVisible(true);*/
					source = html;
					System.out.println(html);
					//String htmlCad = "https://www.google.com/calendar/embed?title=Henry_F%20Calendar&amp;wkst=1&amp;bgcolor=%23FFFFFF&amp;src=rhendie_manutd%40hotmail.com&amp;color=%23875509&amp;ctz=Australia%2FSydney";

					//String htmlCad = "<iframe src=\"https://www.google.com/calendar/embed?title=Henry%20F%20Calendar&amp;wkst=1&amp;bgcolor=%23FFFFFF&amp;src=9m110fnjtbve5ctp9o8u36rak4%40group.calendar.google.com&amp;color=%23875509&amp;ctz=Australia%2FSydney\" height=400 width=600></iframe>";
					
					
					HTMLPanel panel = new HTMLPanel(html);
					//Frame panel = new Frame();
					panel.setStyleName("gwt-Frame");
					//panel.setWidth("100%");
					panel.setPixelSize(600, 400);
					panel.setVisible(true);
					
					Label lblCalendar = new Label("Calendar");
					verticalPanel.add(lblCalendar);
					
					verticalPanel.add(panel);
					verticalPanel.setVisible(true);
				} else {
					//Window.alert("There is no calendar to be displayed. Please select a group");
				}
			}
		});
	
	}
	
	public static VerticalPanel getVPanel(){
		return verticalPanel;
	}
	
	public static String source(){
		return source;
	}
	

}
