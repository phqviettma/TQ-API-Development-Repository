package com.tq.clinikosbmsync.lambda.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.cliniko.time.UtcTimeUtil;
import com.tq.common.lambda.dynamodb.dao.LatestClinikoApptsImpl;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceImpl;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceWrapper;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.service.LatestClinikoApptService;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.lambda.context.Env;
import com.tq.simplybook.lambda.handler.SimplyBookClinikoMapping;
import com.tq.simplybook.req.SetWorkDayInfo;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.ClinikoId;
import com.tq.simplybook.resp.DayInfo;
import com.tq.simplybook.resp.SimplyBookId;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class SyncHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
	private static final Logger m_log = LoggerFactory.getLogger(SyncHandler.class);

	private Env m_env = Env.load();

	private AmazonDynamoDB m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(m_env.getRegions(),
			m_env.getAwsAccessKeyId(), m_env.getAwsSecretAccessKey());
	
	private final ClinikoAppointmentService m_cas = new ClinikiAppointmentServiceImpl(m_env.getClinikoApiKey());
	  private LatestClinikoApptService m_lcs = new LatestClinikoApptServiceImpl(new LatestClinikoApptsImpl(m_amazonDynamoDB));
	    private LatestClinikoApptServiceWrapper m_lcsw = new LatestClinikoApptServiceWrapper(m_lcs);
	private final SpecialdayServiceSbm m_sss = new SpecialdayServiceSbmImpl();
	private final TokenServiceSbm m_tss = new TokenServiceImpl();
	private final SimplyBookClinikoMapping m_sbc  =new SimplyBookClinikoMapping(m_env);

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		try {

			LatestClinikoAppts dbAppts = m_lcsw.load();
			String dbTime = dbAppts.getLatestUpdateTime();

			Set<Long> dbCreateSet = dbAppts.getCreated() != null ? dbAppts.getCreated() : new HashSet<Long>();
			Set<Long> dbRemoveSet = dbAppts.getRemoved() != null ? dbAppts.getRemoved() : new HashSet<Long>();

			AppointmentsInfo apptInf = m_cas.getAppointments(UtcTimeUtil.parseTimeUTC(dbTime));
			Set<Long> lookupedCreateSet = null;
			Set<Long> lookupedRemoveSet = null;

			List<AppointmentInfo> appts = apptInf.getAppointments();
			if (appts == null) {
				appts = Collections.emptyList();
			}

			Map<Long, AppointmentInfo> lookupedMap = null;

			if (appts.size() > 0) {
				lookupedCreateSet = new LinkedHashSet<Long>(apptInf.getAppointments().size());
				lookupedRemoveSet = new LinkedHashSet<Long>(20);
				filterApptIds(apptInf.getAppointments(), lookupedCreateSet, lookupedRemoveSet);
			} else {
				lookupedCreateSet = new HashSet<Long>();
				lookupedRemoveSet = new HashSet<Long>();
			}

			if (dbCreateSet.equals(lookupedCreateSet)) {
				// do nothing for create
			} else {
				Set<Long> newlyCreated = new LinkedHashSet<Long>(20);
				for (Long create : lookupedCreateSet) {
					if (!dbCreateSet.contains(create)) {
						newlyCreated.add(create);
					}
				}

				if (!newlyCreated.isEmpty()) {
					if (lookupedMap == null) {
						lookupedMap = new HashMap<Long, AppointmentInfo>();
						for (AppointmentInfo appt : appts) {
							lookupedMap.put(appt.getId(), appt);
						}
					}

					String token = m_tss.getUserToken(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookUser(),
							m_env.getSimplyBookPassword(), m_env.getSimplyBookServiceUrlLogin());

					for (Long i : newlyCreated) {
						AppointmentInfo appt = lookupedMap.get(i);
						if (appt != null) {
							String date = UtcTimeUtil.extractDate(appt.getAppointment_start());
							String start_time = UtcTimeUtil.extractTime(appt.getAppointment_start());
							String end_time = UtcTimeUtil.extractTime(appt.getAppointment_end());
							m_sss.blockTimeSlot(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl(),
									token, new SetWorkDayInfo(new DayInfo("08:20", "18:00", 0,
											null, 0, "", date, "1", "")));
						}
					}
				}
			}

			if (dbRemoveSet.equals(lookupedRemoveSet)) {
				// do nothing for remove
			} else {
				Set<Long> newlyRemoved = new LinkedHashSet<>();
				for (Long remove : lookupedRemoveSet) {
					if (!dbRemoveSet.contains(remove)) {
						newlyRemoved.add(remove);
					}
				}
				if (!newlyRemoved.isEmpty()) {
					if (lookupedMap == null) {
						lookupedMap = new HashMap<Long,AppointmentInfo>();
						for(AppointmentInfo appt: appts) {
							lookupedMap.put(appt.getId(), appt);
						}
					}
					for(Long i : newlyRemoved) {
						AppointmentInfo appt = lookupedMap.get(i);
						if(appt !=null) {
							String date = UtcTimeUtil.extractDate(appt.getAppointment_start());
							String start_time = UtcTimeUtil.extractTime(appt.getAppointment_start());
							String end_time = UtcTimeUtil.extractTime(appt.getAppointment_end());
							ClinikoId cliniko = new ClinikoId();
							cliniko.setBussinessId(appt.getBusiness_id());
							cliniko.setPractionerId(appt.getPractitioner_id());
							SimplyBookId simplybook = m_sbc.clinikoSbmMapping(cliniko);
							//m_sss.unlockTimeSlot(m_env.getSimplyBookCompanyLogin(),m_env.getSimplyBookAdminServiceUrl(),m_env.getSimplyBookCompanyLogin(), new SetWorkDayInfo(new DayInfo(m_env.getCliniko_start_time(), m_env.getCliniko_end_time(), is_day_off, breaktime, index, name, date, simplybook.getUnit_id(), simplybook.getEvent_id())));
						}
					}
				}

			}

		} catch (ClinikoSDKExeption | SbmSDKException e) {

		}

		return null;
	}

	private Map<String, AppointmentInfo> toMap(List<AppointmentInfo> appts) {
		return null;
	}

	private void filterApptIds(Collection<AppointmentInfo> appointments, Set<Long> createdSet, Set<Long> removedSet) {
		for (AppointmentInfo appt : appointments) {
			if (appt.getCancellation_time() != null || appt.getDeleted_at() != null) {
				createdSet.add(appt.getId());
			} else {
				removedSet.add(appt.getId());
			}
		}

	}

}
