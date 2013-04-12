package edu.unsw.streaming.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.service.UserServiceAsync;
import edu.unsw.streaming.shared.Validate;
import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * Register new user container
 * 
 * @author Lianne
 * 
 */
public class Welcome_RegisterPanel extends Composite {
	private PasswordTextBox pw1Field;
	private PasswordTextBox pw2Field;
	private TextBox emailField;
	private TextBox nameField;
	private Button btnSignup;

	final DialogBox dialogBox = new DialogBox();
	final Button closeButton = new Button("Close");
	final Label textToServerLabel = new Label();
	final HTML serverResponseLabel = new HTML();

	/*
	 * private static final String SERVER_ERROR = "An error occurred while " +
	 * "attempting to contact the server. Please check your network " +
	 * "connection and try again.";
	 */
	private final UserServiceAsync userService = StreamED.userService;

	public Welcome_RegisterPanel() {

		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);

		Label instruction = new Label("or Create a new user account");
		instruction.setStylePrimaryName("h2");
		verticalPanel.add(instruction);

		Label lblIAmA = new Label("Name");
		verticalPanel.add(lblIAmA);

		nameField = new TextBox();
		verticalPanel.add(nameField);

		Label lblNewLabel = new Label("Email");
		verticalPanel.add(lblNewLabel);

		emailField = new TextBox();
		verticalPanel.add(emailField);

		Label lblPassword = new Label("Password");
		verticalPanel.add(lblPassword);

		pw1Field = new PasswordTextBox();
		verticalPanel.add(pw1Field);

		Label lblReenterPassword = new Label("Re-enter password");
		verticalPanel.add(lblReenterPassword);

		pw2Field = new PasswordTextBox();
		verticalPanel.add(pw2Field);

		// Create the popup dialog box
		dialogBox.setText("New User Registration");
		dialogBox.setAnimationEnabled(true);

		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");

		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				btnSignup.setEnabled(true);
				btnSignup.setFocus(true);
			}
		});

		btnSignup = new Button("New button");
		btnSignup.setText("Sign Up!");
		SignupHandler handler = new SignupHandler();
		btnSignup.addClickHandler(handler);
		pw2Field.addKeyUpHandler(handler);
		verticalPanel.add(btnSignup);
	}

	class SignupHandler implements ClickHandler, KeyUpHandler {
		/**
		 * Fired when the user clicks on the loginButton.
		 */
		public void onClick(ClickEvent event) {
			sendDetailsToServer();
		}

		/**
		 * Fired when the user types in the nameField.
		 */
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				sendDetailsToServer();
			}
		}

		/**
		 * Send the name from the nameField to the server and wait for a
		 * response.
		 * 
		 * @throws Exception
		 */
		private void sendDetailsToServer() {
			String pw1 = pw1Field.getText();
			String pw2 = pw2Field.getText();
			String email = emailField.getText();
			String name = nameField.getText();

			if (!Validate.isEmail(email)) {
				Window.alert("Invalid email");
				return;
			} else if (!Validate.isValidPassword(pw1)) {
				Window.alert("Invalid password");
				return;
			} else if (!pw1.contentEquals(pw2)) {
				Window.alert("Please re-enter the same password");
				return;
			}

			btnSignup.setEnabled(false);
			textToServerLabel.setText(email);
			serverResponseLabel.setText("");

			userService.register(email, pw2, name,
					new AsyncCallback<Integer>() {
						@Override
						public void onFailure(Throwable caught) {
							dialogBox.setText("New User Registration - Failed");
							serverResponseLabel
									.addStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML(caught.getMessage());
							dialogBox.center();
							closeButton.setFocus(true);
						}

						@Override
						public void onSuccess(Integer result) {
							dialogBox
									.setText("New User Registration - Success");
							serverResponseLabel
									.removeStyleName("serverResponseLabelError");
							serverResponseLabel.setHTML("User ID : "
									+ result.toString());
							dialogBox.center();
							closeButton.setFocus(true);

						}

					});
			/*
			 * ServiceDefTarget serviceDef = (ServiceDefTarget) userService;
			 * serviceDef.setServiceEntryPoint("/streamed/userservice");
			 * GroupListCallBack grouplistCallBack = new GroupListCallBack();
			 * userService.viewGroups(id, grouplistCallBack);
			 */
		}
	}

}
