package edu.unsw.streaming.client.panel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Container for displaying user profile (to be implemented)
 * @author Lianne
 *
 */
public class ProfilePanel extends Composite {

	private VerticalPanel vPanel;
	static private ProfilePanel widget;
	
	public ProfilePanel() {
		initPage();
		initWidget(vPanel);
	}
	
	public static ProfilePanel returnWidget() {
		if(widget == null) {
			widget = new ProfilePanel();
		}
		return widget;
	}
	
	private void initPage() {
		
	}

}
