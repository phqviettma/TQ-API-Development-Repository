package com.tq.simplybook.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.lambda.context.Env;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateInternalHandlerTest {
    
    public void test() throws SbmSDKException {
        ContactServiceInf csi = new ContactServiceImpl();
        BookingServiceSbm bss = new BookingServiceSbmImpl();
        TokenServiceSbm tss = new TokenServiceImpl();
        ContactItemService cis = mock(ContactItemService.class);
        ContactItem contactItem = new ContactItem();
        ClientInfo ci = new ClientInfo();
        ci.setContactId(10304);
        contactItem.setClient(ci);
        when(cis.load(any())).thenReturn(contactItem);
        Env env = MockUtil.mockEnv();
        
        CreateInternalHandler handler = new CreateInternalHandler(env, tss, bss, csi, cis);

        PayloadCallback payLoad = new PayloadCallback();
        payLoad.setBooking_id(8L);
        payLoad.setBooking_hash("0ae1d64c5981efeca54082536beb5901");
        payLoad.setNotification_type("create");

        handler.handle(payLoad);
    }
}
