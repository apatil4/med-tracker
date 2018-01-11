package com.medbot.alexa.medtracker;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.medbot.alexa.medtracker.storage.MedItem;
import com.medbot.alexa.medtracker.storage.MedItemByUser;
import com.medbot.alexa.medtracker.storage.MedItemDao;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
        if (medItem == null) {
            return buildResponse("Sorry try again");
        }
        List<MedItemByUser> existingMeds = medItemDao.getItemByUserAndDosageDate(medItem.getUserName(), getListDefaultDate(medItem));
        MedItemByUser matchingMed = existingMatchingMedicine(medItem, existingMeds);
        if (matchingMed == null) {
            medItem.setDosageDate(getAddDefaultDate(medItem));
            medItemDao.saveItem(medItem);
            log.info("MedItem saved for user {} and medicine {}", medItem.getUserName(), medItem.getMedicineName());
            speechText = medItem.getMedicineName() + " has been added";
        } else {
            log.info("MedItem already exists for user {} and medicine {} and dosage date {}", matchingMed.getUserName(),
                    matchingMed.getMedicineName(), matchingMed.getDosageDate());
            speechText = matchingMed.getMedicineName() + " already added for " + matchingMed.getDosageDate();
        }

        return buildResponse(speechText);
    }

    public SpeechletResponse getLaunchResponse() {
        String speechText = "Welcome to the MedTracker, you can save your daily medicine dosage here";
        return buildResponse(speechText);
    }

    public SpeechletResponse getListMedicineIntentResponse(Session session, Intent intent) {

        final MedItem medItem = getMedItem(session, intent);
        String speechText = "No medicines";
        List<MedItemByUser> existingMeds = medItemDao.getItemsByUserAndDateGreaterThan(medItem.getUserName(), getListDefaultDate(medItem));
        log.info("List of medicines user {} and dosage {}", medItem.getUserName(), getListDefaultDate(medItem));

        if (existingMeds.size() > 0) {
            speechText = "List of medicines. ";
            Map<String, List<String>> groupByDosageDate = new HashMap<String, List<String>>();
            for (MedItemByUser mu : existingMeds) {
                List<String> li = groupByDosageDate.getOrDefault(mu.getDosageDate(), new ArrayList<String>());
                li.add(mu.getMedicineName());
                groupByDosageDate.put(mu.getDosageDate(), li);
            }
            for (Map.Entry<String, List<String>> e : groupByDosageDate.entrySet()) {
                speechText += " For " + e.getKey() + " take " + String.join(",", e.getValue()) + " .";
            }
        }
        return buildResponse(speechText);
    }

    public SpeechletResponse getDeleteMedicineIntentResponse(Session session, Intent intent) {
        final MedItem medItem = getMedItem(session, intent);
        String speechText = "";
        if(medItem.getUserName() == null) {
            speechText = "No user specified";
        }
        else {
            speechText = "No medicines";
            List<MedItemByUser> medItemByUsersList = medItemDao.getItemsByUserAndDateGreaterThan(medItem.getUserName(), getListDefaultDate(medItem));
            MedItemByUser medItemByUser = existingMatchingMedicine(medItem, medItemByUsersList);
            if(medItemByUser != null) {
                log.info("Found MedItem for user - " + medItemByUser.getUserName());
                medItemDao.delete(medItemByUser);
                speechText = "Removed medicine " + medItemByUser.getMedicineName();
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

//        // Create reprompt
//        Reprompt reprompt = new Reprompt();
//        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    private MedItemByUser existingMatchingMedicine(MedItem newItem, List<MedItemByUser> existingItems) {
        if (existingItems == null) {
            return null;
        }
        for (MedItemByUser mu : existingItems) {
            if (mu.getMedicineName() != null && mu.getMedicineName().equalsIgnoreCase(newItem.getMedicineName())) {
                return mu;
            }
        }
        return null;
    }

    private String getListDefaultDate(MedItem medItem) {
        return medItem.getDosageDate() == null ? DateTime.now().minusWeeks(1).toLocalDate().toString(): medItem.getDosageDate();
    }

    private String getAddDefaultDate(MedItem medItem) {
        return medItem.getDosageDate() == null ? DateTime.now().toLocalDate().toString(): medItem.getDosageDate();
    }
}