package com.tq.common.lambda.dynamodb.dao;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.LatestClinikoAppointment;
import com.tq.common.lambda.dynamodb.service.GenericItem;

public interface LatestClinikoAppointmentDao extends GenericItem<LatestClinikoAppointment, String> {
	public List<LatestClinikoAppointment> queryItem();
}
