/*
 * This is an example of the Android Text to Speech capability.
 * Notice 2 interfaces being used.
 */

package com.course.example.texttospeech;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import java.util.Locale;

public class TextToSpeechActivity extends Activity implements OnClickListener, OnInitListener {

	private EditText mealpricefield;
	private TextView answerfield;
	private Button button;
	private TextToSpeech speaker;
	private static final String tag = "Widgets";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		mealpricefield = (EditText) findViewById(R.id.mealprice);
		answerfield = (TextView) findViewById(R.id.answer);

		button = (Button) findViewById(R.id.calculate);
		button.setOnClickListener(this);
		
		//Initialize Text to Speech engine (context, listener object)
        speaker = new TextToSpeech(this, this);
	}
	
	//speak methods will send text to be spoken
    public void speak(String output){
    //	speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null);  //for APIs before 21
    	speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
    }
    
    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // If a language is not be available, the result will indicate it.
            int result = speaker.setLanguage(Locale.US);
             //int result = speaker.setLanguage(Locale.FRANCE);

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Language data is missing or the language is not supported.
                Log.e(tag, "Language is not available.");
            } else {
                  // The TTS engine has been successfully initialized
            	speak("Please enter your bill amount");
            	Log.i(tag, "TTS Initialization successful.");
            }
        } else {
            // Initialization failed.
            Log.e(tag, "Could not initialize TextToSpeech.");
        }
    }
    
    // on destroy
    public void onDestroy(){
    	
    	// shut down TTS engine
    	if(speaker != null){
    		speaker.stop();
    		speaker.shutdown();
    	}
    	super.onDestroy();
    }

	// Perform action on click
	public void onClick(View v) {
		try {
			Log.i(tag, "onClick invoked.");

			// grab the meal price from the UI
			String mealprice = mealpricefield.getText().toString();
			Log.i(tag, "mealprice is $" + mealprice);
			
			String answer = "";

			// check to see if the meal price includes a "$"
			if (mealprice.indexOf("$") != -1) {
				mealprice = mealprice.substring(1);
			}

			float fmp = Float.parseFloat(mealprice);

			// let's give a nice tip -> 20%
			fmp = fmp * 1.2f;
			Log.i(tag, "Total Meal price is $" + fmp);
			
			// format our result
			answer = String.format("Full Price including tip is $%.2f", fmp);

			// display the answer
			answerfield.setText(answer);
			Log.i(tag, "onClick complete.");
			
			// if speaker is talking, stop it
			if(speaker.isSpeaking()){
				Log.i(tag, "Speaker Speaking");
				speaker.stop();
			// else start speech
			} else {
				Log.i(tag, "Speaker Not Already Speaking");
				speak(answer);
				mealpricefield.setText("");
			} 
			
		} catch (Exception e) {
			Log.e(tag, "Failed to Calculate Tip:" + e.getMessage());
			answerfield.setText("Failed to Calculate Tip:" + e.getMessage());
		}

	}
}
