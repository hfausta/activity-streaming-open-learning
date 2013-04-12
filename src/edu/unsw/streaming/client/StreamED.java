package edu.unsw.streaming.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

import edu.unsw.streaming.bean.GroupBean;
import edu.unsw.streaming.bean.UserBean;
import edu.unsw.streaming.client.listener.ClientListener;
import edu.unsw.streaming.client.listener.NewActivityListener;
import edu.unsw.streaming.client.panel.WelcomePanel;
import edu.unsw.streaming.service.ActivityService;
import edu.unsw.streaming.service.ActivityServiceAsync;
import edu.unsw.streaming.service.GroupService;
import edu.unsw.streaming.service.GroupServiceAsync;
import edu.unsw.streaming.service.MeetingService;
import edu.unsw.streaming.service.MeetingServiceAsync;
import edu.unsw.streaming.service.UserService;
import edu.unsw.streaming.service.UserServiceAsync;

public class StreamED implements EntryPoint {
	// Services
	public static final UserServiceAsync userService = GWT.create(UserService.class);
	public static final GroupServiceAsync groupService = GWT.create(GroupService.class);
	public static final ActivityServiceAsync activityService = GWT.create(ActivityService.class);
	public static final MeetingServiceAsync meetingService = GWT.create(MeetingService.class);
	
	// Beans
	public static UserBean user;
	public static GroupBean group;
	
	// Panels
	
	public static RootPanel headerContainer = RootPanel.get("headerContainer");
	public static RootPanel welcomeContainer = RootPanel.get("welcomeContainer");
	public static RootPanel groupContainer = RootPanel.get("groupContainer");
	public static RootPanel newActivityContainer = RootPanel.get("newActivityContainer");
	public static RootPanel streamContainer = RootPanel.get("streamContainer");
	public static RootPanel calendarContainer = RootPanel.get("calendarContainer");
	public static RootPanel daySchedulerContainer = RootPanel.get("daySchedulerContainer");
	public static RootPanel chatContainer = RootPanel.get("chatContainer");
	
	public static String HEADERCONTAINER = "headerContainer";
	public static String WELCOMECONTAINER = "welcomeContainer";
	public static String GROUPCONTAINER = "groupContainer";
	public static String NEWACTIVITYCONTAINER = "newActivityContainer";
	public static String STREAMCONTAINER = "streamContainer";
	public static String CALENDARCONTAINER = "calendarContainer";
	public static String DAYSCHEDULERCONTAINER = "daySchedulerContainer";

	private static final Domain DOMAIN = DomainFactory.getDomain("Activity_Domain");
	
	public void onModuleLoad() {
		getUserSessionInfo();
		
		//Adding listener for push notification
		RemoteEventServiceFactory resf = RemoteEventServiceFactory.getInstance();
		RemoteEventService res = resf.getRemoteEventService();
		
		res.addListener(DOMAIN, new NewActivityListener());
		
		History.addValueChangeHandler(new ClientListener());
		History.fireCurrentHistoryState();
	}

	public void onLoggedIn() {
		History.newItem("LoginSuccess", true);
		//Cookies.setCookie("sid", user.getId().toString());
	}

	private void getUserSessionInfo() {
		userService.loginFromSessionServer(new AsyncCallback<UserBean>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("failed to contact server");
			}

			@Override
			public void onSuccess(UserBean result) {
				if (result == null) {
					welcomeContainer.add(new WelcomePanel());
				} else {
					user = result;
					onLoggedIn();
				}
			}

		});
	}
}
