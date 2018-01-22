package com.tq.simplybook.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyTimeZone implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8804246379084637222L;
	private String timezone;
	private Integer offset;
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	@Override
	public String toString() {
		return "CompanyTimeZone [timezone=" + timezone + ", offset=" + offset + "]";
	}
	
}
