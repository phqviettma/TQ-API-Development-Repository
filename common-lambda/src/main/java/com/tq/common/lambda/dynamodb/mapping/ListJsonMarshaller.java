package com.tq.common.lambda.dynamodb.mapping;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.common.lambda.dynamodb.model.OrderDetail;

public class ListJsonMarshaller<T> implements DynamoDBTypeConverter<String, List<T>> {
    private static final ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @Override
    public String convert(List<T> object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (final Exception e) {
            throw new DynamoDBMappingException("Unable to write object to JSON", e);
        }
    }

    @Override
    public List<T> unconvert(String object) {
        try {
            return mapper.readValue(object, new TypeReference<List<OrderDetail>>(){});
        } catch (final Exception e) {
            throw new DynamoDBMappingException("Unable to read JSON string", e);
        }
    }
}
