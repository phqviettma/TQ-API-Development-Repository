package com.tq.calendarsbmsync.lambda.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.model.GeneralAppt;
import com.tq.googlecalendar.model.PractitionerApptGroup;
import com.tq.googlecalendar.model.PractitionerApptGroup.EventDateInfo;
import com.tq.googlecalendar.resp.Items;
import com.tq.googlecalendar.time.TimeUtils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.ApplyTagQuery;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.SetWorkDayInfoInfoReq;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.UnitWorkingTime;
import com.tq.simplybook.resp.WorkingTime;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class DeleteGoogleCalendarEventHandler implements GoogleCalendarInternalHandler {
	private static final Logger m_log = LoggerFactory.getLogger(DeleteGoogleCalendarEventHandler.class);
	private Env enV = null;
	private ContactServiceInf contactService = null;
	private TokenServiceSbm tokenService = null;
	private SpecialdayServiceSbm specialdayService = null;
	private SbmBreakTimeManagement sbmBreakTimeManagement = null;
	private ContactItemService contactItemService = null;
	private SbmGoogleCalendarDbService sbmCalendarService = null;
	private BookingServiceSbm bookingService = null;
	private SbmUnitService unitService = null;
	private static final String SBM = "sbm";
	private static final String GOOGLE = "google";

	public DeleteGoogleCalendarEventHandler(Env env, TokenServiceSbm tokenService,
			GoogleCalendarDbService googleCalendarService, SpecialdayServiceSbm specialdayService,
			SbmBreakTimeManagement sbmBreakTimeManagement, ContactItemService contactItemService,
			ContactServiceInf contactInfService, SbmGoogleCalendarDbService sbmCalendarService,
			BookingServiceSbm bookingService, SbmUnitService unitService) {
		this.contactItemService = contactItemService;
		this.enV = env;
		this.tokenService = tokenService;
		this.specialdayService = specialdayService;
		this.sbmBreakTimeManagement = sbmBreakTimeManagement;
		this.contactService = contactInfService;
		this.sbmCalendarService = sbmCalendarService;
		this.bookingService = bookingService;
		this.unitService = unitService;
	}

	@Override
	public void handle(List<Items> item, String sbmId) throws SbmSDKException, InfSDKExecption {
		syncToSbm(item, sbmId);
	}

	private void syncToSbm(List<Items> items, String sbmId) throws SbmSDKException, InfSDKExecption {
		String companyLogin = enV.getSimplyBookCompanyLogin();
		String endpoint = enV.getSimplyBookAdminServiceUrl();
		String endpointLogin = enV.getSimplyBookServiceUrlLogin();
		String password = enV.getSimplyBookPassword();
		String username = enV.getSimplyBookUser();
		String unitId[] = sbmId.split("-");
		String token = tokenService.getUserToken(companyLogin, username, password, endpointLogin);
		Map<String, PractitionerApptGroup> apptGroupMap = new HashMap<String, PractitionerApptGroup>();
		List<Long> sbmBookingIdsTobeCancelled = new ArrayList<>();

		List<Items> eventsTobeCancelled = new ArrayList<>();
		List<Items> eventTobeUnblocked = new ArrayList<>();
		List<SbmGoogleCalendar> listSbmGoogleCalendar = new ArrayList<>();
		List<String> clientEmailsForCancellation = new ArrayList<>();

		for (Items event : items) {
			SbmGoogleCalendar sbmGoogleSync = sbmCalendarService.queryWithIndex(event.getId());

			if (sbmGoogleSync != null) {

				if (sbmGoogleSync.getFlag() == 1 && SBM.equals(sbmGoogleSync.getAgent())) {
					listSbmGoogleCalendar.add(sbmGoogleSync);
					sbmBookingIdsTobeCancelled.add(sbmGoogleSync.getSbmId());
					eventsTobeCancelled.add(event);
					clientEmailsForCancellation.add(sbmGoogleSync.getClientEmail());
				} else if (sbmGoogleSync.getFlag() == 1 && GOOGLE.equals(sbmGoogleSync.getAgent())) {

					eventTobeUnblocked.add(event);
					String dateTime = event.getStart().getDateTime();
					if (dateTime == null) {
						String startDate = event.getStart().getDate();
						int providerId = Integer.valueOf(unitId[1]);
						Map<String, UnitWorkingTime> unitWorkingDayInfoMap = unitService.getUnitWorkDayInfo(
								companyLogin, endpoint, token, startDate, event.getEnd().getDate(), providerId);
						UnitWorkingTime unitWorkingTime = unitWorkingDayInfoMap.get(startDate);
						if (unitWorkingTime != null) {

							Map<String, WorkingTime> unitWorkingTimeMap = unitWorkingTime.getWorkingTime();
							WorkingTime workingTime = unitWorkingTimeMap.get(unitId[1]);
							SetWorkDayInfoInfoReq workDayInfoReq = new SetWorkDayInfoInfoReq(
									workingTime.getStart_time(), workingTime.getEnd_time(), null, 0, startDate,
									unitId[1], unitId[0]);
							SetWorkDayInfoReq workDayInfo = new SetWorkDayInfoReq(workDayInfoReq);
							specialdayService.changeWorkDay(companyLogin, endpoint, token, workDayInfo);
							sbmGoogleSync.setFlag(0);
							sbmCalendarService.put(sbmGoogleSync);

						}

					} else {
						String convertedStartDateTime = TimeUtils.convertAndGetStartDateTimeGoogleEvent(event);
						String convertedEndDateTime = TimeUtils.convertAndGetEndDateTimeGoogleEvent(event);
						dateTime = TimeUtils.extractDate(convertedStartDateTime);
						PractitionerApptGroup group = apptGroupMap.get(dateTime);
						if (group == null) {
							group = new PractitionerApptGroup();
							apptGroupMap.put(dateTime, group);
						}
						group.addAppt(dateTime, new GeneralAppt(convertedStartDateTime,
								convertedEndDateTime, sbmGoogleSync));
					}

				}
			}
		}

		if (!sbmBookingIdsTobeCancelled.isEmpty()) {
			cancelBooking(companyLogin, endpoint, token, sbmBookingIdsTobeCancelled);

			excuteWithInfusionsoft(clientEmailsForCancellation);
			m_log.info("Events are synced to SBM provider " + sbmId + " by cancellation: "
					+ String.valueOf(eventsTobeCancelled));
			for (SbmGoogleCalendar sbmGoogleSync : listSbmGoogleCalendar) {
				sbmCalendarService.delete(sbmGoogleSync);
			}
		}

		if (!apptGroupMap.isEmpty()) {
			removeBreakTime(apptGroupMap, token, Integer.valueOf(unitId[1]), Integer.valueOf(unitId[0]));
			m_log.info("Events are synced to SBM provider " + sbmId + " by unblocking : "
					+ String.valueOf(eventTobeUnblocked));

		}

	}

	private void cancelBooking(String companyLogin, String endpoint, String token, List<Long> bookingIds)
			throws SbmSDKException, InfSDKExecption {
		String batchId = bookingService.createBatch(companyLogin, endpoint, token);
		boolean isCancelled = bookingService.cancelBatch(companyLogin, endpoint, token, batchId, bookingIds);
		if (isCancelled) {
			m_log.info("Cancelled Booking with Ids " + bookingIds.toString());
		}
	}

	private void removeBreakTime(Map<String, PractitionerApptGroup> apptGroup, String token, Integer unitId,
			Integer eventId) throws SbmSDKException {
		for (Entry<String, PractitionerApptGroup> entry : apptGroup.entrySet()) {
			PractitionerApptGroup group = entry.getValue();
			Map<String, UnitWorkingTime> unitWorkingDayInfoMap = unitService.getUnitWorkDayInfo(
					enV.getSimplyBookCompanyLogin(), enV.getSimplyBookAdminServiceUrl(), token,
					group.getStartDateString(), group.getEndDateString(), unitId);
			UnitWorkingTime unitWorkingTime = unitWorkingDayInfoMap.get(group.getStartDateString());
			Map<String, WorkingTime> unitWorkingTimeMap = unitWorkingTime.getWorkingTime();
			WorkingTime workingTime = unitWorkingTimeMap.get(String.valueOf(unitId));
			Map<String, WorksDayInfoResp> workDayInfoMapForUnitId = specialdayService.getWorkDaysInfo(
					enV.getSimplyBookCompanyLogin(), enV.getSimplyBookAdminServiceUrl(), token, unitId, eventId,
					new FromDate(group.getStartDateString(), workingTime.getStart_time()),
					new ToDate(group.getEndDateString(), workingTime.getEnd_time()));

			for (Entry<String, EventDateInfo> dateToSbmBreakTime : group.getEventDateInfoMap().entrySet()) {
				Set<Breaktime> breakTimes = dateToSbmBreakTime.getValue().breakTimeSet;
				String date = dateToSbmBreakTime.getKey();
				if (!breakTimes.isEmpty()) {
					sbmBreakTimeManagement.removeBreakTime(enV.getSimplyBookCompanyLogin(),
							enV.getSimplyBookAdminServiceUrl(), token, unitId, eventId, workingTime.getStart_time(),
							workingTime.getEnd_time(), date, breakTimes, workDayInfoMapForUnitId);
				}
				List<SbmGoogleCalendar> sbmGoogleCalendarList = dateToSbmBreakTime.getValue().sbmGoogleCalendar;

				for (SbmGoogleCalendar sbmGoogleCalendar : sbmGoogleCalendarList) {
					sbmGoogleCalendar.setFlag(0);
					sbmCalendarService.put(sbmGoogleCalendar);
				}

			}

		}

	}

	private void excuteWithInfusionsoft(List<String> clientEmailsForCancellation)
			throws SbmSDKException, InfSDKExecption {
		for (String email : clientEmailsForCancellation) {
			ContactItem contactItem = contactItemService.load(email);
			if (contactItem == null || contactItem.getClient() == null
					|| contactItem.getClient().getContactId() == null) {
				m_log.info("There is no contact on Infusion Soft asociated to the email: " + email + ", ignored");
			}

			Integer ifContactId = contactItem.getClient().getContactId();
			Integer appliedTagId = enV.getInfusionsoftGoogleDeleteTag();
			ApplyTagQuery applyTagQuery = new ApplyTagQuery().withContactID(ifContactId).withTagID(appliedTagId);
			contactService.applyTag(enV.getInfusionSoftApiName(), enV.getInfusionSoftApiKey(), applyTagQuery);
		}
	}
}
