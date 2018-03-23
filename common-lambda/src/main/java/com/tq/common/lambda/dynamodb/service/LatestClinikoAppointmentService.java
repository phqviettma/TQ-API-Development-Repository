package com.tq.common.lambda.dynamodb.service;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.LatestClinikoAppointment;

public interface LatestClinikoAppointmentService extends BaseItemService<LatestClinikoAppointment, String> {
	public void delete(LatestClinikoAppointment clinikoAppt);

	public List<LatestClinikoAppointment> queryItem();
}
