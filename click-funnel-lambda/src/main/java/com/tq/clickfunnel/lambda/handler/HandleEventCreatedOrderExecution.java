package com.tq.clickfunnel.lambda.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.model.ClientInfo;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.model.INFProduct;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderDetail;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderItem;
import com.tq.clickfunnel.lambda.dynamodb.model.ProductItem;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;
import com.tq.clickfunnel.lambda.service.CFLambdaService;
import com.tq.clickfunnel.lambda.service.CFLambdaServiceRepository;
import com.tq.inf.query.OrderQuery;
import com.tq.inf.service.OrderServiceInf;

public class HandleEventCreatedOrderExecution extends HandleEventOrderExecution {
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

                // 2. Load Product in DynamoDB that contact is being purchased.
                ProductItem productItem = loadProductAtDB(contactPayLoad, m_cfServiceRepo);

                //3. Create Order under email on infusion soft
                addOrder = addOrderToInf(contactItem, productItem);

                // 3. Save the Order to DynamoDB for handling in further
                if (addOrder != null) {
                    m_cfServiceRepo.getOrderItemService().put(addOrder);
                }
            }
        } catch (Exception e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        // 5. handle successfully
        handleResponse(input, resp, addOrder);
        return resp;
    }

    private OrderItem addOrderToInf(ContactItem contactItem, ProductItem productItem) {
        OrderItem orderItem = null;
        ClientInfo client = contactItem.getClient();
        Integer contactId = client.getContactId();
        try {
            INFProduct infProduct = productItem.getInfProduct();
            OrderQuery orderQuery = buildOrderQuery(contactId, infProduct);
            OrderServiceInf orderServiceInf = m_cfLambdaService.getOrderServiceInf();
            Map<?, ?> order = (Map<?, ?>)orderServiceInf.addOrder(Config.INFUSIONSOFT_API_NAME, Config.INFUSIONSOFT_API_KEY, orderQuery);
           if (order == null) return null;
            OrderDetail orderDtail = buildOrderDetail(productItem, infProduct, order);
            orderItem = new OrderItem().withContactId(contactId).withEmail(client.getEmail()).withOrderDetails(Arrays.asList(orderDtail));

        } catch (Exception e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        return orderItem;
    }

    private OrderDetail buildOrderDetail(ProductItem productItem, INFProduct infProduct, Map<?, ?> order) {
        Integer orderId = Integer.valueOf((String) order.get("OrderId"));
        Integer invoiceId = Integer.valueOf((String) order.get("InvoiceId"));
        String refNum = (String) order.get("RefNum");
        String message = (String) order.get("Message");
        String successful = (String) order.get("Successful");
        String code = (String) order.get("Code");
        String createdAt = Config.DATE_FORMAT_24_H.format(new Date());

        OrderDetail orderDtail = new OrderDetail().withInvoiceInf(invoiceId).withOrderIdInf(orderId).withMessage(message).withRefNum(refNum)
                .withMessage(message).withSuccessful(successful).withCreatedAt(createdAt).withUpdatedAt(createdAt)
                .withProductIdInf(infProduct.getProductIds()).withProductCf(productItem.getCfProduct().getId()).withCode(code);
        return orderDtail;
    }

    private OrderQuery buildOrderQuery(Integer contactId, INFProduct infProduct) {
        OrderQuery orderQuery = new OrderQuery().withContactID(contactId).withCardID(infProduct.getCartId())
                .withPlanID(infProduct.getPlanId()).withProductionIDs(infProduct.getProductIds())
                .withSubscriptionIDs(infProduct.getSubscriptionPlanIds()).withPromoCodes(Arrays.asList(Config.INFUSION_ORDER_PROMO_CODE));
        return orderQuery;
    }
}
