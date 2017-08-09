package com.tq.common.lambda.utils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.dao.ContactItemDao;
import com.tq.common.lambda.dynamodb.dao.ContactItemDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.service.ContactItemService;

public class DaoRunnerUtils {

    public static void main(String[] args) {
        AmazonDynamoDB client = DynamodbUtils.getLocallyDynamoDB();
        ContactItemDao contactItemDao = new  ContactItemDaoImpl(client);
        ContactItemService service = new ContactItemServiceImpl(contactItemDao);
        ContactItem loadContact = service.load("tnnha124@gmail.com");
        System.out.println(loadContact);
    }
}
