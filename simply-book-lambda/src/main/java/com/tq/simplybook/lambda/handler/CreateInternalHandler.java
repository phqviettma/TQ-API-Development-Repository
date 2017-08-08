package com.tq.simplybook.lambda.handler;

import java.util.HashMap;
import java.util.Map;

import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddDataQuery;
import com.tq.inf.query.ApplyTagQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.context.Env;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateInternalHandler implements InternalHandler {
    private Env m_env = null;
    private BookingServiceSbm m_bss = null;
    private TokenServiceSbm m_tss = null;
    private ContactServiceInf m_csi = null;
    private ContactItemService m_cis = null;

    public CreateInternalHandler(Env env, TokenServiceSbm tss, BookingServiceSbm bss, ContactServiceInf csi, ContactItemService cis) {
        m_env = env;
        m_tss = tss;
        m_bss = bss;
        m_csi = csi;
        m_cis = cis;
    }

    @Override
    public void handle(PayloadCallback payload) throws SbmSDKException {
        String companyLogin = m_env.getSimplyBookCompanyLogin();
        String user = m_env.getSimplyBookUser();
        String password = m_env.getSimplyBookPassword();
        String loginEndPoint = m_env.getSimplyBookServiceUrlLogin();
        String adminEndPoint = m_env.getSimplyBookAdminServiceUrl();
        String infusionSoftApiName = m_env.getInfusionSoftApiName();
        String infusionSoftApiKey = m_env.getInfusionSoftApiKey();
        
        String infusionSoftAppointmentTimeField = m_env.getInfusionSoftAppointmentTimeField();
        String infusionSoftAppointmentLocationField = m_env.getInfusionSoftAppointmentLocationField();
        String infusionSoftServiceProviderField = m_env.getInfusionSoftServiceProviderField();
        String infusionSoftAppointmentInstructionField = m_env.getInfusionSoftAppointmentInstructionField();
        
        int appliedTagId = m_env.getInfusionSoftCreateAppliedTag();
        
        Long bookingId = payload.getBooking_id();

        String token = m_tss.getUserToken(companyLogin, user, password, loginEndPoint);
        BookingInfo bookingInfo = m_bss.getBookingInfo(companyLogin, adminEndPoint, token, bookingId);
        if (bookingInfo == null) {
            throw new SbmSDKException("There is no booking content asociated to the booking id: " + bookingId);
        }

        // load with email as id from DynamoDB
        String clientEmail = bookingInfo.getClient_email();
        
        // get
        ContactItem contactItem = m_cis.get(clientEmail);
        
        if (contactItem == null || contactItem.getClient() == null || contactItem.getClient().getContactId() == null) {
            throw new SbmSDKException("There is no contact on Infusion Soft asociated to the email: " + clientEmail);
        }

        Integer ifContactId = contactItem.getClient().getContactId();
        Map<String, String> updateRecord = new HashMap<>();
        
        updateRecord.put(infusionSoftAppointmentTimeField, buildApppointmentTime(bookingInfo.getStart_date_time(), bookingInfo.getEnd_date_time()));
        updateRecord.put(infusionSoftAppointmentLocationField, bookingInfo.getLocation() == null ? "" : String.valueOf(bookingInfo.getLocation().getTitle()));
        updateRecord.put(infusionSoftServiceProviderField, bookingInfo.getUnit_name());
        updateRecord.put(infusionSoftAppointmentInstructionField, bookingInfo.getUnit_description());

        try {

            m_csi.update(infusionSoftApiName, infusionSoftApiKey, new AddDataQuery().withRecordID(ifContactId).withDataRecord(updateRecord));

        } catch (InfSDKExecption e) {
            throw new SbmSDKException("Updating custom field to Infusion Soft failed", e);
        }
        
        try {
            ApplyTagQuery applyTagQuery = new ApplyTagQuery().withContactID(ifContactId).withTagID(appliedTagId);

            m_csi.appyTag(infusionSoftApiName, infusionSoftApiKey, applyTagQuery);
        } catch (InfSDKExecption e) {
            throw new SbmSDKException("Applying Tag " + appliedTagId + " to contact Infusion Soft failed", e);
        }
    }

    private static String buildApppointmentTime(String start_date_time, String end_date_time) {
        return start_date_time + ((end_date_time == null || end_date_time.isEmpty() ? "" : " - " + end_date_time ));
    }

}
