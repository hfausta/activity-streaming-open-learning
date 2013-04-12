package edu.unsw.streaming.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import edu.unsw.streaming.bean.ParticipantBean;
import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *ParticipantDAOImpl used to access (retrieve and store) Participant ID (group or user) from the database
 */
public class ParticpantDAOImpl {
	public static Integer addParticipant(ParticipantBean participant) throws Exception{
		Connection conn = null;
		Integer ret = null;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(participant,"Participant can't be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("INSERT INTO Participant values(DEFAULT) RETURNING id;");
					rs = stmt.executeQuery();
					rs.next();
					ret = rs.getInt("id");
				}
				catch(Exception ex){
					//throw new Exception("Error in SQL Query");
					throw ex;
				}
			}
			finally{
				if(rs != null && stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return ret;
	}
	
	public static Boolean deleteParticipant(Integer id) throws Exception{
			Connection conn = null;
			Boolean ret = false;
			try {
				conn = JDBC.getConnection();
				Validate.notNull(id,"Id can't be null");
			}
			catch(Exception ex){
				throw ex;
			}
			if(conn!=null){
				PreparedStatement stmt = null;
				ResultSet rs = null;
				try{
					try{
						stmt = conn.prepareStatement("DELETE FROM Participant WHERE id = ?;");
						stmt.setInt(1, id);
						stmt.execute();
						ret = true;
					}
					catch(Exception ex){
						//throw new Exception("Error in SQL Query");
						throw ex;
					}
				}
				finally{
					if(rs != null && stmt != null){
						rs.close();
						stmt.close();
						conn.close();
					}
				}
			}
			return ret;		
	}
	
	public static Integer getParticipant(Integer id) throws Exception{
		Connection conn = null;
		Integer ret = null;
		try {
			conn = JDBC.getConnection();
			Validate.notNull(id,"ID can't be null");
		}
		catch(Exception ex){
			throw ex;
		}
		if(conn!=null){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try{
				try{
					stmt = conn.prepareStatement("SELECT * FROM Participant WHERE id=?;");
					stmt.setInt(1, id);
					rs = stmt.executeQuery();
					while(rs.next()){
						ret = rs.getInt("id");
					}
				}
				catch(Exception ex){
					throw ex;
					//throw new Exception("Error in SQL Query");
				}
			}
			finally{
				if(rs != null && stmt != null){
					rs.close();
					stmt.close();
					conn.close();
				}
			}
		}
		return ret;
	}
}
