package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;

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
        ContactItem contactItem = null;
        try {
            // 1. building Client of Click Funnel from incoming AWS proxy request.
            CFContactPayload contactPayLoad = m_mapper.readValue(input.getBody(), CFContactPayload.class);
            if (contactPayLoad != null && contactPayLoad.getContact() != null) {
                CFContact funnelContact = contactPayLoad.getContact();

                // 2. creating the contact based on Contact of Click Funnel on Infusion soft
                Integer contactInfId = addContactToInfusionsoft(funnelContact, lambdaContext);
                
                // 3. Apply InfusionSoft tag
                applyInfusionSoftTagToContact(lambdaContext, contactInfId);

                // 4. Saving the client & contact ID into DynamoDB.
                contactItem = persitClientVoInDB(funnelContact, null, contactInfId, lambdaContext);
            }
        } catch (IOException | CFLambdaException | InfSDKExecption e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        // 5. Handle respond
        handleResponse(input, resp, contactItem);
        return resp;
    }

    private void applyInfusionSoftTagToContact(LambdaContext lambdaContext, Integer contactInfId) throws InfSDKExecption {
        EnvVar envVar = lambdaContext.getEnvVar();
        Integer appliedTagId = Integer.valueOf(envVar.getEnv(Config.INFUSIONSOFT_CLICKFUNNEL_AFFILIALTE_BACKPACK_SIGNUP_TAG));
        ApplyTagQuery applyTagQuery = new ApplyTagQuery().withContactID(contactInfId).withTagID(appliedTagId);
        lambdaContext.getContactServiceInf().appyTag(envVar.getEnv(Config.INFUSIONSOFT_API_NAME), envVar.getEnv(Config.INFUSIONSOFT_API_KEY), applyTagQuery);
    }
}
