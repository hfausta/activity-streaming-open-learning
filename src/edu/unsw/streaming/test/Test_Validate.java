package edu.unsw.streaming.test;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import edu.unsw.streaming.shared.Validate;

public class Test_Validate {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Test(expected = Exception.class)
	public void testNotNullException() throws Exception {
		Validate.notNull(null, "It is null");
	}
	
	@Test
	public void testNotNull() throws Exception {
		Validate.notNull(new Date(), "It is null");
	}
	
	@Test
	public void testAfterDate() throws Exception {
		Validate.isAfterDate(sdf.parse("2020-10-10"), "It is after");
	}
	
	@Test (expected = Exception.class)
	public void testBeforeDate() throws Exception {
		Validate.isAfterDate(sdf.parse("2000-10-10"), "It is before");
	}
	
	@Test
	public void testInvalidEmail() throws Exception {
		Assert.assertEquals(false,Validate.isEmail("1231231"));
	}
	
	@Test
	public void testValidEmail() throws Exception {
		Assert.assertEquals(true,Validate.isEmail("csje047@cse.unsw.edu.au"));
	}
	
	@Test
	public void testMoreThanZero() throws Exception {
		Validate.isMoreThanZero(Double.valueOf(1), "More than zero");
		Validate.isMoreThanZero(Integer.valueOf(1), "More than zero");
		
	}
	
	@Test(expected = Exception.class)
	public void testLessThanZeroException() throws Exception {
		Validate.isMoreThanZero(Double.valueOf(-1), "More than zero");
	}

	@Test
	public void testValidRole() throws Exception {
		Validate.isRole("lecturer", "valid role");
	}
	@Test(expected = Exception.class)
	public void testInvalidRole() throws Exception {
		Validate.isRole("Lecturer", "invalid role");
	}
	@Test(expected = Exception.class)
	public void testInvalidRate() throws Exception {
		Validate.isRate("true","invalid rate");
	}
	@Test
	public void testValidRate() throws Exception {
		Validate.isRate("t","valid rate");
	}
	@Test
	public void testValidateSetDate() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm");
		Date date = sdf.parse("2012-01-01 12:00");
		date = Validate.setDate(date, "pm", "5", "05");
		String formattedDate = sdf.format(date);
		Assert.assertEquals("2012-01-01 17:05", formattedDate);
	}
}
