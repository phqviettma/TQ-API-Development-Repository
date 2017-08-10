package com.tq.common.lambda.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.NotSupportedException;

public class MockEnvVar implements EnvVar {

    private Map<String, String> m_systemEnv;
    
    private Properties m_systemProp;
    
    public MockEnvVar() {
        this(System.getenv(), System.getProperties());
    }
    public MockEnvVar(Map<String, String> systemEnvironment, Properties properties) {
        m_systemEnv = new HashMap<>(systemEnvironment);
        m_systemProp = new Properties(properties);
    }
    
    @Override
    public Map<String, String> getSystemEnv() {
        return m_systemEnv;
    }

    @Override
    public Properties getProperties() {
        return m_systemProp;
    }

    @Override
    public String getEnv(String key) {
        return m_systemEnv.get(key);
    }

    @Override
    public String getValueProperty(String name) {
        return m_systemProp.getProperty(name);
    }

    @Override
    public void setValueSystems(Map<String, String> values) throws NotSupportedException {
        m_systemEnv.putAll(values);
    }
}
