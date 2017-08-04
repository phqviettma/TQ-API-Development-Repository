package com.tq.clickfunnel.lambda.dynamodb.impl;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.dynamodb.model.CFProduct;
import com.tq.clickfunnel.lambda.dynamodb.model.INFProduct;
import com.tq.clickfunnel.lambda.dynamodb.model.ProductItem;
import com.tq.clickfunnel.lambda.dynamodb.service.ProductItemService;
import com.tq.clickfunnel.lambda.handler.JsonRunner;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;
import com.tq.clickfunnel.lambda.utils.JsonUtils;

public class ProductItemServiceImplTest {

    public static final Logger log = LoggerFactory.getLogger(ContactItemServiceImplTest.class);
    AmazonDynamoDB m_amazonDynamoDB = DynanodbUtils.getLocallyDynamoDB(); // Verify local or mock
    private ProductItemService m_productItemService = new ProductItemServiceImpl(m_amazonDynamoDB);
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testPutProductItem() {
        ProductItem item = new ProductItem();
        item.setId(967487);
        CFProduct cfProduct = new CFProduct();
        cfProduct.setId(967487);
        cfProduct.setCurrency("usd");
        cfProduct.setName("Test Subscription Product");
        item.setCfProduct(cfProduct);

        INFProduct infProduct = new INFProduct();
        infProduct.setId(10308);
        infProduct.setCartId(0);
        infProduct.setPlanId(0);
        infProduct.setProductName("Test client subscription");
        infProduct.setSku("CS1");
        infProduct.setProductPrice(0.00);
        infProduct.setSubscriptionPlanId(11344);
        infProduct.setDescription("Test subscription");
        infProduct.setProductType("Product");
        item.setInfProduct(infProduct);
        m_productItemService.put(item);
    }
    
    @Test
    public void testLoadAlreadPrudctionItem() {
        ProductItem productItem = m_productItemService.load(967487);
        log.info("{}" , productItem);
    }
    
    @Test
    public void testCreateDummyProductFromFile() throws JsonParseException, JsonMappingException, IOException {
        String jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("product-dummy-uf238.json"));
        ProductItem productItem = mapper.readValue(jsonString, ProductItem.class);
        log.info("{}" , productItem);
        m_productItemService.put(productItem);
    }
}
