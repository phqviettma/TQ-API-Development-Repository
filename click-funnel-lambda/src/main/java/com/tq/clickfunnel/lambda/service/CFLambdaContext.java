package com.tq.clickfunnel.lambda.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;

public interface CFLambdaContext {

    public void wrapAwsProxy(Context context);
    
    public Context getAWSProxyContext();

    public AmazonDynamoDB getAmazonDynamoDB();

    public CFLambdaService getCFLambdaService();

    public CFLambdaServiceRepository getCFLambdaServiceRepository();
}
