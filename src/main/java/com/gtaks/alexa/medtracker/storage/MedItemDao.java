package com.gtaks.alexa.medtracker.storage;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.gtaks.alexa.medtracker.Index;
import com.gtaks.alexa.medtracker.MedItemBuilder;
import com.gtaks.alexa.medtracker.MedTrackerResponseService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gaurav on 7/30/17.
 */
public class MedItemDao {
    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(MedTrackerResponseService.class);


    public MedItemDao() {
        this.dynamoDB = AmazonDynamoDBClientBuilder.standard().build();
        this.mapper = new DynamoDBMapper(dynamoDB);
    }

    public void saveItem(MedItem item) {
        mapper.save(item);
    }

    public List<MedItemByUser> getItemByUserAndDosageDate(String username, String dosageDate) {

        HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":un",  new AttributeValue().withS(username));
        eav.put(":dd", new AttributeValue().withS(dosageDate));

        DynamoDBQueryExpression<MedItemByUser> queryExpression = new DynamoDBQueryExpression<MedItemByUser>()
                .withIndexName(Index.USER_DOSAGE_DATE_INDEX)
                .withConsistentRead(false)
                .withKeyConditionExpression("user_name = :un and dosage_date = :dd")
                .withExpressionAttributeValues(eav);
        return mapper.query(MedItemByUser.class, queryExpression);
    }

    public List<MedItemByUser> getItemsByUserAndWeek(String username, int weeksLookBack) {
        String dosageDateLookback = DateTime.now().minusWeeks(weeksLookBack).toString();

        HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":un",  new AttributeValue().withS(username));
        eav.put(":dd", new AttributeValue().withS(dosageDateLookback));

        DynamoDBQueryExpression<MedItemByUser> queryExpression = new DynamoDBQueryExpression<MedItemByUser>()
                .withIndexName(Index.USER_DOSAGE_DATE_INDEX)
                .withConsistentRead(false)
                .withKeyConditionExpression("user_name = :un and dosage_date > :dd")
                .withExpressionAttributeValues(eav);

        return mapper.query(MedItemByUser.class, queryExpression);
    }

    public List<MedItemByUser> getItemsByUser(String username) {

        HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":un",  new AttributeValue().withS(username));

        DynamoDBQueryExpression<MedItemByUser> queryExpression = new DynamoDBQueryExpression<MedItemByUser>()
                .withIndexName(Index.USER_DOSAGE_DATE_INDEX)
                .withConsistentRead(false)
                .withKeyConditionExpression("user_name = :un")
                .withExpressionAttributeValues(eav);

        return mapper.query(MedItemByUser.class, queryExpression);
    }

    public void delete(MedItemByUser medItem) {
        log.info("MedItemByUser key {}", medItem.getId());
        MedItem me = MedItemBuilder.buildFrom(medItem);
        mapper.delete(me);
    }
}
