package com.tq.common.lambda.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtils {
	private static final Logger m_log = LoggerFactory.getLogger(TimeUtils.class);

	public static String getPreviousTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -5);
		Date date = calendar.getTime();
		String currentTime = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(date);
		return currentTime;
	}

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

	public static String convertToLondonTime(DateTimeZone timeZone, String dateTime) {
		return convertTimeZone(timeZone, DateTimeZone.forID("GMT"), dateTime);
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

		SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss");
		SimpleDateFormat sdfs = new SimpleDateFormat("h:mm a");
		Date dt;
		try {
			dt = sdf.parse(time);
			return sdfs.format(dt);
		} catch (ParseException e) {

		}

		return null;
	}

	public static String getNowInGMT() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -5);
		Date date = calendar.getTime();
		String currentTime = sdf.format(date);
		return currentTime;
	}
	public static String getTimeFormatTwHour(String dateTime) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		String displayValue = null;
		try {
			date = dateFormatter.parse(dateTime);
			// Get time from date
			SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
			 displayValue = timeFormatter.format(date);
		} catch (ParseException e) {
		}
		return displayValue;

	}

}
