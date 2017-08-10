package com.tq.clickfunnel.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import com.tq.inf.query.OrderQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.InvoiceServiceInf;
import com.tq.inf.service.OrderServiceInf;
import com.tq.inf.service.RecurringOrderInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

import junit.framework.Assert;

public class InterceptorEventPayloadProxyTest {

    private InterceptorEventPayloadProxy m_interceptorEvent;

    private LambdaContext m_lambdaContext;

    private ObjectMapper mapper = new ObjectMapper();
    
    @Before
    public void init() throws IOException {
        // Initialize the environment for testing
        CFLambdaContext cfLambdaContext = mock(CFLambdaContext.class);
        CFLambdaMockUtils.mockCFLambdaContext(cfLambdaContext);
        m_lambdaContext = cfLambdaContext.getLambdaContext();
        m_interceptorEvent = new InterceptorEventPayloadProxy(cfLambdaContext);
    }

    @SuppressWarnings("serial")
    @Test
    public void testEventCreatedContact() throws SbmSDKException, InfSDKExecption, Exception {
        Context context = mock(Context.class);

        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(InterceptorEventPayloadProxyTest.class.getResourceAsStream("contactpayload.json"));
        req.setBody(jsonString);
        req.setQueryStringParameters(new HashMap<String, String>() {
            {
                put(EventType.EVENT_PARAMETER_NAME, EventType.COTACT_CREATED);
            }
        });

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
        ContactItem contactItem = mapper.readValue(response.getBody(), ContactItem.class);
        Assert.assertNotNull(contactItem);
        Assert.assertEquals(contactItem.getClient().getContactId(), infContactId);
    }

    @SuppressWarnings("serial")
    @Test
    public void testEventCreatedOrder() throws Exception {
        Context context = mock(Context.class);
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("order-payload.json"));
        req.setBody(jsonString);
        req.setQueryStringParameters(new HashMap<String, String>() {
            {
                put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_CREATED);
            }
        });

        jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("contactItem-dummy-uf238.json"));
        ContactItem contactDummy = mapper.readValue(jsonString, ContactItem.class);
        ContactItemService contactItemService = m_lambdaContext.getContactItemService();
        when(contactItemService.load("dev1tma@gmail.com")).thenReturn(contactDummy);

        jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("product-dummy-uf238.json"));
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
        System.out.println(response.getBody());
        OrderItem orderItem = mapper.readValue(response.getBody(), OrderItem.class);
        Assert.assertNotNull(orderItem);
        Assert.assertEquals(Integer.valueOf(order.get("OrderId")), orderItem.getOrderDetails().get(0).getOrderIdInf());
    }
    
    @SuppressWarnings("serial")
    @Test
    public void testEventDeletedOrder() throws Exception {
        Context context = mock(Context.class);
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("order-payload.json"));
        req.setBody(jsonString);
        req.setQueryStringParameters(new HashMap<String, String>() {
            {
                put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_DELETED);
            }
        });
        
        OrderItemService orderItemService = m_lambdaContext.getOrderItemService();
        jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("orderItem-dummy.json"));
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
        when(invoiceServiceInf.deleteInvoice(anyString(), anyString(), anyInt())).thenReturn(1);
        
        //Delete subscription associated with firstly Invoice
        when(invoiceServiceInf.deleteSubscription(anyString(), anyString(), anyInt())).thenReturn(1);
        
        // Mock the order item will be delete DynamoDB
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(orderItemService).delete(17059575);
        
        AwsProxyResponse response = m_interceptorEvent.handleRequest(req, context);
        DeletedOrderResp delOrder = mapper.readValue(response.getBody(), DeletedOrderResp.class);
        Assert.assertNotNull(delOrder);
        Assert.assertEquals(new Integer(1), delOrder.getSubscriptionId());
    }
}
