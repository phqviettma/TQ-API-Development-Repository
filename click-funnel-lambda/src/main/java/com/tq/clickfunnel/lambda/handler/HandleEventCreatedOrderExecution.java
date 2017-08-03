package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.model.INFProduct;
import com.tq.clickfunnel.lambda.dynamodb.model.ProductItem;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.modle.CFProducts;
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
    public AwsProxyResponse execute(AwsProxyRequest input, CFLambdaContext proxyContext) {
        log.info("{}", input.getQueryStringParameters());
        AwsProxyResponse resp = new AwsProxyResponse();
        m_cfLambdaService = proxyContext.getCFLambdaService();
        m_cfServiceRepo = proxyContext.getCFLambdaServiceRepository();
        try {
            CFOrderPayload contactPayLoad = m_mapper.readValue(input.getBody(), CFOrderPayload.class);
            if (contactPayLoad != null) {
                CFContact contact = contactPayLoad.getPurchase().getContact();

                // 1. Load the contact already existed in DynamoDB based on email
                ContactItem contactItem = loadContactAtDB(contact.getEmail());

                // 2. Load Product that contact purchased in DynamoDB
                ProductItem productItem = loadProductAtDB(contactPayLoad);

                // 3. Create Order under email on Infusion soft.
                addOrderToINF(contactItem, productItem);
                
                //4 Save the Order to DynamoDB

            }
        } catch (IOException e) {
            log.error("", e);
        }
        return resp;
    }

    private void addOrderToINF(ContactItem contactItem, ProductItem productItem) {
        Integer infContactId = contactItem.getClient().getContactId();
        try {
            INFProduct infProduct = productItem.getInfProduct();
            OrderQuery orderRecord = new OrderQuery()
                    .withContactID(infContactId).withCardID(infProduct.getCartId())
                    .withProductionIDs(Arrays.asList(infProduct.getId()))
                    .withPromoCodes(Arrays.asList(Config.INFUSION_ORDER_PROMO_CODE))
                    .withSubscriptionIDs(Arrays.asList(infProduct.getSubscriptionPlanId()));

            OrderServiceInf orderServiceInf = m_cfLambdaService.getOrderServiceInf();
            Map<?, ?> addOrder = orderServiceInf.addOrder(Config.INFUSIONSOFT_API_NAME, Config.INFUSIONSOFT_API_KEY, orderRecord);

        } catch (InfSDKExecption e) {
            throw new CFLambdaException("Cannot add Order to infusion soft ", e);
        }
    }

    private ProductItem loadProductAtDB(CFOrderPayload contactPayLoad) {
        List<CFProducts> products = contactPayLoad.getPurchase().getProducts();
        if (products == null) {
            log.info("{}", contactPayLoad);
            throw new CFLambdaException("The contact has not purchased any products.");
        }
        Integer cfProudctionID = products.get(0).getId();
        ProductItem productItem = m_cfServiceRepo.getProductItemService().load(cfProudctionID);
        if (productItem == null)
            throw new CFLambdaException("The product has not existed with " + cfProudctionID);
        return productItem;
    }

    private ContactItem loadContactAtDB(String email) {
        ContactItem contactItem = m_cfServiceRepo.getContactItemService().get(email);
        if (contactItem == null)
            throw new CFLambdaException("The contact had not existed with " + email);
        return contactItem;
    }
}
