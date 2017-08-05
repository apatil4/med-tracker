package com.gtaks.alexa.medtracker;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by akshaypatil on 8/3/17.
 */
public class MedTrackerSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(MedTrackerSpeechlet.class);

    private AmazonDynamoDBClient amazonDynamoDBClient;

    private MedTrackerResponseService medTrackerResponseService;

    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        initializeComponents();
    }


    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        return medTrackerResponseService.getLaunchResponse();
    }

    public SpeechletResponse onIntent(IntentRequest request, Session session)
            throws SpeechletException {
        log.info("onIntent intentName={} requestId={}, sessionId={}", request.getIntent().getName(), request.getRequestId(),
                session.getSessionId());
        initializeComponents();

        Intent intent = request.getIntent();
        if ("AddMedIntent".equals(intent.getName())) {
            return medTrackerResponseService.getAddMedicineIntentResponse(session, intent);
        } else {
            throw new IllegalArgumentException("Unrecognized intent: " + intent.getName());
        }
    }

    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Initializes the instance components if needed.
     */
    private void initializeComponents() {
        if(medTrackerResponseService == null) {
            medTrackerResponseService = new MedTrackerResponseService();
        }
    }
}
