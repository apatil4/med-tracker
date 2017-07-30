package com.gtaks.alexa.medtracker.helloworld.storage;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

/**
 * Created by gaurav on 7/30/17.
 */
public class ItemDao {
    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper mapper;

    public ItemDao() {
        this.dynamoDB = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(dynamoDB);
    }
    // TODO
}
