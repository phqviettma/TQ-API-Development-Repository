package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Settings implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 3565173802348562406L;
	private Account account;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Settings [account=" + account + "]";
	}

	public Settings(Account account) {
		
		this.account = account;
	}

	public Settings() {
		
	}

}
