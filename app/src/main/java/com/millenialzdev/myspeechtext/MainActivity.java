package com.millenialzdev.myspeechtext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;
    private TextView tvText;
    private ImageButton btnSpeak, btnPlay;
    private EditText etText;
    private String ID_BahasaIndonesia = "id";
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Speech to text
        tvText = findViewById(R.id.tvText);
        btnSpeak = findViewById(R.id.btnSpeak);

        //text to speech
        btnPlay = findViewById(R.id.btnPlay);
        etText = findViewById(R.id.etText);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int lang = textToSpeech.setLanguage(Locale.forLanguageTag(ID_BahasaIndonesia));
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etText.getText().toString();
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mic_google = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                mic_google.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                mic_google.putExtra(RecognizerIntent.EXTRA_LANGUAGE, ID_BahasaIndonesia);

                try {
                    startActivityForResult(mic_google, RESULT_SPEECH);
                    tvText.setText("");
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(), "Maaf, Device Kamu Tidak Support Speech To Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tvText.setText(text.get(0));
                }
                break;
        }
    }
}