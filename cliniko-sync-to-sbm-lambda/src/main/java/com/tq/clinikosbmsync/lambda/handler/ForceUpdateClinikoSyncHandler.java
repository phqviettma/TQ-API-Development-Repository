package com.tq.clinikosbmsync.lambda.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.FoundNewApptContext;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;

public class ForceUpdateClinikoSyncHandler {

	private static final Logger m_log = LoggerFactory.getLogger(ForceUpdateClinikoSyncHandler.class);
	private static final Integer MAX_RESULT = 100;
	private ClinikoApiServiceBuilder m_apiServiceBuilder = null;
	private SbmClinikoSyncService m_sbmClinikoSyncService = null;

	public ForceUpdateClinikoSyncHandler(ClinikoApiServiceBuilder apiServiceBuilder, SbmClinikoSyncService sbmClinikoSyncService) {
		m_apiServiceBuilder = apiServiceBuilder;
		m_sbmClinikoSyncService = sbmClinikoSyncService;
	}

	public FoundNewApptContext findAllAppointmentNeedToBeUpdated(Set<String> dateToBeUpdated,
			ClinikoSbmSync clinikoSbmSync, DateTimeZone dateTz) throws ClinikoSDKExeption {
		ClinikoAppointmentService clinikoApiService = m_apiServiceBuilder.build(clinikoSbmSync.getApiKey());
		String clinikoId[] = clinikoSbmSync.getClinikoId().split("-");
		Integer practitionerId = Integer.parseInt(clinikoId[1]);
		Instant instant = new Instant();
		List<Long> bookingId = new LinkedList<Long>();
		List<Long> apptsId = new LinkedList<Long>();
		List<AppointmentInfo> appts = new LinkedList<AppointmentInfo>();
		int num = 0;
		for (String date : dateToBeUpdated) {
			String startDate = instant.parse(date).toDateTime().toLocalDateTime()
					.toDateTime(DateTimeZone.forID(dateTz.toString())).toDateTime(DateTimeZone.UTC).toString();
			String endDate = instant.parse(date).toDateTime().toLocalDateTime()
					.toDateTime(DateTimeZone.forID(dateTz.toString())).toDateTime(DateTimeZone.UTC).plusDays(1)
					.toString();
			AppointmentsInfo appointments = clinikoApiService.getAppointmentsByFromDateAndToDate(startDate, endDate,
					MAX_RESULT, practitionerId);
			for (AppointmentInfo appointmentInfo : appointments.getAppointments()) {
				SbmCliniko sbmClinikoSync = m_sbmClinikoSyncService.queryIndex(appointmentInfo.getId());
				// skip appointment/booking is created from SBM
				if (sbmClinikoSync != null && ClinikoSyncHandler.SBM.equals(sbmClinikoSync.getAgent())) {
					m_log.warn("Skipping appointment id {} due to booked from SBM", appointmentInfo.getId());
					continue;
				}
				num++;
				apptsId.add(appointmentInfo.getId());
				appts.add(appointmentInfo);
			}
		}
		return new FoundNewApptContext(num, apptsId, appts, bookingId);
	}
}
