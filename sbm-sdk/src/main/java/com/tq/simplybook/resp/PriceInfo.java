package com.tq.simplybook.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriceInfo {
	private String operation_datetime;
	private String amount;
	private String currency;
	private String status;
	private String payment_processor;
	
	public String getOperation_datetime() {
		return operation_datetime;
	}
	
	public void setOperation_datetime(String operation_datetime) {
		this.operation_datetime = operation_datetime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayment_processor() {
		return payment_processor;
	}
	public void setPayment_processor(String payment_processor) {
		this.payment_processor = payment_processor;
	}

	@Override
	public String toString() {
		return "operation_datetime=" + operation_datetime + ", amount=" + amount + ", currency=" + currency
				+ ", status=" + status + ", payment_processor=" + payment_processor;
	}
	
}
