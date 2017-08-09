package com.tq.common.lambda.context;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.services.ISExternalService;
import com.tq.common.lambda.services.RepositoryService;
import com.tq.common.lambda.services.SBMExternalService;

public interface LambdaContext extends RepositoryService, SBMExternalService, ISExternalService {
    
    public AmazonDynamoDB getAmazonDynamoDB();
    
    public RepositoryService getRepositoryService();
    
    public SBMExternalService getSBMExternalService();
    
    public ISExternalService getISExternalService();
}
