package com.tq.common.lambda.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;

public class DynamodbUtils {

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
    
    public static AmazonDynamoDB getAmazonDynamoDBInEnv(EnvVar envVar) {
        String awsRegion = envVar.getEnv(Config.DYNAMODB_AWS_REGION);
        String awsAcessKey = envVar.getEnv(Config.AMAZON_ACCESS_KEY);
        String awsSecretKey = envVar.getEnv(Config.AMAZON_SECRET_ACCESS_KEY);
        return getAmazonDynamoDB(Regions.fromName(awsRegion), awsAcessKey, awsSecretKey);
    }
    
    public static AmazonDynamoDB getLocallyDynamoDB() {
        AWSCredentialsProvider credentialsProvider =  new AWSCredentialsProvider() {
            @Override
            public void refresh() {
            }
            
            @Override
            public AWSCredentials getCredentials() {
                return new BasicAWSCredentials(Config.LOCALLY_AMAZON_ACCESS_KEY, Config.LOCALLY_AMAZON_SECRET_ACCESS_KEY);
            }
        };
        AmazonDynamoDBClientBuilder withCredentials = AmazonDynamoDBClientBuilder.standard().withCredentials(credentialsProvider);
		EndpointConfiguration endpointConfiguration = new EndpointConfiguration(Config.DYNAMODB_LOCAL_ENDPOINT, Config.DYNAMODB_LOCAL_REGION_ECLIPSE);
		AmazonDynamoDB client = withCredentials
                .withEndpointConfiguration(endpointConfiguration).build();
        return client;
    }
}
