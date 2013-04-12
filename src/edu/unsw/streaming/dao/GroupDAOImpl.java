package edu.unsw.streaming.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.unsw.streaming.bean.*;
import edu.unsw.streaming.shared.Validate;
import edu.unsw.streaming.google.GoogleAccess;
/**
 * 
 * @author Charagh Jethnani
 *GroupDAOImpl used to access (retrieve and store) Group information from the database
 */
public class GroupDAOImpl {
	public static Integer createGroup(GroupBean group) throws Exception{
		Connection conn = null;
		Integer ret = null;
		try{
			Validate.notNull(group,"Group can't be null");
			group.setId(ParticpantDAOImpl.addParticipant(group));
			conn = JDBC.getConnection();
		}
		catch(Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement("INSERT INTO \"Group\"(id,name,relatedGroup) VALUES (?,?,?);");
			stmt.setInt(1, group.getId());
			stmt.setString(2, group.getName());
			if(group.getRelatedGroup() == null){
				stmt.setNull(3, java.sql.Types.INTEGER);
			}
			else{
				stmt.setInt(3, group.getRelatedGroup());
			}
			stmt.execute();
			ret = group.getId();
		}
		catch(Exception ex){
			stmt.close();
			conn.close();
			ParticpantDAOImpl.deleteParticipant(group.getId());
			//throw new Exception("Unable to add Group");
			throw ex;
		}
		finally{
			if(stmt!= null && conn != null){
				stmt.close();
				conn.close();
				//create google Calendar to the group
				GoogleAccess.createCalendar(group.getName(),"Calendar for " + group.getName());
			}
		}

		return ret;
	}
	//IT DELETES ON CASCADE SO IT WILL AUTOMATICALLY REMOVE PARTICIPANT
	public static Boolean deleteGroup(Integer id) throws Exception{
		Connection conn = null;
		Boolean ret = false;
		try{
			conn = JDBC.getConnection();
			Validate.notNull(id,"Id can't be null");
		}
		catch(Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement("DELETE FROM \"Group\" WHERE id =?;");
			stmt.setInt(1, id);
			stmt.execute();
			ret = true;
		}
		catch (Exception ex){
			//throw new Exception("Unable to delete Group");
			throw ex;
		}
		finally{
			if(stmt!= null && conn != null){
				stmt.close();
				conn.close();
				ParticpantDAOImpl.deleteParticipant(id);
			}
		}
		return ret;
	}

	public Boolean updateGroup(GroupBean group) throws Exception{
		Connection conn = null;
		Boolean ret = false;
		try{
			conn = JDBC.getConnection();
			Validate.notNull(group,"Group can't be null");
		}
		catch(Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement("UPDATE \"Group\" SET relatedGroup = ?,name=? WHERE id = ?;");
			stmt.setInt(1, group.getRelatedGroup());
			stmt.setString(2, group.getName());
			stmt.setInt(3, group.getId());
			stmt.execute();
			ret = true;
		}
		catch(Exception ex){
			//throw new Exception("Update group failed");
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

	public static GroupBean retrieveGroup(String name) throws Exception{
		Connection conn = null;
		GroupBean ret = null;
		try{
			conn = JDBC.getConnection();
			Validate.notNull(name, "Name should not be null");
		}
		catch(Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement("SELECT * from \"Group\" WHERE name = ?;");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ret = new GroupBean(rs.getInt("id"),rs.getString("name"),rs.getInt("relatedGroup"));
			}
			stmt = conn.prepareStatement("SELECT * from belong where groupId = ?;");
			stmt.setInt(1, ret.getId());
			rs = stmt.executeQuery();
			while (rs.next()) {
				ret.addMember(rs.getInt("userid"), rs.getString("role"));
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

	public static List<GroupBean> retrieveMainGroups() throws Exception{
		Connection conn = null;
		List<GroupBean> ret = new ArrayList<GroupBean>();
		try{
			conn = JDBC.getConnection();
		}
		catch(Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement("SELECT * from \"Group\" WHERE relatedGroup = 0 or relatedGroup is null;");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ret.add(new GroupBean(rs.getInt("id"),rs.getString("name"),rs.getInt("relatedGroup")));
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

	public static List<GroupBean> retrieveSubGroups(GroupBean group) throws Exception{
		Connection conn = null;
		List<GroupBean> ret = new ArrayList<GroupBean>();
		try{
			conn = JDBC.getConnection();
			Validate.isNull(group.getRelatedGroup(), "Group has to be a main group");
		}
		catch(Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement("SELECT * from \"Group\" WHERE relatedGroup = ?;");
			stmt.setInt(1, group.getId());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ret.add(new GroupBean(rs.getInt("id"),rs.getString("name"),rs.getInt("relatedGroup")));
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
	public static GroupBean retrieveGroupById(Integer groupID) throws Exception {
		Connection conn = null;
		GroupBean ret = null;
		try{
			conn = JDBC.getConnection();
			Validate.notNull(groupID, "groupID should not be null");
		}
		catch(Exception ex){
			throw ex;
		}
		PreparedStatement stmt = null;
		try{
			stmt = conn.prepareStatement("SELECT * from \"Group\" WHERE id = ?;");
			stmt.setInt(1, groupID);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ret = new GroupBean(rs.getInt("id"),rs.getString("name"),rs.getInt("relatedGroup"));
			}
			stmt = conn.prepareStatement("SELECT * from belong where groupId = ?;");
			stmt.setInt(1, ret.getId());
			rs = stmt.executeQuery();
			while (rs.next()) {
				ret.addMember(rs.getInt("userid"), rs.getString("role"));
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

	public static List<GroupBean> searchGroupsByName(String query) throws Exception{
		Connection conn = null;
		List<GroupBean> ret = new ArrayList<GroupBean>();
		String sql = "SELECT * from \"Group\" WHERE lower(name) LIKE lower('%"+query+"%');";
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
				ret.add(new GroupBean(rs.getInt("id"),rs.getString("name"),rs.getInt("relatedGroup")));
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

	public static List<GroupBean> searchMainGroupsByName(String query) throws Exception{
		Connection conn = null;
		List<GroupBean> ret = new ArrayList<GroupBean>();
		String sql = "SELECT * from \"Group\" WHERE lower(name) LIKE lower('%"+query+"%') and relatedGroup is null;";
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
				ret.add(new GroupBean(rs.getInt("id"),rs.getString("name"),rs.getInt("relatedGroup")));
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