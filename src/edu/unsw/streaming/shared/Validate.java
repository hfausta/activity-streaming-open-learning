package edu.unsw.streaming.shared;

import java.sql.Date;

public class Validate {
	private enum Role {
		student,lecturer,mentor;
	
	@Override public String toString() {
		   //only capitalize the first letter
		   String s = super.toString();
		   return s.substring(0, 1) + s.substring(1).toLowerCase();
		 }
	};
	
	private enum Rate {
		t,f;
	
	@Override public String toString() {
		   //only capitalize the first letter
		   String s = super.toString();
		   return s.substring(0, 1) + s.substring(1).toLowerCase();
		 }
	};
	

	public static void notNull(Object obj, String exception) throws Exception{
		if(obj == null){
			throw new Exception(exception);
		}
	}
	
	public static void isNull(Object obj, String exception) throws Exception{
		if(obj instanceof Integer){
			if(!obj.equals(0)){
				throw new Exception(exception);
			}
		}
		else if(obj != null){
			throw new Exception(exception);
		}
	}
	
	public static void isAfterDate(java.util.Date date,String exception) throws Exception{
		if(new java.util.Date().compareTo(date)>0){
			throw new Exception(exception);
		}
			
	}
	
	public static void isMoreThanZero(Number number, String exception) throws Exception{
		if(number.intValue() < 0 ){
			throw new Exception(exception);
		}
	}
	public static void isRole(String string,String exception) throws Exception {
		if(Role.valueOf(string) != Role.lecturer && Role.valueOf(string) != Role.mentor && Role.valueOf(string) != Role.student){
			throw new Exception(exception);
		}
		
	}
	public static void isRate(String string,String exception) throws Exception {
		if(Rate.valueOf(string) != Rate.t && Rate.valueOf(string) != Rate.f){
			throw new Exception(exception);
		}
		
	}
	public static boolean isEmail(String email) {
		boolean ret = true;
		if(!email.matches("[a-zA-Z0-9_.-]+@[a-zA-Z0-9_.-]+")){
			ret = false;
		}
		return ret;
	}
	
	public static boolean isValidName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() >= 6;
	}
	
	public static boolean isValidPassword(String pw) {
		if (pw == null) {
			return false;
		}
		return pw.length() >= 4;
	}
	public static java.util.Date setDate(java.util.Date date,String ampm,String hour,String minute){
		Long min = 60000L;
		Long hr = 3600000L;
		Date ret = new Date(date.getTime() - (hr*(12)) + (hr*(Long.parseLong(hour)) + (min*(Long.parseLong(minute)))));
		if(ampm.equals("pm")){
			ret = new Date(ret.getTime() + (hr*(12)));
		}
		return ret;
	}

}
