package com.gtaks.alexa.medtracker;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.gtaks.alexa.medtracker.storage.MedItem;
import com.gtaks.alexa.medtracker.storage.MedItemDao;

/**
 * Created by akshaypatil on 8/3/17.
 */
public class MedTrackerResponseService {

    private MedItemDao medItemDao;

    MedTrackerResponseService() {
        medItemDao = new MedItemDao();
    }

    public SpeechletResponse getAddMedicineIntentResponse(Session session, Intent intent) {

        final MedItem medItem = getMedItem(intent);
        medItemDao.saveItem(medItem);
        String speechText = medItem.getMedicineName() + " has been added for " + medItem.getUserName();
        return buildResponse(speechText);
    }

    public SpeechletResponse getLaunchResponse() {
        String speechText = "Welcome to the MedTracker, you can save your daily medicine dosage here";
        return buildResponse(speechText);
    }

    private MedItem getMedItem(Intent intent) {

        final Slot userSlot = intent.getSlot(Slots.USER);
        final Slot medicineSlot = intent.getSlot(Slots.MEDICINE);
        final Slot dateSlot = intent.getSlot(Slots.DATE);

        return new MedItemBuilder().setUserSlot(userSlot)
                                   .setMedicineSlot(medicineSlot)
                                   .setDateSlot(dateSlot)
                                   .build();
    }

    private SpeechletResponse buildResponse(String speechText){
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("MedTracker");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}
