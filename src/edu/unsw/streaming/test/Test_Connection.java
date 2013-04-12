package edu.unsw.streaming.test;
import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Test;

public class Test_Connection {

	@Test
	public void testConnection() {
		Connection conn = edu.unsw.streaming.dao.JDBC.getConnection();
		if(conn == null){
			fail("Connection fail");
		}
	}

}
