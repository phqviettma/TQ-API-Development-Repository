package com.tq.common.lambda.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Config {

    public static final String INFUSIONSOFT_API_NAME = "INFUSIONSOFT_API_NAME";

    public static final String INFUSIONSOFT_API_KEY = "INFUSIONSOFT_API_KEY";
    
    public static final String INFUSIONSOFT_CLICKFUNNEL_ORDER_PAID_TAG = "INFUSIONSOFT_CLICKFUNNEL_ORDER_PAID_TAG";
    
    public static final String INFUSIONSOFT_CLICKFUNNEL_AFFILIALTE_BACKPACK_SIGNUP_TAG = "INFUSIONSOFT_CLICKFUNNEL_AFFILIALTE_BACKPACK_SIGNUP_TAG";
    
    public static final String INFUSIONSOFT_CLINIKO_PRACTITIONER_TAG = "INFUSIONSOFT_CLINIKO_PRACTITIONER_TAG";
    
    public static final String INFUSION_ORDER_PROMO_CODE = "INFUSION_ORDER_PROMO_CODE";
    
    public static final String INFUSION_CLICKFUNNEL_YOUTUBE_OPTIN_TAG = "INFUSION_CLICKFUNNEL_YOUTUBE_OPTIN_TAG";
    public static final String INFUSION_CLICKFUNNEL_GOOGLE_AD_OPTIN_TAG = "INFUSION_CLICKFUNNEL_GOOGLE_AD_OPTIN_TAG";
    public static final String INFUSION_CLICKFUNNEL_FACEBOOK_LEAD_OPTIN_TAG = "INFUSION_CLICKFUNNEL_FACEBOOK_LEAD_OPTIN_TAG";
    public static final String INFUSION_CLICKFUNNEL_WANTSTOQUIT_OPTIN_TAG = "INFUSION_CLICKFUNNEL_WANTSTOQUIT_OPTIN_TAG";
    
    public static final String INFUSION_CLICKFUNNEL_OPTIN_TAG = "INFUSION_CLICKFUNNEL_OPTIN_TAG";

    // Simply book
    public static final String SIMPLY_BOOK_COMPANY_LOGIN = "SIMPLY_BOOK_COMPANY_LOGIN";
    
    public static final String SIMPLY_BOOK_USER_NAME = "SIMPLY_BOOK_USER_NAME";
    
    public static final String SIMPLY_BOOK_PASSWORD = "SIMPLY_BOOK_PASSWORD";

    public static final String SIMPLY_BOOK_API_KEY = "SIMPLY_BOOK_API_KEY";
    
    public static final String SIMPLY_BOOK_SECRET_KEY = "SIMPLY_BOOK_SECRET_KEY";
    
    public static final String SIMPLY_BOOK_DEFAULT_USER_PASSWORD = "SIMPLY_BOOK_DEFAULT_USER_PASSWORD";

    public static final String DEFAULT_SIMPLY_BOOK_SERVICE_URL = "https://user-api.simplybook.me/"; /* Env.getEnv("SIMPLY_BOOK_SERVICE_URL"); */

    public static final String DEFAULT_SIMPLY_BOOK_SERVICE_URL_lOGIN = "https://user-api.simplybook.me/login"; /*Env.getEnv("SIMPLY_BOOK_SERVICE_URL_lOGIN");*/

    public static final String DEFAULT_SIMPLY_BOOK_ADMIN_SERVICE_URL = "https://user-api.simplybook.me/admin/"; /*Env.getEnv("SIMPLY_BOOK_ADMIN_SERVICE_URL");*/
  
    public static final String CLINIKO_API_KEY = "CLINIKO_API_KEY";
    
    /**
     * AWS Access key
     */
    public static final String AMAZON_ACCESS_KEY = "AMAZON_ACCESS_KEY";

    /**
     * AWS SECRET ACCESS KEY
     */
    public static final String AMAZON_SECRET_ACCESS_KEY = "AMAZON_SECRET_ACCESS_KEY";

    /**
     * "us-east-1", "us-east-2", "us-west-1", "eu-west-1", "eu-west-2", "eu-central-1", "ap-south-1" "ap-southeast-1", "ap-southeast-2", "ap-northeast-1",
     * "ap-northeast-2", "sa-east-1", "cn-north-1", "ca-central-1"
     */
    public static final String DYNAMODB_AWS_REGION = "DYNAMODB_AWS_REGION";
    
    /**
     * Locally end point DynamoDB for connecting to locally DynamoDB
     */
    public static final String DYNAMODB_LOCAL_ENDPOINT = "http://localhost:8000/";

    /**
     * When starting DynamoDb on eclipse via AWS toolkit, how to the program know the created table on eclipse. the local region is specified. See more details
     * https://github.com/aws/aws-toolkit-eclipse/issues/69
     */
    public static final String DYNAMODB_LOCAL_REGION_ECLIPSE = "local";

    /**
     * AWS Access key
     */
    public static final String LOCALLY_AMAZON_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";

    /**
     * AWS SECRET ACCESS KEY
     */
    public static final String LOCALLY_AMAZON_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    
    /**
     * AWS DynamoDB is not supported the Date, so need to format default 24h
     */
    public static DateFormat DATE_FORMAT_24_H = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    

   

}
