package com.tq.clickfunnel.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.CountryItem;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.model.ProductItem;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
import com.tq.common.lambda.dynamodb.service.ProductItemService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddNewContactQuery;
import com.tq.inf.query.OptQuery;
import com.tq.inf.query.OrderQuery;
import com.tq.inf.service.APIEmailServiceInf;
import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.InvoiceServiceInf;
import com.tq.inf.service.OrderServiceInf;
import com.tq.inf.service.RecurringOrderInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

import junit.framework.Assert;

public class MockStripeBillingIntegrationTest {

    private static final Logger log = Logger.getLogger(MockStripeBillingIntegrationTest.class);
    
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
    public void testEventCreatedContact() throws SbmSDKException, InfSDKExecption, Exception {
        Context context = mock(Context.class);

        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("contactpayload.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.COTACT_CREATED);
        req.setQueryStringParameters(event);

        String adminToken = "adminToken";
        TokenServiceSbm tokenServiceSbm = m_lambdaContext.getTokenServiceSbm();
        ClientServiceSbm clientServiceSbm = m_lambdaContext.getClientServiceSbm();
        EnvVar envVar = m_lambdaContext.getEnvVar();
        when(tokenServiceSbm.getUserToken(envVar.getEnv(Config.SIMPLY_BOOK_COMPANY_LOGIN), envVar.getEnv(Config.SIMPLY_BOOK_USER_NAME), envVar.getEnv(Config.SIMPLY_BOOK_PASSWORD),
                Config.DEFAULT_SIMPLY_BOOK_SERVICE_URL_lOGIN)).thenReturn(adminToken);
        Integer smbContactId = Integer.valueOf(100000);
        when(clientServiceSbm.addClient(any(String.class), any(String.class), any(String.class), any(ClientData.class)))
                .thenReturn(smbContactId);

        Integer infContactId = Integer.valueOf(20000);
        ContactServiceInf contactServiceInf = m_lambdaContext.getContactServiceInf();
        when(contactServiceInf.addWithDupCheck(any(String.class), any(String.class), any(AddNewContactQuery.class)))
                .thenReturn(infContactId);
        
        APIEmailServiceInf apiEmailServiceInf = m_lambdaContext.getAPIEmailServiceInf();
        when(apiEmailServiceInf.optIn(any(String.class), any(String.class), any(OptQuery.class))).thenReturn(true);

        ContactItemService contactItemService = m_lambdaContext.getContactItemService();
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(contactItemService).put(any(ContactItem.class));

        CountryItem countryItem = new CountryItem("Viet Nam", "VN");
        CountryItemService countryItemService = m_lambdaContext.getCountryItemService();
        when(countryItemService.load("Viet Nam")).thenReturn(countryItem);
        AwsProxyResponse response = m_interceptorEvent.handleRequest(req, context);
        log.info(response.getBody());
        ContactItem contactItem = mapper.readValue(response.getBody(), ContactItem.class);
        Assert.assertNotNull(contactItem);
        Assert.assertEquals(contactItem.getClient().getContactId(), infContactId);
        verify(contactServiceInf,times(1)).addWithDupCheck(any(String.class), any(String.class), any(AddNewContactQuery.class));
        verify(contactItemService,times(1)).put(any(ContactItem.class));
       verify(clientServiceSbm,times(1)).addClient(any(String.class), any(String.class), any(String.class), any(ClientData.class));
       verify(tokenServiceSbm,times(1)).getUserToken(envVar.getEnv(Config.SIMPLY_BOOK_COMPANY_LOGIN), envVar.getEnv(Config.SIMPLY_BOOK_USER_NAME), envVar.getEnv(Config.SIMPLY_BOOK_PASSWORD),
                Config.DEFAULT_SIMPLY_BOOK_SERVICE_URL_lOGIN);
    }

    @Test
    public void testEventCreatedStripeOrderIntegratioin() throws Exception {
        Context context = mock(Context.class);
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("order-payload-stripe.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_CREATED);
        req.setQueryStringParameters(event);

        jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("contactItem-dummy-uf238.json"));
        ContactItem contactDummy = mapper.readValue(jsonString, ContactItem.class);
        ContactItemService contactItemService = m_lambdaContext.getContactItemService();
        when(contactItemService.load("dev4094tma@gmail.com")).thenReturn(contactDummy);

        jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("product-dummy-uf238.json"));
        ProductItem productItem = mapper.readValue(jsonString, ProductItem.class);

        ProductItemService productItemService = m_lambdaContext.getProductItemService();
        when(productItemService.load(anyInt())).thenReturn(productItem);

        Map<String, String> order = new HashMap<>();
        order.put("OrderId", "1");
        order.put("InvoiceId", "4206");
        order.put("Code", "None");
        order.put("RefNum", "2879922578");
        order.put("Message", "DECLINE - Response code(200)");
        order.put("Successful", "false");
        // Adding order into infusion soft mock
        OrderServiceInf orderServiceInf = m_lambdaContext.getOrderServiceInf();
        when(orderServiceInf.addOrder(any(String.class), any(String.class), any(OrderQuery.class))).thenReturn(order);
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
      /*  Assert.assertEquals(Integer.valueOf(order.get("OrderId")), orderItem.getOrderDetails().get(0).getOrderIdInf());
       */
        verify(contactItemService,times(1)).load(anyString());
    }
    
    @Test
    public void testEventCreatedStripeOrderIntegrationForOrderAlreadyProcessed() throws Exception {
        Context context = mock(Context.class);
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("order-payload-stripe.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_CREATED);
        req.setQueryStringParameters(event);

        jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("contactItem-dummy-uf238.json"));
        ContactItem contactDummy = mapper.readValue(jsonString, ContactItem.class);
        ContactItemService contactItemService = m_lambdaContext.getContactItemService();
        when(contactItemService.load("dev4094tma@gmail.com")).thenReturn(contactDummy);

        jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("product-dummy-uf238.json"));
        ProductItem productItem = mapper.readValue(jsonString, ProductItem.class);

        ProductItemService productItemService = m_lambdaContext.getProductItemService();
        when(productItemService.load(anyInt())).thenReturn(productItem);

        Map<String, String> order = new HashMap<>();
        order.put("OrderId", "1");
        order.put("InvoiceId", "4206");
        order.put("Code", "None");
        order.put("RefNum", "2879922578");
        order.put("Message", "DECLINE - Response code(200)");
        order.put("Successful", "false");
        // Adding order into infusion soft mock
        OrderServiceInf orderServiceInf = m_lambdaContext.getOrderServiceInf();
        when(orderServiceInf.addOrder(any(String.class), any(String.class), any(OrderQuery.class))).thenReturn(order);
        // Adding order into DynamoDB mock
        OrderItemService orderItemService = m_lambdaContext.getOrderItemService();
        OrderItem alreadyProcessOrder = new OrderItem();
        alreadyProcessOrder.setPurchaseId(123);
        when(orderItemService.load(anyInt())).thenReturn(alreadyProcessOrder);
        AwsProxyResponse response = m_interceptorEvent.handleRequest(req, context);
        log.info(response.getBody());
        HandleEventCreatedOrderExecution.AlreadyProccessedOrder orderItem = mapper.readValue(response.getBody(), HandleEventCreatedOrderExecution.AlreadyProccessedOrder.class);
        Assert.assertNotNull(orderItem);
        verify(productItemService,times(0)).load(anyInt());
        verify(orderServiceInf,times(0)).addOrder(any(String.class), any(String.class), any(OrderQuery.class));
        verify(orderItemService,times(0)).put(any(OrderItem.class));
        verify(orderItemService,times(0)).load(anyInt());
        verify(contactItemService,times(1)).load(anyString());
    }
    
    @Test
    public void testEventCreatedStripeOrderIntegrationForOrderAlreadyProcessed2() throws Exception {
        Context context = mock(Context.class);
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("order-payload-stripe2.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_CREATED);
        req.setQueryStringParameters(event);

        jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("contactItem-dummy-uf238.json"));
        ContactItem contactDummy = mapper.readValue(jsonString, ContactItem.class);
        ContactItemService contactItemService = m_lambdaContext.getContactItemService();
        when(contactItemService.load("dev50tma@gmail.com")).thenReturn(contactDummy);

        jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("product-dummy-uf238.json"));
        ProductItem productItem = mapper.readValue(jsonString, ProductItem.class);

        ProductItemService productItemService = m_lambdaContext.getProductItemService();
        when(productItemService.load(anyInt())).thenReturn(productItem);

        Map<String, String> order = new HashMap<>();
        order.put("OrderId", "1");
        order.put("InvoiceId", "4206");
        order.put("Code", "None");
        order.put("RefNum", "2879922578");
        order.put("Message", "DECLINE - Response code(200)");
        order.put("Successful", "false");
        // Adding order into infusion soft mock
        OrderServiceInf orderServiceInf = m_lambdaContext.getOrderServiceInf();
        when(orderServiceInf.addOrder(any(String.class), any(String.class), any(OrderQuery.class))).thenReturn(order);
        // Adding order into DynamoDB mock
        OrderItemService orderItemService = m_lambdaContext.getOrderItemService();
        OrderItem alreadyProcessOrder = new OrderItem();
        alreadyProcessOrder.setPurchaseId(123);
        when(orderItemService.load(anyInt())).thenReturn(alreadyProcessOrder);
        AwsProxyResponse response = m_interceptorEvent.handleRequest(req, context);
        log.info(response.getBody());
        HandleEventCreatedOrderExecution.AlreadyProccessedOrder orderItem = mapper.readValue(response.getBody(), HandleEventCreatedOrderExecution.AlreadyProccessedOrder.class);
        Assert.assertNotNull(orderItem);
        verify(productItemService,times(0)).load(anyInt());
        verify(orderServiceInf,times(0)).addOrder(any(String.class), any(String.class), any(OrderQuery.class));
        verify(orderItemService,times(0)).put(any(OrderItem.class));
        verify(orderItemService,times(0)).load(anyInt());
        verify(contactItemService,times(1)).load(anyString());
    }
    
    
    @Test
    
    public void testEventDeletedStripeOrderIntegration() throws Exception {
        Context context = mock(Context.class);
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("order-payload-stripe.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_DELETED);
        req.setQueryStringParameters(event);
        
        OrderItemService orderItemService = m_lambdaContext.getOrderItemService();
        jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("orderItem-dummy-billing-stripe.json"));
        OrderItem orderItem = mapper.readValue(jsonString, OrderItem.class);
        when(orderItemService.load(17059575)).thenReturn(orderItem);
        
        //Mock the RecurringOrder (subscription) from infusion soft.
        RecurringOrderInf recurringOrderInf = m_lambdaContext.getRecurringOrderInf();
        Object[] recurringOrders = new Object[1];
        HashMap<Object, Object> recordSubs = new HashMap<>();
        recordSubs.put("Id", 1); // subscription id. see more manageJobRecurring.jsp?view=edit&ID=[number of sub]
        recurringOrders[0] = recordSubs;
        when(recurringOrderInf.getRecurringOrderFromOriginatingOrderId(anyString(), anyString(), anyInt(), anyInt(), anyList())).thenReturn(recurringOrders );
        
        //delete firstly Invoice purchased via shopping cart or API Order.
        InvoiceServiceInf invoiceServiceInf = m_lambdaContext.getInvoiceServiceInf();
        when(invoiceServiceInf.deleteInvoice(anyString(), anyString(), anyInt())).thenReturn(true);
        
        //Delete subscription associated with firstly Invoice
        when(invoiceServiceInf.deleteSubscription(anyString(), anyString(), anyInt())).thenReturn(true);
        
        // Mock the order item will be delete DynamoDB
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(orderItemService).delete(any(OrderItem.class));
        
        AwsProxyResponse response = m_interceptorEvent.handleRequest(req, context);
        log.info(response.getBody());
        DeletedOrderResp delOrder = mapper.readValue(response.getBody(), DeletedOrderResp.class);
        Assert.assertNotNull(delOrder);
        Assert.assertEquals(new Integer(1), delOrder.getSubscriptionId());
        verify(orderItemService,times(1)).load(anyInt());
        verify(recurringOrderInf,times(1)).getRecurringOrderFromOriginatingOrderId(anyString(), anyString(), anyInt(), anyInt(), anyList());
        verify(invoiceServiceInf,times(1)).deleteInvoice(anyString(), anyString(), anyInt());
        verify(invoiceServiceInf,times(1)).deleteSubscription(anyString(), anyString(), anyInt());
        verify(orderItemService,times(1)).delete(any(OrderItem.class));
    }
}
