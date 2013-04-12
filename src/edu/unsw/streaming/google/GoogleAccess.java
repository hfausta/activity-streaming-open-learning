package edu.unsw.streaming.google;

import java.util.ArrayList;

//google libraries
import com.google.gdata.client.*;
import com.google.gdata.client.calendar.*;
import com.google.gdata.*;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.acl.*;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.data.extensions.BaseEventEntry.EventStatus;
import com.google.gdata.data.extensions.Reminder.Method;
import com.google.gdata.util.*;

import edu.unsw.streaming.bean.GroupBean;
import edu.unsw.streaming.bean.MeetingBean;
import edu.unsw.streaming.bean.UserBean;
import edu.unsw.streaming.client.StreamED;

import sample.util.*;

//other libraries
import java.net.*;
import java.io.*;
import java.util.Date;


//TO-DO FIX PRIVILEDGE OF MEMBERS
//TO-DO BAD URL EXCEPTION

public class GoogleAccess {

	public static String createCalendar(String calendarName, String description) throws MalformedURLException {
		CalendarService myService = new CalendarService("9323 activity stream");
		try {
			myService.setUserCredentials(Constants.username, Constants.password);
		} catch (AuthenticationException e) {
			System.out.println("Authentication Failed");
			e.printStackTrace();
		}

		deleteCalendar(calendarName);

		URL postURL = new URL("https://www.google.com/calendar/feeds/default/owncalendars/full");
		CalendarEntry newCalendar = new CalendarEntry();
		newCalendar.setTitle(new PlainTextConstruct(calendarName));
		//newCalendar.setCanEdit(false);
		TimeZoneProperty timeZone = new TimeZoneProperty();
		timeZone.setValue(Constants.defaultLocation);
		newCalendar.setTimeZone(timeZone);
		newCalendar.setContent(new PlainTextConstruct(description));
		//newCalendar.setAccessLevel(AccessLevelProperty.READ);
		//newCalendar.setHidden(HiddenProperty.FALSE);
		try {
			CalendarEntry insertedEntry = myService.insert(postURL, newCalendar);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}


		return getCalendarIDfromGoogle(calendarName);
	}

	public static String getCalendarSource(String groupName) throws MalformedURLException {
		if (groupName != null && !groupName.equals("")) {
			return "https://www.google.com/calendar/embed?title"+groupName+"%20Calendar"+
					"&amp;wkst=1&amp;bgcolor=%23FFFFFF&amp;src="+getCalendarIDfromGoogle(groupName).replace("@", "%40")+"&amp;color=%23875509&amp;ctz=Australia%2FSydney";
		} else {
			return null;
		}
	}
	
	public static String getCalendarSourceByUser(String userName, String email) {
		if (userName != null && email != null) {
			return "<iframe src=\"https://www.google.com/calendar/embed?title="+userName.replaceAll(" +", "_")+"%20Calendar"+
					"&amp;wkst=1&amp;bgcolor=%23FFFFFF&amp;src="+email.replace("@", "%40")+"&amp;color=%23875509&amp;ctz=Australia%2FSydney\" height=400 width=600> </iframe>";
		} else {
			return null;
		}
	}

	public static String createEvent(String groupName, String title, String content, Date startTime,
			String location, Date endTime, ArrayList<String> participant) throws MalformedURLException {
		CalendarService myService = new CalendarService("9323 activity stream");
		try {
			myService.setUserCredentials(Constants.username, Constants.password);
		} catch (AuthenticationException e) {
			System.out.println("Authentication Failed");
			e.printStackTrace();
		}

		URL postURL = new URL("http://www.google.com/calendar/feeds/"+getCalendarIDfromGoogle(groupName)+"/private/full");
		CalendarEventEntry myEvent = new CalendarEventEntry();

		//Set the title and description
		myEvent.setTitle(new PlainTextConstruct(groupName + ": " + title));
		myEvent.setContent(new PlainTextConstruct(content));

		//add participants
		for (int i = 0; i < participant.size(); i++) {
			EventWho part = new EventWho();
			part.setEmail(participant.get(i));
			myEvent.addParticipant(part);
		}

		//add location
		if (location != null) {
			Where where = new Where();
			where.setValueString(location);
			myEvent.addLocation(where);
		}
		//Create DateTime events and create a When object to hold them, then add
		//the When event to the event
		DateTime start = null;
		if (startTime != null) {
			start = new DateTime(startTime.getTime() + Constants.extraTime);
		} else {
			start = new DateTime(endTime.getTime() + Constants.extraTime);
		}
		DateTime end = new DateTime(endTime.getTime() + Constants.extraTime);
		When eventTimes = new When();
		eventTimes.setStartTime(start);
		eventTimes.setEndTime(end);
		myEvent.addTime(eventTimes);
		CalendarEventEntry insertedEntry = null;
		myEvent.setAnyoneCanAddSelf(false);
		myEvent.setGuestsCanInviteOthers(false);
		myEvent.setGuestsCanModify(false);
		myEvent.setSendEventNotifications(true);
		myEvent.setGuestsCanSeeGuests(true);
		Reminder r = new Reminder();
		r.setMinutes(60);
		r.setMethod(Method.EMAIL);
		myEvent.getReminder().add(r);
		// POST the request and receive the response:
		try {
			insertedEntry = myService.insert(postURL, myEvent);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		if(insertedEntry != null){
			return insertedEntry.getId();
		}
		else{
			return null;
		}
	}

	public static String getCalendarFrame(String groupName) throws MalformedURLException {
		String iframe = "<b>No Calendar to be displayed</b>";
		if (groupName != null && !groupName.equals("")) {
			System.out.println("current group = " + groupName);
			iframe = "<iframe src=\"https://www.google.com/calendar/embed?title"+groupName+"%20Calendar"+
					"&amp;height="+Constants.height+"&amp;wkst=1&amp;bgcolor=%23FFFFFF&amp;src="+getCalendarIDfromGoogle(groupName).replace("@", "%40")+"&amp;color=%23875509&amp;ctz=Australia%2FSydney\" " +
					"style=\" border-width:0 \" width=\""+Constants.width+"\" height=\""+Constants.height+"\" frameborder=\"0\" ></iframe>";
			System.out.println("iframe printed = " + iframe);
		}
		return iframe;
	}


	public static void cancelEvent(MeetingBean meeting) throws Exception {
		CalendarService myService = new CalendarService("9323 activity stream");
		try {
			myService.setUserCredentials(Constants.username, Constants.password);
		} catch (AuthenticationException e) {
			System.out.println("Authentication Failed");
			e.printStackTrace();
		}

		//GroupBean currGroup = GroupDAOImpl.retrieveGroupById(meeting.getParticipantID());
		GroupBean currGroup = StreamED.group;
		URL updateURL = new URL("http://www.google.com/calendar/feeds/"+getCalendarIDfromGoogle(currGroup.getName())+"/private/full");
		CalendarEventFeed resultFeed = myService.getFeed(updateURL, CalendarEventFeed.class);

		for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			CalendarEventEntry entry = resultFeed.getEntries().get(i);
			if (entry.getId().equals(meeting.getGoogleId())) {
				entry.setStatus(EventStatus.CANCELED);
				entry.update();
				i = resultFeed.getEntries().size();
			}
		}

	}

	private static void deleteCalendar(String calendarName) throws MalformedURLException {
		CalendarService myService = new CalendarService("9323 activity stream");
		try {
			myService.setUserCredentials(Constants.username, Constants.password);
		} catch (AuthenticationException e) {
			System.out.println("Authentication Failed");
			e.printStackTrace();
		}

		URL feedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");
		CalendarFeed resultFeed;
		try {
			resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);
			for (int i = 0; i < resultFeed.getEntries().size(); i++) {
				CalendarEntry entry = resultFeed.getEntries().get(i);
				if (entry.getTitle().getPlainText().equals(calendarName)) {
					entry.delete();
					i = resultFeed.getEntries().size();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}


	}

	//returns null for invalid calendar
	private static String getCalendarIDfromGoogle(String groupName) throws MalformedURLException {
		String calendarID = null;
		CalendarService myService = new CalendarService("9323 activity stream");
		try {
			myService.setUserCredentials(Constants.username, Constants.password);
		} catch (AuthenticationException e) {
			System.out.println("Authentication Failed");
			e.printStackTrace();
		}

		URL feedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");
		CalendarFeed resultFeed;
		try {
			resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);
			for (int i = 0; i < resultFeed.getEntries().size(); i++) {
				CalendarEntry entry = resultFeed.getEntries().get(i);
				if (entry.getTitle().getPlainText().equals(groupName)) {
					calendarID = entry.getId().split("/")[7].replace("%40", "@");
					i = resultFeed.getEntries().size();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return calendarID;
	}

}