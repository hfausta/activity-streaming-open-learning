package edu.unsw.streaming.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.unsw.streaming.bean.UserBean;
import edu.unsw.streaming.dao.UserDAOImpl;
import edu.unsw.streaming.service.UserService;
/**
 * 
 * @author Charagh Jethnani
 *Business processes or services that a User can perform relating to User related functions
 */
@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService {
	@Override
	public UserBean login(String email, String pw) throws IllegalArgumentException {
		UserBean user = null;
		try {
			user = UserDAOImpl.retrieveUser(email);
			if (user != null) {
				if (!pw.contentEquals(user.getPassword())) {
					throw new IllegalArgumentException("Wrong password");
				} else {
					 HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
					  HttpSession session = httpServletRequest.getSession();
					  session.setAttribute("user", user);
				}
			} else {
				throw new IllegalArgumentException("User does not exist");
			}
			//Cookies.setCookie("sid", user.getId().toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new IllegalArgumentException(e.getMessage());
		}
		return user;
	}
	
	@Override
	public int register(String email, String pw, String name)
			throws IllegalArgumentException {
		int id = 0;
		try {
			UserBean newUser = new UserBean();
			newUser.setEmail(email);
			newUser.setPassword(pw);
			newUser.setName(name);
			newUser.setIsAdmin(false);
			id = UserDAOImpl.createUser(newUser);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return id;
	}
	
	@Override
	public String resetPassword(String email) throws IllegalArgumentException {
		/*UserBean user = null;
		try{
			user = UserDAOImpl.retrieveUser(email);
		}
		catch (Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}*/
		//user.setPassword(password)
		return null;
	}

	@Override
	public UserBean loginFromSessionServer() {
		UserBean user = null;
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		Object userObj = session.getAttribute("user");
		if (userObj != null && userObj instanceof UserBean) {
			user = (UserBean) userObj;
		}
		return user;
	}

	@Override
	public void logout() {
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		  HttpSession session = httpServletRequest.getSession();
		  session.invalidate();
	}

	@Override
	public UserBean getUserByID(int userID) throws IllegalArgumentException {
		try {
			return UserDAOImpl.retrieveUserById(userID);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}



}
