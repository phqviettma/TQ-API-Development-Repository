package com.tq.clickfunnel.lambda.handler;

public class FactoryOrderBillingIntegration {
    
    public static OrderBillingIntergtion getBillingIntegration(String billingItegration) {
        if ("Infusionsoft".equalsIgnoreCase(billingItegration))
            return new InfOrderBillingIntegration();
        return new StripeOrderBillingIntergration();
    }
    
}
