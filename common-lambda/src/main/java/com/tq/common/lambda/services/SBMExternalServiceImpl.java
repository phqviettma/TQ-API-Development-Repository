package com.tq.common.lambda.services;

import com.tq.simplybook.impl.ClientServiceImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.ClientServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class SBMExternalServiceImpl implements SBMExternalService {

    private TokenServiceSbm m_tokenServiceSbm;

    private ClientServiceSbm m_clientServiceSbm;

    public SBMExternalServiceImpl() {
        this(builderDefaults());
    }

    public SBMExternalServiceImpl(SBMExternalServiceImpl.SBMExternalServiceBuilder builder) {
        this.m_tokenServiceSbm = builder.tokenServiceSbm;
        this.m_clientServiceSbm = builder.clientServiceSbm;
    }

    public static SBMExternalServiceBuilder builder() {
        return new SBMExternalServiceBuilder();
    }

    public static SBMExternalServiceBuilder builderDefaults() {
        return builder()
                .withClientServiceSbm(new ClientServiceImpl())
                .withTokenServiceSbm(new TokenServiceImpl());
    }

    public void setTokenServiceSbm(TokenServiceSbm tokenServiceSbm) {
        this.m_tokenServiceSbm = tokenServiceSbm;
    }

    public void setClientServiceSbm(ClientServiceSbm clientServiceSbm) {
        this.m_clientServiceSbm = clientServiceSbm;
    }

    @Override
    public TokenServiceSbm getTokenServiceSbm() {
        return m_tokenServiceSbm;
    }

    @Override
    public ClientServiceSbm getClientServiceSbm() {
        return m_clientServiceSbm;
    }

    public static class SBMExternalServiceBuilder {

        private TokenServiceSbm tokenServiceSbm;

        private ClientServiceSbm clientServiceSbm;

        public TokenServiceSbm getTokenServiceSbm() {
            return tokenServiceSbm;
        }

        public void setTokenServiceSbm(TokenServiceSbm tokenServiceSbm) {
            this.tokenServiceSbm = tokenServiceSbm;
        }

        public SBMExternalServiceBuilder withTokenServiceSbm(TokenServiceSbm tokenServiceSbm) {
            this.tokenServiceSbm = tokenServiceSbm;
            return this;
        }

        public ClientServiceSbm getClientServiceSbm() {
            return clientServiceSbm;
        }

        public void setClientServiceSbm(ClientServiceSbm clientServiceSbm) {
            this.clientServiceSbm = clientServiceSbm;
        }

        public SBMExternalServiceBuilder withClientServiceSbm(ClientServiceSbm clientServiceSbm) {
            this.clientServiceSbm = clientServiceSbm;
            return this;
        }

        public SBMExternalService build() {
            return new SBMExternalServiceImpl(this);
        }
    }

}
