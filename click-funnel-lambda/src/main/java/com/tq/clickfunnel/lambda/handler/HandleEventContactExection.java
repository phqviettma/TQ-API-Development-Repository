package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.model.ClientInfo;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.exception.ClickFunnelLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFContactPayload;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;
import com.tq.clickfunnel.lambda.service.CFLambdaService;
import com.tq.clickfunnel.lambda.service.CFLambdaServiceRepository;
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
public class HandleEventContactExection extends AbstractEventPayloadExecution {

    private static final Logger log = LoggerFactory.getLogger(HandleEventContactExection.class);
    private ObjectMapper m_mapper = new ObjectMapper();

    private CFLambdaService m_cfLambdaService;

    @Override
    public AwsProxyResponse execute(AwsProxyRequest input, CFLambdaContext proxyContext) {
        AwsProxyResponse resp = new AwsProxyResponse();
        m_cfLambdaService = proxyContext.getCFLambdaService();
        ContactItem contactItem = null;
        try {
            // 1. building Client of Click Funnel from incoming AWS proxy request.
            CFContactPayload contactPayLoad = m_mapper.readValue(input.getBody(), CFContactPayload.class);
            if (contactPayLoad != null && contactPayLoad.getContact() != null) {
                CFContact funnelContact = contactPayLoad.getContact();

                // 2. creating the client based on Contact of Click Funnel on Simplybook.me
                Integer clientSbmId = addClientToSimplyBookMe(funnelContact);

                // 3. creating the contact based on Contact of Click Funnel on Infusion soft
                Integer contactInfId = addContactToInfusionsoft(funnelContact);

                // 4. Saving the client & contact ID into DynamoDB.
                contactItem = persitClientVoInDB(funnelContact, clientSbmId, contactInfId, proxyContext.getCFLambdaServiceRepository());
            }
        } catch (IOException e) {
            log.error("Can't create contact in SMB/ INF or Dynamodb : ", e);
            String rebuild = String.format("{\"error\": \"%s\"}", e.getMessage());
            resp.setBody(rebuild);
            resp.setHeaders(input.getHeaders());
            resp.setStatusCode(503);
            return resp;
        }
        // 5. Handle respond
        handleResponse(input, resp, contactItem);
        return resp;
    }

    private void handleResponse(AwsProxyRequest input, AwsProxyResponse resp, ContactItem contactItem) {
        String rebuild = null;
        try {
            rebuild = contactItem == null ? String.format("{\"error\": \"%s\"}", "null") : m_mapper.writeValueAsString(contactItem);
        } catch (JsonProcessingException e) {
            log.error("", e);
            // ignore
        }
        resp.setBody(rebuild);
        resp.setHeaders(input.getHeaders());
        resp.setStatusCode(200);
    }

    private ContactItem persitClientVoInDB(CFContact funnelContact, Integer clientSbmId, Integer contactInfId,
            CFLambdaServiceRepository repository) throws JsonProcessingException {
        // build client to save Dynomadb
        ClientInfo clientInfo = new ClientInfo().withClientId(clientSbmId).withContactId(contactInfId).withEmail(funnelContact.getEmail())
                .withFirstName(funnelContact.getFirstName()).withLastName(funnelContact.getLastName()).withPhone1(funnelContact.getPhone())
                .withAddress1(funnelContact.getAddress()).withCountry(funnelContact.getCountry())
                .withCreatedAt(Config.DATE_FORMAT_24_H.format(funnelContact.getCreateAt()))
                .withUpdatedAt(Config.DATE_FORMAT_24_H.format(funnelContact.getUpdateAt()));

        ContactItem contactItem = new ContactItem().withEmail(funnelContact.getEmail()) // unique key
                .withContactInfo(clientInfo);
        repository.getContactItemService().put(contactItem);
        return contactItem;
    }

    /**
     * @param funnelContact
     * @return contact id See more https://developer.infusionsoft.com/docs/table-schema/ For Contact table
     */
    private Integer addContactToInfusionsoft(CFContact funnelContact) {
        Integer contactId = null;
        try {
            Map<String, String> dataRecord = new HashMap<>();
            dataRecord.put("Email", funnelContact.getEmail());
            dataRecord.put("FirstName", funnelContact.getFirstName());
            dataRecord.put("LastName", funnelContact.getLastName());
            dataRecord.put("Phone1", funnelContact.getPhone());
            dataRecord.put("Country", funnelContact.getCountry());
            dataRecord.put("City", funnelContact.getCity());
            dataRecord.put("StreetAddress1", funnelContact.getAddress());

            ContactServiceInf contactServiceInf = m_cfLambdaService.getContactServiceInf();
            contactId = contactServiceInf.addWithDupCheck(Config.INFUSIONSOFT_API_NAME, Config.INFUSIONSOFT_API_KEY,
                    new AddNewContactQuery().withDataRecord(dataRecord));
        } catch (InfSDKExecption e) {
            throw new ClickFunnelLambdaException(e);
        }
        return contactId;
    }

    private Integer addClientToSimplyBookMe(CFContact funnelContact) {
        Integer clientSbmId = null;
        try {
            TokenServiceSbm tokenServiceSbm = m_cfLambdaService.getTokenServiceSbm();
            ClientServiceSbm clientServiceSbm = m_cfLambdaService.getClientServiceSbm();
            String userToken = tokenServiceSbm.getUserToken(Config.SIMPLY_BOOK_COMPANY_LOGIN, Config.SIMPLY_BOOK_USER,
                    Config.SIMPLY_BOOK_PASSWORD, Config.SIMPLY_BOOK_SERVICE_URL_lOGIN);
            ClientData client = new ClientData(funnelContact.getFirstName(), funnelContact.getEmail(), funnelContact.getPhone());
            clientSbmId = clientServiceSbm.addClient(Config.SIMPLY_BOOK_COMPANY_LOGIN, Config.SIMPLY_BOOK_ADMIN_SERVICE_URL, userToken,
                    client);
        } catch (SbmSDKException e) {
            throw new ClickFunnelLambdaException(e);
        }
        return clientSbmId;
    }

}
