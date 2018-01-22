package com.tq.cliniko.time;

import static org.junit.Assert.assertFalse;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

public class UtcTimeUtilTest {
    //@Test
    public void test(){
        UtcTimeUtil.getNowInUTC("Australia/Melbourne");
        System.out.println(UtcTimeUtil.getNowInUTC("Australia/Melbourne"));
        assertFalse(UtcTimeUtil.getNowInUTC("Australia/Melbourne").isEmpty());
        
        System.out.println(DateTime.now(DateTimeZone.forID("Australia/Melbourne")));
        
        String newDate = UtcTimeUtil.extractDate("2017-11-23T22:00:00Z");
        
        System.out.println(newDate);
        
        System.out.println(UtcTimeUtil.convertToTzFromLondonTz(DateTimeZone.forID("Australia/Melbourne"), "2017-11-28T04:00:00Z"));
    }
    @Test
    public void testTime() {
    	
    	System.out.println(UtcTimeUtil.extractTime("2017-11-23T22:00:00Z"));
    	
    }
    @Test
    public void testParseDate() {
    	String input ="2018-10-11";
    	Date output = UtcTimeUtil.parseDate(input);
    	System.out.println(output);
    }
    @Test
    public void testParseTime() {
    	String time= UtcTimeUtil.getTimeFullOffset("2018-01-18T16:00:00", "Asia/Saigon");
    	System.out.println(time);
    }
}
