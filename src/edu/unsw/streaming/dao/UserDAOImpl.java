package edu.unsw.streaming.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.unsw.streaming.bean.UserBean;
import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *UserDAOImpl used to access (retrieve and store) User information from the database
 */
public class UserDAOImpl {
	public static Integer createUser(UserBean user) throws Exception {
		Connection conn = null;
		Integer ret = null;
		try {
			Validate.notNull(user, "User can't be null");
			user.setId(ParticpantDAOImpl.addParticipant(user));
			conn = JDBC.getConnection();
		} catch (Exception ex) {
			throw ex;
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("INSERT INTO \"User\" (id,email,password,is_admin,name) VALUES (?,?,?,?,?);");
			stmt.setInt(1, user.getId());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getPassword());
			stmt.setBoolean(4, user.getIsAdmin());
			stmt.setString(5, user.getName());
			stmt.execute();
			ret = user.getId();
		} catch (Exception ex) {
			stmt.close();
			conn.close();
			ParticpantDAOImpl.deleteParticipant(user.getId());
			// throw new Exception("Unable to add User");
			throw ex;
		}

		finally {
			if (stmt != null && conn != null) {
				stmt.close();
				conn.close();
			}
		}

		return ret;
	}

	public static Boolean deleteUser(Integer id) throws Exception {
		Connection conn = null;
		Boolean ret = false;
		try {
			Validate.notNull(id, "Id can't be null");
			conn = JDBC.getConnection();
		} catch (Exception ex) {
			throw ex;
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("DELETE FROM  \"User\" WHERE id =?;");
			stmt.setInt(1, id);
			stmt.execute();
			ret = true;
		} catch (Exception ex) {
			// throw new Exception("Unable to delete User");
			throw ex;
		} finally {
			if (stmt != null && conn != null) {
				stmt.close();
				conn.close();
				ParticpantDAOImpl.deleteParticipant(id);
			}
		}
		return ret;
	}

	public static Boolean updateUser(UserBean user) throws Exception {
		Connection conn = null;
		Boolean ret = false;
		try {
			Validate.notNull(user, "User can't be null");
			conn = JDBC.getConnection();
		} catch (Exception ex) {
			throw ex;
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("UPDATE \"User\" SET email = ?, password = ?, is_admin = ?,name=? WHERE id = ?;");
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getPassword());
			stmt.setBoolean(3, user.getIsAdmin());
			stmt.setString(4, user.getName());
			stmt.setInt(5, user.getId());
			stmt.execute();
			ret = true;
		} catch (Exception ex) {
			// throw new Exception("Update user failed");
			throw ex;
		} finally {
			if (stmt != null && conn != null) {
				stmt.close();
				conn.close();
			}
		}
		return ret;
	}

	public static UserBean retrieveUser(String email) throws Exception {
		Connection conn = null;
		UserBean ret = null;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(email, "Email can't be null");
			Validate.isEmail(email);
		} catch (Exception ex) {
			throw ex;
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("SELECT * from \"User\" WHERE email = ?;");
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ret = new UserBean(rs.getInt("id"), rs.getString("name"),
						email, rs.getString("password"));
			}

		} catch (Exception ex) {
			// throw new Exception("Can't retrieve User");
			throw ex;
		} finally {
			if (stmt != null && conn != null) {
				stmt.close();
				conn.close();

			}
		}
		return ret;
	}

	public static UserBean retrieveUserById(Integer id) throws Exception {
		Connection conn = null;
		UserBean ret = null;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(id, "User id can't be null");
		} catch (Exception ex) {
			throw ex;
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement("SELECT * from \"User\" WHERE id = ?;");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			ret = new UserBean(id, rs.getString("name"), rs.getString("email"),
					rs.getString("password"));
		} catch (Exception ex) {
			// throw new Exception("Can't retrieve User");
			throw ex;
		} finally {
			if (stmt != null && conn != null) {
				stmt.close();
				conn.close();

			}
		}
		return ret;
	}
	
	public static List<UserBean> searchUsersByName(String query) throws Exception{
		Connection conn = null;
		List<UserBean> ret = new ArrayList<UserBean>();
		String sql = "SELECT * from \"User\" WHERE lower(name) LIKE lower('%"+query+"%');";
		try{
			conn = JDBC.getConnection();
			Validate.notNull(query, "Query can't be null");
		}
		catch(Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		try{
			
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ret.add(new UserBean(rs.getInt("id"), rs.getString("name"), rs.getString("email"),rs.getString("password")));
			}
		}
		catch(Exception ex){
			//throw new Exception("Can't retrieve Group");
			throw ex;
		}
		finally{
			if(stmt!= null && conn != null){
				stmt.close();
				conn.close();
				
			}
		}
		return ret;	
	}
}
