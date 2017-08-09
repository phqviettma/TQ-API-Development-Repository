package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.ContactItemDao;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.service.ContactItemService;

public class ContactItemServiceImpl implements ContactItemService {
    
    private ContactItemDao m_contactItemDao;

    public ContactItemServiceImpl(ContactItemDao contactItemDao) {
        m_contactItemDao = contactItemDao;
    }

    @Override
    public void put(ContactItem contactItem) {
        m_contactItemDao.putItem(contactItem);
    }

    @Override
    public ContactItem load(String email) {
        return m_contactItemDao.loadItem(email);
    }
}
