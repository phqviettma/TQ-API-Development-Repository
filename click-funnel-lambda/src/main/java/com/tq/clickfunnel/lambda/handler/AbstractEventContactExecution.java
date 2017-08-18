package com.tq.clickfunnel.lambda.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.utils.Utils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddNewContactQuery;
import com.tq.inf.service.ContactServiceInf;

public abstract class AbstractEventContactExecution extends AbstractEventPayloadExecution {

    protected static final Logger log = Logger.getLogger(AbstractEventContactExecution.class);

    protected ContactItem persitClientVoInDB(CFContact funnelContact, Integer clientSbmId, Integer contactInfId, LambdaContext lambdaContext)
            throws JsonProcessingException {
        // build client to save DynomaDB
        long start = System.currentTimeMillis();
        ClientInfo clientInfo = new ClientInfo().withClientId(clientSbmId).withContactId(contactInfId).withEmail(funnelContact.getEmail())
                .withFirstName(funnelContact.getFirstName()).withLastName(funnelContact.getLastName()).withPhone1(funnelContact.getPhone())
                .withAddress1(funnelContact.getAddress()).withCountry(funnelContact.getCountry())
                .withCreatedAt(Config.DATE_FORMAT_24_H.format(funnelContact.getCreateAt()))
                .withUpdatedAt(Config.DATE_FORMAT_24_H.format(funnelContact.getUpdateAt()));

        ContactItem contactItem = new ContactItem().withEmail(funnelContact.getEmail()) // unique
                                                                                        // key
                .withContactInfo(clientInfo);
        lambdaContext.getContactItemService().put(contactItem);
        log.info(String.format("addDBContact()= %d ms", (System.currentTimeMillis() - start)));
        return contactItem;
    }

    /**
     * @param funnelContact
     * @param lambdaContext
     * @return contact id See more
     *         https://developer.infusionsoft.com/docs/table-schema/ For Contact
     *         table
     */
    protected Integer addContactToInfusionsoft(CFContact funnelContact, LambdaContext lambdaContext) throws CFLambdaException {
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

    protected String getCity(CFContact funnelContact) {
        return Utils.isEmpty(funnelContact.getCity()) ? funnelContact.getShippingCity() : funnelContact.getCity();
    }

    protected String getAddress(CFContact funnelContact) {
        return Utils.isEmpty(funnelContact.getAddress()) ? funnelContact.getShippingAddress() : funnelContact.getAddress();
    }

    protected String getCountry(CFContact funnelContact) {
        String country = Utils.isEmpty(funnelContact.getCountry()) ? funnelContact.getShippingCountry() : funnelContact.getCountry();
        return country;
    }

    protected String getState(CFContact funnelContact) {
        String stage = Utils.isEmpty(funnelContact.getState()) ? funnelContact.getShippingState() : funnelContact.getState();
        return stage;
    }
}
