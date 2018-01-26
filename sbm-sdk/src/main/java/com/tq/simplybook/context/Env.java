package com.tq.simplybook.context;

import java.util.Map;

import com.amazonaws.regions.Regions;

public class Env {
	private Regions regions = Regions.valueOf(System.getenv("REGIONS"));
	private String awsAccessKeyId = new String(System.getenv("AMAZON_ACCESS_KEY"));
	private String awsSecretAccessKey = new String(System.getenv("AMAZON_SECRET_ACCESS_KEY"));
	private String infusionSoftApiName = System.getenv("INFUSIONSOFT_API_NAME");
	private String infusionSoftApiKey = new String(System.getenv("INFUSIONSOFT_API_KEY"));
	private Integer infusionSoftCancelAppliedTag = Integer.valueOf(System.getenv("INFUSIONSOFT_SBM_CANCEL_TAG"));
	private Integer infusionSoftCreateAppliedTag = Integer.valueOf(System.getenv("INFUSIONSOFT_SBM_CREATE_TAG"));

	private String infusionSoftAppointmentTimeField = System.getenv("INFUSIONSOFT_APT_TIME_FILED");
	private String infusionSoftAppointmentLocationField = System.getenv("INFUSIONSOFT_APT_LOC_FIELD");
	private String infusionSoftServiceProviderField = System.getenv("INFUSIONSOFT_SER_PROV_FIELD");
	private String infusionSoftAppointmentInstructionField = System.getenv("INFUSIONSOFT_APT_INST_FIELD");
	private Integer infusionsoftGoogleDeleteTag = Integer
			.valueOf(System.getenv("INFUSIONSOFT_GOOGLE_CALENDAR_DELETE_TAG"));
	private String simplyBookServiceUrlLogin = System.getenv("SIMPLY_BOOK_SERVICE_URL_lOGIN");
	private String simplyBookAdminServiceUrl = System.getenv("SIMPLY_BOOK_ADMIN_SERVICE_URL");
	private String simplyBookCompanyLogin = System.getenv("SIMPLY_BOOK_COMPANY_LOGIN");
	private String simplyBookUser = System.getenv("SIMPLY_BOOK_USER_NAME");
	private String simplyBookPassword = new String(System.getenv("SIMPLY_BOOK_PASSWORD"));
	private String simplyBookApiKey = new String(System.getenv("SIMPLY_BOOK_API_KEY"));
	private String simplyBookSecretKey = new String(System.getenv("SIMPLY_BOOK_SECRET_KEY"));
	private String clinikoApiKey = new String(System.getenv("CLINIKO_API_KEY"));
	private String SBM_CLINIKO_MAPPING_1 = new String(System.getenv("SBM_CLINIKO_MAPPING_1"));
	private Integer clinikoPatientId = Integer.valueOf(System.getenv("CLINIKO_PATIENT_ID"));
	private Integer cliniko_standard_appointment = Integer.valueOf(System.getenv("CLINIKO_STANDARD_APPOINTMENT"));
	private String cliniko_start_time = new String(System.getenv("CLINIKO_START_TIME"));
	private String cliniko_end_time = new String(System.getenv("CLINIKO_END_TIME"));

	private String googleClientId = new String(System.getenv("GOOGLE_CLIENT_ID"));
	private String googleClientSecrets = new String(System.getenv("GOOGLE_CLIENT_SECRETS"));
	private String simplybookWorkingStartTime = new String(System.getenv("SIMPLY_BOOK_WORKING_START_TIME"));
	private String simplybookWorkingEndTime = new String(System.getenv("SIMPLY_BOOK_WORKING_END_TIME"));
	private String googleCalendarEventName = new String(System.getenv("GOOGLE_CALENDAR_EVENT_NAME"));
	private Integer googleCalendarMaxResult = Integer.valueOf(System.getenv("GOOGLE_CALENDAR_MAX_RESULT"));

	private Env() {
		assertVar(googleCalendarEventName, "GOOGLE_CALENDAR_EVENT_NAME");
		assertVar(regions, "REGIONS");
		assertVar(awsAccessKeyId, "AMAZON_ACCESS_KEY");
		assertVar(awsSecretAccessKey, "AMAZON_SECRET_ACCESS_KEY");
		assertVar(infusionSoftApiName, "INFUSIONSOFT_API_NAME");
		assertVar(infusionSoftApiKey, "INFUSIONSOFT_API_KEY");
		assertVar(infusionSoftCancelAppliedTag, "INFUSIONSOFT_SBM_CANCEL_TAG");
		assertVar(infusionSoftCreateAppliedTag, "INFUSIONSOFT_SBM_CREATE_TAG");
		assertVar(infusionSoftAppointmentTimeField, "INFUSIONSOFT_APT_TIME_FILED");
		assertVar(infusionSoftAppointmentLocationField, "INFUSIONSOFT_APT_LOC_FIELD");
		assertVar(infusionSoftServiceProviderField, "INFUSIONSOFT_SER_PROV_FIELD");
		assertVar(infusionSoftAppointmentInstructionField, "INFUSIONSOFT_APT_INST_FIELD");
		assertVar(simplyBookServiceUrlLogin, "SIMPLY_BOOK_SERVICE_URL_lOGIN");
		assertVar(simplyBookAdminServiceUrl, "SIMPLY_BOOK_ADMIN_SERVICE_URL");
		assertVar(simplyBookCompanyLogin, "SIMPLY_BOOK_COMPANY_LOGIN");
		assertVar(simplyBookUser, "SIMPLY_BOOK_USER_NAME");
		assertVar(simplyBookPassword, "SIMPLY_BOOK_PASSWORD");
		assertVar(simplyBookApiKey, "SIMPLY_BOOK_API_KEY");
		assertVar(simplyBookSecretKey, "SIMPLY_BOOK_SECRET_KEY");
		assertVar("clinikoApiKey", "CLINIKO_API_KEY");
		assertVar("SBM_CLINIKO_MAPPING_1", "SBM_CLINIKO_MAPPING_1");
		assertVar("clinikoPatientId", "CLINIKO_PATIENT_ID");
		assertVar("cliniko_standard_appointment", "CLINIKO_STANDARD_APPOINTMENT");
		assertVar("cliniko_start_time", "CLINIKO_START_TIME");
		assertVar("cliniko_end_time", "CLINIKO_END_TIME");
		assertVar(googleClientId, "GOOGLE_CLIENT_ID");
		assertVar(googleClientSecrets, "GOOGLE_CLIENT_SECRETS");
		assertVar(simplybookWorkingStartTime, "GOOGLE_CALENDAR_START_TIME");
		assertVar(simplybookWorkingEndTime, "SIMPLY_BOOK_WORKING_END_TIME");
		assertVar(infusionsoftGoogleDeleteTag, "INFUSIONSOFT_GOOGLE_CALENDAR_DELETE_TAG");
		assertVar(googleCalendarMaxResult, "GOOGLE_CALENDAR_MAX_RESULT");
	}

	private static Env instance;

	public static Env getInstance() {
		return instance;
	}

	private void assertVar(Object value, String name) {
		if (value == null) {
			throw new IllegalArgumentException("Enviroment variable " + name + " is not set");
		}
	}

	public synchronized static Env load() {
		if (instance == null) {
			instance = new Env();
		}
		return instance;
	}

	public Integer getGoogleCalendarMaxResult() {
		return googleCalendarMaxResult;
	}

	public Regions getRegions() {
		return regions;
	}

	public String getAwsAccessKeyId() {
		return awsAccessKeyId;
	}

	public String getAwsSecretAccessKey() {
		return awsSecretAccessKey;
	}

	public String getGoogleCalendarEventName() {
		return googleCalendarEventName;
	}

	public String getInfusionSoftApiName() {
		return infusionSoftApiName;
	}

	public String getInfusionSoftApiKey() {
		return infusionSoftApiKey;
	}

	public String getSimplybookWorkingStartTime() {
		return simplybookWorkingStartTime;
	}

	public String getSimplybookWorkingEndTime() {
		return simplybookWorkingEndTime;
	}

	public Integer getInfusionsoftGoogleDeleteTag() {
		return infusionsoftGoogleDeleteTag;
	}

	public Integer getClinikoPatientId() {
		return Integer.valueOf(clinikoPatientId);
	}

	public Integer getInfusionSoftCancelAppliedTag() {
		return Integer.valueOf(infusionSoftCancelAppliedTag);
	}

	public Integer getInfusionSoftCreateAppliedTag() {
		return Integer.valueOf(infusionSoftCreateAppliedTag);
	}

	public String getInfusionSoftAppointmentTimeField() {
		return infusionSoftAppointmentTimeField;
	}

	public String getInfusionSoftAppointmentLocationField() {
		return infusionSoftAppointmentLocationField;
	}

	public String getInfusionSoftServiceProviderField() {
		return infusionSoftServiceProviderField;
	}

	public String getInfusionSoftAppointmentInstructionField() {
		return infusionSoftAppointmentInstructionField;
	}

	public String getSimplyBookServiceUrlLogin() {
		return simplyBookServiceUrlLogin;
	}

	public String getSBM_CLINIKO_MAPPING_1() {
		return SBM_CLINIKO_MAPPING_1;
	}

	public String getSimplyBookAdminServiceUrl() {
		return simplyBookAdminServiceUrl;
	}

	public String getSimplyBookCompanyLogin() {
		return simplyBookCompanyLogin;
	}

	public String getSimplyBookUser() {
		return simplyBookUser;
	}

	public String getSimplyBookPassword() {
		return simplyBookPassword;
	}

	public String getSimplyBookApiKey() {
		return simplyBookApiKey;
	}

	public String getGoogleClientId() {
		return googleClientId;
	}

	public String getGoogleClientSecrets() {
		return googleClientSecrets;
	}

	public String getSimplyBookSecretKey() {
		return simplyBookSecretKey;
	}

	public String getClinikoApiKey() {
		return clinikoApiKey;
	}

	public Integer getCliniko_standard_appointment() {
		return Integer.valueOf(cliniko_standard_appointment);
	}

	public String getCliniko_start_time() {
		return cliniko_start_time;
	}

	public String getCliniko_end_time() {
		return cliniko_end_time;
	}

	public Map<String, String> getAllEnvVariables() {
		return System.getenv();
	}


}
