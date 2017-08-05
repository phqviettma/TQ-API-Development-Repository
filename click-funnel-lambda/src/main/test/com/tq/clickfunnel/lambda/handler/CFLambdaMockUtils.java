package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class CFLambdaMockUtils {
    /**
     * the script command for setting up the environment
     * See more Config.java 
     * @throws IOException
     */
    public static void initDefaultsEnvOnWin() throws IOException {
        
        ProcessBuilder pb = new ProcessBuilder("CMD", "/C", "SET");
        Map<String, String> env = pb.environment();
        
        env.put("INFUSIONSOFT_API_KEY", "https://uf238.infusionsoft.com/api/xmlrpc");
        env.put("Config.INFUSIONSOFT_API_KEY", "");
        env.put("Config.INFUSION_ORDER_PROMO_CODE", "");
        
        env.put("Config.SIMPLY_BOOK_SERVICE_URL", "https://user-api.simplybook.me/");
        env.put("Config.SIMPLY_BOOK_SERVICE_URL_lOGIN", "https://user-api.simplybook.me/login");
        env.put("Config.SIMPLY_BOOK_ADMIN_SERVICE_URL", "https://user-api.simplybook.me/admin/");
        env.put("Config.SIMPLY_BOOK_COMPANY_LOGIN", "phqviet93gmailcom");
        env.put("Config.SIMPLY_BOOK_USER_NAME", "admin");
        env.put("Config.SIMPLY_BOOK_PASSWORD", "");
        env.put("Config.SIMPLY_BOOK_SECRET_KEY", "");
        
        env.put("Config.AWS_ACCESS_KEY", "");
        env.put("Config.AWS_SECRET_ACCESS_KEY", "");
        env.put("Config.DYNAMODB_AWS_REGION", "us-east-1");
        
        Process p = pb.start();
        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        char[] buf = new char[1024];
        while (!isr.ready()) {
            ;
        }
        while (isr.read(buf) != -1) {
            System.out.println(buf);
        }
    }
}
