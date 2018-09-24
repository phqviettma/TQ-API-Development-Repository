package com.tq.inf.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddNewContactQuery;
import com.tq.inf.query.AddDataQuery;
import com.tq.inf.query.ApplyTagQuery;
import com.tq.inf.query.LoadContactQuery;
import com.tq.inf.query.SearchEmailQuery;
import com.tq.inf.service.ActionCallback;
import com.tq.inf.service.ApiContext;
import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.utils.XmlRqcUtils;

public class ContactServiceImpl implements ContactServiceInf {
	private static final Logger log = Logger.getLogger(ContactServiceImpl.class);
	public Object load(String apiName, String apiKey, final LoadContactQuery loaderQuery) throws InfSDKExecption {
		Object result = XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
			@Override
			public List<?> getParameters(ApiContext apiContext) {
				List<Object> params = new LinkedList<>();
				params.add(apiContext.getApiKey());// secure key

				params.add(loaderQuery.getContactID());
				params.add(loaderQuery.getFields());
				return params;
			}
		}, "ContactService.load");
		return result;
	}

	@Override
	public Object findByEmail(String apiName, String apiKey, final SearchEmailQuery emailQuery)
			throws InfSDKExecption {
		return (Object) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
			@Override
			public List<?> getParameters(ApiContext apiContext) {
				List<Object> params = new LinkedList<>();
				params.add(apiContext.getApiKey());// secure key

				params.add(emailQuery.getEmail());
				params.add(emailQuery.getSelectedFields());
				return params;
			}
		}, "ContactService.findByEmail");
	}

	@Override
	public Integer add(String apiName, String apiKey, final AddNewContactQuery addQuery) throws InfSDKExecption {
		return (Integer) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
			@Override
			public List<?> getParameters(ApiContext apiContext) {
				List<Object> params = new LinkedList<>();
				params.add(apiContext.getApiKey());// secure key

				params.add(addQuery.getDataRecord());
				return params;
			}
		}, "ContactService.add");
	}

	@Override
	public Integer addWithDupCheck(String apiName, String apiKey, final AddNewContactQuery addQuery)
			throws InfSDKExecption {
		return (Integer) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
			@Override
			public List<?> getParameters(ApiContext apiContext) {
				List<Object> params = new LinkedList<>();
				params.add(apiContext.getApiKey());// secure key

				params.add(addQuery.getDataRecord());
				params.add(addQuery.getDupCheckType().getOption()); // default email
				return params;
			}
		}, "ContactService.addWithDupCheck");
	}

	@Override
	public Boolean addToGroup(String apiName, String apiKey, final ApplyTagQuery applyTagQuery) throws InfSDKExecption {
		return makeTagQuery(apiName, apiKey, applyTagQuery, "ContactService.addToGroup");
	}

	private Boolean makeTagQuery(String apiName, String apiKey, final ApplyTagQuery applyTagQuery, String method)
			throws InfSDKExecption {
		return (Boolean) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
			@Override
			public List<?> getParameters(ApiContext apiContext) {
				List<Object> params = new LinkedList<>();
				params.add(apiContext.getApiKey());// secure key

				params.add(applyTagQuery.getContactID());
				params.add(applyTagQuery.getTagID());
				log.info("Param when make apply tag query" + params);
				return params;
			}
		}, method);
	}

	@Override
	public Boolean removeTag(String apiName, String apiKey, ApplyTagQuery removeTagQuery) throws InfSDKExecption {
		return makeTagQuery(apiName, apiKey, removeTagQuery, "ContactService.removeFromGroup");
	}

	@Override
	public Integer update(String apiName, String apiKey, final AddDataQuery updateQuery) throws InfSDKExecption {
		return (Integer) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
			@Override
			public List<?> getParameters(ApiContext apiContext) {
				List<Object> params = new LinkedList<>();
				params.add(apiContext.getApiKey());// secure key

				params.add(updateQuery.getRecordID());
				params.add(updateQuery.getDataRecord());
				return params;
			}
		}, "ContactService.update");
	}

	@Override
	public Boolean applyTag(String apiName, String apiKey, ApplyTagQuery applyTagQuery) throws InfSDKExecption {
		return addToGroup(apiName, apiKey, applyTagQuery);
	}

	@Override
	public Boolean merge(String apiName, String apiKey, Integer duplicateContactId) throws InfSDKExecption {
		return (Boolean) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
			@Override
			public List<?> getParameters(ApiContext apiContext) {
				List<Object> params = new LinkedList<>();
				params.add(apiContext.getApiKey());// secure key

				params.add(duplicateContactId);
				return params;
			}
		}, "ContactService.merge");
	}

}
