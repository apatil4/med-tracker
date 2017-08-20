package com.gtaks.alexa.medtracker;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
            if (request.getDialogState() != IntentRequest.DialogState.COMPLETED) {
                return getDelegateResponse(intent);
            }
            return medTrackerResponseService.getAddMedicineIntentResponse(session, intent);
        } else if ("ListMedIntent".equals(intent.getName())) {
            return medTrackerResponseService.getListMedicineIntentResponse(session, intent);
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

    public SpeechletResponse getDelegateResponse(Intent intent) {
        DialogIntent dialogIntent = new DialogIntent(intent);

        // 2.
        DelegateDirective dd = new DelegateDirective();
        dd.setUpdatedIntent(dialogIntent);

        List<Directive> directiveList = new ArrayList<Directive>();
        directiveList.add(dd);

        SpeechletResponse speechletResp = new SpeechletResponse();
        speechletResp.setDirectives(directiveList);
        // 3.
        speechletResp.setNullableShouldEndSession(false);
        return speechletResp;
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
