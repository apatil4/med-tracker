package com.gtaks.alexa.medtracker.storage;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.gtaks.alexa.medtracker.Index;

/**
 * Created by gaurav on 8/12/17.
 */
@DynamoDBTable(tableName="MedTracker_items")
public class MedItemByUser {

    private String userName;
    private String createdDatetime;

    private String Id;
    private String medicineName;
    private String dosageDate;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = Index.USER_DOSAGE_DATE_INDEX, attributeName = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = Index.USER_DOSAGE_DATE_INDEX, attributeName = "dosage_date")
    public String getDosageDate() {
        return dosageDate;
    }

    public void setDosageDate(String dosageDate) {
        this.dosageDate = dosageDate;
    }

    @DynamoDBAttribute(attributeName="created_datetime")
    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    @DynamoDBHashKey(attributeName="Id")
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @DynamoDBAttribute(attributeName="medicine_name")
    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

}
