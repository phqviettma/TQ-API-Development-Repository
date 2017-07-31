package com.tq.clickfunnel.lambda.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.amazonaws.regions.Regions;

public class Config {

    public static final String INFUSIONSOFT_API_NAME = "https://uf238.infusionsoft.com/api/xmlrpc";

    public static final String INFUSIONSOFT_API_KEY = "da2083451dab102fbd9122c05629fe63";

    // Simply book
    public static final String SIMPLY_BOOK_SERVICE_URL = "https://user-api.simplybook.me/";
    
    public static final String SIMPLY_BOOK_SERVICE_URL_lOGIN = "https://user-api.simplybook.me/login";
    
    public static final String SIMPLY_BOOK_ADMIN_SERVICE_URL = "https://user-api.simplybook.me/admin/";
    
    public static final String SIMPLY_BOOK_COMPANY_LOGIN = "phqviet93gmailcom";
    
    public static final String SIMPLY_BOOK_API_KEY = "4de2a2545aed8e5e19861ee91221460c7e7e0ec90bff6dcad96fcc4d68c8e228";
    
    public static final String SIMPLY_BOOK_USER = "admin";

    public static final String SIMPLY_BOOK_PASSWORD = "12345678x@X";
    
    public static final String SIMPLY_BOOK_SECRET_KEY = "c9c8476bdedc3a2294a5fc69c9d0019e2af44dbe126ccb10f7431739a720b1ef";
    
    /**
     * AWS Access key
     */
    public static final String AWS_ACCESS_KEY = "AKIAI33G4J47Y4ODORVQ";
    
    /**
     * AWS SECRET ACCESS KEY
     */
    public static final String AWS_SECRET_ACCESS_KEY = "gr9p2wEVYMe1iwmMz3NDaYiK55N88jg4Nd5p/hKc";
    
    /**
     * Locally end point DynamoDB for connecting to locally DynamoDB
     */
    public static final String DYNAMODB_LOCAL_ENDPOINT = "http://localhost:8000/";
    
    /**
     * When starting DynamoDb on eclipse via AWS toolkit, how to the program know the created table on eclipse.
     * the local region is specified. See more details https://github.com/aws/aws-toolkit-eclipse/issues/69
     */
    public static final String DYNAMODB_LOCAL_REGION_ECLIPSE = "local";
    
    public static final Regions DYNAMODB_DEFAULT_REGION = Regions.US_EAST_1;
    
    /**
     * Contact table is used for saving the contact into DynamoDB
     */
    public static final String CLICK_FUNNEL_CONTACT = "Contact";

    /**
     * Order table is used for saving the order into DynamoDB
     */
    public static final String CLICK_FUNNEL_ORDER = "Order";
    
    /**
     * AWS DynamoDB is not supported the Date, so need to format default 24h
     */
    public static DateFormat DATE_FORMAT_24_H = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

}
