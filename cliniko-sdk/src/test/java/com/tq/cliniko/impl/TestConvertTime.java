package com.tq.cliniko.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TestConvertTime {
	public static void main(String[] args) throws ParseException {
		 TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
		 TimeZone.setDefault(tz);
		DateFormat dateFormatUtc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    dateFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));

	    String dateStrInPDT = "2017-11-14 11:00:00";
	    Date dateInPDT = dateFormat.parse(dateStrInPDT);


	    String dateInUtc = dateFormatUtc.format(dateInPDT);

	    System.out.println("Date In UTC is " + dateInUtc);
	}
	   
}
