package com.tq.common.lambda.dynamodb.service;

import com.tq.common.lambda.dynamodb.model.SbmCliniko;

public interface SbmClinikoSyncService extends BaseItemService<SbmCliniko, Long> {
	public void delete(SbmCliniko sbmCliniko);

	public SbmCliniko queryIndex(Long clinikoId);
}
