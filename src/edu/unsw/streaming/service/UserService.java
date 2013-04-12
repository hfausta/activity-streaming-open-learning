package edu.unsw.streaming.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.unsw.streaming.bean.UserBean;

@RemoteServiceRelativePath("userservice")
public interface UserService extends RemoteService {
	@SuppressWarnings("serial")
	static class NotLoggedInException extends Exception {
		public NotLoggedInException() {
			super();
		}
		public NotLoggedInException(String error) {
			super(error);
		}
	}
	
	UserBean login(String id, String pw) throws IllegalArgumentException;
	
	UserBean loginFromSessionServer();
	
	void logout();

	int register(String email, String pw, String name) throws IllegalArgumentException;

	String resetPassword(String email) throws IllegalArgumentException;
	
	UserBean getUserByID(int userID) throws IllegalArgumentException;
}
