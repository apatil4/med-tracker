package com.gtaks.alexa.medtracker;

import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.gtaks.alexa.medtracker.storage.MedItem;
import com.gtaks.alexa.medtracker.storage.MedItemByUser;
import com.gtaks.alexa.medtracker.storage.MedItemDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshaypatil on 8/3/17.
 */
public class MedTrackerResponseService {

    private static final Logger log = LoggerFactory.getLogger(MedTrackerResponseService.class);
    private MedItemDao medItemDao;

    MedTrackerResponseService() {
        medItemDao = new MedItemDao();
    }

    public SpeechletResponse getAddMedicineIntentResponse(Session session, Intent intent) {
        String speechText = "";
        final MedItem medItem = getMedItem(session, intent);
        List<MedItemByUser> existingMeds = medItemDao.getItemByUserAndDosageDate(medItem.getUserName(), medItem.getDosageDate());
        MedItemByUser matchingMed = existingMatchingMedicine(medItem, existingMeds);
        if (matchingMed == null) {
            medItemDao.saveItem(medItem);
            log.info("MedItem saved for user {} and medicine {}", medItem.getUserName(), medItem.getMedicineName());
            speechText = medItem.getMedicineName() + " has been added";
        } else {
            log.info("MedItem already exists for user {} and medicine {} and dosage date {}", matchingMed.getUserName(),
                    matchingMed.getMedicineName(), matchingMed.getDosageDate());
            speechText = matchingMed.getMedicineName() + " already added";
        }

        return buildResponse(speechText);
    }

    public SpeechletResponse getLaunchResponse() {
        String speechText = "Welcome to the MedTracker, you can save your daily medicine dosage here";
        return buildResponse(speechText);
    }

    public SpeechletResponse getListMedicineIntentResponse(Session session, Intent intent) {

        final MedItem medItem = getMedItem(session, intent);
        String speechText = "No medicines for " + medItem.getUserName();
        List<MedItemByUser> existingMeds;
        if (medItem.getDosageDate() != null) {
            existingMeds = medItemDao.getItemByUserAndDosageDate(medItem.getUserName(), medItem.getDosageDate());
            log.info("List of medicines user {} and dosage {}", medItem.getUserName(), medItem.getDosageDate());
        } else {
            existingMeds = medItemDao.getItemsByUser(medItem.getUserName());
            log.info("List of medicines user {}, {} found", medItem.getUserName(), existingMeds.size());
        }
        if (existingMeds.size() > 0) {
            speechText = medItem.getUserName() + " has these medicines. ";
            // TODO group by dosage date
            for (MedItemByUser mu : existingMeds) {
                speechText += " For " + mu.getDosageDate() + " take " + mu.getMedicineName();
            }
        }
        return buildResponse(speechText);
    }

    private MedItem getMedItem(Session session, Intent intent) {

        final Slot userSlot = intent.getSlot(Slots.USER);
        final String defaultUser = session.getUser().getUserId();
        log.info("Default User is {}", defaultUser);

        final Slot medicineSlot = intent.getSlot(Slots.MEDICINE);
        final Slot dateSlot = intent.getSlot(Slots.DATE);

        return new MedItemBuilder().setUserSlot(userSlot, defaultUser)
                .setMedicineSlot(medicineSlot)
                .setDateSlot(dateSlot)
                .build();
    }

    private SpeechletResponse buildResponse(String speechText) {
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

    private MedItemByUser existingMatchingMedicine(MedItem newItem, List<MedItemByUser> existingItems) {
        for (MedItemByUser mu : existingItems) {
            if (mu.getMedicineName() != null && mu.getMedicineName().equalsIgnoreCase(newItem.getMedicineName())) {
                return mu;
            }
        }
        return null;
    }
}