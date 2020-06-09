package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFContactPayload;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.CountryItem;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.utils.Utils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

/**
 * 
 * @author phqviet The class will handle for update, create event on contact of click funnel
 *
 */
public class HandleEventContactExecution extends AbstractEventContactExecution {

    private static final Logger log = Logger.getLogger(HandleEventContactExecution.class);
    private static final String TOKEN_STRING = " ";

    @Override
    public AwsProxyResponse handleLambdaProxy(AwsProxyRequest request, CFLambdaContext cfLambdaContext) throws CFLambdaException {
        AwsProxyResponse resp = new AwsProxyResponse();
        LambdaContext lambdaContext = cfLambdaContext.getLambdaContext();
        ContactItem contactItem = null;
        try {
            // 1. building Client of Click Funnel from incoming AWS proxy request.
            CFContactPayload contactPayLoad = m_mapper.readValue(request.getBody(), CFContactPayload.class);
            if (contactPayLoad != null && contactPayLoad.getContact() != null) {
                CFContact funnelContact = contactPayLoad.getContact();
                //Set opt-in reason
                funnelContact.setOptInReason("Opt-in " + getOptinTypeParam(request));

                // 2. creating the client based on Contact of Click Funnel on Simplybook.me
                Integer clientSbmId = addClientToSimplyBookMe(funnelContact, lambdaContext);

                // 3. creating the contact based on Contact of Click Funnel on Infusion soft
                Integer contactInfId = addContactToInfusionsoft(funnelContact, lambdaContext);
                //Applied Optin tag
                applyTagToInfusionsoft(lambdaContext, contactInfId, getOptInTag(request, lambdaContext));
                // 4. Saving the client & contact ID into DynamoDB.
                contactItem = persitClientVoInDB(funnelContact, clientSbmId, contactInfId, lambdaContext);
            }
        } catch (IOException | CFLambdaException | InfSDKExecption e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        // 5. Handle respond
        handleResponse(request, resp, contactItem);
        return resp;
    }

    private Object getOptInTag(AwsProxyRequest request, LambdaContext lambdaContext) {
        OptInType optInType = OptInType.parse(getOptinTypeParam(request));
        if (optInType == null) {
            log.info("No provide OPT_IN_PARRAM.");
            return null;
        }
        return lambdaContext.getEnvVar().getEnv(optInType.getTag());
    }

    private String getOptinTypeParam(AwsProxyRequest request) {
        return request.getQueryStringParameters().get(EventType.OPT_IN_PARRAM);
    }

    private Integer addClientToSimplyBookMe(CFContact funnelContact, LambdaContext lambdaContext) {
        EnvVar envVar = lambdaContext.getEnvVar();
        long start = System.currentTimeMillis();
        Integer clientSbmId = null;
        try {
            TokenServiceSbm tokenServiceSbm = lambdaContext.getTokenServiceSbm();
            ClientServiceSbm clientServiceSbm = lambdaContext.getClientServiceSbm();
            ClientData client = buildSBMContact(funnelContact, lambdaContext);

            String userToken = tokenServiceSbm.getUserToken(envVar.getEnv(Config.SIMPLY_BOOK_COMPANY_LOGIN), envVar.getEnv(Config.SIMPLY_BOOK_USER_NAME),
                    envVar.getEnv(Config.SIMPLY_BOOK_PASSWORD), Config.DEFAULT_SIMPLY_BOOK_SERVICE_URL_lOGIN);
            clientSbmId = clientServiceSbm.addClient(envVar.getEnv(Config.SIMPLY_BOOK_COMPANY_LOGIN), Config.DEFAULT_SIMPLY_BOOK_ADMIN_SERVICE_URL, userToken,
                    client);
        } catch (SbmSDKException e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        log.info(String.format("addSBMClient()= %d ms", (System.currentTimeMillis() - start)));
        return clientSbmId;
    }

    private ClientData buildSBMContact(CFContact funnelContact, LambdaContext lambdaContext) {
        String rebuilName = Utils.joinValues(TOKEN_STRING, funnelContact.getFirstName(), funnelContact.getLastName());
        CountryItemService countryItemService = lambdaContext.getCountryItemService();
        String country = getCountry(funnelContact);
        CountryItem countryItem = countryItemService.load(country);
        // verify country or shipping country
        String countryId = countryItem == null ? country : countryItem.getCode();
        String address = getAddress(funnelContact);
        String city = getCity(funnelContact);
        String zip = getZip(funnelContact);
        ClientData client = new ClientData().withEmail(funnelContact.getEmail()).withName(rebuilName).withPhone(funnelContact.getPhone())
                .withAddress1(address).withCity(city).withZip(zip).withCountry_id(countryId);
        return client;
    }

    private String getZip(CFContact funnelContact) {
        return Utils.isEmpty(funnelContact.getZip()) ? funnelContact.getShippingZip() : funnelContact.getZip();
    }

}
