package com.tq.cliniko.time;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtcTimeUtilTest {
    @Test
    public void test(){
        UtcTimeUtil.getNowInUTC("Australia/Melbourne");
        assertFalse(UtcTimeUtil.getNowInUTC("Australia/Melbourne").isEmpty());
    }
}
