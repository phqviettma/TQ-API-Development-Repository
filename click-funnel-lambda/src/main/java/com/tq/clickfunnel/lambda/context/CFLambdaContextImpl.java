package com.tq.clickfunnel.lambda.context;

import com.amazonaws.services.lambda.runtime.Context;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.context.LambdaContextImpl;

public class CFLambdaContextImpl implements CFLambdaContext {
    
    private Context m_context;
    private LambdaContext m_lambdaContext;
    
    public CFLambdaContextImpl(Context context) {
        this(context, new LambdaContextImpl());
    }
    
    public CFLambdaContextImpl() {
        m_lambdaContext = new LambdaContextImpl();
    }
    
    public CFLambdaContextImpl(Context context, LambdaContext lambdaContext) {
        this.m_context = context;
        this.m_lambdaContext = lambdaContext;
    }

    @Override
    public Context getAwsProxyContext() {
        return m_context;
    }

    @Override
    public LambdaContext getLambdaContext() {
        return m_lambdaContext;
    }

    @Override
    public void wrapAwsProxyContext(Context context) {
        this.m_context = context;
    }

    @Override
    public void setLambdaContext(LambdaContext lambdaContext) {
        m_lambdaContext = lambdaContext;
    }
}
