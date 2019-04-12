package com.tq.simplybook.lambda.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.common.lambda.dynamodb.dao.ClinikoCompanyInfoDaoImpl;
import com.tq.common.lambda.dynamodb.dao.ClinikoSyncToSbmDaoImpl;
import com.tq.common.lambda.dynamodb.dao.ContactItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.CountryItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.GoogleCalendarDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmBookingInfoDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmClinikoSyncDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmGoogleCalendarSyncDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoCompanyInfoServiceImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoSyncToSbmServiceImpl;
import com.tq.common.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.CountryItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.GoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmBookingInfoServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmClinikoSyncImpl;
import com.tq.common.lambda.dynamodb.impl.SbmGoogleCalendarServiceImpl;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.impl.DataServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.DataServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

/**
 * http://docs.aws.amazon.com/lambda/latest/dg/java-handler-using-predefined-
 * interfaces.html
 */
public class FilterEventHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static final Logger m_log = LoggerFactory.getLogger(FilterEventHandler.class);
	private static int STATUS_CODE = 200;
	private ObjectMapper m_jsonMapper = new ObjectMapper();
	public DataServiceInf dataServiceInf = null;
	private Env m_env = null;
	private AmazonDynamoDB m_amazonDynamoDB = null;
	private ContactServiceInf m_csi = null;
	private BookingServiceSbm m_bss = null;
	private TokenServiceSbm m_tss = null;
	private SbmClinikoSyncService m_scs = null;
	private ContactItemService m_cis = null;
	private GoogleCalendarDbService m_gcs = null;
	private SbmGoogleCalendarDbService m_sgcs = null;
	private TokenGoogleCalendarImpl m_tgc = null;
	private ClinikoSyncToSbmService clinikoSyncToSbmService = null;
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = null;
	private GoogleCalendarApiServiceBuilder googleApiServiceBuilder = null;
	private CountryItemService countryItemService = null;
	private ClinikoCompanyInfoService clinikoCompanyService = null;
	private SbmBookingInfoService sbmBookingInfoService = null;
	private InternalHandler m_createHandler = null;
	private InternalHandler m_cancelHandler = null;
	private InternalHandler m_changeHandler = null;

	public FilterEventHandler() {
		this.m_env = Env.load();
		this.dataServiceInf = new DataServiceImpl();
		this.m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(m_env.getRegions(), m_env.getAwsAccessKeyId(),
				m_env.getAwsSecretAccessKey());
		this.m_csi = new ContactServiceImpl();
		this.m_bss = new BookingServiceSbmImpl();
		this.m_tss = new TokenServiceImpl();
		this.m_scs = new SbmClinikoSyncImpl(new SbmClinikoSyncDaoImpl(m_amazonDynamoDB));
		this.m_cis = new ContactItemServiceImpl(new ContactItemDaoImpl(m_amazonDynamoDB));
		this.m_gcs = new GoogleCalendarServiceImpl(new GoogleCalendarDaoImpl(m_amazonDynamoDB));
		this.m_sgcs = new SbmGoogleCalendarServiceImpl(new SbmGoogleCalendarSyncDaoImpl(m_amazonDynamoDB));
		this.m_tgc = new TokenGoogleCalendarImpl();
		this.clinikoSyncToSbmService = new ClinikoSyncToSbmServiceImpl(new ClinikoSyncToSbmDaoImpl(m_amazonDynamoDB));
		this.clinikoApiServiceBuilder = new ClinikoApiServiceBuilder();
		this.googleApiServiceBuilder = new GoogleCalendarApiServiceBuilder();
		this.countryItemService = new CountryItemServiceImpl(new CountryItemDaoImpl(m_amazonDynamoDB));
		this.sbmBookingInfoService = new SbmBookingInfoServiceImpl(new SbmBookingInfoDaoImpl(m_amazonDynamoDB));
		this.clinikoCompanyService = new ClinikoCompanyInfoServiceImpl(new ClinikoCompanyInfoDaoImpl(m_amazonDynamoDB));
		this.m_createHandler = new CreateInternalHandler(m_env, m_tss, m_bss, m_csi, m_cis, m_scs, m_gcs, m_sgcs, m_tgc,
				clinikoSyncToSbmService, clinikoCompanyService, clinikoApiServiceBuilder, googleApiServiceBuilder,
				countryItemService,sbmBookingInfoService);
		this.m_cancelHandler = new CancelInternalHandler(m_env, m_tss, m_bss, m_csi, m_cis, m_scs, m_sgcs, m_gcs, m_tgc,
				clinikoSyncToSbmService, clinikoApiServiceBuilder, googleApiServiceBuilder, sbmBookingInfoService);
		this.m_changeHandler = new ChangeInternalHandler(m_env, m_bss, m_tss, countryItemService, m_csi, m_cis,
				clinikoSyncToSbmService, m_gcs, m_tgc, clinikoApiServiceBuilder, m_sgcs, googleApiServiceBuilder,
				m_scs, sbmBookingInfoService);
	}

	// for testing only
	FilterEventHandler(Env m_env, ContactServiceInf m_csi, BookingServiceSbm m_bss, InternalHandler m_createHandler,
			InternalHandler m_cancelHandler, InternalHandler changeHandler) {

		this.m_env = m_env;
		this.m_csi = m_csi;
		this.m_bss = m_bss;
		this.m_createHandler = m_createHandler;
		this.m_cancelHandler = m_cancelHandler;
		this.m_changeHandler = changeHandler;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();

		m_log.info("Received one request with body: " + input.getBody());

		PayloadCallback payLoad = getPayloadCallback(input.getBody());
		boolean ignored = true;

		Throwable error = null;

		if (payLoad != null) {
			try {
				if ("create".equalsIgnoreCase(payLoad.getNotification_type())) {
					m_createHandler.handle(payLoad);
				} else if ("cancel".equalsIgnoreCase(payLoad.getNotification_type())) {
					m_cancelHandler.handle(payLoad);
				} else if ("change".equals(payLoad.getNotification_type())) {
					m_changeHandler.handle(payLoad);
				} else {
					ignored = false;
				}

				if (ignored) {
					m_log.info("Notification:" + payLoad.getNotification_type() + " for booking ID:"
							+ payLoad.getBooking_id() + " is handled without error");
				} else {
					m_log.info("Notification:" + payLoad.getNotification_type() + " for booking ID:"
							+ payLoad.getBooking_id() + " is unhandled");
				}

			} catch (SbmSDKException | ClinikoSDKExeption | GoogleApiSDKException | InfSDKExecption e) {
				m_log.error("Processed notification: " + payLoad.getNotification_type() + " for booking ID: "
						+ payLoad.getBooking_id() + " results in error: ", e);
				error = e;
			}
		}

		handleResponse(input, resp, payLoad, error);

		return resp;
	}

	public PayloadCallback getPayloadCallback(String value) {
		PayloadCallback payLoad = null;
		try {
			payLoad = m_jsonMapper.readValue(value, PayloadCallback.class);
		} catch (IOException e) {
			m_log.error("Error during parsing {} : {} .", value, e);
		}
		return payLoad;
	}

	private void handleResponse(AwsProxyRequest input, AwsProxyResponse resp, PayloadCallback pc, Throwable t) {
		String body = null;
		if (pc != null) {
			if (t != null) {
				body = "ERROR";
			} else {
				body = "OK: ";
			}
		} else {
			body = "Invalid request body";
		}

		resp.setBody(body);
		resp.setHeaders(input.getHeaders());
		resp.setStatusCode(STATUS_CODE);
	}
}
