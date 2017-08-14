package com.tq.clickfunnel.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.resp.DeletedOrderResp;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.model.ProductItem;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
import com.tq.common.lambda.dynamodb.service.ProductItemService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.inf.service.InvoiceServiceInf;
import com.tq.inf.service.RecurringOrderInf;

import junit.framework.Assert;

public class MockInfusionsoftBillingIntegrationTest {
    
    private static final Logger log = Logger.getLogger(MockInfusionsoftBillingIntegrationTest.class);
    private InterceptorEventPayloadProxy m_interceptorEvent;

    private LambdaContext m_lambdaContext;

    private ObjectMapper mapper = new ObjectMapper();
    
    @Before
    public void init() throws IOException {
        // Initialize the environment for testing
        CFLambdaContext cfLambdaContext = mock(CFLambdaContext.class);
        CFLambdaMockUtils.mockCFLambdaContext(cfLambdaContext);
        m_lambdaContext = cfLambdaContext.getLambdaContext();
        m_interceptorEvent = new InterceptorEventPayloadProxy(m_lambdaContext) {
            @Override
            protected CFLambdaContext initialize(Context context, LambdaContext lambdaContext) {
                return cfLambdaContext;
            }
        };
    }
    
    @Test
    public void testEventCreatedInfusionSoftOrderIntegration() throws Exception {
        Context context = mock(Context.class);
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("order-payload-inf.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_CREATED);
        req.setQueryStringParameters(event);

        jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("contactItem-dummy-uf238.json"));
        ContactItem contactDummy = mapper.readValue(jsonString, ContactItem.class);
        ContactItemService contactItemService = m_lambdaContext.getContactItemService();
        when(contactItemService.load("dev1tma@gmail.com")).thenReturn(contactDummy);

        jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("product-dummy-uf238.json"));
        ProductItem productItem = mapper.readValue(jsonString, ProductItem.class);

        ProductItemService productItemService = m_lambdaContext.getProductItemService();
        when(productItemService.load(anyInt())).thenReturn(productItem);

        RecurringOrderInf recurringOrderInf = m_lambdaContext.getRecurringOrderInf();
        HashMap<String, Object> latestSubs = new HashMap<>(); 
        latestSubs.put("Id", 1);
        latestSubs.put("ContactId", 10308);
        latestSubs.put("OriginatingOrderId", 1);
        latestSubs.put("ProductId", 1);
        latestSubs.put("StartDate", new Date());
        latestSubs.put("EndDate", null);
        latestSubs.put("LastBillDate", new Date());
        latestSubs.put("NextBillDate", new Date());
        latestSubs.put("Status", "Active");
        Object obj = latestSubs;
        when(recurringOrderInf.getLatestRecurringOrderFromProduct(anyString(), anyString(), anyInt(), anyInt(), anyList())).thenReturn(obj);
        
        
        // Adding order into DynamoDB mock
        OrderItemService orderItemService = m_lambdaContext.getOrderItemService();
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(orderItemService).put(any(OrderItem.class));
        AwsProxyResponse response = m_interceptorEvent.handleRequest(req, context);
        log.info(response.getBody());
        OrderItem orderItem = mapper.readValue(response.getBody(), OrderItem.class);
        Assert.assertNotNull(orderItem);
        Assert.assertNotNull(orderItem.getRecurringOrder());
    }
    
    @Test
    public void testEventDeletedInfusionSoftOrderIntegration() throws Exception {
        Context context = mock(Context.class);
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("order-payload-inf.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_DELETED);
        req.setQueryStringParameters(event);
        
        OrderItemService orderItemService = m_lambdaContext.getOrderItemService();
        jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("orderItem-dummy-billing-inf.json"));
        OrderItem orderItem = mapper.readValue(jsonString, OrderItem.class);
        when(orderItemService.load(17059575)).thenReturn(orderItem);
        
        InvoiceServiceInf invoiceServiceInf = m_lambdaContext.getInvoiceServiceInf();
        when(invoiceServiceInf.deleteInvoice(anyString(), anyString(), anyInt())).thenReturn(Boolean.TRUE);
        
        when(invoiceServiceInf.deleteSubscription(anyString(), anyString(), anyInt())).thenReturn(Boolean.TRUE);
        
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(orderItemService).delete(any(OrderItem.class)); // dummy purchase id
        
        AwsProxyResponse response = m_interceptorEvent.handleRequest(req, context);
        log.info(response.getBody());
        DeletedOrderResp delOrder = mapper.readValue(response.getBody(), DeletedOrderResp.class);
        Assert.assertNotNull(delOrder);
        Assert.assertEquals(new Integer(1), delOrder.getSubscriptionId());
    }
}
