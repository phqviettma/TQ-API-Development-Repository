package com.tq.cliniko.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class UtcTimeUtil {
	public static String getNowInUTC(String timezone) {
		String interim = ZonedDateTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.SECONDS)
				.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return interim + "Z";
	}

	public static String utcToBasicFormat(String utcDatetime) {
		return utcDatetime.replace("T", " ").replace("Z", "");
	}

	public static String extractDate(String datetime) {
		String convertedDateTime = utcToBasicFormat(datetime);
		DateFormat f = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
		try {
			Date d = f.parse(convertedDateTime);
			DateFormat date = new SimpleDateFormat("YYYY-MM-DD");
			return date.format(d);
		} catch (ParseException e) {

		}
		return null;
	}

	public static String extractTime(String datetime) {
		String convertedDateTime = utcToBasicFormat(datetime);
		DateFormat f = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
		try {
			Date d = f.parse(convertedDateTime);
			DateFormat time = new SimpleDateFormat("hh:mm:ss");
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
}
