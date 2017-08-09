package com.tq.common.lambda.services;

import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.impl.DataServiceImpl;
import com.tq.inf.impl.OrderServiceImpl;
import com.tq.inf.impl.RecurringOrderImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.OrderServiceInf;
import com.tq.inf.service.RecurringOrderInf;

public class ISExternalServiceImpl implements ISExternalService {

    private DataServiceInf m_dataServiceInf;
    private ContactServiceInf m_contactServiceInf;
    private OrderServiceInf m_orderServiceInf;
    private RecurringOrderInf m_recurringOrderInf;

    public ISExternalServiceImpl() {
        this(buildDefault());
    }

    public ISExternalServiceImpl(ISExternalServiceImpl.ISExternalServiceBuilder builder) {
        this.m_dataServiceInf = builder.dataServiceInf;
        this.m_contactServiceInf = builder.contactServiceInf;
        this.m_orderServiceInf = builder.orderServiceIn;
        this.m_recurringOrderInf = builder.recurringOrderInf;
    }

    public static ISExternalServiceBuilder builder() {
        return new ISExternalServiceBuilder();
    }

    public static ISExternalServiceBuilder buildDefault() {
        DataServiceImpl dataServiceInf = new DataServiceImpl();
        return builder().
                withContactServiceInf(new ContactServiceImpl())
                .withDataServiceInf(dataServiceInf)
                .withOrderServiceIn(new OrderServiceImpl())
                .withRecurringOrderInf(new RecurringOrderImpl(dataServiceInf));
    }

    @Override
    public ContactServiceInf getContactServiceInf() {
        return m_contactServiceInf;
    }

    @Override
    public OrderServiceInf getOrderServiceInf() {
        return m_orderServiceInf;
    }

    @Override
    public DataServiceInf getDataServiceInf() {
        return m_dataServiceInf;
    }

    @Override
    public RecurringOrderInf getRecurringOrderInf() {
        return m_recurringOrderInf;
    }

    public static class ISExternalServiceBuilder {
        private DataServiceInf dataServiceInf;
        private ContactServiceInf contactServiceInf;
        private OrderServiceInf orderServiceIn;
        private RecurringOrderInf recurringOrderInf;

        public DataServiceInf getDataServiceInf() {
            return dataServiceInf;
        }

        public void setDataServiceInf(DataServiceInf dataServiceInf) {
            this.dataServiceInf = dataServiceInf;
        }

        public ISExternalServiceBuilder withDataServiceInf(DataServiceInf dataServiceInf) {
            this.dataServiceInf = dataServiceInf;
            return this;
        }

        public ContactServiceInf getContactServiceInf() {
            return contactServiceInf;
        }

        public void setContactServiceInf(ContactServiceInf contactServiceInf) {
            this.contactServiceInf = contactServiceInf;
        }

        public ISExternalServiceBuilder withContactServiceInf(ContactServiceInf contactServiceInf) {
            this.contactServiceInf = contactServiceInf;
            return this;
        }

        public OrderServiceInf getOrderServiceIn() {
            return orderServiceIn;
        }

        public void setOrderServiceIn(OrderServiceInf orderServiceIn) {
            this.orderServiceIn = orderServiceIn;
        }

        public ISExternalServiceBuilder withOrderServiceIn(OrderServiceInf orderServiceIn) {
            this.orderServiceIn = orderServiceIn;
            return this;
        }

        public RecurringOrderInf getRecurringOrderInf() {
            return recurringOrderInf;
        }

        public void setRecurringOrderInf(RecurringOrderInf recurringOrderInf) {
            this.recurringOrderInf = recurringOrderInf;
        }

        public ISExternalServiceBuilder withRecurringOrderInf(RecurringOrderInf recurringOrderInf) {
            this.recurringOrderInf = recurringOrderInf;
            return this;
        }

        public ISExternalService build() {
            return new ISExternalServiceImpl(this);
        }
    }

}
