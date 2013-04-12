package edu.unsw.streaming.client.panel;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.service.ActivityServiceAsync;

/**
 * Container for posting a new activity, which can be an upload of course material/assignment, posting a message, or scheduling a meeting
 * @author Lianne
 *
 */
public class ChatPanel extends Composite {
	private final ActivityServiceAsync activityService = StreamED.activityService;

	//for chat
	private static TextArea chatMessages;
	private static TextBox typeMessage;
	
	public ChatPanel() {

		final VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);

		//For chat
		Label chatTitle = new Label("Chat with " + StreamED.group.getName());
		chatTitle.setStylePrimaryName("h1");
		verticalPanel.add(chatTitle);
		chatMessages = new TextArea();
		typeMessage = new TextBox();
		chatMessages.setWidth("100%");
		typeMessage.setWidth("90%");
		typeMessage.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if(typeMessage.getText().length() > 0) {
						activityService.groupChat(StreamED.group, StreamED.user.getName(), typeMessage.getText(), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								typeMessage.setText("");
							}
							
						});
					}
				}
			}
		});
		verticalPanel.add(chatMessages);
		verticalPanel.add(typeMessage);
	}
	
	public static TextArea getChatMessages() {
		return chatMessages;
	}
	
	public static TextBox getTypeMessage() {
		return typeMessage;
	}

}
