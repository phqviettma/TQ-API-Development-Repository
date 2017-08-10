package com.tq.common.lambda.config;

import java.util.Map;
import java.util.Properties;

import javax.ws.rs.NotSupportedException;

public interface EnvVar {
    
    public Map<String, String> getSystemEnv();
    
    public Properties getProperties();
    
    public String getEnv(String key);
    
    public String getValueProperty(String name);
    
    public void setValueSystems(Map<String, String> values) throws NotSupportedException;
}
