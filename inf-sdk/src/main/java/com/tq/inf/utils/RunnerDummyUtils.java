package com.tq.inf.utils;

import java.util.Arrays;
import java.util.List;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.impl.DataServiceImpl;
import com.tq.inf.impl.RecurringOrderImpl;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.RecurringOrderInf;

public class RunnerDummyUtils {
    
    private static final String API_NAME = "";
    private static final String API_KEY = "";

    public static void main(String[] args) throws InfSDKExecption {
        
        DataServiceInf dataService = new DataServiceImpl();
        RecurringOrderInf recurringOrderInf = new RecurringOrderImpl(dataService);
        
        List<String> selectedFields=  Arrays.asList("Id", "ContactId", "OriginatingOrderId", "ProductId", "StartDate", "EndDate", "LastBillDate",
                "NextBillDate", "Status", "AutoCharge", "CC1", "CC2", "MerchantAccountId");
        
        Object[] allRecuringOrder = recurringOrderInf.getAllRecurringOrder(API_NAME, API_KEY, 28, selectedFields);
        for (Object bj : allRecuringOrder ) {
            System.out.println(bj);
        }
    }

}
