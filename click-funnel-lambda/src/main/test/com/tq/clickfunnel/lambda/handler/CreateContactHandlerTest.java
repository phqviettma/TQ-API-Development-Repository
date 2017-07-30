package com.tq.clickfunnel.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.configuration.ClickFunnelExternalService;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;
import com.tq.clickfunnel.lambda.utils.JsonUtils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddNewContactQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateContactHandlerTest {

    private CreateContactHandler m_handleLambda;
    
    private ContactServiceInf m_contactServiceInf;
    
    private TokenServiceSbm m_tokenServiceSbm;
    
    private ClientServiceSbm m_clientServiceSbm;
    
    private ClickFunnelExternalService m_clickFunnelExternalService;
    
    AmazonDynamoDB m_amazonDynamoDB = DynanodbUtils.getLocallyDynamoDB(); // Verify local or mock
    private ContactItemService m_contactItemService = new ContactItemServiceImpl(m_amazonDynamoDB);
    
    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws JsonParseException, JsonMappingException, IOException {
        m_contactServiceInf = mock(ContactServiceInf.class);
        m_tokenServiceSbm = mock(TokenServiceSbm.class);
        m_clientServiceSbm = mock(ClientServiceSbm.class);
        m_clickFunnelExternalService = mock(ClickFunnelExternalService.class);
        when(m_clickFunnelExternalService.getContactServiceInf()).thenReturn(m_contactServiceInf);
        when(m_clickFunnelExternalService.getClientServiceSbm()).thenReturn(m_clientServiceSbm);
        when(m_clickFunnelExternalService.getTokenServiceSbm()).thenReturn(m_tokenServiceSbm);
        m_handleLambda = new CreateContactHandler(m_clickFunnelExternalService, m_amazonDynamoDB);
        m_handleLambda.setContactItemService(m_contactItemService);
    }

    @Test
    public void testContactHandleMock() throws SbmSDKException, InfSDKExecption {
        Context context = mock(Context.class);
        AwsProxyRequest input = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(CreateContactHandlerTest.class.getResourceAsStream("contactpayload.json"));
        input.setBody(jsonString);

        String adminToken = "adminToken";
        when(m_tokenServiceSbm.getUserToken(Config.SIMPLY_BOOK_COMPANY_LOGIN, Config.SIMPLY_BOOK_USER, Config.SIMPLY_BOOK_PASSWORD,
                Config.SIMPLY_BOOK_SERVICE_URL_lOGIN)).thenReturn(adminToken);
        Integer smbContactId = Integer.valueOf(100000);
        when(m_clientServiceSbm.addClient(any(String.class), any(String.class), any(String.class), any(ClientData.class)))
                .thenReturn(smbContactId);

        Integer infContactId = Integer.valueOf(20000);
        when(m_contactServiceInf.addWithDupCheck(any(String.class), any(String.class), any(AddNewContactQuery.class)))
                .thenReturn(infContactId);
        
        m_handleLambda.handleRequest(input, context);
    }
    
    @Test
    public void testHandleExternalRequestWithDynamoDBLocally() {
        AwsProxyRequest input = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(CreateContactHandlerTest.class.getResourceAsStream("contactpayload.json"));
        input.setBody(jsonString);
        m_handleLambda = new CreateContactHandler();
        m_handleLambda.setContactItemService(m_contactItemService); // Locally Dynamodb
        Context context = mock(Context.class);
        m_handleLambda.handleRequest(input, context);
    }

}
