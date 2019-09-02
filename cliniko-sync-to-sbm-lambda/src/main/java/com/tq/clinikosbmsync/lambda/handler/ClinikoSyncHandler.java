package com.tq.clinikosbmsync.lambda.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.FoundNewApptContext;
import com.tq.cliniko.lambda.model.GeneralAppt;
import com.tq.cliniko.lambda.model.PractitionerApptGroup;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.clinikosbmsync.lambda.utils.ClinikoSyncUtils;
import com.tq.clinikosbmsync.lambda.utils.SbmBreakTimeUtils;
import com.tq.clinikosbmsync.lamdbda.context.Env;
import com.tq.common.lambda.dynamodb.dao.ClinikoItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.ClinikoSyncToSbmDaoImpl;
import com.tq.common.lambda.dynamodb.dao.SbmClinikoSyncDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.ClinikoSyncToSbmServiceImpl;
import com.tq.common.lambda.dynamodb.impl.SbmClinikoSyncImpl;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.common.lambda.utils.TimeUtils;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClinikoSyncHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static final Logger m_log = LoggerFactory.getLogger(ClinikoSyncHandler.class);
	
	private static final Integer maxResult = 20;
	public static final String CLINIKO = "cliniko";
	public static final String SBM = "sbm";
	private AmazonDynamoDB m_amazonDynamoDB = null;
	private Env env = null;
	private SpecialdayServiceSbm m_sss = null;
	private TokenServiceSbm m_tss = null;
	private SbmBreakTimeManagement m_sbtm = null;
	private ClinikoSyncToSbmService clinikoSyncService = null;
	private ClinikoItemService clinikoItemService = null;
	private SbmClinikoSyncService sbmClinikoSyncService = null;
	private SbmUnitService unitService = null;
	private Integer maxAppt = 20;
	private BookingServiceSbm bookingService = null;
	private ClinikoApiServiceBuilder apiServiceBuilder = null;
	private ChangeClinikoSyncHandler m_changeClinikoHandler = null;
	private ForceUpdateClinikoSyncHandler m_forceUpdateHandle = null;

	public ClinikoSyncHandler() {
		this.env = Env.load();
		this.m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(env.getRegions(), env.getAwsAccessKeyId(),
				env.getAwsSecretAccessKey());
		this.m_sss = new SpecialdayServiceSbmImpl();
		this.m_tss = new TokenServiceImpl();
		this.m_sbtm = new SbmBreakTimeManagement();
		this.clinikoItemService = new ClinikoItemServiceImpl(new ClinikoItemDaoImpl(m_amazonDynamoDB));
		this.sbmClinikoSyncService = new SbmClinikoSyncImpl(new SbmClinikoSyncDaoImpl(m_amazonDynamoDB));
		this.unitService = new SbmUnitServiceImpl();
		this.clinikoSyncService = new ClinikoSyncToSbmServiceImpl(new ClinikoSyncToSbmDaoImpl(m_amazonDynamoDB));
		this.bookingService = new BookingServiceSbmImpl();
		this.apiServiceBuilder = new ClinikoApiServiceBuilder();
		this.m_changeClinikoHandler = new ChangeClinikoSyncHandler(env, sbmClinikoSyncService, bookingService, m_tss);
		this.m_forceUpdateHandle = new ForceUpdateClinikoSyncHandler(apiServiceBuilder, sbmClinikoSyncService);
	}

	// for testing only
	ClinikoSyncHandler(Env env, AmazonDynamoDB db, SpecialdayServiceSbm specialdayService, TokenServiceSbm tokenService,
			SbmBreakTimeManagement sbmTimeManagement, ClinikoSyncToSbmService clinikoSyncService,
			ClinikoItemService clinikoItemService, SbmClinikoSyncService sbmClinikoSyncService,
			SbmUnitService unitService, BookingServiceSbm bookingService, ClinikoApiServiceBuilder apiServiceBuilder,
			ChangeClinikoSyncHandler changeHandler, ForceUpdateClinikoSyncHandler forceUpdateHandler) {
		this.env = env;
		this.m_amazonDynamoDB = db;
		this.m_sss = specialdayService;
		this.m_tss = tokenService;
		this.m_sbtm = sbmTimeManagement;
		this.clinikoItemService = clinikoItemService;
		this.clinikoSyncService = clinikoSyncService;
		this.sbmClinikoSyncService = sbmClinikoSyncService;
		this.unitService = unitService;
		this.bookingService = bookingService;
		this.apiServiceBuilder = apiServiceBuilder;
		this.m_changeClinikoHandler = changeHandler;
		this.m_forceUpdateHandle = forceUpdateHandler;
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		AwsProxyResponse resp = new AwsProxyResponse();
		String dbTime = null;
		boolean errorOccured = false;
		String latestUpdateTime = null;
		DateTimeZone dateTz = null;

		m_log.info("Start CLINIKO-SBM synchronization lambda");
		try {

			List<ClinikoSyncStatus> clinikoItems = clinikoItemService.queryIndex();

			Iterator<ClinikoSyncStatus> it = clinikoItems.iterator();
			while (it.hasNext()) {
				ClinikoSyncStatus clinikoItem = it.next();
				String apiKey = clinikoItem.getApiKey();
				ClinikoSbmSync clinikoSbmSync = clinikoSyncService.queryWithIndex(apiKey);
				Set<String> dateToBeUpdated = new HashSet<String>();
				m_log.info("Syncing appointment with API key " + apiKey);
				if (clinikoSbmSync != null) {
					String clinikoId[] = clinikoSbmSync.getClinikoId().split("-");
					Integer practitionerId = Integer.parseInt(clinikoId[1]);
					ClinikoAppointmentService clinikoApiService = apiServiceBuilder.build(apiKey);
					Settings settings = clinikoApiService.getAllSettings();
					if (settings == null || settings.getAccount() == null) {
						m_log.info("Have something wrong on account settings. Ignore this practitioner");
						continue;
					}
					String country = settings.getAccount().getCountry();
					String time_zone = settings.getAccount().getTime_zone();
					try {
						dateTz = DateTimeZone.forID(country + "/" + time_zone);
					} catch (Exception e) {
						m_log.error(e.getMessage());
						continue;
					}
					dbTime = clinikoItem.getLatestTime();
					latestUpdateTime = TimeUtils.getNowInGMT();
					AppointmentsInfo appts = null;

					if (dbTime == null) {
						appts = clinikoApiService.getAppointments(latestUpdateTime, maxResult, practitionerId);
					} else {
						appts = clinikoApiService.getNewestAppointment(clinikoItem.getLatestTime(), maxResult,
								practitionerId);
					}

					if (appts != null && appts.getAppointments().size() > 0) {
						m_log.info("Fetched: " + appts.getAppointments().size() + " updated Cliniko appointment(s)");
						List<AppointmentInfo> fetchedAppts = appts.getAppointments();
						FoundNewApptContext news = findNewAppts(fetchedAppts, clinikoItem);
						while (news.getCount() < maxAppt && AppointmentsInfo.hasNext(appts)) {
							appts = clinikoApiService.next(appts);
							if (appts != null && appts.getAppointments().size() > 0) {
								FoundNewApptContext newAppt = findNewAppts(appts.getAppointments(), clinikoItem);
								if (newAppt.getCount() > 0) {
									addUpToMax(news, newAppt, maxAppt);
								}
							}
						}
						m_log.info("New appointmentIds " + news.getNewApptsId());
						if (news.getCount() > 0) {
							Map<Long, AppointmentInfo> lookupedMap = toLookupMap(news.getNewAppts());
							long start = System.currentTimeMillis();
							syncToSbm(dateTz, news.getNewApptsId(), lookupedMap, true, clinikoSbmSync, false);
							m_log.info("Take " + (System.currentTimeMillis() - start)
									+ "s to sync create appointment to Sbm");
							saveDb(news, true, 1, apiKey, null, false);
						}
						
						// for the modifed appointment
						
						FoundNewApptContext modifedAppts = findModifedAppts(fetchedAppts, dateToBeUpdated, dateTz, clinikoItem);
						while (modifedAppts.getCount() < maxAppt && AppointmentsInfo.hasNext(appts)) {
							appts = clinikoApiService.next(appts);
							if (appts != null && appts.getAppointments().size() > 0) {
								FoundNewApptContext newAppt = findModifedAppts(appts.getAppointments(), dateToBeUpdated, dateTz, clinikoItem);
								if (newAppt.getCount() > 0) {
									addUpToMax(modifedAppts, newAppt, maxAppt);
								}
							}
						}
						
						m_log.info("Modified appointmentIds " + modifedAppts.getNewApptsId());
						if (modifedAppts.getCount() > 0) {
							Map<Long, AppointmentInfo> lookupedMap = toLookupMap(modifedAppts.getNewAppts());
							long start = System.currentTimeMillis();
							syncToSbm(dateTz, modifedAppts.getNewApptsId(), lookupedMap, false, clinikoSbmSync, true);
							m_log.info("Take " + (System.currentTimeMillis() - start)
									+ "s to sync modified appointment to Sbm");
							saveDb(modifedAppts, false, 1, apiKey, null, true);
						}
						
					} else {

					}
					m_log.info("Synchronized created appoinments to Simplybook.me completely");
					AppointmentsInfo cancelledAppt;
					if (dbTime == null) {
						cancelledAppt = clinikoApiService.getCancelAppointments(latestUpdateTime, maxResult,
								practitionerId);
					} else {
						cancelledAppt = clinikoApiService.getNewestCancelledAppointments(clinikoItem.getLatestTime(),
								maxResult, practitionerId);
					}

					if (cancelledAppt != null && cancelledAppt.getAppointments().size() > 0) {
						m_log.info(
								"Fetched: " + cancelledAppt.getAppointments().size() + " cancelled Cliniko appointment(s)");
						List<AppointmentInfo> fetchedAppts = cancelledAppt.getAppointments();
						FoundNewApptContext news = findNewCancelledAppts(fetchedAppts, dateToBeUpdated, dateTz);
						while (news.getCount() < maxAppt && AppointmentsInfo.hasNext(cancelledAppt)) {
							cancelledAppt = clinikoApiService.next(cancelledAppt);
							if (cancelledAppt != null && cancelledAppt.getAppointments().size() > 0) {
								FoundNewApptContext newAppt = findNewCancelledAppts(cancelledAppt.getAppointments(), dateToBeUpdated, dateTz);
								if (newAppt.getCount() > 0) {
									addUpToMax(news, newAppt, maxAppt);
								}
							}
						}
						m_log.info("New appointmentIds " + news.getNewApptsId());
						if (news.getCount() > 0) {
							Map<Long, AppointmentInfo> lookupedMap = toLookupMap(news.getNewAppts());
							long start = System.currentTimeMillis();
							syncToSbm(dateTz, news.getNewApptsId(), lookupedMap, false, clinikoSbmSync, false);
							m_log.info("Take " + (System.currentTimeMillis() - start)
									+ "s to sync cancel appointment to Sbm");
							saveDb(news, false, 1, apiKey, news.getBookingId(), false);
						} else {

						}
					} else {
						// there have no new cancel appointment
					}
					m_log.info("Synchronized cancelled appoinments to Simplybook.me completely");
					//
					AppointmentsInfo deletedAppt;
					if (dbTime == null) {
						deletedAppt = clinikoApiService.getDeletedAppointments(latestUpdateTime, maxResult,
								practitionerId);
					} else {
						deletedAppt = clinikoApiService.getNewestDeletedAppointments(clinikoItem.getLatestTime(), maxResult,
								practitionerId);
					}
					if (deletedAppt != null && deletedAppt.getAppointments().size() > 0) {
						m_log.info("Fetched: " + deletedAppt.getAppointments().size() + " removed Cliniko appointment(s)");
						List<AppointmentInfo> fetchedAppts = deletedAppt.getAppointments();
						FoundNewApptContext news = findNewCancelledAppts(fetchedAppts, dateToBeUpdated, dateTz);
						while (news.getCount() < maxAppt && AppointmentsInfo.hasNext(deletedAppt)) {
							deletedAppt = clinikoApiService.next(deletedAppt);
							if (deletedAppt != null && deletedAppt.getAppointments().size() > 0) {
								FoundNewApptContext newAppt = findNewCancelledAppts(deletedAppt.getAppointments(), dateToBeUpdated, dateTz);
								if (newAppt.getCount() > 0) {
									addUpToMax(news, newAppt, maxAppt);
								}
							}
						}
						m_log.info("New appointmentIds " + news.getNewApptsId());
						if (news.getCount() > 0) {
							Map<Long, AppointmentInfo> lookupedMap = toLookupMap(news.getNewAppts());
							long start = System.currentTimeMillis();
							syncToSbm(dateTz, news.getNewApptsId(), lookupedMap, false, clinikoSbmSync, false);
							m_log.info("Take " + (System.currentTimeMillis() - start)
									+ "s to sync delete appointment to Sbm");
							saveDb(news, false, 1, apiKey, news.getBookingId(), false);
						} else {

						}
					} else {
						// there have no new deleted appointment
					}
					m_log.info("Synchronized deleted appoinments to Simplybook.me completely");
					
					if (!dateToBeUpdated.isEmpty()) {
						m_log.info("Forcing to re-add all appointment of date {} after modifying an appointment",
								dateToBeUpdated);
						FoundNewApptContext apptsNeedToBeUpdated = m_forceUpdateHandle
								.findAllAppointmentNeedToBeUpdated(dateToBeUpdated, clinikoSbmSync, dateTz);
						m_log.info("Appointment Ids Need to be updated: " + apptsNeedToBeUpdated.getNewApptsId());
						if (apptsNeedToBeUpdated.getNewApptsId().size() != 0) {
    						Map<Long, AppointmentInfo> lookupedMap = toLookupMap(apptsNeedToBeUpdated.getNewAppts());
    						syncToSbm(dateTz, apptsNeedToBeUpdated.getNewApptsId(), lookupedMap, true, clinikoSbmSync, false);
    						saveDb(apptsNeedToBeUpdated, false, 1, apiKey, apptsNeedToBeUpdated.getBookingId(), true);
						} else {
							m_log.info("There are no appointments on the date {}", dateToBeUpdated);
						}
					}
					Long timeStamp = Calendar.getInstance().getTimeInMillis();
					clinikoItem.setTimeStamp(timeStamp);
					clinikoItem.setLatestTime(latestUpdateTime);
					clinikoItem.setReSync(false);
					clinikoItemService.put(clinikoItem);
				} else {
					m_log.info("Can't find the API key " + apiKey);
				}
			}
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

	private void saveDb(FoundNewApptContext news, boolean isCreated, int i, String apiKey, List<Long> bookingIds, boolean isModified) {

		m_log.info("Saving DB with isCreated = "+ isCreated+", isModified="+isModified);
		if (isCreated) {
			for (AppointmentInfo apptInfo : news.getNewAppts()) {
				UUID uuid = UUID.randomUUID();

				Long sbmId = uuid.getMostSignificantBits();
				SbmCliniko sbmCliniko = sbmClinikoSyncService.queryIndex(apptInfo.getId());
				
				if (sbmCliniko == null) {
					sbmCliniko = new SbmCliniko(sbmId, apptInfo.getId(), 1, apiKey, "cliniko", apptInfo.getUpdated_at(), apptInfo.getAppointment_start(), apptInfo.getAppointment_end());
					updateSbmClinikoSync(sbmCliniko);
				}
			}
		} else if(isModified) {
			for (AppointmentInfo apptInfo : news.getNewAppts()) {
				SbmCliniko sbmCliniko = sbmClinikoSyncService.queryIndex(apptInfo.getId());
				if (sbmCliniko != null) {
					sbmCliniko.setUpdatedAt(apptInfo.getUpdated_at());
					sbmCliniko.setAppointmentStart(apptInfo.getAppointment_start());
					sbmCliniko.setAppointmentEnd(apptInfo.getAppointment_end());
					updateSbmClinikoSync(sbmCliniko);
				}
			}
		} else {
			for (Long sbmBookingId : bookingIds) {
				SbmCliniko sbmCliniko = sbmClinikoSyncService.load(sbmBookingId);
				if (sbmCliniko != null) {
					sbmCliniko.setFlag(0);
					updateSbmClinikoSync(sbmCliniko);
				}
			}
		}

	}

	private FoundNewApptContext findNewCancelledAppts(List<AppointmentInfo> fetchedAppts, Set<String> dateToBeUpdated, DateTimeZone dateTz) throws SbmSDKException {
		int num = 0;

		List<Long> newApptsId = new LinkedList<Long>();
		List<AppointmentInfo> newAppts = new LinkedList<AppointmentInfo>();
		List<Long> cancelBookingIds = new ArrayList<>();
		List<Long> bookingId = new LinkedList<Long>();
		SbmCliniko sbmClinikoSync = null;
		for (AppointmentInfo fetchAppt : fetchedAppts) {
			sbmClinikoSync = sbmClinikoSyncService.queryIndex(fetchAppt.getId());
			if (sbmClinikoSync != null && ClinikoSyncUtils.isNotDeleted(sbmClinikoSync)) {
				if (ClinikoSyncUtils.isCreatedInCliniko(sbmClinikoSync)) {
					newApptsId.add(fetchAppt.getId());
					newAppts.add(fetchAppt);
					num++;
					bookingId.add(sbmClinikoSync.getSbmId());
					String convertedStartDateTime = TimeUtils.convertToTzFromLondonTz(dateTz, fetchAppt.getAppointment_start());
					dateToBeUpdated.add(TimeUtils.extractDate(convertedStartDateTime));

				} else if (ClinikoSyncUtils.isCreatedInSBM(sbmClinikoSync)) {
					cancelBookingIds.add(sbmClinikoSync.getSbmId());
					sbmClinikoSync.setFlag(0);
					sbmClinikoSyncService.put(sbmClinikoSync);
				}
			}
		}
		if (!cancelBookingIds.isEmpty()) {
			cancelSbmBooking(cancelBookingIds);
		}
		return new FoundNewApptContext(num, newApptsId, newAppts, bookingId);
	}

	private void cancelSbmBooking(List<Long> cancelBookingIds) throws SbmSDKException {
		String companyLogin = env.getSimplyBookCompanyLogin();
		String endpoint = env.getSimplyBookAdminServiceUrl();
		String endpointLogin = env.getSimplyBookServiceUrlLogin();
		String password = env.getSimplyBookPassword();
		String username = env.getSimplyBookUser();
		String token = m_tss.getUserToken(companyLogin, username, password, endpointLogin);
		String batchId = bookingService.createBatch(companyLogin, endpoint, token);
		boolean isCancelled = bookingService.cancelBatch(companyLogin, endpoint, token, batchId, cancelBookingIds);
		if (isCancelled) {
			m_log.info("Cancelled booking");

		} else {
			m_log.info("Can not cancel booking");
		}
	}

	private Map<Long, AppointmentInfo> toLookupMap(List<AppointmentInfo> appts) {
		Map<Long, AppointmentInfo> lookupedMap = new HashMap<Long, AppointmentInfo>();
		for (AppointmentInfo appt : appts) {
			lookupedMap.put(appt.getId(), appt);
		}
		return lookupedMap;
	}

	private FoundNewApptContext findNewAppts(List<AppointmentInfo> fetchedAppts, ClinikoSyncStatus clinikoItem) {
		int num = 0;

		List<Long> newApptsId = new LinkedList<Long>();
		List<AppointmentInfo> newAppts = new LinkedList<AppointmentInfo>();
		boolean isReSync = clinikoItem.isReSync();
		for (AppointmentInfo fetchAppt : fetchedAppts) {
			SbmCliniko sbmClinikoSync = sbmClinikoSyncService.queryIndex(fetchAppt.getId());
			if (sbmClinikoSync == null) {
				newApptsId.add(fetchAppt.getId());
				newAppts.add(fetchAppt);
				num++;
				continue;
			}
			
			// TSI-70
			// in case an appointment is existed in DB and Resync = true
			if (isReSync) {
				if (ClinikoSyncUtils.isNotDeleted(sbmClinikoSync) && ClinikoSyncUtils.isCreatedInCliniko(sbmClinikoSync)
						&& ClinikoSyncUtils.equalsUpdateAt(fetchAppt, sbmClinikoSync)) {
					newApptsId.add(fetchAppt.getId());
					newAppts.add(fetchAppt);
					num++;
					continue;
				}
			}
		}
		return new FoundNewApptContext(num, newApptsId, newAppts);
	}

	private FoundNewApptContext findModifedAppts(List<AppointmentInfo> fetchedAppts, Set<String> dateToBeUpdated, DateTimeZone dateTz, ClinikoSyncStatus clinikoItem) throws SbmSDKException {
		return m_changeClinikoHandler.findModifedAppts(fetchedAppts, dateToBeUpdated, dateTz, clinikoItem);
	}

	private void updateSbmClinikoSync(SbmCliniko sbmClinikoSync) {
		sbmClinikoSyncService.put(sbmClinikoSync);
		m_log.info("Update to database successfully with value "+sbmClinikoSync);
	}
	public boolean syncToSbm(DateTimeZone dateTz, List<Long> apptsToBeSynced, Map<Long, AppointmentInfo> lookupedMap,
			boolean isCreate, ClinikoSbmSync clinikoSbmSync, boolean isModify) throws SbmSDKException {
		PractitionerApptGroup apptGroup = new PractitionerApptGroup();
		PractitionerApptGroup apptModifiedGroup = new PractitionerApptGroup();
		for (Long i : apptsToBeSynced) {
			AppointmentInfo appt = lookupedMap.get(i);
			appt.setAppointment_start(TimeUtils.convertToTzFromLondonTz(dateTz, appt.getAppointment_start()));
			appt.setAppointment_end(TimeUtils.convertToTzFromLondonTz(dateTz, appt.getAppointment_end()));
			String date = TimeUtils.extractDate(appt.getAppointment_start());
			apptGroup.addAppt(date, new GeneralAppt(appt.getAppointment_start(), appt.getAppointment_end()));
			
			if (isModify) {
				SbmCliniko sbmClinikoSync = sbmClinikoSyncService.queryIndex(i);
				String oldAppointmentStart = TimeUtils.convertToTzFromLondonTz(dateTz, sbmClinikoSync.getAppointmentStart());
				String oldAppointmentEnd = TimeUtils.convertToTzFromLondonTz(dateTz, sbmClinikoSync.getAppointmentEnd());
				String oldDate = TimeUtils.extractDate(sbmClinikoSync.getAppointmentStart());
				apptModifiedGroup.addAppt(oldDate, new GeneralAppt(oldAppointmentStart, oldAppointmentEnd));
			}
		}
		
		String token = m_tss.getUserToken(env.getSimplyBookCompanyLogin(), env.getSimplyBookUser(),
				env.getSimplyBookPassword(), env.getSimplyBookServiceUrlLogin());
		
		if (isModify) {
			SbmBreakTimeUtils.changeSbmBreakTime(env, unitService, m_sbtm, m_sss, apptModifiedGroup, token, false, clinikoSbmSync);
			isCreate = true;
		}
		
		SbmBreakTimeUtils.changeSbmBreakTime(env, unitService, m_sbtm, m_sss, apptGroup, token, isCreate, clinikoSbmSync);
		return true;
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
}
