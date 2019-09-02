package com.tq.clinikosbmsync.lambda.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.FoundNewApptContext;
import com.tq.clinikosbmsync.lambda.utils.ClinikoSyncUtils;
import com.tq.clinikosbmsync.lamdbda.context.Env;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.EditBookReq;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.simplybook.utils.SbmUtils;

public class ChangeClinikoSyncHandler {
	private static final Logger m_log = LoggerFactory.getLogger(ChangeClinikoSyncHandler.class);
	private SbmClinikoSyncService m_sbmClinikoSyncService = null;
	private BookingServiceSbm m_bookingService = null;
	private Env m_env = null;
	private TokenServiceSbm m_tokenServiceSbm = null;
	public ChangeClinikoSyncHandler(Env env, SbmClinikoSyncService sbmClinikoSyncService, BookingServiceSbm bookingService, TokenServiceSbm tokenServiceSbm) {
		m_env = env;
		m_sbmClinikoSyncService = sbmClinikoSyncService;
		m_bookingService = bookingService;
		m_tokenServiceSbm = tokenServiceSbm;
	}
	
	private void updateSbmClinikoSync(SbmCliniko sbmClinikoSync) {
		m_sbmClinikoSyncService.put(sbmClinikoSync);
		m_log.info("Update to database successfully with value " + sbmClinikoSync);
	}
	
	public FoundNewApptContext findModifedAppts(List<AppointmentInfo> fetchedAppts, Set<String> dateToBeUpdated, DateTimeZone dateTz, ClinikoSyncStatus clinikoItem) throws SbmSDKException {
		int num = 0;

		List<Long> newApptsId = new LinkedList<Long>();
		List<AppointmentInfo> newAppts = new LinkedList<AppointmentInfo>();
		boolean isReSync = clinikoItem.isReSync();
		for (AppointmentInfo fetchAppt : fetchedAppts) {
			SbmCliniko sbmClinikoSync = m_sbmClinikoSyncService.queryIndex(fetchAppt.getId());
			if(sbmClinikoSync != null && ClinikoSyncUtils.isNotDeleted(sbmClinikoSync)) {
				// if the appointment is created/booked from CLINIKO
				if(ClinikoSyncUtils.isCreatedInCliniko(sbmClinikoSync) && sbmClinikoSync.getUpdatedAt() != null) {
    				if (sbmClinikoSync.getAppointmentStart() == null) {
    					m_log.warn("The appointment cliniko id {} doesn't have appointmentStart (before this fix)", sbmClinikoSync.getClinikoId());
    					continue;
    				}
    				
    				if (!ClinikoSyncUtils.equalsUpdateAt(fetchAppt, sbmClinikoSync) || isReSync) {
    					newApptsId.add(fetchAppt.getId());
    					newAppts.add(fetchAppt);
    					num++;
    					String convertedStartDateTime = TimeUtils.convertToTzFromLondonTz(dateTz, fetchAppt.getAppointment_start());
    					dateToBeUpdated.add(TimeUtils.extractDate(convertedStartDateTime));
    				}
				} else if (ClinikoSyncUtils.isCreatedInSBM(sbmClinikoSync)) { // if the appointment is created/booked from SBM
					String oldUpdatedAt = sbmClinikoSync.getUpdatedAt();
					if (oldUpdatedAt != null && ClinikoSyncUtils.equalsUpdateAt(fetchAppt, sbmClinikoSync)) {
						m_log.info("Ignoring appointment id {} due to no difference updated time or booked first time.", sbmClinikoSync.getSbmId());
						sbmClinikoSync.setUpdatedAt(fetchAppt.getUpdated_at());
						updateSbmClinikoSync(sbmClinikoSync);
						continue;
					}
					m_log.info("The appointment id {} is updated. Handle syncing to SBM (agent = SBM)", sbmClinikoSync.getSbmId());
					String companyLogin = m_env.getSimplyBookCompanyLogin();
					String endpoint = m_env.getSimplyBookAdminServiceUrl();
					String token = m_tokenServiceSbm.getUserToken(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookUser(),
							m_env.getSimplyBookPassword(), m_env.getSimplyBookServiceUrlLogin());
					BookingInfo bookingInfo = m_bookingService.getBookingInfo(companyLogin, endpoint, token, sbmClinikoSync.getSbmId());
					
					String newStartDateTime = TimeUtils.convertToTzFromLondonTz(dateTz, fetchAppt.getAppointment_start());
					String newEndDateTime = TimeUtils.convertToTzFromLondonTz(dateTz, fetchAppt.getAppointment_end());
					
					String newStartDate = TimeUtils.extractDate(newStartDateTime);
					String newEndDate = TimeUtils.extractDate(newEndDateTime);
					String newStartTime = TimeUtils.extractTimeHMS(newStartDateTime);
					String newEndTime = TimeUtils.extractTimeHMS(newEndDateTime);
					
					if (SbmUtils.compareCurrentDateTimeToNewDateTime(bookingInfo, newStartDate + " " + newStartTime, newEndDate + " " + newEndTime)) {
						m_log.info("There is no change which related to the date or time. Ignore this");
						sbmClinikoSync.setUpdatedAt(fetchAppt.getUpdated_at());
						updateSbmClinikoSync(sbmClinikoSync);
						continue;
					}
					
					EditBookReq editBookReq = new EditBookReq(bookingInfo, newStartDate, newStartTime, newEndDate, newEndTime);
					
					boolean result = m_bookingService.editBooking(companyLogin, endpoint, token, editBookReq);
					if (result) {
						m_log.info("Synced the modification event to SBM: Success");
						sbmClinikoSync.setUpdatedAt(fetchAppt.getUpdated_at());
						updateSbmClinikoSync(sbmClinikoSync);
					} else {
						m_log.error("Synced the modification event to SBM: Failure");
					}
					
				}
			} 
		}
		return new FoundNewApptContext(num, newApptsId, newAppts);
	}
}
