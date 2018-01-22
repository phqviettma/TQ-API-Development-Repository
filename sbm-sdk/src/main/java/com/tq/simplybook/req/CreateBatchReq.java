package com.tq.simplybook.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBatchReq {
	List<Long> sbmId;
}
