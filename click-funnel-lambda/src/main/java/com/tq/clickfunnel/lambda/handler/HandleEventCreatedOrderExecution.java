package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.model.INFProduct;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderInf;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderItem;
import com.tq.clickfunnel.lambda.dynamodb.model.ProductItem;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;
import com.tq.clickfunnel.lambda.service.CFLambdaService;
import com.tq.clickfunnel.lambda.service.CFLambdaServiceRepository;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.OrderQuery;
import com.tq.inf.service.OrderServiceInf;

public class HandleEventCreatedOrderExecution extends HandleEventOrderExecution {
    private static final Logger log = LoggerFactory.getLogger(HandleEventCreatedOrderExecution.class);
    private CFLambdaService m_cfLambdaService;
    private CFLambdaServiceRepository m_cfServiceRepo;

    @Override
    public AwsProxyResponse handleLambdaProxy(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException {
        AwsProxyResponse resp = new AwsProxyResponse();
        m_cfLambdaService = cfLambdaContext.getCFLambdaService();
        m_cfServiceRepo = cfLambdaContext.getCFLambdaServiceRepository();
        OrderItem addOrder = null;
        try {
            CFOrderPayload contactPayLoad = m_mapper.readValue(input.getBody(), CFOrderPayload.class);
            if (contactPayLoad != null) {
                CFContact contact = contactPayLoad.getPurchase().getContact();

                // 1. Load the contact already existed in DynamoDB based on email
                ContactItem contactItem = loadContactAtDB(contact.getEmail(), m_cfServiceRepo);

                // 2. Load Product that contact purchased in DynamoDB
                ProductItem productItem = loadProductAtDB(contactPayLoad, m_cfServiceRepo);

                // 3. Create Order under email on Infusion soft.
                addOrder = addOrderToINF(contactItem, productItem);

                // 4 Save the Order to DynamoDB for handling in further
                m_cfServiceRepo.getOrderItemService().put(addOrder);
            }
        } catch (IOException | CFLambdaException e) {
            log.error("", e);
            throw new CFLambdaException("Error hanpping during Created Order.", e);
        }
        //5. handle successfully 
        handleResponse(input, resp, addOrder);
        return resp;
    }

    private OrderItem addOrderToINF(ContactItem contactItem, ProductItem productItem) {
        long start = System.currentTimeMillis();
        Integer infContactId = contactItem.getClient().getContactId();
        OrderItem orderItem = null;
        try {
            INFProduct infProduct = productItem.getInfProduct();
            OrderQuery orderRecord = buildOrderQuery(infContactId, infProduct);
            OrderServiceInf orderServiceInf = m_cfLambdaService.getOrderServiceInf();
            // Adding Order to Infusion soft
            Map<?, ?> addOrder = orderServiceInf.addOrder(Config.INFUSIONSOFT_API_NAME, Config.INFUSIONSOFT_API_KEY, orderRecord);

            OrderInf orderInf = buildOrderItem(contactItem, infContactId, addOrder);
            orderItem = new OrderItem(contactItem.getEmail()).withInfOrders(Arrays.asList(orderInf));
            
        } catch (InfSDKExecption e) {
            throw new CFLambdaException("Cannot add Order to infusion soft ", e);
        }
        log.info("addInfOrder() = {} ms", System.currentTimeMillis() -start);
        return orderItem;
    }

    private OrderInf buildOrderItem(ContactItem contactItem, Integer infContactId, Map<?, ?> addOrder) {
        Integer orderId = Integer.valueOf((String)addOrder.get("OrderId"));
        Integer invoiceId = Integer.valueOf((String)addOrder.get("InvoiceId"));
        String refNum = (String)addOrder.get("RefNum");
        String code =  (String) addOrder.get("Code");
        String message = (String) addOrder.get("Message");
        String successful = (String) addOrder.get("Successful");

        String createdAt = Config.DATE_FORMAT_24_H.format(new Date());// dynamoDB still does not supported Date.

        OrderInf orderInf = new OrderInf().withContactId(infContactId).withEmail(contactItem.getEmail()).withOrderId(orderId)
                .withInvoiceId(invoiceId).withCreatedAt(createdAt).withUpdatedAt(createdAt).withSuccessfull(successful)
                .withRefNum(refNum).withCode(code).withMessage(message);
        return orderInf;
    }

    private OrderQuery buildOrderQuery(Integer infContactId, INFProduct infProduct) {
        OrderQuery orderRecord = new OrderQuery()
                .withContactID(infContactId)
                .withCardID(infProduct.getCartId())
                .withPlanID(infProduct.getPlanId())
                .withProductionIDs(Arrays.asList(infProduct.getId()))
                .withSubscriptionIDs(Arrays.asList(infProduct.getSubscriptionPlanId()))
                .withPromoCodes(Arrays.asList(Config.INFUSION_ORDER_PROMO_CODE));
        return orderRecord;
    }
}
