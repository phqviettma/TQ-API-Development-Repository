package com.tq.clickfunnel.lambda.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;
import com.tq.clickfunnel.lambda.service.CFLambdaService;
import com.tq.clickfunnel.lambda.service.CFLambdaServiceRepository;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;

public class CFLambdaContextImpl implements CFLambdaContext {

    private Context m_context;

    private CFLambdaService cfLambdaContext;

    private AmazonDynamoDB m_amazonDynamoDB;

    private CFLambdaServiceRepository m_cfLambdaServiceRepository;

    public CFLambdaContextImpl() {
        this(CFLambdaServiceImpl.defaults(),
                DynanodbUtils.getAmazonDynamoDB(Config.DYNAMODB_DEFAULT_REGION, Config.AWS_ACCESS_KEY, Config.AWS_SECRET_ACCESS_KEY));

    }

    public CFLambdaContextImpl(CFLambdaService clCfLambdaService, AmazonDynamoDB amazonDynamoDB) {
        cfLambdaContext = clCfLambdaService;
        m_amazonDynamoDB = amazonDynamoDB;
        m_cfLambdaServiceRepository = new CFLambdaServiceRepositoryImpl(m_amazonDynamoDB);
    }

    public CFLambdaContextImpl(CFLambdaService clCfLambdaService, CFLambdaServiceRepository cfLambdaServiceRepository) {
        cfLambdaContext = clCfLambdaService;
        m_cfLambdaServiceRepository = cfLambdaServiceRepository;
        m_amazonDynamoDB = cfLambdaServiceRepository.getAmazonDynamoDB();
    }

    @Override
    public void wrapAwsProxy(Context context) {
        this.m_context = context;
    }

    @Override
    public Context getAWSProxyContext() {
        return m_context;
    }

    @Override
    public AmazonDynamoDB getAmazonDynamoDB() {
        return m_amazonDynamoDB;
    }

    @Override
    public CFLambdaService getCFLambdaService() {
        return cfLambdaContext;
    }

    @Override
    public CFLambdaServiceRepository getCFLambdaServiceRepository() {
        return m_cfLambdaServiceRepository;
    }
}
