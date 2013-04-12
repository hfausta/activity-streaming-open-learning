package edu.unsw.streaming.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.service.UserServiceAsync;

/**
 * @author Lianne
 * HTML header layout
 * Contains logout button
 *
 */
public class BannerPanel extends Composite {
	private final UserServiceAsync userService = StreamED.userService;

	public BannerPanel() {
		SimplePanel horizontalPanel = new SimplePanel();
		initWidget(horizontalPanel);
		horizontalPanel.setStyleName("searchform");
		
		/*Button btnProfile = new Button("New button");
		btnProfile.setText("My Profile");
		horizontalPanel.add(btnProfile);
		*/
		Button btnLogout = new Button("Logout");
		horizontalPanel.add(btnLogout);
		btnLogout.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				userService.logout(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("failed to logout");
					}

					@Override
					public void onSuccess(Void result) {
						History.newItem("Logout", true);
					}
					
				});
			}
			
		});
	}

}
