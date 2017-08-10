package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFContactPayload;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.CountryItem;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.utils.Utils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddNewContactQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

/**
 * 
 * @author phqviet The class will handle for update, create event on contact of click funnel
 *
 */
public class HandleEventContactExecution extends AbstractEventPayloadExecution {

    private static final Logger log = Logger.getLogger(HandleEventContactExecution.class);
    private static final String TOKEN_STRING = " ";

    @Override
    public AwsProxyResponse handleLambdaProxy(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException {
        AwsProxyResponse resp = new AwsProxyResponse();
        LambdaContext lambdaContext = cfLambdaContext.getLambdaContext();
        ContactItem contactItem = null;
        try {
            // 1. building Client of Click Funnel from incoming AWS proxy request.
            CFContactPayload contactPayLoad = m_mapper.readValue(input.getBody(), CFContactPayload.class);
            if (contactPayLoad != null && contactPayLoad.getContact() != null) {
                CFContact funnelContact = contactPayLoad.getContact();

                // 2. creating the client based on Contact of Click Funnel on Simplybook.me
                Integer clientSbmId = addClientToSimplyBookMe(funnelContact, lambdaContext);

                // 3. creating the contact based on Contact of Click Funnel on Infusion soft
                Integer contactInfId = addContactToInfusionsoft(funnelContact, lambdaContext);

                // 4. Saving the client & contact ID into DynamoDB.
                contactItem = persitClientVoInDB(funnelContact, clientSbmId, contactInfId, lambdaContext);
            }
        } catch (IOException | CFLambdaException e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        // 5. Handle respond
        handleResponse(input, resp, contactItem);
        return resp;
    }

    private ContactItem persitClientVoInDB(CFContact funnelContact, Integer clientSbmId, Integer contactInfId, LambdaContext lambdaContext)
            throws JsonProcessingException {
        // build client to save DynomaDB
        long start = System.currentTimeMillis();
        ClientInfo clientInfo = new ClientInfo().withClientId(clientSbmId).withContactId(contactInfId).withEmail(funnelContact.getEmail())
                .withFirstName(funnelContact.getFirstName()).withLastName(funnelContact.getLastName()).withPhone1(funnelContact.getPhone())
                .withAddress1(funnelContact.getAddress()).withCountry(funnelContact.getCountry())
                .withCreatedAt(Config.DATE_FORMAT_24_H.format(funnelContact.getCreateAt()))
                .withUpdatedAt(Config.DATE_FORMAT_24_H.format(funnelContact.getUpdateAt()));

        ContactItem contactItem = new ContactItem().withEmail(funnelContact.getEmail()) // unique key
                .withContactInfo(clientInfo);
        lambdaContext.getContactItemService().put(contactItem);
        log.info(String.format("addDBContact()= %d ms", (System.currentTimeMillis() - start)));
        return contactItem;
    }

    /**
     * @param funnelContact
     * @param lambdaContext
     * @return contact id See more https://developer.infusionsoft.com/docs/table-schema/ For Contact table
     */
    private Integer addContactToInfusionsoft(CFContact funnelContact, LambdaContext lambdaContext) throws CFLambdaException {
        Integer contactId = null;
        EnvVar envVar = lambdaContext.getEnvVar();
        long start = System.currentTimeMillis();
        try {
            Map<String, String> dataRecord = new HashMap<>();
            dataRecord.put("Email", funnelContact.getEmail());
            dataRecord.put("FirstName", funnelContact.getFirstName());
            dataRecord.put("LastName", funnelContact.getLastName());
            dataRecord.put("Phone1", funnelContact.getPhone());
            dataRecord.put("Country", getCountry(funnelContact));
            dataRecord.put("City", getCity(funnelContact));
            dataRecord.put("State", getState(funnelContact));
            dataRecord.put("StreetAddress1", getAddress(funnelContact));

            ContactServiceInf contactServiceInf = lambdaContext.getContactServiceInf();
            contactId = contactServiceInf.addWithDupCheck(envVar.getEnv(Config.INFUSIONSOFT_API_NAME), envVar.getEnv(Config.INFUSIONSOFT_API_KEY),
                    new AddNewContactQuery().withDataRecord(dataRecord));
        } catch (InfSDKExecption e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        log.info(String.format("addINFContact()= %d ms", (System.currentTimeMillis() - start)));
        return contactId;
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

    private String getCity(CFContact funnelContact) {
        return Utils.isEmpty(funnelContact.getCity()) ? funnelContact.getShippingCity() : funnelContact.getCity();
    }

    private String getAddress(CFContact funnelContact) {
        return Utils.isEmpty(funnelContact.getAddress()) ? funnelContact.getShippingAddress() : funnelContact.getAddress();
    }

    private String getCountry(CFContact funnelContact) {
        String country = Utils.isEmpty(funnelContact.getCountry()) ? funnelContact.getShippingCountry() : funnelContact.getCountry();
        return country;
    }

    private String getState(CFContact funnelContact) {
        String stage = Utils.isEmpty(funnelContact.getState()) ? funnelContact.getShippingState() : funnelContact.getState();
        return stage;
    }

}
