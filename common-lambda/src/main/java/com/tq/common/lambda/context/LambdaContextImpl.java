package com.tq.common.lambda.context;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.config.SystemEnvVar;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
import com.tq.common.lambda.dynamodb.service.ProductItemService;
import com.tq.common.lambda.services.ISExternalService;
import com.tq.common.lambda.services.ISExternalServiceImpl;
import com.tq.common.lambda.services.RepositoryService;
import com.tq.common.lambda.services.RepositoryServiceImpl;
import com.tq.common.lambda.services.SBMExternalService;
import com.tq.common.lambda.services.SBMExternalServiceImpl;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.InvoiceServiceInf;
import com.tq.inf.service.OrderServiceInf;
import com.tq.inf.service.RecurringOrderInf;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class LambdaContextImpl implements LambdaContext {

    private AmazonDynamoDB m_client;

    private RepositoryService m_repositoryService;

    private ISExternalService m_iSExternalService;

    private SBMExternalService m_sbmExternalService;
    
    private EnvVar m_envVar;

    public LambdaContextImpl() {
        this(builderDefauls(), new SystemEnvVar());
    }

    public LambdaContextImpl(LambdaContextBuilder builder, EnvVar envVar) {
        m_envVar = envVar;
        this.m_client = builder.client;
        this.m_repositoryService = builder.repositoryService;
        this.m_iSExternalService = builder.iSExternalService;
        this.m_sbmExternalService = builder.sbmExternalService;
    }

    public static LambdaContextBuilder builder() {
        return new LambdaContextBuilder();
    }

    public static LambdaContextBuilder builderDefauls() {
        AmazonDynamoDB client = DynamodbUtils.getAmazonDynamoDBInEnv();
        SystemEnvVar envVar = new SystemEnvVar();
        return builder()
                .withEnvVar(envVar)
                .withClient(client)
                .withRepositoryService(new RepositoryServiceImpl(client))
                .withiSExternalService(new ISExternalServiceImpl())
                .withSbmExternalService(new SBMExternalServiceImpl());
    }
    
    @Override
    public EnvVar getEnvVar() {
        return m_envVar;
    }

    @Override
    public AmazonDynamoDB getAmazonDynamoDB() {
        return m_client;
    }

    @Override
    public RepositoryService getRepositoryService() {
        return m_repositoryService;
    }

    @Override
    public ISExternalService getISExternalService() {
        return m_iSExternalService;
    }

    @Override
    public SBMExternalService getSBMExternalService() {
        return m_sbmExternalService;
    }

    @Override
    public ContactItemService getContactItemService() {
        return m_repositoryService.getContactItemService();
    }

    @Override
    public CountryItemService getCountryItemService() {
        return m_repositoryService.getCountryItemService();
    }

    @Override
    public OrderItemService getOrderItemService() {
        return m_repositoryService.getOrderItemService();
    }

    @Override
    public ProductItemService getProductItemService() {
        return m_repositoryService.getProductItemService();
    }

    @Override
    public TokenServiceSbm getTokenServiceSbm() {
        return m_sbmExternalService.getTokenServiceSbm();
    }

    @Override
    public ClientServiceSbm getClientServiceSbm() {
        return m_sbmExternalService.getClientServiceSbm();
    }

    @Override
    public ContactServiceInf getContactServiceInf() {
        return m_iSExternalService.getContactServiceInf();
    }

    @Override
    public OrderServiceInf getOrderServiceInf() {
        return m_iSExternalService.getOrderServiceInf();
    }

    @Override
    public DataServiceInf getDataServiceInf() {
        return m_iSExternalService.getDataServiceInf();
    }

    @Override
    public RecurringOrderInf getRecurringOrderInf() {
        return m_iSExternalService.getRecurringOrderInf();
    }
    
    @Override
    public InvoiceServiceInf getInvoiceServiceInf() {
        return m_iSExternalService.getInvoiceServiceInf();
    }
    
    public static class LambdaContextBuilder {
        
        private EnvVar envVar;
        
        private AmazonDynamoDB client;

        private RepositoryService repositoryService;

        private ISExternalService iSExternalService;

        private SBMExternalService sbmExternalService;

        public AmazonDynamoDB getClient() {
            return client;
        }

        public void setClient(AmazonDynamoDB client) {
            this.client = client;
        }

        public LambdaContextBuilder withClient(AmazonDynamoDB client) {
            this.client = client;
            return this;
        }

        public RepositoryService getRepositoryService() {
            return repositoryService;
        }

        public void setRepositoryService(RepositoryService repositoryService) {
            this.repositoryService = repositoryService;
        }

        public LambdaContextBuilder withRepositoryService(RepositoryService repositoryService) {
            this.repositoryService = repositoryService;
            return this;
        }

        public ISExternalService getiSExternalService() {
            return iSExternalService;
        }

        public void setiSExternalService(ISExternalService iSExternalService) {
            this.iSExternalService = iSExternalService;
        }

        public LambdaContextBuilder withiSExternalService(ISExternalService iSExternalService) {
            this.iSExternalService = iSExternalService;
            return this;
        }

        public SBMExternalService getSbmExternalService() {
            return sbmExternalService;
        }

        public void setSbmExternalService(SBMExternalService sbmExternalService) {
            this.sbmExternalService = sbmExternalService;
        }

        public LambdaContextBuilder withSbmExternalService(SBMExternalService sbmExternalService) {
            this.sbmExternalService = sbmExternalService;
            return this;
        }

        public EnvVar getEnvVar() {
            return envVar;
        }

        public void setEnvVar(EnvVar envVar) {
            this.envVar = envVar;
        }
        
        public LambdaContextBuilder withEnvVar(EnvVar envVar) {
            this.envVar = envVar;
            return this;
        }
        
        public LambdaContext build() {
            return new LambdaContextImpl(this, envVar);
        }
    }
}
