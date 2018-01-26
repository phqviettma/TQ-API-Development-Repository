package com.tq.clinikosbmsync.lambda.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.lambda.model.AppoinmentUtil;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.FoundNewApptContext;
import com.tq.cliniko.lambda.model.GeneralAppt;
import com.tq.cliniko.lambda.model.PractitionerApptGroup;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.cliniko.time.UtcTimeUtil;
import com.tq.common.lambda.dynamodb.dao.LatestClinikoApptsImpl;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceImpl;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceWrapper;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.service.LatestClinikoApptService;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.context.SimplyBookClinikoMapping;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.ClinikoId;
import com.tq.simplybook.resp.SimplyBookId;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class SyncHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static final Logger m_log = LoggerFactory.getLogger(SyncHandler.class);

	private Env m_env = null;
	private Integer maxAppt = 100;

	private AmazonDynamoDB m_amazonDynamoDB = null;

	private ClinikoAppointmentService m_cas = null;
	private LatestClinikoApptService m_lcs = null;
	private LatestClinikoApptServiceWrapper m_lcsw = null;
	private SpecialdayServiceSbm m_sss = null;
	private TokenServiceSbm m_tss = null;
	private SimplyBookClinikoMapping m_sbc = null;
	private SbmBreakTimeManagement m_sbtm = null;

	// For testing only
	SyncHandler(Env env, AmazonDynamoDB db, ClinikoAppointmentService clinikoService,
			LatestClinikoApptService latestService, LatestClinikoApptServiceWrapper clinikoApptService,
			SpecialdayServiceSbm specialdayService, TokenServiceSbm tokenService,
			SimplyBookClinikoMapping sbmClinikoMapping, SbmBreakTimeManagement sbmTimeManagement) {
		this.m_amazonDynamoDB = db;
		this.m_cas = clinikoService;
		this.m_lcs = latestService;
		this.m_lcsw = clinikoApptService;
		this.m_sss = specialdayService;
		this.m_tss = tokenService;
		this.m_sbc = sbmClinikoMapping;
		this.m_sbtm = sbmTimeManagement;
		this.m_env = env;
	}

	public SyncHandler() {
		this.m_env = Env.load();
		this.m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(m_env.getRegions(), m_env.getAwsAccessKeyId(),
				m_env.getAwsSecretAccessKey());
		this.m_lcs = new LatestClinikoApptServiceImpl(new LatestClinikoApptsImpl(m_amazonDynamoDB));
		this.m_cas = new ClinikiAppointmentServiceImpl(m_env.getClinikoApiKey());
		this.m_lcsw = new LatestClinikoApptServiceWrapper(m_lcs);
		this.m_sss = new SpecialdayServiceSbmImpl();
		this.m_tss = new TokenServiceImpl();
		this.m_sbc = new SimplyBookClinikoMapping(m_env);
		this.m_sbtm = new SbmBreakTimeManagement();
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		m_log.info("Start CLINIKO-SBM synchronization lambda");
		boolean errorOccured = false;
		DateTimeZone dateTz = null;
		String dbTime = null;
		String latestUpdateTime = null;

		Set<Long> dbCreateSet = null;
		Set<Long> dbRemoveSet = null;

		try {
			m_log.info("Going to load database");
			LatestClinikoAppts dbAppts = m_lcsw.load();
			if (dbAppts != null) {
				dbTime = dbAppts.getLatestUpdateTime();
			} else {
				dbAppts = new LatestClinikoAppts();
			}
			m_log.info("Loaded database");
			Settings settings = m_cas.getAllSettings();
			m_log.info("Loaded setting information from cliniko");
			String country = settings.getAccount().getCountry();
			String time_zone = settings.getAccount().getTime_zone();
			dateTz = DateTimeZone.forID(country + "/" + time_zone);
			latestUpdateTime = UtcTimeUtil.getNowInUTC(country + "/" + time_zone);

			m_log.info("Now: " + latestUpdateTime + " at timezone " + country + "/" + time_zone);
			if (dbTime == null) {
				dbTime = latestUpdateTime;
			}

			dbCreateSet = dbAppts.getCreated();
			dbRemoveSet = dbAppts.getRemoved();

			m_log.info(
					"DB appointments status, created: " + (dbCreateSet == null ? Collections.emptySet() : dbCreateSet));
			m_log.info(
					"DB appointments status, removed: " + (dbRemoveSet == null ? Collections.emptySet() : dbRemoveSet));

			m_log.info("To fetch Cliniko appointment with start time:" + latestUpdateTime);
			AppointmentsInfo appts = m_cas.getAppointments(latestUpdateTime);

			m_log.info("Fetched: " + appts.getAppointments().size() + " created Cliniko appointment(s)");
			if (appts != null && appts.getAppointments().size() > 0) {

				List<AppointmentInfo> fetchedAppts = appts.getAppointments();

				FoundNewApptContext news = findNewAppts(dbCreateSet, fetchedAppts);

				while (news.getCount() < maxAppt && AppointmentsInfo.hasNext(appts)) {
					appts = m_cas.next(appts);
					if (appts != null && appts.getAppointments().size() > 0) {
						FoundNewApptContext newAppt = findNewAppts(dbCreateSet, appts.getAppointments());
						if (newAppt.getCount() > 0) {
							addUpToMax(news, newAppt, maxAppt);
						}
					}
				}
				m_log.info("New appointmentIds " + news.getNewApptsId());
				if (news.getCount() > 0) {
					Map<Long, AppointmentInfo> lookupedMap = toLookupMap(news.getNewAppts());
					syncToSbm(dateTz, news.getNewApptsId(), lookupedMap, true);

					dbCreateSet.addAll(news.getNewApptsId());
					m_log.info("dbCreatedSet value currently" + dbCreateSet);

					saveDb(latestUpdateTime, dbCreateSet, dbRemoveSet);
					m_log.info("Save to database successfully");
				} else {
					m_log.info("There is no new appointment found");
				}
			} else {
				m_log.info("Cleaned up created value");
				dbCreateSet = null;
				saveDb(latestUpdateTime, dbCreateSet, dbRemoveSet);
			}

			m_log.info("Synchronized created appoinments to Simplybook.me completely");

			AppointmentsInfo deletedApptInfo = m_cas.getCancelAppointments(latestUpdateTime);
			m_log.info("Fetched: " + deletedApptInfo.getAppointments().size() + " removed Cliniko appointment(s)");
			if (deletedApptInfo != null && deletedApptInfo.getAppointments().size() > 0) {
				List<AppointmentInfo> fetchedAppts = deletedApptInfo.getAppointments();
				FoundNewApptContext news = findNewAppts(dbRemoveSet, fetchedAppts);

				while (news.getCount() < maxAppt && AppointmentsInfo.hasNext(deletedApptInfo)) {
					deletedApptInfo = m_cas.next(deletedApptInfo);
					if (deletedApptInfo != null && deletedApptInfo.getAppointments().size() > 0) {
						FoundNewApptContext newAppt = findNewAppts(dbRemoveSet, deletedApptInfo.getAppointments());
						if (newAppt.getCount() > 0) {
							addUpToMax(news, newAppt, maxAppt);
						}
					}
				}

				m_log.info("New appointmentIds " + news.getNewApptsId());
				if (news.getCount() > 0) {

					Map<Long, AppointmentInfo> lookupedMap = toLookupMap(news.getNewAppts());
					syncToSbm(dateTz, news.getNewApptsId(), lookupedMap, false);

					dbRemoveSet.addAll(news.getNewApptsId());

					saveDb(latestUpdateTime, dbCreateSet, dbRemoveSet);
					m_log.info("Save to database successfully");
				} else {
					m_log.info("There is no new appointment found");
				}
			} else {
				m_log.info("Cleaned up removed value");
				saveDb(latestUpdateTime, dbCreateSet, null);
			}
			m_log.info("Synchronized deleted appoinments to Simplybook.me completely");

		} catch (ClinikoSDKExeption | SbmSDKException e) {
			m_log.error("Error occurs", e);
			resp.setStatusCode(500);
			errorOccured = true;
		}

		if (!errorOccured) {
			resp.setStatusCode(200);
		}

		return resp;
	}

	private void addUpToMax(FoundNewApptContext news, FoundNewApptContext newAppt, Integer maxAppt) {
		List<Long> addedAppts = new LinkedList<Long>();
		List<Long> adddedApptsId = new LinkedList<Long>();

		for (int i = 0; i < newAppt.getCount() && news.count < maxAppt; i++) {
			AppointmentInfo addedAppt = newAppt.getNewAppts().get(i);
			Long id = newAppt.getNewApptsId().get(i);
			news.newAppts.add(addedAppt);
			news.newApptsId.add(id);
			news.count++;

			addedAppts.add(addedAppt.getId());
			adddedApptsId.add(id);
		}

		if (addedAppts.equals(adddedApptsId)) {
			m_log.info("addUpToMax OK");
		} else {
			throw new IllegalArgumentException(
					"addUpToMax MISMATCHED, left =" + addedAppts + ", right = " + adddedApptsId);
		}
	}

	private void saveDb(String updateTime, Set<Long> dbCreateSet, Set<Long> dbRemoveSet) {
		LatestClinikoAppts lca = new LatestClinikoAppts();
		lca.setLatestUpdateTime(updateTime);

		if (dbCreateSet != null && !dbCreateSet.isEmpty()) {
			lca.setCreated(dbCreateSet);
		}

		if (dbRemoveSet != null && !dbRemoveSet.isEmpty()) {

			lca.setRemoved(dbRemoveSet);
		}

		m_log.info("LatestClinikoAppt" + lca.toString());
		m_lcsw.put(lca);
	}

	private FoundNewApptContext findNewAppts(Set<Long> dbSet, List<AppointmentInfo> fetchedAppts) {
		int num = 0;

		List<Long> newApptsId = new LinkedList<Long>();
		List<AppointmentInfo> newAppts = new LinkedList<AppointmentInfo>();

		for (AppointmentInfo fetchAppt : fetchedAppts) {
			if (!dbSet.contains(fetchAppt.getId())) {
				newApptsId.add(fetchAppt.getId());
				newAppts.add(fetchAppt);
				num++;
			}
		}

		return new FoundNewApptContext(num, newApptsId, newAppts);

	}

	public boolean syncToSbm(DateTimeZone dateTz, List<Long> apptsToBeSynced, Map<Long, AppointmentInfo> lookupedMap,
			boolean isCreate) throws SbmSDKException {
		m_log.info("syncToSbm with isCreate = " + isCreate);

		Map<ClinikoId, PractitionerApptGroup> practitionerApptGroupMap = new HashMap<ClinikoId, PractitionerApptGroup>();

		for (Long i : apptsToBeSynced) {
			AppointmentInfo appt = lookupedMap.get(i);
			appt.setAppointment_start(UtcTimeUtil.convertToTzFromLondonTz(dateTz, appt.getAppointment_start()));
			appt.setAppointment_end(UtcTimeUtil.convertToTzFromLondonTz(dateTz, appt.getAppointment_end()));
			if (appt != null) {
				String date = UtcTimeUtil.extractDate(appt.getAppointment_start());
				ClinikoId cliniko = new ClinikoId();
				cliniko.setBussinessId(AppoinmentUtil.getBusinessId(appt));
				cliniko.setPractionerId(AppoinmentUtil.getPractitionerId(appt));

				PractitionerApptGroup group = practitionerApptGroupMap.get(cliniko);

				if (group == null) {
					group = new PractitionerApptGroup();
					practitionerApptGroupMap.put(cliniko, group);
				}

				group.addAppt(date, new GeneralAppt(appt.getAppointment_start(), appt.getAppointment_end()));

			}
		}

		String token = m_tss.getUserToken(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookUser(),
				m_env.getSimplyBookPassword(), m_env.getSimplyBookServiceUrlLogin());

		m_log.info("Proceed for practitioner group map: " + String.valueOf(practitionerApptGroupMap));
		changeSbmBreakTime(practitionerApptGroupMap, token, isCreate);

		return true;
	}

	private void changeSbmBreakTime(Map<ClinikoId, PractitionerApptGroup> practitionerApptGroupMap, String token,
			boolean isAdditional) throws SbmSDKException {
		for (Entry<ClinikoId, PractitionerApptGroup> entry : practitionerApptGroupMap.entrySet()) {
			ClinikoId clinikoId = entry.getKey();
			m_log.info("Proceed for practitioner: " + String.valueOf(clinikoId));
			PractitionerApptGroup group = entry.getValue();
			SimplyBookId simplybookId = m_sbc.clinikoSbmMapping(clinikoId);
			Integer unitId = Integer.valueOf(simplybookId.getUnit_id());
			Integer eventId = Integer.valueOf(simplybookId.getEvent_id());
			Map<String, WorksDayInfoResp> workDayInfoMapForUnitId = m_sss.getWorkDaysInfo(
					m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl(), token, unitId, eventId,
					new FromDate(group.getStartDateString(), m_env.getCliniko_start_time()),
					new ToDate(group.getEndDateString(), m_env.getCliniko_end_time()));

			for (Entry<String, Set<Breaktime>> dateToSbmBreakTime : group.getDateToSbmBreakTimesMap().entrySet()) {
				Set<Breaktime> breakTimes = dateToSbmBreakTime.getValue();
				String date = dateToSbmBreakTime.getKey();
				if (!breakTimes.isEmpty()) {
					if (isAdditional) {
						m_sbtm.addBreakTime(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl(),
								token, unitId, eventId, m_env.getCliniko_start_time(), m_env.getCliniko_end_time(),
								date, breakTimes, workDayInfoMapForUnitId);
					} else {
						m_sbtm.removeBreakTime(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl(),
								token, unitId, eventId, m_env.getCliniko_start_time(), m_env.getCliniko_end_time(),
								date, breakTimes, workDayInfoMapForUnitId);
					}
				}

			}
		}
	}

	private Map<Long, AppointmentInfo> toLookupMap(List<AppointmentInfo> appts) {
		Map<Long, AppointmentInfo> lookupedMap = new HashMap<Long, AppointmentInfo>();
		for (AppointmentInfo appt : appts) {
			lookupedMap.put(appt.getId(), appt);
		}
		return lookupedMap;
	}

}
