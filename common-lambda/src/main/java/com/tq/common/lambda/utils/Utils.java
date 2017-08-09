package com.tq.common.lambda.utils;

import java.util.Date;

import com.tq.common.lambda.config.Config;

public final class Utils {

    public static final String EMPTY_STRING = "";

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String emptyVal(String value) {
        return isEmpty(value) ? EMPTY_STRING : value;
    }

    public static String joinValues(String token, String... values) {
        if (values == null || values.length == 0)
            return EMPTY_STRING;
        StringBuilder builder = new StringBuilder();
        for (String va : values) {
            String emptyVal = emptyVal(va);
            builder.append(emptyVal);
            if (!isEmpty(emptyVal))
                builder.append(token);
        }

        if (builder.toString().isEmpty())
            return EMPTY_STRING;
        String rebuil = builder.substring(0, builder.length() - (token.length()));
        return rebuil;
    }

    public static String formatDate(Object date) {
        if (date == null)
            return EMPTY_STRING;
        return Config.DATE_FORMAT_24_H.format((Date) date);
    }
}
