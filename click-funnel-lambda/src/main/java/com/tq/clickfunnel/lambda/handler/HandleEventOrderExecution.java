package com.tq.clickfunnel.lambda.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.model.ProductItem;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.modle.CFProducts;
import com.tq.clickfunnel.lambda.service.CFLambdaServiceRepository;

public abstract class HandleEventOrderExecution extends AbstractEventPayloadExecution {

    private static final Logger log = LoggerFactory.getLogger(HandleEventOrderExecution.class);

    protected ContactItem loadContactAtDB(String email, CFLambdaServiceRepository cfServiceRepo) {
        ContactItem contactItem = cfServiceRepo.getContactItemService().get(email);
        if (contactItem == null)
            throw new CFLambdaException("The contact had not existed with " + email);
        return contactItem;
    }

    protected ProductItem loadProductAtDB(CFOrderPayload contactPayLoad, CFLambdaServiceRepository cfServiceRepo) {
        List<CFProducts> products = contactPayLoad.getPurchase().getProducts();
        if (products == null || products.isEmpty()) {
            log.info("{}", contactPayLoad);
            throw new CFLambdaException("The contact has not purchased any products.");
        }
        Integer cfProudctionID = products.get(0).getId();
        ProductItem productItem = cfServiceRepo.getProductItemService().load(cfProudctionID);
        if (productItem == null)
            throw new CFLambdaException("The product has not existed with " + cfProudctionID);
        return productItem;
    }
}
