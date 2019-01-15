package com.tq.clickfunnel.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.MockEnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.CountryItem;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.ApplyTagQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class HandleEventContactExecutionTest {
    private CFLambdaContext cfLambdaContext = mock(CFLambdaContext.class);
    private LambdaContext lambdaContext = mock(LambdaContext.class);
    private TokenServiceSbm tokenServiceSbm = mock(TokenServiceSbm.class);
    private ClientServiceSbm clientServiceSbm = mock(ClientServiceSbm.class);
    private ContactServiceInf contactServiceInf = mock(ContactServiceInf.class);
    private CountryItemService countryItemService = mock(CountryItemService.class);
    private ContactItemService contactItemService = mock(ContactItemService.class);
    
    private HandleEventContactExecution handle = new HandleEventContactExecution();

    @Before
    public void init() {
        when(cfLambdaContext.getLambdaContext()).thenReturn(lambdaContext);
        when(lambdaContext.getTokenServiceSbm()).thenReturn(tokenServiceSbm);
        when(lambdaContext.getClientServiceSbm()).thenReturn(clientServiceSbm);
        when(lambdaContext.getEnvVar()).thenReturn(mockEnv());
        when(lambdaContext.getCountryItemService()).thenReturn(countryItemService);
        when(lambdaContext.getContactServiceInf()).thenReturn(contactServiceInf);
        when(lambdaContext.getContactItemService()).thenReturn(contactItemService);
        when(countryItemService.load("Viet Nam")).thenReturn(mockVNCountry());
    }

    @Test
    public void createClickFunnelContact_OptinYoubute_Sucessfull() throws Exception {
        // Given
        AwsProxyRequest req = optInPayloadToCreateContact("YoutubeOptin");
        mockDefault3RdConnections();
        // When
        handle.handleLambdaProxy(req, cfLambdaContext);
        // Then
        verifyDefault3RdConnections();
        ArgumentCaptor<ApplyTagQuery> argumentCaptor = ArgumentCaptor.forClass(ApplyTagQuery.class);
        verify(contactServiceInf, times(1)).applyTag(eq("apiName"), eq("apiKey"), argumentCaptor.capture());
        ApplyTagQuery applyTagQuery = argumentCaptor.getValue();
        Assert.assertEquals(Integer.valueOf(123), applyTagQuery.getTagID());
    }

    @Test
    public void createClickFunnelContact_OptinGoogleAds_Sucessfull() throws Exception {
        // Given
        AwsProxyRequest req = optInPayloadToCreateContact("GoogleOptin");
        mockDefault3RdConnections();
        // When
        handle.handleLambdaProxy(req, cfLambdaContext);
        // Then
        verifyDefault3RdConnections();
        ArgumentCaptor<ApplyTagQuery> argumentCaptor = ArgumentCaptor.forClass(ApplyTagQuery.class);
        verify(contactServiceInf, times(1)).applyTag(eq("apiName"), eq("apiKey"), argumentCaptor.capture());
        ApplyTagQuery applyTagQuery = argumentCaptor.getValue();
        Assert.assertEquals(Integer.valueOf(456), applyTagQuery.getTagID());
    }
    
    @Test
    public void createClickFunnelContact_FacebookLead_Sucessfull() throws Exception {
        // Given
        AwsProxyRequest req = optInPayloadToCreateContact("FacebookLead");
        mockDefault3RdConnections();
        // When
        handle.handleLambdaProxy(req, cfLambdaContext);
        // Then
        verifyDefault3RdConnections();
        ArgumentCaptor<ApplyTagQuery> argumentCaptor = ArgumentCaptor.forClass(ApplyTagQuery.class);
        verify(contactServiceInf, times(1)).applyTag(eq("apiName"), eq("apiKey"), argumentCaptor.capture());
        ApplyTagQuery applyTagQuery = argumentCaptor.getValue();
        Assert.assertEquals(Integer.valueOf(954), applyTagQuery.getTagID());
    }
    
    @Test
    public void createClickFunnelContact_WantsToQuit_Sucessfull() throws Exception {
        // Given
        AwsProxyRequest req = optInPayloadToCreateContact("Wantstoquit");
        mockDefault3RdConnections();
        // When
        handle.handleLambdaProxy(req, cfLambdaContext);
        // Then
        verifyDefault3RdConnections();
        ArgumentCaptor<ApplyTagQuery> argumentCaptor = ArgumentCaptor.forClass(ApplyTagQuery.class);
        verify(contactServiceInf, times(1)).applyTag(eq("apiName"), eq("apiKey"), argumentCaptor.capture());
        ApplyTagQuery applyTagQuery = argumentCaptor.getValue();
        Assert.assertEquals(Integer.valueOf(946), applyTagQuery.getTagID());
    }
    
    private void verifyDefault3RdConnections() throws SbmSDKException, InfSDKExecption {
        verify(tokenServiceSbm, times(1)).getUserToken("companyLogin", "username", "password", "https://user-api.simplybook.me/login");
        verify(clientServiceSbm, times(1)).addClient(eq("companyLogin"), eq(Config.DEFAULT_SIMPLY_BOOK_ADMIN_SERVICE_URL), eq("userToken"),
                any(ClientData.class));
        verify(contactItemService, times(1)).put(any(ContactItem.class));
    }

    private void mockDefault3RdConnections() throws SbmSDKException {
        when(tokenServiceSbm.getUserToken("companyLogin", "username", "password", Config.DEFAULT_SIMPLY_BOOK_SERVICE_URL_lOGIN))
                .thenReturn("userToken");
        when(clientServiceSbm.addClient(eq("companyLogin"), eq(Config.DEFAULT_SIMPLY_BOOK_ADMIN_SERVICE_URL), eq("userToken"),
                any(ClientData.class))).thenReturn(100);
    }

    private AwsProxyRequest optInPayloadToCreateContact(String optIn) {
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("contactpayload.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        req.setQueryStringParameters(event);
        event.put(EventType.OPT_IN_PARRAM, optIn);
        return req;
    }

    private CountryItem mockVNCountry() {
        CountryItem countryItem = new CountryItem();
        countryItem.setCode("VN");
        countryItem.setName("Viet Name");
        return countryItem;
    }

    private MockEnvVar mockEnv() {
        Map<String, String> env = new HashMap<>();
        env.put(Config.INFUSIONSOFT_API_NAME, "apiName");
        env.put(Config.INFUSIONSOFT_API_KEY, "apiKey");
        env.put(Config.INFUSION_ORDER_PROMO_CODE, "promoCode");
        env.put(Config.SIMPLY_BOOK_COMPANY_LOGIN, "companyLogin");
        env.put(Config.SIMPLY_BOOK_USER_NAME, "username");
        env.put(Config.SIMPLY_BOOK_PASSWORD, "password");
        env.put(Config.SIMPLY_BOOK_API_KEY, "apiKey");
        env.put(Config.SIMPLY_BOOK_DEFAULT_USER_PASSWORD, "");
        env.put(Config.AMAZON_ACCESS_KEY, "amazonKey");
        env.put(Config.AMAZON_SECRET_ACCESS_KEY, "amazonScretKey");
        env.put(Config.DYNAMODB_AWS_REGION, "amazonRegion");
        
        env.put(Config.INFUSION_CLICKFUNNEL_YOUTUBE_OPTIN_TAG, "123");
        env.put(Config.INFUSION_CLICKFUNNEL_GOOGLE_AD_OPTIN_TAG, "456");
        env.put(Config.INFUSION_CLICKFUNNEL_FACEBOOK_LEAD_OPTIN_TAG, "954");
        env.put(Config.INFUSION_CLICKFUNNEL_WANTSTOQUIT_OPTIN_TAG, "946");
        MockEnvVar envVar = new MockEnvVar();
        envVar.setValueSystems(env);
        return envVar;
    }

}
