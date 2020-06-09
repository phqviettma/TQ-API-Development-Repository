package com.tq.clickfunnel.lambda.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.MockEnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
import com.tq.common.lambda.dynamodb.service.ProductItemService;
import com.tq.common.lambda.services.ISExternalService;
import com.tq.common.lambda.services.RepositoryService;
import com.tq.common.lambda.services.SBMExternalService;
import com.tq.inf.service.APIEmailServiceInf;
import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.InvoiceServiceInf;
import com.tq.inf.service.OrderServiceInf;
import com.tq.inf.service.RecurringOrderInf;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CFLambdaMockUtils {
    /**
     * the script command for setting up the environment See more Config.java
     * 
     * @throws IOException
     */
    private static MockEnvVar initMockEnv() {
        MockEnvVar  mockEnv =new MockEnvVar();
        Map<String, String> env = new HashMap<>();
        env.put("INFUSIONSOFT_API_NAME", "https://bh321.infusionsoft.com/api/xmlrpc");
        env.put("INFUSIONSOFT_API_KEY", "");
        env.put("INFUSION_ORDER_PROMO_CODE", "SIMPLY_BOOK_SERVICE_URL");
        env.put("SIMPLY_BOOK_COMPANY_LOGIN", "phqviet93gmailcom");
        env.put("SIMPLY_BOOK_USER_NAME", "admin");
        env.put("SIMPLY_BOOK_PASSWORD", "");
        env.put("SIMPLY_BOOK_SECRET_KEY", "");
        env.put("AMAZON_ACCESS_KEY", Config.LOCALLY_AMAZON_ACCESS_KEY);
        env.put("AMAZON_SECRET_ACCESS_KEY", Config.LOCALLY_AMAZON_SECRET_ACCESS_KEY);
        env.put("DYNAMODB_AWS_REGION", Config.DYNAMODB_LOCAL_REGION_ECLIPSE);
        env.put("INFUSIONSOFT_CLICKFUNNEL_ORDER_PAID_TAG","110");
        env.put("INFUSION_CLICKFUNNEL_OPTIN_TAG", "101");
        env.put("INFUSION_CLICKFUNNEL_WEBINAR_OPTIN_TAG", "1616");
        mockEnv.setValueSystems(env);
        return mockEnv;
    }

    public static CFLambdaContext mockCFLambdaContext(CFLambdaContext mock) {
        Context context = mock(Context.class);
        LambdaContext lambdaContext = mock(LambdaContext.class);
        when(mock.getAwsProxyContext()).thenReturn(context);
        when(mock.getLambdaContext()).thenReturn(lambdaContext);
        
        //Mock environment
        MockEnvVar environment = initMockEnv();
        when(lambdaContext.getEnvVar()).thenReturn(environment);
        RepositoryService repositoryService = mock(RepositoryService.class);
        when(lambdaContext.getRepositoryService()).thenReturn(repositoryService);
        SBMExternalService sbmExternalService = mock(SBMExternalService.class);
        when(lambdaContext.getSBMExternalService()).thenReturn(sbmExternalService);
        ISExternalService isExternalService = mock(ISExternalService.class);
        when(lambdaContext.getISExternalService()).thenReturn(isExternalService);

        // mock service of RepositoryService
        ContactItemService contactItemService = mock(ContactItemService.class);
        CountryItemService countryItemService = mock(CountryItemService.class);
        OrderItemService orderItemService = mock(OrderItemService.class);
        ProductItemService productItemService = mock(ProductItemService.class);
        when(repositoryService.getContactItemService()).thenReturn(contactItemService);
        when(repositoryService.getCountryItemService()).thenReturn(countryItemService);
        when(repositoryService.getOrderItemService()).thenReturn(orderItemService);
        when(repositoryService.getProductItemService()).thenReturn(productItemService);

        // mock service of SBMExternalService
        TokenServiceSbm tokenServiceSbm = mock(TokenServiceSbm.class);
        when(sbmExternalService.getTokenServiceSbm()).thenReturn(tokenServiceSbm);
        ClientServiceSbm clientServiceSbm = mock(ClientServiceSbm.class);
        when(sbmExternalService.getClientServiceSbm()).thenReturn(clientServiceSbm);

        // mock service of ISExternalService
        ContactServiceInf contactServiceInf = mock(ContactServiceInf.class);
        DataServiceInf dataServiceInf = mock(DataServiceInf.class);
        OrderServiceInf orderServiceInf = mock(OrderServiceInf.class);
        RecurringOrderInf recurringOrderInf = mock(RecurringOrderInf.class);
        InvoiceServiceInf invoiceServiceInf = mock(InvoiceServiceInf.class);
        APIEmailServiceInf apiEmailServiceInf = mock(APIEmailServiceInf.class);
        when(isExternalService.getContactServiceInf()).thenReturn(contactServiceInf);
        when(isExternalService.getDataServiceInf()).thenReturn(dataServiceInf);
        when(isExternalService.getOrderServiceInf()).thenReturn(orderServiceInf);
        when(isExternalService.getRecurringOrderInf()).thenReturn(recurringOrderInf);
        when(isExternalService.getInvoiceServiceInf()).thenReturn(invoiceServiceInf);
        when(isExternalService.getAPIEmailServiceInf()).thenReturn(apiEmailServiceInf);

        // LambdaContext mock for services
        when(lambdaContext.getClientServiceSbm()).thenReturn(clientServiceSbm);
        when(lambdaContext.getTokenServiceSbm()).thenReturn(tokenServiceSbm);
        when(lambdaContext.getContactServiceInf()).thenReturn(contactServiceInf);
        when(lambdaContext.getDataServiceInf()).thenReturn(dataServiceInf);
        when(lambdaContext.getOrderServiceInf()).thenReturn(orderServiceInf);
        when(lambdaContext.getRecurringOrderInf()).thenReturn(recurringOrderInf);
        when(lambdaContext.getInvoiceServiceInf()).thenReturn(invoiceServiceInf);
        when(lambdaContext.getAPIEmailServiceInf()).thenReturn(apiEmailServiceInf);

        when(lambdaContext.getCountryItemService()).thenReturn(countryItemService);
        when(lambdaContext.getProductItemService()).thenReturn(productItemService);
        when(lambdaContext.getContactItemService()).thenReturn(contactItemService);
        when(lambdaContext.getOrderItemService()).thenReturn(orderItemService);

        return mock;
    }

}
