package com.gtaks.alexa.medtracker;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by akshaypatil on 8/3/17.
 */
public final class MedTrackerSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
  private static final Set<String> supportedApplicationIds = new HashSet<String>();
  static {
    supportedApplicationIds.add("amzn1.ask.skill.e52a6cc8-7219-4e73-9a3f-9e1190bf6bf5");
  }

  public MedTrackerSpeechletRequestStreamHandler() {
    super(new MedTrackerSpeechlet(), supportedApplicationIds);
  }
}