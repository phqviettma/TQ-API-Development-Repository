package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFAffilicateSingupPayload;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.SignupInfo;
import com.tq.common.lambda.dynamodb.model.SignupItem;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.ApplyTagQuery;

/**
 * 
 * @author lccanh The class will handle for affiliating Singup in Clickfunnel Backpack
 *
 */

public class HandleEventAffiliateBackpackSignupExecution extends AbstractEventContactExecution {
    
    @Override
    public AwsProxyResponse handleLambdaProxy(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException {
        AwsProxyResponse resp = new AwsProxyResponse();
        LambdaContext lambdaContext = cfLambdaContext.getLambdaContext();
        SignupItem contactItem = null;
        try {
            // 1. building Client of Click Funnel from incoming AWS proxy request.
            CFAffilicateSingupPayload contactPayLoad = m_mapper.readValue(input.getBody(), CFAffilicateSingupPayload.class);
            if (contactPayLoad != null && contactPayLoad.getContact_profile() != null) {
                CFContact funnelContact = contactPayLoad.getContact_profile();

                // 2. creating the contact based on Contact of Click Funnel on Infusion soft
                Integer contactInfId = addContactToInfusionsoft(funnelContact, lambdaContext);
                
                // 3. Apply InfusionSoft tag
                applyInfusionSoftTagToContact(lambdaContext, contactInfId);

                // 4. Saving the client & contact ID into DynamoDB.
                contactItem = persitSignupVoInDB(funnelContact, contactInfId, lambdaContext);
            }
        } catch (IOException | CFLambdaException | InfSDKExecption e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        // 5. Handle respond
        handleResponse(input, resp, contactItem);
        return resp;
    }

    protected SignupItem persitSignupVoInDB(CFContact funnelContact, Integer contactInfId, LambdaContext lambdaContext)
            throws JsonProcessingException {
        // build client to save DynomaDB
        long start = System.currentTimeMillis();
        SignupInfo clientInfo = new SignupInfo().withContactId(contactInfId).withEmail(funnelContact.getEmail())
                .withFirstName(funnelContact.getFirstName()).withLastName(funnelContact.getLastName()).withPhone1(funnelContact.getPhone())
                .withAddress1(funnelContact.getAddress()).withCountry(funnelContact.getCountry())
                .withCreatedAt(Config.DATE_FORMAT_24_H.format(funnelContact.getCreateAt()))
                .withUpdatedAt(Config.DATE_FORMAT_24_H.format(funnelContact.getUpdateAt()));

        SignupItem signupItem = new SignupItem().withEmail(funnelContact.getEmail()) // unique
                                                                                        // key
                .withContactInfo(clientInfo);
        lambdaContext.getSignupItemService().put(signupItem);
        log.info(String.format("addDBSignup()= %d ms", (System.currentTimeMillis() - start)));
        
        return signupItem;
    }
    
    private void applyInfusionSoftTagToContact(LambdaContext lambdaContext, Integer contactInfId) throws InfSDKExecption {
        EnvVar envVar = lambdaContext.getEnvVar();
        Integer appliedTagId = Integer.valueOf(envVar.getEnv(Config.INFUSIONSOFT_CLICKFUNNEL_AFFILIALTE_BACKPACK_SIGNUP_TAG));
        ApplyTagQuery applyTagQuery = new ApplyTagQuery().withContactID(contactInfId).withTagID(appliedTagId);
        lambdaContext.getContactServiceInf().appyTag(envVar.getEnv(Config.INFUSIONSOFT_API_NAME), envVar.getEnv(Config.INFUSIONSOFT_API_KEY), applyTagQuery);
    }
}
