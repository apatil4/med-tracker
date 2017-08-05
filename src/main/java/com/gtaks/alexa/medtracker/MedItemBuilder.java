package com.gtaks.alexa.medtracker;

import com.amazon.speech.slu.Slot;
import com.gtaks.alexa.medtracker.storage.MedItem;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by akshaypatil on 8/4/17.
 */
public class MedItemBuilder {

    private static final Logger log = LoggerFactory.getLogger(MedItemBuilder.class);
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

        if(userSlot !=null && userSlot.getValue() != null) {
            log.info("Adding userslot");
            medItem.setUserName(userSlot.getValue());
        }

        if(medicineSlot != null && medicineSlot.getValue() != null) {
            log.info("Adding medicineslot");
            medItem.setMedicineName(medicineSlot.getValue());
        }

        if(dateSlot != null && dateSlot.getValue() != null) {
            log.info("Adding dateslot");
            medItem.setDosageDate(dateSlot.getValue());
        }

        medItem.setCreatedDatetime(DateTime.now().toString());
        return medItem;
    }
}