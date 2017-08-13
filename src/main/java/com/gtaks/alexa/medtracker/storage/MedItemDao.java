package com.gtaks.alexa.medtracker.storage;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.gtaks.alexa.medtracker.Index;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;

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

    public List<MedItemByUser> getItemsByUser(String username) {
        // Lookback maximum 1 week
        String dosageDateLookback = DateTime.now().minusWeeks(1).toString();

        HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":un",  new AttributeValue().withS(username));
        eav.put(":dd", new AttributeValue().withS(dosageDateLookback));

        DynamoDBQueryExpression<MedItemByUser> queryExpression = new DynamoDBQueryExpression<MedItemByUser>()
                .withIndexName(Index.USER_DOSAGE_DATE_INDEX)
                .withConsistentRead(false)
                .withKeyConditionExpression("user_name = :un and dosage_date > :dd")
                .withExpressionAttributeValues(eav);

        List<MedItemByUser> iList =  mapper.query(MedItemByUser.class, queryExpression);
        return iList;
    }
}
