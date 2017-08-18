package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.SignupItemDao;
import com.tq.common.lambda.dynamodb.model.SignupItem;
import com.tq.common.lambda.dynamodb.service.SignupItemService;

public class SignupItemServiceImpl implements SignupItemService {

    private SignupItemDao m_signupItemDao;

    public SignupItemServiceImpl(SignupItemDao signupItemDao) {
        m_signupItemDao = signupItemDao;
    }

    @Override
    public void put(SignupItem item) {
        m_signupItemDao.putItem(item);
    }

    @Override
    public SignupItem load(String key) {
        return m_signupItemDao.loadItem(key);
    }

   

}
