package com.tq.googlecalendar.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.googlecalendar.resp.Items;

public class TimeUtils {
	private static final Logger m_log = LoggerFactory.getLogger(TimeUtils.class);

	public static String getNowInUTC(String timezone) {
		String interim = ZonedDateTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.SECONDS)
				.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return interim + "Z";
	}

	public static String utcToBasicFormat(String utcDatetime) {
		return utcDatetime.replace("T", " ").replace("Z", "");
	}

	public static String getTimeFullOffset(String time, String timeZone) {
		DateTimeZone dateTimeZone = DateTimeZone.forID(timeZone);
		String fullTime = TimeUtils.parseTime(time);
		DateTime convertTime = new DateTime(fullTime, dateTimeZone);
		return convertTime.toString();

	}

	public static String extractDate(String datetime) {
		String convertedDateTime = utcToBasicFormat(datetime);
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date d = f.parse(convertedDateTime);
			DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			return date.format(d);
		} catch (ParseException e) {
			m_log.info("" + e);
		}
		return null;
	}

	public static String extractTimeHMS(String datetime) {
		String convertedDateTime = utcToBasicFormat(datetime);
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = f.parse(convertedDateTime);
			DateFormat time = new SimpleDateFormat("HH:mm:ss");
			return time.format(d);
		} catch (ParseException e) {

		}
		return null;
	}
	
	public static String extractTime(String datetime) {
		String convertedDateTime = utcToBasicFormat(datetime);
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = f.parse(convertedDateTime);
			DateFormat time = new SimpleDateFormat("HH:mm");
			return time.format(d);
		} catch (ParseException e) {

		}
		return null;
	}

	public static String parseTimeUTC(String time) {
		String t = time.replace(" ", "T").concat("Z");
		return t;
	}

	public static String parseTime(String time) {
		String t = time.replace(" ", "T");
		return t;
	}

	public static Date parseDate(String date) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		Date t;
		try {
			t = ft.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}

		return t;
	}

	public static String convertTimeZone(DateTimeZone srcTimeZone, DateTimeZone destTimeZone, String datetime) {
		DateTime srcDt = new DateTime(datetime, srcTimeZone);
		DateTime destDt = srcDt.withZone(destTimeZone);
		return destDt.toString();
	}

	public static String convertToTzFromLondonTz(DateTimeZone destTimeZone, String datetime) {
		return convertTimeZone(DateTimeZone.forID("Europe/London"), destTimeZone, datetime);
	}

	public static String extractDateSbm(String datetime) {
		String convertedDateTime = utcToBasicFormat(datetime);
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date d = f.parse(convertedDateTime);
			DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			return date.format(d);
		} catch (ParseException e) {

		}
		return null;
	}

	public static String extractTimeSbm(String datetime) {
		StringTokenizer tk = new StringTokenizer(datetime);
		String date = tk.nextToken();
		String time = tk.nextToken();

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
		Date dt;
		try {
			dt = sdf.parse(time);
			return sdfs.format(dt);
		} catch (ParseException e) {

		}

		return null;
	}
	
	public static String convertAndGetStartDateTimeGoogleEvent(Items event) {
		String dateTime = event.getStart().getDateTime();
		String eventTimeZone = event.getStart().getTimeZone();
		if (eventTimeZone != null) {
			dateTime = convertToTzFromLondonTz(DateTimeZone.forID(eventTimeZone), dateTime);
		}
		return dateTime;
	}
	
	public static String convertAndGetEndDateTimeGoogleEvent(Items event) {
		String dateTime = event.getEnd().getDateTime();
		String eventTimeZone = event.getEnd().getTimeZone();
		if (eventTimeZone != null) {
			dateTime = convertToTzFromLondonTz(DateTimeZone.forID(eventTimeZone), dateTime);
		}
		return dateTime;
	}
	
	public static String convertAndGetStartDateTimeGoogleEvent(SbmGoogleCalendar event) {
		String dateTime = event.getStartDateTime();
		String eventTimeZone = event.getStartTimeZone();
		if (eventTimeZone != null) {
			dateTime = convertToTzFromLondonTz(DateTimeZone.forID(eventTimeZone), dateTime);
		}
		return dateTime;
	}
	
	public static String convertAndGetEndDateTimeGoogleEvent(SbmGoogleCalendar event) {
		String dateTime = event.getEndDateTime();
		String eventTimeZone = event.getEndTimeZone();
		if (eventTimeZone != null) {
			dateTime = convertToTzFromLondonTz(DateTimeZone.forID(eventTimeZone), dateTime);
		}
		return dateTime;
	}
}