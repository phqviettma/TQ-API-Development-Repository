package com.tq.simplybook.lambda.handler;

import static org.mockito.Mockito.mock;

import com.tq.simplybook.lambda.context.Env;

public class MockUtil {
    public static Env mockEnv() {
        Env env = mock(Env.class);
        return env;
    }
}
