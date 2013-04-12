package edu.unsw.streaming.client.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.unsw.streaming.bean.GroupBean;
import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.service.GroupServiceAsync;

/**
 * Container holding the collection of containers needed for the web application (excluding BannerPanel)
 * @author Lianne
 *
 */
public class MainPanel extends Composite {
	
	private final GroupServiceAsync groupService = StreamED.groupService;
	
	private VerticalPanel vPanel;
	
	public MainPanel(final String userID) {
		vPanel = new VerticalPanel();
		initWidget(vPanel);
		
		final BannerPanel bpanel = new BannerPanel();
		final GroupPanel gpanel = new GroupPanel();
		
		groupService.viewGroups(Integer.parseInt(userID), new AsyncCallback<List<GroupBean>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("userID not available");
			}

			@Override
			public void onSuccess(List<GroupBean> result) {
				List<Integer> participants = new ArrayList<Integer>();
				for (GroupBean group: result) {
					participants.add(group.getId());
				}
				participants.add(Integer.parseInt(userID));
				
				ActivityStreamPanel apanel = new ActivityStreamPanel(participants);
				//NewActivityPanel napanel = new NewActivityPanel(userID);
				RootPanel.get("headerContainer").add(bpanel);
				RootPanel.get("groupContainer").add(gpanel);
				//RootPanel.get("newActivityContainer").add(napanel);
				RootPanel.get("streamContainer").add(apanel);
			}
		});
		
	}
}
