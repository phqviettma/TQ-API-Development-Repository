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
import com.tq.cliniko.lambda.dynamodb.dao.LatestClinikoApptsDaoImpl;
import com.tq.cliniko.lambda.dynamodb.impl.LatestClinikoApptsServiceImpl;
import com.tq.cliniko.lambda.dynamodb.service.LatestClinikoApptsService;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.LatestClinikoAppts;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.cliniko.time.UtcTimeUtil;
import com.tq.clinikosbmsync.lambda.context.Env;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.req.SetWorkDayInfo;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.DayInfo;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class SyncHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static final Logger m_log = LoggerFactory.getLogger(SyncHandler.class);
    
    private Env m_env = Env.load();
    
    private AmazonDynamoDB m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(m_env.getRegions(), m_env.getAwsAccessKeyId(),
            m_env.getAwsSecretAccessKey());
    
	private final ClinikoAppointmentService m_cas  = new ClinikiAppointmentServiceImpl(m_env.getClinikoApiKey());
	private final LatestClinikoApptsService m_lcas = new LatestClinikoApptsServiceImpl(new LatestClinikoApptsDaoImpl(m_amazonDynamoDB));
	private final SpecialdayServiceSbm m_sss = new SpecialdayServiceSbmImpl();
	private final TokenServiceSbm m_tss = new TokenServiceImpl();
    private String m_timezone = m_env.getTimezone();
    
    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
    	try {
    		
    		LatestClinikoAppts dbAppts = m_lcas.load(LatestClinikoAppts.LATEST_UPDATED_KEY);
    		
    		Set<Long> dbCreateSet = dbAppts.getCreated() != null ? dbAppts.getCreated() : new HashSet<Long>();
    		Set<Long> dbRemoveSet = dbAppts.getRemoved() != null ? dbAppts.getRemoved() : new HashSet<Long>();
    		
			AppointmentsInfo apptInf = m_cas.getAppointments(UtcTimeUtil.getNowInUTC(m_timezone));
			Set<Long> lookupedCreateSet = null;
			Set<Long> lookupedRemoveSet = null;
			
			List<AppointmentInfo> appts = apptInf.getAppointments();
			if(appts == null) {
				appts = Collections.emptyList();
			}
			
			Map<Long, AppointmentInfo> lookupedMap = null;
			
			if(appts.size() > 0 ) {
				lookupedCreateSet = new LinkedHashSet<Long>(apptInf.getAppointments().size());
				lookupedRemoveSet = new LinkedHashSet<Long>(20);
				filterApptIds(apptInf.getAppointments(), lookupedCreateSet, lookupedRemoveSet);
			} else {
				lookupedCreateSet = new HashSet<Long>();
				lookupedRemoveSet = new HashSet<Long>();
			}
			
			if(dbCreateSet.equals(lookupedCreateSet)) {
				//do nothing for create
			} else {
				Set<Long> newlyCreated = new LinkedHashSet<Long>(20);
				for(Long create : lookupedCreateSet) {
					if(!dbCreateSet.contains(create)){
						newlyCreated.add(create);
					}
				}
				
				if(!newlyCreated.isEmpty()) {
					if(lookupedMap == null) {
						lookupedMap = new HashMap<Long, AppointmentInfo>();
						for(AppointmentInfo appt : appts) {
							lookupedMap.put(appt.getId(), appt);
						}
					}
					
					String token = m_tss.getUserToken(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookUser(), 
							m_env.getSimplyBookPassword(), m_env.getSimplyBookServiceUrlLogin());
					
					for(Long i : newlyCreated) {
						AppointmentInfo appt = lookupedMap.get(i);
						if(appt != null) {
							String date = UtcTimeUtil.extractDate(appt.getAppointment_start());
							String start_time = UtcTimeUtil.extractTime(appt.getAppointment_start());
							String end_time = UtcTimeUtil.extractTime(appt.getAppointment_end());
							m_sss.blockTimeSlot(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl(), token, 
									new SetWorkDayInfo(new DayInfo("08:20", "18:00", 0, Arrays.asList(new Breaktime(start_time, end_time)), 0, "", date, "1", "")));
						}
					}
				}
			}
			
			if(dbRemoveSet.equals(lookupedRemoveSet)) {
				//do nothing for remove
			} else {
				
			}
			
			
    	} catch(ClinikoSDKExeption | SbmSDKException e){
    		
    	}
    	
        return null;
    }

    private Map<String, AppointmentInfo> toMap(List<AppointmentInfo> appts) {
		return null;
    }
    
	private void filterApptIds(Collection<AppointmentInfo> appointments, Set<Long> createdSet, Set<Long> removedSet) {
		for(AppointmentInfo appt : appointments) {
			if(appt.getCancellation_time() != null || appt.getDeleted_at() != null) {
				removedSet.add(appt.getId());
			} else {
				createdSet.add(appt.getId());
			}
		}
	
	}
    
    
}
