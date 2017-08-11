package com.tq.inf.impl;

import java.util.LinkedList;
import java.util.List;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddSubscriptionQuery;
import com.tq.inf.query.BlankOrderQuery;
import com.tq.inf.service.ActionCallback;
import com.tq.inf.service.ApiContext;
import com.tq.inf.service.InvoiceServiceInf;
import com.tq.inf.utils.XmlRqcUtils;

/**
 * 
 * https://keys.developer.infusionsoft.com/docs/read/Invoice_Service
 */
public class InvoiceServiceImpl implements InvoiceServiceInf {

    @Override
    public Boolean deleteSubscription(String apiName, String apiKey, final Integer subscriptionId) throws InfSDKExecption {
        return (Boolean) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey());// secure key
                params.add(subscriptionId);
                return params;
            }
        }, "InvoiceService.deleteSubscription");
    }

    @Override
    public Boolean deleteInvoice(String apiName, String apiKey, final Integer invoiceID) throws InfSDKExecption {
        return (Boolean) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey()); // secure key

                params.add(invoiceID);
                return params;
            }
        }, "InvoiceService.deleteInvoice");
    }

    @Override
    public Integer addRecurringOrder(String apiName, String apiKey, final AddSubscriptionQuery subQuery) throws InfSDKExecption {
        return (Integer) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey()); // secure key

                params.add(subQuery.getContactID());
                params.add(subQuery.getAllowDup());
                params.add(subQuery.getSubscriptionPlanID());
                params.add(subQuery.getQty());
                params.add(subQuery.getPrice());
                params.add(subQuery.getAllowTax());
                params.add(subQuery.getMerchantID());
                params.add(subQuery.getCreditCardID());
                params.add(subQuery.getAffiliateID());
                params.add(subQuery.getDayCharge());
                return params;
            }
        }, "InvoiceService.addRecurringOrder");
    }

    @Override
    public Integer createInvoiceForRecurring(String apiName, String apiKey, final Integer recurringOrderId) throws InfSDKExecption {
        return (Integer) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey());// secure key

                params.add(recurringOrderId);
                return params;
            }
        }, "InvoiceService.createInvoiceForRecurring");
    }

    @Override
    public Integer createBlankOrder(String apiName, String apiKey, BlankOrderQuery blankOrder) throws InfSDKExecption {
        return (Integer) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey());// secure key

                params.add(blankOrder.getContactID());
                params.add(blankOrder.getDescription());
                params.add(blankOrder.getOrderDate());
                params.add(blankOrder.getLeadAffiliateId());
                params.add(blankOrder.getSaleAffiliateId());
                return params;
            }
        }, "InvoiceService.createBlankOrder");
    }
}
