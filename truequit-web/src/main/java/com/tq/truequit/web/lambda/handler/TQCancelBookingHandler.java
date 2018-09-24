package com.tq.truequit.web.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.SbmBookingInfo;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.ApplyTagQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.truequit.lambda.context.ShowBookingLambdaEnv;
import com.tq.truequit.web.lambda.response.ShowBookingInfoResponse;

public class TQCancelBookingHandler implements BookingHandler {
	private static final Logger m_log = LoggerFactory.getLogger(TQCancelBookingHandler.class);
	private BookingServiceSbm bookingService = null;
	private ShowBookingLambdaEnv env = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private ContactItemService contactService = null;
	private ContactServiceInf contactInfService = null;
	private SbmBookingInfoService sbmBookingService = null;

	public TQCancelBookingHandler(BookingServiceSbm bookingService, ShowBookingLambdaEnv env,
			TokenServiceSbm tokenServiceSbm, ContactServiceInf contactInfService, ContactItemService contactService,
			SbmBookingInfoService sbmBookingService) {
		this.bookingService = bookingService;
		this.env = env;
		this.tokenServiceSbm = tokenServiceSbm;
		this.contactInfService = contactInfService;
		this.contactService = contactService;
		this.sbmBookingService = sbmBookingService;
	}

	@Override
	public ShowBookingInfoResponse handle(ShowBookingRequest request) throws SbmSDKException, InfSDKExecption {
		String companyLogin = env.getSimplyBookCompanyLogin();
		String user = env.getSimplyBookUser();
		String password = env.getSimplyBookPassword();
		String loginEndpoint = env.getSimplyBookServiceUrlLogin();
		String endpoint = env.getSimplyBookAdminServiceUrl();
		String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndpoint);
		bookingService.cancelBooking(companyLogin, endpoint, token, request.getParams().getBookingId());
		excuteInfusionsoft(request);
		deleteItemInDB(request);
		ShowBookingInfoResponse response = new ShowBookingInfoResponse();
		response.setSucceeded(true);
		return response;
	}

	private void deleteItemInDB(ShowBookingRequest request) {
		SbmBookingInfo bookingItem = sbmBookingService.load(request.getParams().getBookingId().longValue());
		if (bookingItem != null) {
			sbmBookingService.delete(bookingItem);
			
		}
		
	}

	private void excuteInfusionsoft(ShowBookingRequest request){
		String apiName = env.getInfusionSoftApiName();
		String apiKey = env.getInfusionSoftApiKey();
		Integer cancelTag = env.getInfusionSoftAppliedCancelTag();
		ContactItem clientContact = contactService.load(request.getParams().getClientEmail());
		if (clientContact != null) {
			try {
				ApplyTagQuery applyTagQuery = new ApplyTagQuery().withContactID(clientContact.getClient().getContactId())
						.withTagID(cancelTag);
				Boolean isApplied = contactInfService.applyTag(apiName, apiKey, applyTagQuery);
				if(isApplied) {
					m_log.info("Applied tag successfully with tag id " + cancelTag + "under contact id " + clientContact.getClient().getContactId());
				} else {
					m_log.info(String.format("Can not apply tag id %d under contact id: %d", cancelTag, clientContact.getClient().getContactId()));
				}
			} catch (InfSDKExecption e) {
				m_log.info("fail during apply tag " + e);
			}
		}
		
	}

}
