package com.tq.clickfunnel.lambda.utils;

public final class Env {
    public static final String getEnv(String name) {
        return System.getenv(name);
    }
}
