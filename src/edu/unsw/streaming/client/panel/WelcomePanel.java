package edu.unsw.streaming.client.panel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class WelcomePanel extends Composite {
	
	public WelcomePanel() {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSize("548px", "330px");
		horizontalPanel.setStylePrimaryName("welcome");
		initWidget(horizontalPanel);
		
		Welcome_LoginPanel loginPanel = new Welcome_LoginPanel();
		horizontalPanel.add(loginPanel);
		loginPanel.setSize("267px", "200px");

		Welcome_RegisterPanel registerPanel = new Welcome_RegisterPanel();
		horizontalPanel.add(registerPanel);
		registerPanel.setSize("267px", "330px");
	}	

}
