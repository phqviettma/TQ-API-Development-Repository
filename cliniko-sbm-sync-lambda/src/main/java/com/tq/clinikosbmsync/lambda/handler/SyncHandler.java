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
	    
	    boolean isDbUpdateNeededForCreated = false;
	    boolean isCreationSyncComplete = false;
	    boolean isDbUpdateNeededForRemoved = false;
	    boolean isRemovalSyncComplete = false;
	    
	    Set<Long> newlyCreated = new LinkedHashSet<Long>(20);
	    Set<Long> newlyRemoved= new LinkedHashSet<Long>(20);
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
			
			Set<Long> dbCreateSet = dbAppts.getCreated() != null ? dbAppts.getCreated() : new HashSet<Long>();
			Set<Long> dbRemoveSet = dbAppts.getRemoved() != null ? dbAppts.getRemoved() : new HashSet<Long>();
			m_log.info("DB appointments status, created: " + dbCreateSet.toArray());
			m_log.info("DB appointments status, removed: " + dbRemoveSet.toArray());
			
			m_log.info("To fetch Cliniko appointment with start time:" + dbTime);
		
			
			AppointmentsInfo apptInf = m_cas.getAppointments(dbTime);
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
				m_log.info("Fetched: " + appts.size() + " Cliniko appointment(s)");

			if (dbCreateSet.containsAll(lookupedCreateSet)) {
				// do nothing for create
			} else {
				for (Long create : lookupedCreateSet) {
					if (!dbCreateSet.contains(create)) {
						newlyCreated.add(create);
					}
				}
				m_log.info("Newly created:" + Arrays.toString(newlyCreated.toArray()));
				if (!newlyCreated.isEmpty()) {
					if (lookupedMap == null) {
						lookupedMap = toLookupMap(appts);
					}
					
					Map<ClinikoId, PractitionerApptGroup> practitionerApptGroupMap = new HashMap<ClinikoId, PractitionerApptGroup>();
					
					for (Long i : newlyCreated) {
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
					
					m_log.info("Newly created: Proceed for practitioner group map: " +  String.valueOf(practitionerApptGroupMap));
					changeSbmBreakTime(practitionerApptGroupMap, token, true);
					isDbUpdateNeededForCreated = true;
				}
				
			}
			
			isCreationSyncComplete = true;
			
			if (dbRemoveSet.containsAll(lookupedRemoveSet)) {
				// do nothing for remove
			} else {
				for (Long remove : lookupedRemoveSet) {
					if (!dbRemoveSet.contains(remove)) {
						newlyRemoved.add(remove);
					}
				}
				m_log.info("Newly removed:" + Arrays.toString(newlyRemoved.toArray()));
				if (!newlyRemoved.isEmpty()) {
					if (lookupedMap == null) {
						lookupedMap = toLookupMap(appts);
					}
					
					Map<ClinikoId, PractitionerApptGroup> practitionerApptGroupMap = new HashMap<ClinikoId, PractitionerApptGroup>();
					
					for(Long i : newlyRemoved) {
						AppointmentInfo appt = lookupedMap.get(i);
						//TODO:hacky
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
                    
					m_log.info("Newly removed: Proceed for practitioner group map: " +  String.valueOf(practitionerApptGroupMap));
                    changeSbmBreakTime(practitionerApptGroupMap, token, false);
                    isDbUpdateNeededForRemoved = true;
				}

			}
			
			isRemovalSyncComplete = true;
			m_log.info("Removal Sync Complete");

		} catch (ClinikoSDKExeption | SbmSDKException e) {
		    m_log.error("Error occurs", e);
		    resp.setStatusCode(500);
		    errorOccured = true;
		}
		
		if(!errorOccured) {
		    resp.setStatusCode(200);
		}
		
		updateDb(dbTime, latestUpdateTime, isDbUpdateNeededForCreated, isCreationSyncComplete, isDbUpdateNeededForRemoved, isRemovalSyncComplete, newlyCreated, newlyRemoved);
		
		return resp;
	}

    private void updateDb(String dbTime, String latestUpdateTime, boolean isDbUpdateNeededForCreated, boolean isCreationSyncComplete,
            boolean isDbUpdateNeededForRemoved, boolean isRemovalSyncComplete, Set<Long> newlyCreated, Set<Long> newlyRemoved) {
        if(isCreationSyncComplete || isRemovalSyncComplete) {
            
		    LatestClinikoAppts lca = new LatestClinikoAppts();
		    
		    if(isDbUpdateNeededForCreated) {
		        lca.setCreated(newlyCreated);
		    }
		    
		    if(isDbUpdateNeededForRemoved) {
		        lca.setRemoved(newlyRemoved);
		    }
		    
		    if(isCreationSyncComplete && isRemovalSyncComplete) {
		        //OK, synchronization is complete, lets record the latest time
		        lca.setLatestUpdateTime(latestUpdateTime);
		    } else {
		        /* Error somewhere, synchronization is not complete fully. 
		           Lets record the current DB time to give a change the next time the synchronization could be complete
		        */  
		        lca.setLatestUpdateTime(dbTime);
		    }
		    
		    m_lcsw.put(lca);
		}
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
