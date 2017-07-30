package com.tq.clickfunnel.lambda.utils;

import org.junit.Test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;

public class DynamodbRequestLocallyUtilsTest {

    @Test
    public void testListtable() throws InterruptedException {

        // createCustomersTableWithMapper();
        AmazonDynamoDB locallyDynamoDB = DynanodbUtils.getLocallyDynamoDB();
        ListTablesResult listTables = locallyDynamoDB.listTables();
        System.out.println(listTables.getTableNames());
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
