package com.tq.clickfunnel.lambda.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.tq.clickfunnel.lambda.configuration.Config;

public class DynanodbUtils {

    /**
     * @param region
     * @param accessKey : AWS access Key
     * @param secretAccessKey : AWS secret access key
     * @return AmazonDynamoDB
     */
    public static AmazonDynamoDB getAmazonDynamoDB(Regions region, String accessKey, String secretAccessKey) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(credentials);;
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(region.getName()).build();
        return client;
    }
    
    public static AmazonDynamoDB getLocallyDynamoDB() {
        AWSCredentialsProvider credentialsProvider =  new AWSCredentialsProvider() {
            @Override
            public void refresh() {
            }
            
            @Override
            public AWSCredentials getCredentials() {
                return new BasicAWSCredentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
            }
        };
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(new EndpointConfiguration(Config.DYNAMODB_LOCAL_ENDPOINT, Config.DYNAMODB_LOCAL_REGION_ECLIPSE)).build();
        return client;
    }
}
