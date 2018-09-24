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

public class TQConfirmNoShowHandler implements BookingHandler {
	private static final Logger m_log = LoggerFactory.getLogger(TQConfirmNoShowHandler.class);
	private BookingServiceSbm bookingService = null;
	private ShowBookingLambdaEnv env = null;
	private TokenServiceSbm tokenServiceSbm = null;
	private ContactItemService contactService = null;
	private ContactServiceInf contactInfService = null;
	private SbmBookingInfoService sbmBookingInfoService = null;

	public TQConfirmNoShowHandler(BookingServiceSbm bookingService, ShowBookingLambdaEnv env,
			TokenServiceSbm tokenServiceSbm, ContactServiceInf contactInfService, ContactItemService contactService,
			SbmBookingInfoService sbmBookingInfoService) {
		this.bookingService = bookingService;
		this.env = env;
		this.tokenServiceSbm = tokenServiceSbm;
		this.contactInfService = contactInfService;
		this.contactService = contactService;
		this.sbmBookingInfoService = sbmBookingInfoService;
	}

	@Override
	public ShowBookingInfoResponse handle(ShowBookingRequest request) throws SbmSDKException, InfSDKExecption {
		m_log.info("Received request " + request);
		String companyLogin = env.getSimplyBookCompanyLogin();
		String user = env.getSimplyBookUser();
		String password = env.getSimplyBookPassword();
		String loginEndpoint = env.getSimplyBookServiceUrlLogin();
		String endpoint = env.getSimplyBookAdminServiceUrl();
		String token = tokenServiceSbm.getUserToken(companyLogin, user, password, loginEndpoint);
		bookingService.setBookingStatus(env.getSimplyBookCompanyLogin(), endpoint, token,
				request.getParams().getBookingId(), env.getStatusClientNoShow());
		excuteInfusionsoft(request);
		changeBookingStatus(request);
		ShowBookingInfoResponse response = new ShowBookingInfoResponse();
		response.setSucceeded(true);
		return response;
	}

	private void changeBookingStatus(ShowBookingRequest request) {
		SbmBookingInfo bookingInfo = sbmBookingInfoService
				.load(request.getParams().getBookingId().longValue());
		if (bookingInfo != null) {
			bookingInfo.setBookingStatus("no_show");
			sbmBookingInfoService.put(bookingInfo);
		} else {
			m_log.info("Error, can't find this booking");
		}
	}

	private void excuteInfusionsoft(ShowBookingRequest request) throws InfSDKExecption {
		String apiName = env.getInfusionSoftApiName();
		String apiKey = env.getInfusionSoftApiKey();
		Integer tagId = env.getInfusionSoftAppliedNoShowTag();
		ContactItem clientContact = contactService.load(request.getParams().getClientEmail());
		if (clientContact != null) {
			try {
				ApplyTagQuery applyTagQuery = new ApplyTagQuery()
						.withContactID(clientContact.getClient().getContactId()).withTagID(tagId);
				Boolean isApplied = contactInfService.applyTag(apiName, apiKey, applyTagQuery);
				if (isApplied) {
					m_log.info(String.format("Applied tag with tagId %d under contact id %d",
							clientContact.getClient().getContactId(), tagId));

				}
			} catch (InfSDKExecption e) {
				m_log.info("Can not apply tag" + e);
			}
		}
	}

}
