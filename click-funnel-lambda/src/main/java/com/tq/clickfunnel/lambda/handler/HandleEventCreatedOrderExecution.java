package com.tq.clickfunnel.lambda.handler;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.modle.CFProducts;
import com.tq.clickfunnel.lambda.modle.CFPurchase;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.ApplyTagQuery;

public class HandleEventCreatedOrderExecution extends HandleEventOrderExecution {

    public static final Logger log = Logger.getLogger(HandleEventCreatedOrderExecution.class);

    @Override
    protected AwsProxyResponse handleEventOrderLambda(AwsProxyRequest input, CFOrderPayload orderPayload, CFLambdaContext cfLambdaContext) {
        AwsProxyResponse resp = new AwsProxyResponse();
        LambdaContext lambdaContext = cfLambdaContext.getLambdaContext();
        List<CFProducts> products = null;
        CFPurchase purchase = orderPayload.getPurchase();
        ContactItem contactItem = lambdaContext.getContactItemService().load(orderPayload.getPurchase().getContact().getEmail());
        
        Integer purchaseId = (purchase == null) ? orderPayload.getId() :  purchase.getId();
        
        if(purchaseId != null) {
        	OrderItem purchasedOrder = lambdaContext.getOrderItemService().load(purchaseId);
        	if(purchasedOrder != null) {
        		handleResponse(input, resp, new AlreadyProccessedOrder(purchasedOrder));
        	} else {
        		products = (purchase == null) ? orderPayload.getProducts() : purchase.getProducts();
                if (products == null || products.isEmpty()) {
                    throw new CFLambdaException("No any products is purchased with " + purchase.getId() + " identification.");
                }
                // Currently we just support 1 product
                CFProducts cfProducts = products.get(0);
                // 1. Create Order under email on infusion soft
                OrderBillingIntergtion billingIntegration = FactoryOrderBillingIntegration
                        .getBillingIntegration(cfProducts.getBillingIntegration());
                OrderItem addOrder = billingIntegration.createBilling(orderPayload, lambdaContext);
                applyTagToInfusionsoft(contactItem, lambdaContext);
                // 2. Save the Order to DynamoDB for handling in further
                if (addOrder != null) {
                    lambdaContext.getOrderItemService().put(addOrder);
                }
                // 3. handle successfully
                handleResponse(input, resp, addOrder);
        	}
        } 
        
        return resp;
    }
    private void applyTagToInfusionsoft(ContactItem contactItem, LambdaContext lambdaContext)
    {
    	String infusionsoftApiName = lambdaContext.getEnvVar().getEnv(Config.INFUSIONSOFT_API_NAME);
        String infusionsoftApiKey = lambdaContext.getEnvVar().getEnv(Config.INFUSIONSOFT_API_KEY);
      Integer infusionsoftTag = Integer.valueOf(lambdaContext.getEnvVar().getEnv(Config.INFUSIONSOFT_CLICKFUNNEL_ORDER_PAID_TAG));
      Integer contactId = contactItem.getClient().getContactId();
        try {
        ApplyTagQuery applyTagQuery =new ApplyTagQuery().withContactID(contactId).withTagID(infusionsoftTag);
       
			lambdaContext.getContactServiceInf().appyTag(infusionsoftApiName, infusionsoftApiKey, applyTagQuery);
		} catch (InfSDKExecption e) {
			throw new CFLambdaException(e.getMessage());
		}
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AlreadyProccessedOrder implements Serializable {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 9017025311556738105L;
		private String additionalInfo = "The order was added already";
    	private OrderItem order = null;
    	public AlreadyProccessedOrder() {
    		
    	}
    	public AlreadyProccessedOrder(OrderItem order) {
    		this.order = order;
    	}
    	
		public String getAdditionalInfo() {
			return additionalInfo;
		}
		public void setAdditionalInfo(String additionalInfo) {
			this.additionalInfo = additionalInfo;
		}
		
		public OrderItem getOrder() {
			return order;
		}
		public void setOrder(OrderItem order) {
			this.order = order;
		}
    }
}
