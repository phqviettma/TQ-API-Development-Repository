package com.tq.common.lambda.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonUtils {

    public static String getJsonString(InputStream reader) {
        StringBuilder jsonBuff = new StringBuilder();
        String line = "";
        try (BufferedReader buff = new BufferedReader(new InputStreamReader(reader))) {
            while ((line = buff.readLine()) != null)
                jsonBuff.append(line);

        } catch (Exception e) {
            /* error */
        }
        return jsonBuff.toString();
    }
}
