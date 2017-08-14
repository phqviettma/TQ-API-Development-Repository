package com.tq.inf.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.impl.DataServiceImpl;
import com.tq.inf.impl.InvoiceServiceImpl;
import com.tq.inf.impl.RecurringOrderImpl;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.InvoiceServiceInf;
import com.tq.inf.service.RecurringOrderInf;

public class RunnerDummyUtils {
    
    private static final String API_NAME = "https://uf238.infusionsoft.com/api/xmlrpc";
    private static final String API_KEY = "";

    public static void main(String[] args) throws InfSDKExecption {
        getAllRecurringOrders();
        //deleteInvoice();
    }

    public static void getAllRecurringOrders() throws InfSDKExecption {
        DataServiceInf dataService = new DataServiceImpl();
        RecurringOrderInf recurringOrderInf = new RecurringOrderImpl(dataService);
        
        List<String> selectedFields=  Arrays.asList("Id", "ContactId", "OriginatingOrderId", "ProductId", "StartDate", "EndDate", "LastBillDate",
                "NextBillDate", "Status", "AutoCharge", "CC1", "CC2", "MerchantAccountId");
        
        Map<?, ?> latestRecurringOrderFromProduct = (Map<?, ?>) recurringOrderInf.getLatestRecurringOrderFromProduct(API_NAME, API_KEY, 10314, 11340, selectedFields);
        System.out.println(latestRecurringOrderFromProduct);
        
      /*  Object[] allRecuringOrder = recurringOrderInf.getAllRecurringOrder(API_NAME, API_KEY, 28, selectedFields);
        for (Object bj : allRecuringOrder ) {
            System.out.println(bj);
        }*/
    }
    
    public static void deleteInvoice() throws InfSDKExecption {
        InvoiceServiceInf invoice  = new InvoiceServiceImpl();
        Integer invoiceID = 182;
        invoice.deleteInvoice(API_NAME, API_KEY, invoiceID);
        invoice.deleteSubscription(API_NAME, API_KEY, 230);
    }

}
