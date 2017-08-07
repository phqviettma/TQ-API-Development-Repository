package com.tq.clickfunnel.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.model.CountryItem;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderItem;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.clickfunnel.lambda.dynamodb.service.CountryItemService;
import com.tq.clickfunnel.lambda.dynamodb.service.OrderItemService;
import com.tq.clickfunnel.lambda.dynamodb.service.ProductItemService;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;
import com.tq.clickfunnel.lambda.service.CFLambdaService;
import com.tq.clickfunnel.lambda.service.CFLambdaServiceRepository;
import com.tq.clickfunnel.lambda.utils.JsonUtils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddNewContactQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.RecurringOrderInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class InterceptorEventPayloadProxyTest {

    private InterceptorEventPayloadProxy m_interceptorEvent;

    private ContactServiceInf m_contactServiceInf;

    private TokenServiceSbm m_tokenServiceSbm;

    private ClientServiceSbm m_clientServiceSbm;

    private CFLambdaService m_cfLambdaService;

    private CFLambdaServiceRepository m_cfLambdaServiceRepo;
    
    private ContactItemService m_contactItemService;
    
    private ProductItemService m_productItemService;
    
    private OrderItemService m_orderItemService;
    
    private CountryItemService m_countryItemService;
    
    private RecurringOrderInf m_recurringOrderInf ;
    
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() throws IOException {
        CFLambdaContext cfLambdaContext = mock(CFLambdaContext.class);
        m_interceptorEvent = new InterceptorEventPayloadProxy(cfLambdaContext);

        // mock service of infusion soft
        m_contactServiceInf = mock(ContactServiceInf.class);
        m_recurringOrderInf = mock(RecurringOrderInf.class);

        // mock services of simplybook.me
        m_tokenServiceSbm = mock(TokenServiceSbm.class);
        m_clientServiceSbm = mock(ClientServiceSbm.class);
        
        
        m_cfLambdaService = mock(CFLambdaService.class);
        when(m_cfLambdaService.getClientServiceSbm()).thenReturn(m_clientServiceSbm);
        when(m_cfLambdaService.getTokenServiceSbm()).thenReturn(m_tokenServiceSbm);
        when(m_cfLambdaService.getContactServiceInf()).thenReturn(m_contactServiceInf);
        when(m_cfLambdaService.getRecurringOrderInf()).thenReturn(m_recurringOrderInf);
        when(cfLambdaContext.getCFLambdaService()).thenReturn(m_cfLambdaService);
        m_cfLambdaServiceRepo = mock(CFLambdaServiceRepository.class);
        m_contactItemService = mock(ContactItemService.class);
        when(m_cfLambdaServiceRepo.getContactItemService()).thenReturn(m_contactItemService);
        
        m_orderItemService = mock(OrderItemService.class);
        when(m_cfLambdaServiceRepo.getOrderItemService()).thenReturn(m_orderItemService);
        
        m_productItemService = mock(ProductItemService.class);
        when(m_cfLambdaServiceRepo.getProductItemService()).thenReturn(m_productItemService);
        
        m_countryItemService = mock(CountryItemService.class);
        when(m_cfLambdaServiceRepo.getCountryItemService()).thenReturn(m_countryItemService);
        
        // mock repository service to populate DynamoDB
        when(cfLambdaContext.getCFLambdaServiceRepository()).thenReturn(m_cfLambdaServiceRepo);
        
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
        when(m_tokenServiceSbm.getUserToken(Config.SIMPLY_BOOK_COMPANY_LOGIN, Config.SIMPLY_BOOK_USER_NAME, Config.SIMPLY_BOOK_PASSWORD,
                Config.SIMPLY_BOOK_SERVICE_URL_lOGIN)).thenReturn(adminToken);
        Integer smbContactId = Integer.valueOf(100000);
        when(m_clientServiceSbm.addClient(any(String.class), any(String.class), any(String.class), any(ClientData.class)))
                .thenReturn(smbContactId);

        Integer infContactId = Integer.valueOf(20000);
        when(m_contactServiceInf.addWithDupCheck(any(String.class), any(String.class), any(AddNewContactQuery.class)))
                .thenReturn(infContactId);

        when(m_contactItemService.put(any(ContactItem.class))).thenReturn(true);
        CountryItem countryItem = new CountryItem("Viet Nam", "VN");
        when(m_countryItemService.load("Viet Nam")).thenReturn(countryItem);
        m_interceptorEvent.handleRequest(req, context);
    }
    
    @SuppressWarnings("serial")
    @Test
    public void  testEventCreatedOrder() throws Exception {
        Context context = mock(Context.class);
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(InterceptorEventPayloadProxyTest.class.getResourceAsStream("order-payload.json"));
        req.setBody(jsonString);
        req.setQueryStringParameters(new HashMap<String, String>() {
            {
                put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_CREATED);
            }
        });
        
        jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("contactItem-dummy-uf238.json"));
        ContactItem contactDummy = mapper.readValue(jsonString, ContactItem.class);
        when(m_contactItemService.get("dev1tma@gmail.com")).thenReturn(contactDummy );
        
        Object[] recurringOrders = new Object[1] ;
        Integer contactId = 10308;
        buildRecurringOrder(recurringOrders, contactId);
        List<String> selectedFields = Arrays.asList("Id", "ContactId", "OriginatingOrderId", "ProductId", "StartDate", "EndDate",
                "LastBillDate", "NextBillDate", "Status", "AutoCharge", "SubscriptionPlanId");
        when(m_recurringOrderInf.getAllRecurringOrder(Config.INFUSIONSOFT_API_NAME, Config.INFUSIONSOFT_API_KEY, contactId, selectedFields)).thenReturn(recurringOrders);
        when(m_orderItemService.put(any(OrderItem.class))).thenReturn(true);
        m_interceptorEvent.handleRequest(req, context);
    }

    private void buildRecurringOrder(Object[] recurringOrders, Integer contactId) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("Id", 1);
        hashMap.put("ProductId", 1000);
        hashMap.put("ContactId", contactId);
        hashMap.put("OriginatingOrderId", 4000);
        hashMap.put("StartDate", new Date());
        hashMap.put("EndDate", new Date());
        hashMap.put("LastBillDate", new Date());
        hashMap.put("NextBillDate", new Date());
        hashMap.put("AutoCharge",1 );
        hashMap.put("Status", "success");
        recurringOrders[0] = hashMap;
    }
}
