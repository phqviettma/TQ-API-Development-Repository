package com.tq.clickfunnel.lambda.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.tq.clickfunnel.lambda.utils.Env;

public class Config {

    public static final String INFUSIONSOFT_API_NAME = Env.getEnv("INFUSIONSOFT_API_NAME");

    public static final String INFUSIONSOFT_API_KEY = Env.getEnv("INFUSIONSOFT_API_KEY");
    
    public static final String INFUSION_ORDER_PROMO_CODE = Env.getEnv("INFUSION_ORDER_PROMO_CODE");

    // Simply book
    public static final String SIMPLY_BOOK_SERVICE_URL = Env.getEnv("SIMPLY_BOOK_SERVICE_URL");
    
    public static final String SIMPLY_BOOK_SERVICE_URL_lOGIN = Env.getEnv("SIMPLY_BOOK_SERVICE_URL_lOGIN");
    
    public static final String SIMPLY_BOOK_ADMIN_SERVICE_URL = Env.getEnv("SIMPLY_BOOK_ADMIN_SERVICE_URL");
    
    public static final String SIMPLY_BOOK_COMPANY_LOGIN = Env.getEnv("SIMPLY_BOOK_COMPANY_LOGIN");
    
    public static final String SIMPLY_BOOK_API_KEY = Env.getEnv("SIMPLY_BOOK_API_KEY");
    
    public static final String SIMPLY_BOOK_USER_NAME = Env.getEnv("SIMPLY_BOOK_USER_NAME");

    public static final String SIMPLY_BOOK_PASSWORD =  Env.getEnv("SIMPLY_BOOK_PASSWORD");
    
    public static final String SIMPLY_BOOK_SECRET_KEY = Env.getEnv("SIMPLY_BOOK_SECRET_KEY");
    
    /**
     * AWS Access key
     */
    public static final String AWS_ACCESS_KEY = Env.getEnv("AWS_ACCESS_KEY");
    
    /**
     * AWS SECRET ACCESS KEY
     */
    public static final String AWS_SECRET_ACCESS_KEY = Env.getEnv("AWS_SECRET_ACCESS_KEY");
    
    /**
     * Locally end point DynamoDB for connecting to locally DynamoDB
     */
    public static final String DYNAMODB_LOCAL_ENDPOINT = "http://localhost:8000/";
    
    /**
     * When starting DynamoDb on eclipse via AWS toolkit, how to the program know the created table on eclipse.
     * the local region is specified. See more details https://github.com/aws/aws-toolkit-eclipse/issues/69
     */
    public static final String DYNAMODB_LOCAL_REGION_ECLIPSE = "local";
    
    /**
     * "us-east-1",
     * "us-east-2",
     * "us-west-1",
     * "eu-west-1",
     * "eu-west-2",
     * "eu-central-1",
     * "ap-south-1"
     * "ap-southeast-1",
     * "ap-southeast-2",
     * "ap-northeast-1",
     * "ap-northeast-2",
     * "sa-east-1",
     * "cn-north-1",
     * "ca-central-1"
     */
    public static final String DYNAMODB_AWS_REGION = Env.getEnv("DYNAMODB_AWS_REGION");
    
    /**
     * AWS DynamoDB is not supported the Date, so need to format default 24h
     */
    public static DateFormat DATE_FORMAT_24_H = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

}
