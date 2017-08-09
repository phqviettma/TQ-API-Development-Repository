package com.tq.clickfunnel.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.common.lambda.config.Config;
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
import com.tq.inf.service.OrderServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class InterceptorEventPayloadProxyTest {

    private InterceptorEventPayloadProxy m_interceptorEvent;

    private LambdaContext m_lambdaContext;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() throws IOException {
        CFLambdaContext cfLambdaContext = mock(CFLambdaContext.class);
        CFLambdaMockUtils.mockCFLambdaContext(cfLambdaContext);
        m_lambdaContext = cfLambdaContext.getLambdaContext();
        m_interceptorEvent = new InterceptorEventPayloadProxy(cfLambdaContext);
        // Initialize the environment for testing
        CFLambdaMockUtils.initDefaultsEnvOnWin();
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
        when(tokenServiceSbm.getUserToken(Config.SIMPLY_BOOK_COMPANY_LOGIN, Config.SIMPLY_BOOK_USER_NAME, Config.SIMPLY_BOOK_PASSWORD,
                Config.SIMPLY_BOOK_SERVICE_URL_lOGIN)).thenReturn(adminToken);
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
        m_interceptorEvent.handleRequest(req, context);
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

        Map<Object, Object> order = new HashMap<Object, Object>();
        order.put("OrderId", "1");
        order.put("InvoiceId", "1000");
        order.put("Code", "None");
        order.put("RefNum", "2879922578");
        order.put("Message", "DECLINE - Response code(200)");
        order.put("Successful", "false");
        // Adding order into infusion soft mock
        OrderServiceInf orderServiceInf = m_lambdaContext.getOrderServiceInf();
        when(orderServiceInf.addOrder(any(), any(), any(OrderQuery.class))).thenReturn(order);
        // Adding order into DynamoDB mock
        OrderItemService orderItemService = m_lambdaContext.getOrderItemService();
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(orderItemService).put(any(OrderItem.class));
        m_interceptorEvent.handleRequest(req, context);
    }
}
