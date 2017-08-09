package com.tq.common.lambda.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedJson;

@DynamoDBTable(tableName = "Product")
public class ProductItem {
    /**
     * Click Funnel Product id as hash key
     */
    @DynamoDBHashKey(attributeName = "id")
    private Integer id;

    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "infProduct")
    private INFProduct infProduct;

    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute(attributeName = "cfProduct")
    private CFProduct cfProduct;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public INFProduct getInfProduct() {
        return infProduct;
    }

    public void setInfProduct(INFProduct infProduct) {
        this.infProduct = infProduct;
    }

    public CFProduct getCfProduct() {
        return cfProduct;
    }

    public void setCfProduct(CFProduct cfProduct) {
        this.cfProduct = cfProduct;
    }

    @Override
    public String toString() {
        return "ProductItem [id=" + id + ", infProduct=" + infProduct + ", cfProduct=" + cfProduct + "]";
    }
}
