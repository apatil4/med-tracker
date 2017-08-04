package com.gtaks.alexa.medtracker.helloworld;

import com.amazon.speech.slu.Slot;
import com.gtaks.alexa.medtracker.helloworld.storage.MedItem;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by akshaypatil on 8/4/17.
 */
public class MedItemBuilder {

    private Slot userSlot;
    private Slot medicineSlot;
    private Slot dateSlot;

    public MedItemBuilder setUserSlot(Slot userSlot){
        this.userSlot = userSlot;
        return this;
    }

    public MedItemBuilder setMedicineSlot(Slot medicineSlot) {
        this.medicineSlot = medicineSlot;
        return this;
    }

    public MedItemBuilder setDateSlot(Slot dateSlot) {
        this.dateSlot = dateSlot;
        return this;
    }

    public MedItem build() {

        final MedItem medItem = new MedItem();

        if(userSlot.getValue() != null) {
            medItem.setUserName(userSlot.getValue());
        }

        if(medicineSlot.getValue() != null) {
            medItem.setMedicineName(medicineSlot.getValue());
        }

        if(dateSlot.getValue() != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime dosageDateTime = dateTimeFormatter.parseDateTime(dateSlot.getValue());
            medItem.setDosageDate(dosageDateTime);
        }

        medItem.setCreatedDatetime(DateTime.now());
        return medItem;
    }
}
