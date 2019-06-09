package com.example.a442servertts;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TTS extends Thread implements
        TextToSpeech.OnInitListener  {

    private TextToSpeech tts;
    private Context context;
    public Handler handler;
    private String last;
    private SpeechRecognizer sr;
    private MainActivity mainRef;

    // Should store messages that would other wise not be spoken due to over lapping with other tts messages.
    ArrayList<String> TTS_backlog;

    public TTS(Context con, SpeechRecognizer sr, MainActivity main){

        TTS_backlog = new ArrayList<>();
        tts = new TextToSpeech(con, this, "com.google.android.tts");
        this.sr = sr;
        this.mainRef = main;
        context = con;

        tts.setPitch(.4f);
        tts.setSpeechRate(.7f);

        last = "c";

        this.sr = SpeechRecognizer.createSpeechRecognizer(con);
        listen();
    }

    public void run()
    {
        Looper.prepare();
        handler = new Handler(){
            public void handleMessage(Message msg) {
                String aResponse = msg.getData().getString("LM");
                System.out.println("MSG: "+aResponse);

                speakOut(aResponse);

            }
        };
        Looper.loop();
    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            //tts.setPitch(5); // set pitch level

            //tts.setSpeechRate(2); // set speech speed rate

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "Language is not supported", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "Initilization Failed", Toast.LENGTH_SHORT).show();

        }

    }
    MediaPlayer anthem;
    public void playSound(int sound){
        MediaPlayer mediaPlayer = null;
        switch(sound) {
            case 0:
                System.out.println("PLAYING MEDIA. Context is:" + context);
                mediaPlayer = MediaPlayer.create(context, R.raw.forthemotherland);
                anthem = mediaPlayer;
                break;
            case 4:
                System.out.println("PLAYING MEDIA. Context is:" + context);
                mediaPlayer = MediaPlayer.create(context, R.raw.rickroll);
                break;
        }
        if(mediaPlayer != null){
            mediaPlayer.setVolume(.40f, .40f);
            mediaPlayer.start();
        }
    }

    // Makes the Robot listen for user input
    // TODO: Make it properly reset the display if possible? (NEED TO TEST w/ BOT)
    private void listen(){
        if(sr != null){
            if(listening == false) {
                mainRef.displaySpeechRecognizer();
                listening = true;
                System.out.println("Should be showing");
            }else{
                if(System.currentTimeMillis() - listenGap > 10000){
                    //sr.stopListening();
                    System.out.println(System.currentTimeMillis() - listenGap);
                    //sr.startListening(RecognitionListener listener);
                    listening = false;
                    listenGap = System.currentTimeMillis();
                }
            }
            // Restart if 5 seconds have gone by

        }else{
            System.out.println("RSR not yet initialized");
        }
    }
    int anthemVal = 0;

    long listenGap = System.currentTimeMillis();
    boolean listening = false;

    // TODO: Put in sound bits and stuff here
    public void speakOut(String text) {
        Log.v("LOGGING SPEECH", "text: " + text);
        //String text = txtText.getText().toString();
        listen();
        // Plays a tts in the backlog if there is one, then proceeds to remove it from backlog.
        if(!TTS_backlog.isEmpty()){
            last = TTS_backlog.get(0);
            tts.speak(TTS_backlog.get(0), TextToSpeech.QUEUE_FLUSH, null);
            while (tts.isSpeaking()) {
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                }
            }
            TTS_backlog.remove(0);
        }
        else if(text != null) {
            if(tts.isSpeaking() && !TTS_backlog.contains(text)){
                // If the robot is already speaking, add the message to the Queue
                TTS_backlog.add(text);
            }
            else if(last != text) {
                if(text.equals("MotherLand")){
                    playSound(0);
                }
                else {
                    if(text.equals("Where have you gone, Comrad?")){
                        // Pause the music if it is still going
                        anthem.pause();
                        anthemVal = anthem.getCurrentPosition();
                    }
                    else if(text.equals("AH! There you are Comrad! I, Conrad, missed you Comrad!")){
                        anthem.start();
                        anthem.seekTo(anthemVal);
                    }

                    if(text.equals("Long live the people!")){
                        // Pause the music
                        if (anthem != null)
                            anthem.pause();

                        //Play death sound

                    }

                    last = text;
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    while (tts.isSpeaking()) {
                        try {
                            Thread.sleep(200);
                        } catch (Exception e) {
                        }
                    }
                }
            }

        }else{
            System.out.println("None got through? empty");
        }
    }
}


