package com.gtaks.alexa.medtracker.helloworld.storage;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

/**
 * Created by gaurav on 7/30/17.
 */
public class MedItemDao {
    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper mapper;


    public MedItemDao() {
        this.dynamoDB = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    public void saveItem(MedItem item) {
        this.mapper.save(item);
    }

}
