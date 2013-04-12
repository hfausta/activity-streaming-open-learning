package edu.unsw.streaming.client.panel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.unsw.streaming.bean.UserBean;

public class CommentPanel extends Composite {
	
	public CommentPanel(UserBean user, String timeStamp, String message) {
		VerticalPanel vPanel = new VerticalPanel();
		initWidget(vPanel);
		vPanel.setStylePrimaryName("comment");
		HorizontalPanel commentHeading = new HorizontalPanel();
		Label commentor = new Label(user.getName());
		commentor.setStylePrimaryName("commentor");
		Label commentTime = new Label("|"+timeStamp);
		commentTime.setStylePrimaryName("date");
		commentHeading.add(commentor);
		commentHeading.add(commentTime);
		
		Label lblComment = new Label(message);
		vPanel.add(commentHeading);
		vPanel.add(lblComment);
	}

}
