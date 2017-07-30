package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.configuration.BasicClickFunnelExertenalService;
import com.tq.clickfunnel.lambda.configuration.ClickFunnelExternalService;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.clickfunnel.lambda.dynamodb.model.ClientInfo;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.clickfunnel.lambda.exception.ClickFunnelLambdaException;
import com.tq.clickfunnel.lambda.modle.Contact;
import com.tq.clickfunnel.lambda.modle.ContactPayload;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddNewContactQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.ClientData;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

/**
 * Handle CREATE contact
 *
 */
public class CreateContactHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final Logger log = LoggerFactory.getLogger(CreateContactHandler.class);

    private ObjectMapper mapper = new ObjectMapper();

    private ClickFunnelExternalService m_clickFunnelExternalService;;

    private AmazonDynamoDB m_amazonDynamoDB;

    private ContactItemService m_contactItemService;

    public CreateContactHandler() {
        m_clickFunnelExternalService = BasicClickFunnelExertenalService.defaults();
        m_amazonDynamoDB = DynanodbUtils.getAmazonDynamoDB(Config.DYNAMODB_DEFAULT_REGION, Config.AWS_ACCESS_KEY, Config.AWS_SECRET_ACCESS_KEY);
        setContactItemService(new ContactItemServiceImpl(m_amazonDynamoDB));
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        log.info("{} ", input.getBody());
        AwsProxyResponse resp = new AwsProxyResponse();
        ContactItem contactItem = null;
        try {
            // 1. building Client of Click Funnel from incoming AWS proxy request.
            ContactPayload contactPayLoad = mapper.readValue(input.getBody(), ContactPayload.class);
            if (contactPayLoad != null && contactPayLoad.getContact() != null) {
                Contact sbmContact = contactPayLoad.getContact();

                // 2. creating the client based on Contact of Click Funnel on Simplybook.me
                Integer clientSbmId = addClientToSimplyBookMe(sbmContact);

                // 3. creating the contact based on Contact of Click Funnel on Infusion soft
                Integer contactInfId = addContactToInfusionsoft(sbmContact);

                // 4. Saving the client & contact ID into DynamoDB.
                contactItem = persitClientVoInDB(sbmContact, clientSbmId, contactInfId);
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
            rebuild = contactItem == null ? String.format("{\"error\": \"%s\"}", "null") : mapper.writeValueAsString(contactItem);
        } catch (JsonProcessingException e) {
            //ignore
        }
        resp.setBody(rebuild);
        resp.setHeaders(input.getHeaders());
        resp.setStatusCode(200);
    }

    private ContactItem persitClientVoInDB(Contact sbmContact, Integer clientSbmId, Integer contactInfId) throws JsonProcessingException {
        // build client to save Dynomadb
        ClientInfo clientInfo = new ClientInfo().withClientId(clientSbmId).withContactId(contactInfId).withEmail(sbmContact.getEmail())
                .withFirstName(sbmContact.getFirstName()).withLastName(sbmContact.getLastName()).withPhone1(sbmContact.getPhone())
                .withAddress1(sbmContact.getAddress()).withCountry(sbmContact.getCountry())
                .withCreatedAt(Config.DATE_FORMAT_24_H.format(sbmContact.getCreateAt()))
                .withUpdatedAt(Config.DATE_FORMAT_24_H.format(sbmContact.getUpdateAt()));
        ContactItem contactItem = new ContactItem().withContactInfo(clientInfo);
        m_contactItemService.put(contactItem);
        return contactItem;
    }

    /**
     * @param sbmContact
     * @return contact id 
     * See more https://developer.infusionsoft.com/docs/table-schema/
     * For Contact table
     */
    private Integer addContactToInfusionsoft(Contact sbmContact) {
        Integer contactId = null;
        try {
            Map<String, String> dataRecord = new HashMap<>();
            dataRecord.put("Email", sbmContact.getEmail());
            dataRecord.put("FirstName", sbmContact.getFirstName());
            dataRecord.put("LastName", sbmContact.getLastName());
            dataRecord.put("Phone1", sbmContact.getPhone());
            dataRecord.put("Country", sbmContact.getCountry());
            dataRecord.put("City", sbmContact.getCity());
            dataRecord.put("StreetAddress1", sbmContact.getAddress());
            
            ContactServiceInf contactServiceInf = m_clickFunnelExternalService.getContactServiceInf();
            contactId = contactServiceInf.addWithDupCheck(Config.INFUSIONSOFT_API_NAME, Config.INFUSIONSOFT_API_KEY,
                    new AddNewContactQuery().withDataRecord(dataRecord));
        } catch (InfSDKExecption e) {
            throw new ClickFunnelLambdaException(e);
        }
        return contactId;
    }

    private Integer addClientToSimplyBookMe(Contact sbmContact) {
        Integer clientSbmId = null;
        try {
            TokenServiceSbm tokenServiceSbm = m_clickFunnelExternalService.getTokenServiceSbm();
            ClientServiceSbm clientServiceSbm = m_clickFunnelExternalService.getClientServiceSbm();
            String userToken = tokenServiceSbm.getUserToken(Config.SIMPLY_BOOK_COMPANY_LOGIN, Config.SIMPLY_BOOK_USER,
                    Config.SIMPLY_BOOK_PASSWORD, Config.SIMPLY_BOOK_SERVICE_URL_lOGIN);
            ClientData client = new ClientData(sbmContact.getFirstName(), sbmContact.getEmail(), sbmContact.getPhone());
            clientSbmId = clientServiceSbm.addClient(Config.SIMPLY_BOOK_COMPANY_LOGIN, Config.SIMPLY_BOOK_ADMIN_SERVICE_URL, userToken,
                    client);
        } catch (SbmSDKException e) {
            throw new ClickFunnelLambdaException(e);
        }
        return clientSbmId;
    }

    /**
     * @param clickFunnelContext
     * @param amazonDynamoDB
     *            Take a note : the constructor is used for Unit test
     */
    public CreateContactHandler(ClickFunnelExternalService clickFunnelContext, AmazonDynamoDB amazonDynamoDB) {
        this.m_clickFunnelExternalService = clickFunnelContext;
        this.m_amazonDynamoDB = amazonDynamoDB;
    }

    /**
     * @param contactItemService
     *            Only for UTs
     */
    public void setContactItemService(ContactItemService contactItemService) {
        this.m_contactItemService = contactItemService;
    }

}
