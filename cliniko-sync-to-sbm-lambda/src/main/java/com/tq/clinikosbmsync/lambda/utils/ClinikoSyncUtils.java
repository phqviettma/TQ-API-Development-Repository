package com.tq.clinikosbmsync.lambda.utils;

import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;

public class ClinikoSyncUtils {
	public static final String CLINIKO = "cliniko";
	public static final String SBM = "sbm";
	
	public static boolean isCreatedInCliniko(SbmCliniko sbmClinikoSync) {
		return CLINIKO.equalsIgnoreCase(sbmClinikoSync.getAgent());
	}
	
	public static boolean isCreatedInSBM(SbmCliniko sbmClinikoSync) {
		return SBM.equalsIgnoreCase(sbmClinikoSync.getAgent());
	}
	
	public static boolean isNotDeleted(SbmCliniko sbmClinikoSync) {
		return sbmClinikoSync.getFlag() == 1;
	}
	
	public static boolean equalsUpdateAt(AppointmentInfo fetchAppt, SbmCliniko sbmClinikoSync) {
		String oldUpdatedAt = sbmClinikoSync.getUpdatedAt();
		String newUpdatedAt = fetchAppt.getUpdated_at();
		return oldUpdatedAt.equalsIgnoreCase(newUpdatedAt);
	}
}
