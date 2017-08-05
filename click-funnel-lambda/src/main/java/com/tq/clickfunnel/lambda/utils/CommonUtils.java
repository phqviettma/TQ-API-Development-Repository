package com.tq.clickfunnel.lambda.utils;

public  final class CommonUtils {

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
    
    public static String emptyVal(String value) {
        return isEmpty(value) ? "" : value;
    }
    
    public static String joinValues(String token, String... values) {
        if (values == null || values.length == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (String va : values) {
            String emptyVal = emptyVal(va);
            builder.append(emptyVal);
            if (!isEmpty(emptyVal)) builder.append(token);
        }
        String rebuil = builder.substring(0, builder.length() - (token.length()));
        return rebuil;
    }
}
