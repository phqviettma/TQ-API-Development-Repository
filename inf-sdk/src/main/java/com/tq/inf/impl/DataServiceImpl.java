package com.tq.inf.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.AddDataQuery;
import com.tq.inf.query.CountDataQuery;
import com.tq.inf.query.CustomFieldDataQuery;
import com.tq.inf.query.DeleteDataQuery;
import com.tq.inf.query.LoadDataQuery;
import com.tq.inf.query.DataQuery;
import com.tq.inf.service.ActionCallback;
import com.tq.inf.service.ApiContext;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.utils.XmlRqcUtils;

public class DataServiceImpl implements DataServiceInf {

    @Override
    public Integer dsAdd(String apiName, String apiKey, final AddDataQuery addQuery) throws InfSDKExecption {
        final class InsertInfuService implements ActionCallback {

            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey()); // secure key

                if (addQuery.getTable() != null) {
                    params.add(addQuery.getTable());
                }
                params.add(addQuery.getDataRecord());
                return params;
            }
        }
        return (Integer) XmlRqcUtils.execute(apiName, apiKey, new InsertInfuService(), "DataService.add");
    }

    @Override
    public Object[] query(String apiName, String apiKey, final DataQuery query) throws InfSDKExecption {
        return (Object[]) queryForObject(apiName, apiKey, query, "DataService.query");
    }

    @Override
    public Integer count(String apiName, String apiKey, final CountDataQuery countQuery) throws InfSDKExecption {
        class CountServiceQuery implements ActionCallback {
            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey()); // secure key

                params.add(countQuery.getTable());
                // where colms & values to constrain
                params.add(countQuery.getFilter());
                return params;
            }

        }
        return (Integer) XmlRqcUtils.execute(apiName, apiKey, new CountServiceQuery(), "DataService.count");
    }

    @Override
    public Object load(String apiName, String apiKey, final LoadDataQuery loadQuery) throws InfSDKExecption {
        class LoadDataQuery implements ActionCallback {
            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey()); // secure key

                params.add(loadQuery.getTable());
                // where colms & values to constrain
                params.add(loadQuery.getRecordID());
                // return columns
                params.add(loadQuery.getSelectedFields());
                return params;
            }
        }
        return XmlRqcUtils.execute(apiName, apiKey, new LoadDataQuery(), "DataService.load");
    }

    private Object queryForObject(String apiName, String apiKey, final DataQuery query, String method) throws InfSDKExecption {
        class DataServiceQuery implements ActionCallback {

            @Override
            public List<?> getParameters(ApiContext apiContext) {

                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey()); // secure key

                params.add(query.getTable());
                // limit maximizes 1000
                params.add(query.getLimit());
                params.add(query.getPage());
                // where colms & values to constrain
                params.add(query.getFilter());
                // return columns
                params.add(query.getSelectedFields());

                // order by
                if (query.getOrderBy() != null) {
                    params.add(query.getOrderBy());
                }
                // accessing
                if (query.getAscending() != null) {
                    params.add(query.getAscending());
                }
                return params;
            }
        }
        return XmlRqcUtils.execute(apiName, apiKey, new DataServiceQuery(), method);
    }

    @Override
    public Boolean delete(String apiName, String apiKey, final DeleteDataQuery deleteQuery) throws InfSDKExecption {
        return (Boolean) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {
            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey());// secure key

                params.add(deleteQuery.getTable());
                params.add(deleteQuery.getRecordID());
                return params;
            }
        }, "DataService.delete");
    }

    @Override
    public Integer update(String apiName, String apiKey, final AddDataQuery updateQuery) throws InfSDKExecption {
        return (Integer) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {

            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey());// secure key

                params.add(updateQuery.getTable());
                params.add(updateQuery.getRecordID());
                params.add(updateQuery.getDataRecord());
                return params;
            }
        }, "DataService.update");
    }

    @Override
    public Integer addCustomField(String apiName, String apiKey, CustomFieldDataQuery customField) throws InfSDKExecption {
        return (Integer) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {

            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey());// secure key

                params.add(customField.getCustomFieldType().getTable());
                params.add(customField.getDisplayName());
                params.add(customField.getDataType().getType());
                params.add(customField.getHeaderID());
                return params;
            }
        }, "DataService.addCustomField");
    }

    @Override
    public Boolean updateCustomField(String apiName, String apiKey, Integer customFieldId, Map<?, ?> values) throws InfSDKExecption {
        return (Boolean) XmlRqcUtils.execute(apiName, apiKey, new ActionCallback() {

            @Override
            public List<?> getParameters(ApiContext apiContext) {
                List<Object> params = new LinkedList<>();
                params.add(apiContext.getApiKey());// secure key

                params.add(customFieldId);
                params.add(values);
                return params;
            }
        }, "DataService.updateCustomField");
    }
}
