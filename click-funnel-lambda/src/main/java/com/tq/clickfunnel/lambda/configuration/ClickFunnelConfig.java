package com.tq.clickfunnel.lambda.configuration;

import com.tq.clickfunnel.lambda.utils.Env;

public class ClickFunnelConfig {

    // Used for saving the detailed contact click funnel
    public static final String CLICK_FUNNEL_CONTACT_TABLE = Env.getEnv("CLICK_FUNNEL_CONTACT_TABLE");
    
    // Used for saving the detailed order +event click funnel
    public static final String CLICK_FUNNEL_ORDER_TABLE = Env.getEnv("CLICK_FUNNEL_ORDER_TABLE");
    
    // Get Infusionsoft details for making request to get info
    public static final String INFUSIONSOFT_API_NAME = Env.getEnv("INFUSIONSOFT_API_NAME");

    public static final String INFUSIONSOFT_API_KEY = Env.getEnv("INFUSIONSOFT_API_KEY");

    // Simply book
    public static final String SIMPLY_BOOK_SERVICE_URL = Env.getEnv("SIMPLY_BOOK_SERVICE_URL");
    
    public static final String SIMPLY_BOOK_SERVICE_URL_lOGIN = Env.getEnv("SIMPLY_BOOK_SERVICE_URL_lOGIN");
    
    public static final String SIMPLY_BOOK_ADMIN_SERVICE_URL = Env.getEnv("SIMPLY_BOOK_ADMIN_SERVICE_URL");
    
    public static final String SIMPLY_BOOK_COMPANY_LOGIN = Env.getEnv("SIMPLY_BOOK_COMPANY_LOGIN");
    
    public static final String SIMPLY_BOOK_API_KEY = Env.getEnv("SIMPLY_BOOK_API_KEY");
    
    public static final String SIMPLY_BOOK_USER = Env.getEnv("SIMPLY_BOOK_USER");

    public static final String SIMPLY_BOOK_PASSWORD = Env.getEnv("SIMPLY_BOOK_PASSWORD");
    
    public static final String SIMPLY_BOOK_SECRET_KEY = Env.getEnv("SIMPLY_BOOK_SECRET_KEY");
    

}
