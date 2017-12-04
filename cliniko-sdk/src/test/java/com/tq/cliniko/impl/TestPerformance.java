package com.tq.cliniko.impl;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.model.AppointmentInfo;

public class TestPerformance {
	private static ClinikiAppointmentServiceImpl cl = new ClinikiAppointmentServiceImpl("2b2f8a6c0238919e66b81c089da283d2");

	public static void main(String[] args) throws ClinikoSDKExeption {
		AppointmentInfo appointmentInfo = new AppointmentInfo();
		//for(int j=1;j<=12;j++) {
		for (int i = 1; i < 31; i++) {
			
				//String s = "2019-"+j+"-" + i + "T04:45:00Z";
			String apptStart = "2018-07-"+i+"T5:00:00Z";
			String apptEnd = "2018-07-"+i+"T6:00:00Z";
				System.out.println(apptStart);
				appointmentInfo.setAppointment_start(apptStart);
				appointmentInfo.setAppointment_end(apptEnd);
				appointmentInfo.setPatient_id(46101691);
				appointmentInfo.setAppointment_type_id(252503);
				appointmentInfo.setBusiness_id(57535);
				appointmentInfo.setPractitioner_id(87313);	
				AppointmentInfo info = 	cl.createAppointment(appointmentInfo);
				System.out.println(info);
			}
			
		
		//}

	}

}
