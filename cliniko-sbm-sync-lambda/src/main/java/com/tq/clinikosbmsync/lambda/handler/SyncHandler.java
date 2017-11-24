package com.tq.clinikosbmsync.lambda.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

	private Env m_env = Env.load();

	private AmazonDynamoDB m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(m_env.getRegions(),
			m_env.getAwsAccessKeyId(), m_env.getAwsSecretAccessKey());
	
	private final ClinikoAppointmentService m_cas = new ClinikiAppointmentServiceImpl(m_env.getClinikoApiKey());
	private LatestClinikoApptService m_lcs = new LatestClinikoApptServiceImpl(
			new LatestClinikoApptsImpl(m_amazonDynamoDB));
	private LatestClinikoApptServiceWrapper m_lcsw = new LatestClinikoApptServiceWrapper(m_lcs);
	private final SpecialdayServiceSbm m_sss = new SpecialdayServiceSbmImpl();
	private final TokenServiceSbm m_tss = new TokenServiceImpl();
	private final SimplyBookClinikoMapping m_sbc = new SimplyBookClinikoMapping(m_env);
	private SbmBreakTimeManagement m_sbtm = new SbmBreakTimeManagement();
	
	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
	    AwsProxyResponse resp = new AwsProxyResponse();
	    m_log.info("Start CLINIKO-SBM synchronization lambda");
	    boolean errorOccured = false;
	    DateTimeZone dateTz = null;
	    String dbTime = null;
        String latestUpdateTime = null;
	    
	    boolean isCreateSyncedComplete = false;
	    boolean isRemoveSyncedComplete = false;
	    
	    UpdateResult createDbUpdateResult = null;
	    UpdateResult removeDbUpdateResult = null;
	    Set<Long> dbCreateSet = null;
	    Set<Long> dbRemoveSet = null;
	    
		try {
			m_log.info("Going to load database");
			LatestClinikoAppts dbAppts = m_lcsw.load();
			if(dbAppts != null) {
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
			if(dbTime == null) {
			    dbTime = latestUpdateTime;
			} 
			
			dbCreateSet = dbAppts.getCreated();
			dbRemoveSet = dbAppts.getRemoved();
			
			m_log.info("DB appointments status, created: " + (dbCreateSet == null ? Collections.emptySet() : dbCreateSet));
			m_log.info("DB appointments status, removed: " + (dbRemoveSet == null ? Collections.emptySet() : dbRemoveSet));
			
			m_log.info("To fetch Cliniko appointment with start time:" + dbTime);
		
			AppointmentsInfo apptInf = m_cas.getAppointments(dbTime);

			List<AppointmentInfo> appts = apptInf.getAppointments();
			
			if(appts == null) {
				appts = new LinkedList<AppointmentInfo>();
			}
			
			m_log.info("Fetched: " + appts.size() + " created Cliniko appointment(s)");
			
			createDbUpdateResult = syncToSbm(dateTz, dbCreateSet == null ? Collections.emptySet() : dbCreateSet, appts, true);
			isCreateSyncedComplete = true;
			m_log.info("Synchronized created appoinments to Simplybook.me completely");
			
			AppointmentsInfo deletedApptInfo = m_cas.getDeletedAppointments(dbTime);
			List<AppointmentInfo> deletedAppts = deletedApptInfo.getAppointments();
			
			if(deletedAppts == null) {
				deletedAppts = new LinkedList<AppointmentInfo>();
			}
			
			m_log.info("Fetched: " + deletedAppts.size() + " deleted Cliniko appointment(s)");
			
			removeDbUpdateResult = syncToSbm(dateTz, dbRemoveSet == null ? Collections.emptySet() : dbRemoveSet, deletedAppts, false);
			isRemoveSyncedComplete = true;
			m_log.info("Synchronized deleted appoinments to Simplybook.me completely");

		} catch (ClinikoSDKExeption | SbmSDKException e) {
		    m_log.error("Error occurs", e);
		    resp.setStatusCode(500);
		    errorOccured = true;
		}
		
		if(!errorOccured) {
		    resp.setStatusCode(200);
		}
		
		if(isCreateSyncedComplete || isRemoveSyncedComplete) {
			updateDb(latestUpdateTime, createDbUpdateResult, removeDbUpdateResult, dbCreateSet, dbRemoveSet);
		}
		
		return resp;
	}

	private void updateDb(String updateTime, UpdateResult createDbUpdateResult, UpdateResult removeDbUpdateResult,
			Set<Long> dbCreateSet, Set<Long> dbRemoveSet) {
		
		boolean tobeUpdated = (createDbUpdateResult!= null && createDbUpdateResult.flag !=0) 
				|| (removeDbUpdateResult != null && removeDbUpdateResult.flag !=0);
		
		if(tobeUpdated) {
			LatestClinikoAppts lca = new LatestClinikoAppts();
			lca.setLatestUpdateTime(updateTime);
			lca.setCreated(dbCreateSet);
			lca.setRemoved(dbRemoveSet);
			
			if(createDbUpdateResult != null) {
				if(createDbUpdateResult.flag == 1) {
					lca.setCreated(createDbUpdateResult.updateSet);
				} else if(createDbUpdateResult.flag == -1) {
					lca.setCreated(null);
				}
			}
			
			if(removeDbUpdateResult != null) {
				if(removeDbUpdateResult.flag == 1) {
					lca.setRemoved(removeDbUpdateResult.updateSet);
				} else if(removeDbUpdateResult.flag == -1) {
					lca.setCreated(null);
				}
			}
			
			m_lcsw.put(lca);
			
		}
	}
	
	class UpdateResult {
		public UpdateResult(int flag, Set<Long> updateSet) {
			this.flag = flag;
			this.updateSet = updateSet;
		}
		
		public UpdateResult(int flag) {
			this.flag = flag;
		}
		
		/**
		 *  flag 0 means no need to update DB
		 *  flag 1 means need to update DB with appt id from param
		 *  flag -1 means need to update DB with appt id with null
		 */
		
		int flag = 0;
		Set<Long> updateSet = null;
	}
	
	/**
	 * return flag 0 means no need to update DB
	 * return flag 1 means need to update DB with appt id from param
	 * return flag -1 means need to update DB with appt id with null
	 **/
	private UpdateResult syncToSbm(DateTimeZone dateTz, Set<Long> dbSet, List<AppointmentInfo> appts, boolean isCreate) throws SbmSDKException {
		Set<Long> lookupedSet;
		m_log.info("syncToSbm with isCreate = " + isCreate);
		
		if (appts.size() > 0) {
			lookupedSet = new LinkedHashSet<Long>(appts.size());
			extractApptId(appts, lookupedSet);
		} else {
			lookupedSet = new HashSet<Long>();
		}
		
		if(dbSet.equals(lookupedSet)) {
			return new UpdateResult(0);
		} else if (dbSet.containsAll(lookupedSet)) {
			if(lookupedSet.isEmpty()) {
				return new UpdateResult(-1);
			} else {
				return new UpdateResult(1, lookupedSet);
			}
		} else {
			Set<Long> newOnes = new HashSet<Long>();
			Set<Long> existingOnes = new HashSet<Long>();
			for (Long id : lookupedSet) {
				if (!dbSet.contains(id)) {
					newOnes.add(id);
				} else {
					existingOnes.add(id);
				}
			}
			
			//newOnes is not empty always
			
			m_log.info("New ones:" + Arrays.toString(newOnes.toArray()));
			
			Map<Long, AppointmentInfo> lookupedMap = null;
			Set<Long> updateSet = new HashSet<>(existingOnes);
			
			lookupedMap = toLookupMap(appts);
			
			Map<ClinikoId, PractitionerApptGroup> practitionerApptGroupMap = new HashMap<ClinikoId, PractitionerApptGroup>();
			
			for (Long i : newOnes) {
				AppointmentInfo appt = lookupedMap.get(i);
				appt.setAppointment_start(UtcTimeUtil.convertToTzFromLondonTz(dateTz, appt.getAppointment_start()));
				appt.setAppointment_end(UtcTimeUtil.convertToTzFromLondonTz(dateTz, appt.getAppointment_end()));
				if (appt != null) {
					String date = UtcTimeUtil.extractDate(appt.getAppointment_start());
					ClinikoId cliniko = new ClinikoId();
					cliniko.setBussinessId(AppoinmentUtil.getBusinessId(appt));
					cliniko.setPractionerId(AppoinmentUtil.getPractitionerId(appt));
					
					PractitionerApptGroup group = practitionerApptGroupMap.get(cliniko);
					
					if(group == null) {
						group = new PractitionerApptGroup();
						practitionerApptGroupMap.put(cliniko, group);
					} 
					
					group.addAppt(date, appt);
					
				}
			}
			
			String token = m_tss.getUserToken(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookUser(),
					m_env.getSimplyBookPassword(), m_env.getSimplyBookServiceUrlLogin());
			
			m_log.info("Proceed for practitioner group map: " +  String.valueOf(practitionerApptGroupMap));
			changeSbmBreakTime(practitionerApptGroupMap, token, isCreate);
			updateSet.addAll(newOnes);
			
			return new UpdateResult(1, updateSet);
		}
	}

    private void extractApptId(List<AppointmentInfo> appts, Set<Long> lookupedCreateSet) {
    	for (AppointmentInfo appt : appts) {
    		lookupedCreateSet.add(appt.getId());
		}
	}
    
    private void updateDb(String updateTime, Set<Long> created, Set<Long> removed) {
    	LatestClinikoAppts lca = new LatestClinikoAppts();
    	m_lcsw.put(lca);
    }

    private void changeSbmBreakTime(Map<ClinikoId, PractitionerApptGroup> practitionerApptGroupMap, String token, boolean isAdditional) throws SbmSDKException {
        for(Entry<ClinikoId, PractitionerApptGroup> entry : practitionerApptGroupMap.entrySet()) {
            ClinikoId clinikoId = entry.getKey();
            m_log.info("Proceed for practitioner: " +  String.valueOf(clinikoId));
            PractitionerApptGroup group = entry.getValue();
            SimplyBookId simplybookId = m_sbc.clinikoSbmMapping(clinikoId);
            Integer unitId = Integer.valueOf(simplybookId.getUnit_id());
            Integer eventId = Integer.valueOf(simplybookId.getEvent_id());
            
            Map<String, WorksDayInfoResp> workDayInfoMapForUnitId = m_sss.getWorkDaysInfo(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl()
                    ,token, unitId, eventId, 
                    new FromDate(group.getStartDateString(), m_env.getCliniko_start_time()),
                    new ToDate(group.getEndDateString(), m_env.getCliniko_end_time()));
            
            for(Entry<String, Set<Breaktime>> dateToSbmBreakTime : group.getDateToSbmBreakTimesMap().entrySet()){
                Set<Breaktime> breakTimes = dateToSbmBreakTime.getValue();
                String date = dateToSbmBreakTime.getKey();
                if(!breakTimes.isEmpty()) {
                    if(isAdditional) {
                        m_sbtm.addBreakTime(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl()
                        ,token, unitId, eventId, m_env.getCliniko_start_time()
                        ,m_env.getCliniko_end_time(), date, breakTimes, workDayInfoMapForUnitId);
                    } else {
                        m_sbtm.removeBreakTime(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl()
                                ,token, unitId, eventId, m_env.getCliniko_start_time()
                                ,m_env.getCliniko_end_time(), date, breakTimes, workDayInfoMapForUnitId);
                    }
                }
           
            }
        }
    }

    private Map<Long, AppointmentInfo> toLookupMap(List<AppointmentInfo> appts) {
        Map<Long, AppointmentInfo> lookupedMap = new HashMap<Long,AppointmentInfo>();
        for(AppointmentInfo appt: appts) {
        	lookupedMap.put(appt.getId(), appt);
        }
        return lookupedMap;
    }

	private void filterApptIds(Collection<AppointmentInfo> appointments, Set<Long> createdSet, Set<Long> removedSet) {
		for (AppointmentInfo appt : appointments) {
			if (appt.getCancellation_time() != null || appt.getDeleted_at() != null) {
				removedSet.add(appt.getId());
			} else {
				createdSet.add(appt.getId());
			}
		}

	}
	
	private static class PractitionerApptGroup {
	    private Set<AppointmentInfo> appts = new HashSet<AppointmentInfo>();
	    private Set<String> apptDates = new HashSet<String>();
	    private Date startDate = null;
	    private Date endDate = null;
	    private Map<String, Set<Breaktime>> dateToSbmBreakTimesMap = new HashMap<String, Set<Breaktime>>();
	    
        public Map<String, Set<Breaktime>> getDateToSbmBreakTimesMap() {
            return dateToSbmBreakTimesMap;
        }
        public void addAppt(String date, AppointmentInfo appt) {
            this.appts.add(appt);
            this.addDate(date);
            
            Set<Breaktime> breakTimeSet = dateToSbmBreakTimesMap.get(date);
            
            if(breakTimeSet == null) {
                breakTimeSet = new HashSet<Breaktime>();
                dateToSbmBreakTimesMap.put(date, breakTimeSet);
            }
            
            String start_time = UtcTimeUtil.extractTime(appt.getAppointment_start());
            String end_time = UtcTimeUtil.extractTime(appt.getAppointment_end());            
            breakTimeSet.add(new Breaktime(start_time, end_time));
            
        }
        
        private void addDate(String date) {
            Date newDate = UtcTimeUtil.parseDate(date);
            if(startDate == null || startDate.after(newDate)) {
                startDate = newDate; 
            }
            
            if(endDate == null || endDate.before(newDate)) {
                endDate = newDate; 
            }
        }

        public Set<AppointmentInfo> getAppts() {
            return appts;
        }
        
        public Set<String> getApptDates() {
            return apptDates;
        }
        
        public String getStartDateString() {
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            return date.format(startDate);
        }
        
        public String getEndDateString() {
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            return date.format(endDate);
        }
		@Override
		public String toString() {
			return "PractitionerApptGroup [appts=" + appts + ", apptDates=" + apptDates + ", startDate=" + startDate
					+ ", endDate=" + endDate + ", dateToSbmBreakTimesMap=" + dateToSbmBreakTimesMap + "]";
		}

        
	}
	
	
	
}
