package com.tq.cliniko.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class UtcTimeUtil {
	public static String getNowInUTC(String timezone) {
		String interim = ZonedDateTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.SECONDS)
							.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return interim + "Z";
	}
	
	public static String utcToBasicFormat(String utcDatetime) {
		return utcDatetime.replace("T", " ").replace("Z", "");
	}
}
