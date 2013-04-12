package edu.unsw.streaming.service;

import edu.unsw.streaming.bean.UserBean;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {
	void login(String id, String pw, AsyncCallback<UserBean> callback);
	
	void loginFromSessionServer(AsyncCallback<UserBean> callback);
	
	void logout(AsyncCallback<Void> callback);

	void register(String email, String pw, String name, AsyncCallback<Integer> callback);

	void resetPassword(String email, AsyncCallback<String> callback);
	
	void getUserByID(int userID, AsyncCallback<UserBean> callback);
}
