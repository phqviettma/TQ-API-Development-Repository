package com.tq.simplybook.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.tq.clickfunnel.lambda.dynamodb.model.ClientInfo;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.lambda.context.Env;
import com.tq.simplybook.lambda.handler.CreateInternalHandler;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateInternalHandlerTest {
    @Test
    public void test() throws SbmSDKException {
        Env env = mock(Env.class);
        when(env.getSimplyBookServiceUrlLogin()).thenReturn("https://user-api.simplybook.me/login");
        when(env.getSimplyBookAdminServiceUrl()).thenReturn("https://user-api.simplybook.me/admin/");
        when(env.getSimplyBookCompanyLogin()).thenReturn("phqviet93gmailcom");
        when(env.getSimplyBookUser()).thenReturn("admin");
        when(env.getSimplyBookPassword()).thenReturn("12345678x@X");
        when(env.getSimplyBookApiKey()).thenReturn("4de2a2545aed8e5e19861ee91221460c7e7e0ec90bff6dcad96fcc4d68c8e228");
        when(env.getSimplyBookSecretKey()).thenReturn("c9c8476bdedc3a2294a5fc69c9d0019e2af44dbe126ccb10f7431739a720b1ef");
        when(env.getInfusionSoftApiName()).thenReturn("https://uf238.infusionsoft.com/api/xmlrpc");
        when(env.getInfusionSoftApiKey()).thenReturn("da2083451dab102fbd9122c05629fe63");
        when(env.getInfusionSoftCustomFieldName()).thenReturn("_BookingDetails");

        ContactServiceInf csi = new ContactServiceImpl();
        BookingServiceSbm bss = new BookingServiceSbmImpl();
        TokenServiceSbm tss = new TokenServiceImpl();
        ContactItemService cis = mock(ContactItemService.class);
        ContactItem contactItem = new ContactItem();
        ClientInfo ci = new ClientInfo();
        ci.setContactId(10304);
        contactItem.setClient(ci);
        when(cis.get(any())).thenReturn(contactItem);

        CreateInternalHandler handler = new CreateInternalHandler(tss, bss, csi, cis);

        PayloadCallback payLoad = new PayloadCallback();
        payLoad.setBooking_id(8L);
        payLoad.setBooking_hash("0ae1d64c5981efeca54082536beb5901");
        payLoad.setNotification_type("create");

        handler.handle(env, payLoad);
    }
}
