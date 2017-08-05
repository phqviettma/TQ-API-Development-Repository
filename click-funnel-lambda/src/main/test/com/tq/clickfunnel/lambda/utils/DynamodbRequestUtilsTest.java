package com.tq.clickfunnel.lambda.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;

public class DynamodbRequestUtilsTest {
    
    private final static Logger log = LoggerFactory.getLogger(DynamodbRequestUtilsTest.class);

    @Test
    public void testExternalAWSListAllTable() throws InterruptedException {
        AmazonDynamoDB locallyDynamoDB = DynanodbUtils.getAmazonDynamoDB(Regions.fromName("us-east-1"), Config.AWS_ACCESS_KEY,
                Config.AWS_SECRET_ACCESS_KEY);
        ListTablesResult listTables = locallyDynamoDB.listTables();
        System.out.println(listTables.getTableNames());
        log.info("{}", listTables.getTableNames());
    }
    

    @Test
    public void testExternalLocallyListAllTable() throws InterruptedException {
        //createCustomersTableWithMapper();
        AmazonDynamoDB locallyDynamoDB = DynanodbUtils.getLocallyDynamoDB();
        ListTablesResult listTables = locallyDynamoDB.listTables();
        log.info("{}", listTables.getTableNames());
    }

    public void createCustomersTableWithMapper() throws InterruptedException {
        AmazonDynamoDB locallyDynamoDB = DynanodbUtils.getLocallyDynamoDB();
        DynamoDBMapper dymapper = new DynamoDBMapper(locallyDynamoDB);
        CreateTableRequest request = dymapper.generateCreateTableRequest(ContactItem.class);
        DynamoDB client = new DynamoDB(locallyDynamoDB);
        try {
            ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(1L, 1L);
            request.setProvisionedThroughput(provisionedThroughput);
            Table table = client.createTable(request);
            table.waitForActive();
        } catch (ResourceInUseException e) {
            System.out.println();
        }
    }
}
