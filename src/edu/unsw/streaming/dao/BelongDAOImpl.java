package edu.unsw.streaming.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.unsw.streaming.bean.*;
import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *BelongDAOImpl used to access (retrieve and store) Belong information from the database
 */
public class BelongDAOImpl {
	/**
	 * allow a user to join a group
	 * @param belong - determines the role of a user
	 * @param groupId  - determines the group the user wants to join
	 * @return the boolean determining whether the join was successful
	 * @throws Exception
	 */
	public static Boolean joinGroup(BelongBean belong,Integer groupId) throws Exception{
		Connection conn = null;
		Boolean ret = false;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(groupId,"Group ID can't be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			try{
				try{
					stmt = conn.prepareStatement("INSERT INTO belong(userId,groupId,role) VALUES (?,?,?);");
					stmt.setInt(1, belong.getUserID());
					stmt.setInt(2,groupId);
					stmt.setString(3, belong.getRole());
					stmt.execute();
					ret = true;
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					stmt.close();
					conn.close();
				}
			}
		}
		return ret;		
		
	}
	/**
	 * ensures the user is able to leave a group
	 * @param belong - determines the role of a user
	 * @param groupId  - determines the group the user has joined
	 * @return the boolean determining whether the leave was successful
	 * @throws Exception
	 */
	public static Boolean leaveGroup(Integer userId,Integer groupId) throws Exception{
		Connection conn = null;
		Boolean ret = false;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(groupId,"Group ID can't be null");
			Validate.notNull(userId,"User ID can't be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			try{
				try{
					stmt = conn.prepareStatement("DELETE FROM belong WHERE userId = ? AND groupId = ?;");
					stmt.setInt(1, userId);
					stmt.setInt(2,groupId);
					stmt.execute();
					ret = true;
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					stmt.close();
					conn.close();
				}
			}
		}
		return ret;		
		
	}
	/**
	 * ensures the user is able to change a role in a group - run by administrator
	 * @param belong - determines the role of a user
	 * @param groupId  - determines the group the user has joined
	 * @return the boolean determining whether the change was successful
	 * @throws Exception
	 */
	public Boolean changeRole(BelongBean belong,Integer groupId) throws Exception{
		Connection conn = null;
		Boolean ret = false;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(groupId,"Group ID can't be null");
			Validate.notNull(belong,"Belong can't be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			try{
				try{
					stmt = conn.prepareStatement("UPDATE TABLE belong SET role = ? WHERE userId = ? AND groupId = ?;");
					stmt.setString(1, belong.getRole());
					stmt.setInt(2,belong.getUserID());
					stmt.setInt(3,groupId);
					stmt.execute();
					ret = true;
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					stmt.close();
					conn.close();
				}
			}
		}
		return ret;		
	}
	/**
	 * retrieves role of a user to determine access privileges
	 * @param userId
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public BelongBean retrieveRole(Integer userId,Integer groupId) throws Exception{
		Connection conn = null;
		BelongBean ret = null;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(groupId,"Group ID can't be null");
			Validate.notNull(userId,"Belong can't be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("SELECT role FROM belong WHERE userId = ? AND groupId = ?;");
					stmt.setInt(1, userId);
					stmt.setInt(2,groupId);
					rs = stmt.executeQuery();
					while(rs.next()){
						ret = new BelongBean(userId,rs.getString("role"));
					}
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(stmt != null){
					stmt.close();
					rs.close();
					conn.close();
				}
			}
		}
		return ret;				
	}
	/**
	 * retrieves the list of Groups a particular user belongs to
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> retrieveSubscribedGroups(Integer userID) throws Exception{
		List<Integer> groups = new ArrayList<Integer>();
		Connection conn = null;
		try{
			conn = JDBC.getConnection();
			Validate.notNull(userID, "User ID can't be null");
		}
		catch (Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("SELECT groupId FROM belong WHERE userId = ?;");
					stmt.setInt(1, userID);
					rs = stmt.executeQuery();
					while(rs.next()){
						groups.add(rs.getInt("groupId"));
					}
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL query");
				}
			}
			finally{
				if(stmt!=null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
			
		}
		
		return groups;
	}
}
