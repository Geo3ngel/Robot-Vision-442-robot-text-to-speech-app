package com.example.a442servertts;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public TextView tv;
    NetworkConnectionThread nct;
    public Context context;
    private TTS tts;
    public TextView cmdText;
    private SpeechRecognizer sr;
    private MainActivity selfReference;
    private ProcessCommand pc;

    public MainActivity(){
        selfReference = this;
        System.out.println("INSTANTIANTED!!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        ///show IP address for connection
        tv = (TextView) findViewById(R.id.ipTextView);
        tv.setText("boogied " +getIpAddress());
        pc = new ProcessCommand();
        cmdText = (TextView) findViewById(R.id.cmdText);
        cmdText.setText(getIpAddress());
        //Button sendButton = (Button) findViewById(R.id.sendButton);
        //sendButton.setOnClickListener(this);
        ///Start network thread / server listening for connnection
      nct = NetworkConnectionThread.getInstance();
        nct.setParent(this);
        nct.start();

        sr = SpeechRecognizer.createSpeechRecognizer(this);
        tts = new TTS(this, sr, selfReference);
        tts.start();

        System.out.println("This should only be called once");
    }

    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    public void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText TODO: Process spoken text into commands for the robot to say (PUSH TO TTS)
            System.out.println("Spoken Text:"+spokenText);
            tts.speakOut(pc.process(spokenText)); // TODO: May have to make it so that it won't listen when talking to avoid repeated feedback
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts = null;
    }

    public static String getIpAddress() {
        String ipAddress = "Unable to Fetch IP......";
        try {
            Enumeration en;

            en = NetworkInterface.getNetworkInterfaces();

            while ( en.hasMoreElements()) {
                Log.v("LOGGING", "has Elements*****************");
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {

                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {

                        ipAddress=inetAddress.getHostAddress();
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ipAddress;
    }

    public final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String aResponse = msg.getData().getString("client");

            // TODO: Try and send command to the robot from the app based on voice command?
            //int choice = new Integer(aResponse);
            Message sendMsg = tts.handler.obtainMessage();
            Bundle b = new Bundle();
            sendMsg.setData(b);
            tts.handler.sendMessage(sendMsg);
            //switch (choice) {
               // case 0:
                    sendMsg = tts.handler.obtainMessage();
                    b = new Bundle();
                    b.putString("LM", aResponse);
                    sendMsg.setData(b);
                    tts.handler.sendMessage(sendMsg);
                   // break;

            //}

        }
    };
}
