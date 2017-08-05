package com.gtaks.alexa.medtracker;

import com.amazon.speech.slu.Slot;
import com.gtaks.alexa.medtracker.storage.MedItem;
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
            medItem.setDosageDate(dateSlot.getValue());
        }

        medItem.setCreatedDatetime(DateTime.now().toString("YYYY-MM-DD"));
        return medItem;
    }
}
