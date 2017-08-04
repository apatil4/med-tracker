package com.gtaks.alexa.medtracker;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;


/**
 * This class could be the handler for an AWS Lambda function powering an Alexa Skills Kit
 * experience. To do this, simply set the handler field in the AWS Lambda console to
 * "medtracler.MedTrackerSpeechletRequestStreamHandler" For this to work, you'll also need to build
 * this project using the {@code lambda-compile} Ant task and upload the resulting zip file to power
 * your function.
 */
public final class MedTrackerSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
  private static final Set<String> supportedApplicationIds = new HashSet<String>();
  static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
    supportedApplicationIds.add("amzn1.ask.skill.e52a6cc8-7219-4e73-9a3f-9e1190bf6bf5");
  }

  public MedTrackerSpeechletRequestStreamHandler() {
    super(new MedTrackerSpeechlet(), supportedApplicationIds);
  }
}