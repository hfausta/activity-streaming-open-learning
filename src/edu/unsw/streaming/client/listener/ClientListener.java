package edu.unsw.streaming.client.listener;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import edu.unsw.streaming.bean.GroupBean;
import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.client.panel.ActivityStreamPanel;
import edu.unsw.streaming.client.panel.CalendarPanel;
import edu.unsw.streaming.client.panel.ChatPanel;
import edu.unsw.streaming.client.panel.MainPanel;
import edu.unsw.streaming.client.panel.NewActivityPanel;
import edu.unsw.streaming.client.panel.ProfilePanel;
import edu.unsw.streaming.client.panel.WelcomePanel;

public class ClientListener implements ValueChangeHandler<String> {

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if(event.getValue().equals("LoginSuccess")) {
			StreamED.group = null;
			StreamED.groupContainer.clear();
			StreamED.welcomeContainer.clear();
			StreamED.newActivityContainer.clear();
			StreamED.streamContainer.clear();
			StreamED.calendarContainer.clear();
			StreamED.daySchedulerContainer.clear();
			
			RootPanel.get().add(new MainPanel(StreamED.user.getId().toString()));
			//RootPanel.get().add(new MainPanel(Cookies.getCookie("sid")));
		} else if(event.getValue().equals("Logout")) {
			StreamED.headerContainer.clear();
			StreamED.groupContainer.clear();
			StreamED.welcomeContainer.clear();
			StreamED.newActivityContainer.clear();
			StreamED.streamContainer.clear();
			StreamED.calendarContainer.clear();
			StreamED.daySchedulerContainer.clear();
			StreamED.chatContainer.clear();
			StreamED.welcomeContainer.add(new WelcomePanel());
		} else if(event.getValue().equals("MyProfile")) {
			RootPanel.get().clear();
			RootPanel.get().add(ProfilePanel.returnWidget());
		} else if(event.getValue().startsWith("GroupChanged")) {
			final String groupstr = event.getValue().split("GroupChanged")[1];
			StreamED.groupService.getGroupByID(Integer.parseInt(groupstr), new AsyncCallback<GroupBean>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(GroupBean result) {
					StreamED.group = result;
					StreamED.chatContainer.clear();
					StreamED.chatContainer.add(new ChatPanel());
					StreamED.newActivityContainer.clear();
					StreamED.newActivityContainer.add(new NewActivityPanel());
					StreamED.streamContainer.clear();
					StreamED.streamContainer.add(new ActivityStreamPanel());
					StreamED.calendarContainer.clear();
					StreamED.calendarContainer.add(new CalendarPanel());
				}
				
			});
		}
	}
}
