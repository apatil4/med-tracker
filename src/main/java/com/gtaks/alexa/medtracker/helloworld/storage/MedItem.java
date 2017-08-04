package com.gtaks.alexa.medtracker.helloworld.storage;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.joda.time.DateTime;

/**
 * Created by gaurav on 7/30/17.
 */
@DynamoDBTable(tableName="MedTracker_items")
public class MedItem {
    private String id;
    private DateTime dosageDate;
    private String userName;
    private String medicineName;
    private DateTime createdDatetime;

    @DynamoDBHashKey(attributeName="Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBRangeKey(attributeName="dosage_date")
    public DateTime getDosageDate() {
        return dosageDate;
    }

    public void setDosageDate(DateTime dosageDate) {
        this.dosageDate = dosageDate;
    }

    @DynamoDBRangeKey(attributeName="created_datetime")
    public DateTime getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(DateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    @DynamoDBAttribute(attributeName="user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @DynamoDBAttribute(attributeName="medicine_name")
    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
}
