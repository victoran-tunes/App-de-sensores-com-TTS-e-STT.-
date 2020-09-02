package com.example.alunos.testesensores;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class SensoresActivity extends AppCompatActivity {
    TextView txtvalor;
    Button btnAcelerometro, btnGiroscopio, btnLuminosidade, btnCapturar;
    Sensor sAcelerometro, sGiroscopio, sLuminosidade;
    String vAcelerometro, vGiroscopio, vLuminosidade;
    int sacudidasX = 0, sacudidasY = 0;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    //se <i> for diferente de erro...
                    tts.setLanguage(Locale.getDefault());
                }//if
            }//void onInit
        });//onInitListener

        txtvalor = findViewById(R.id.txtValor);
        btnAcelerometro = findViewById(R.id.btnAcelerometro);
        btnGiroscopio = findViewById(R.id.btnGiroscopio);
        btnLuminosidade = findViewById(R.id.btnLuminosidade);
        btnCapturar = findViewById(R.id.btnCapturar);


        btnAcelerometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtvalor.setText(vAcelerometro);
            }
        });

        btnGiroscopio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtvalor.setText(vGiroscopio);
            }
        });

        btnLuminosidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtvalor.setText(vLuminosidade);
            }
        });

        btnCapturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            capturar();
            }//void onClick
        });//onClickListener

        sAcelerometro = PrincipalActivity.mSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sGiroscopio = PrincipalActivity.mSensores.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sLuminosidade = PrincipalActivity.mSensores.getDefaultSensor(Sensor.TYPE_LIGHT);

        PrincipalActivity.mSensores.registerListener(
                new Acelerometro(),
                sAcelerometro,
                SensorManager.SENSOR_DELAY_GAME
        );

        PrincipalActivity.mSensores.registerListener(
                new Giroscopio(),
                sGiroscopio,
                SensorManager.SENSOR_DELAY_GAME
        );

        PrincipalActivity.mSensores.registerListener(
                new Luminosidade(),
                sLuminosidade,
                SensorManager.SENSOR_DELAY_GAME
        );
    }//onCreate

    class Acelerometro implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {

            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            if (x < -30 || x > 30) {
                sacudidasX++;
            }
            if (sacudidasX >= 15) {
                sacudidasX = 0;
                txtvalor.setText(vAcelerometro);
                narrar(""+vAcelerometro);
            }

            if (y < -30 || y > 30) {
                sacudidasY++;
            }
            if (sacudidasY >= 15) {
                sacudidasY = 0;
                txtvalor.setText(vLuminosidade);
                narrar(""+vLuminosidade);
            }


            vAcelerometro = "ÚLTIMOS VALORES DO ACELERÔMETRO\n";
            vAcelerometro += "Valor de x: " + String.format("%.3f",x) + "\n";
            vAcelerometro += "Valor de y: " + String.format("%.3f",y) + "\n";
            vAcelerometro += "Valor de z: " + String.format("%.3f",z);
        }//onSensorChanged

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }//onAccurancyChanged
    }//class Acelerometro

    class Giroscopio implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            vGiroscopio = "ÚLTIMOS VALORES DO GIROSCÓPIO\n";
            vGiroscopio += "Valor de x: " +String.format("%.3f",x) + "\n";
            vGiroscopio += "Valor de y: " +String.format("%.3f",y) + "\n";
            vGiroscopio += "Valor de z: " +String.format("%.3f",z);

        }//onSensorChanged

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }//onAcurrancyChanged
    }//class Giroscopio

    class Luminosidade implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            double lux = event.values[0];
            vLuminosidade = "ÚLTIMO VALOR DO SENSOR DE LUMINOSIDADE \n";
            vLuminosidade += "Luminosidade: " + lux + " lux";
        }//onSensorChanged

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }//onAccuracyChanged
    }//Class Luminosidade


    private void capturar() {
        Intent iSTT = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        iSTT.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        iSTT.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        iSTT.putExtra(RecognizerIntent.EXTRA_PROMPT, "Escutando");
        startActivityForResult(iSTT, 100);
    }//void capturar

    private void narrar(String texto){
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> resultado = data.getStringArrayListExtra(RecognizerIntent
                        .EXTRA_RESULTS);
                String textoReconhecido = resultado.get(0);
                txtvalor.setText(textoReconhecido);
                if (textoReconhecido.toUpperCase().contains("FECHAR")
                        || textoReconhecido.toUpperCase().contains("FECHAR APLICAÇÃO")
                        || textoReconhecido.toUpperCase().contains("FECHAR APLICATIVO")
                        || textoReconhecido.toUpperCase().contains("FECHAR O APLICATIVO")) {
                    finishAffinity();
                }//if fechar app
                    if (textoReconhecido.toUpperCase().contains("ACELERÔMETRO"))
                    {
                        txtvalor.setText(vAcelerometro);
                         narrar(""+vAcelerometro);
                    }//if acelerometro

                    if(textoReconhecido.toUpperCase().contains("LUMINOSIDADE"))
                    {
                        txtvalor.setText(vLuminosidade);
                        narrar(""+vLuminosidade);
                    }

                    if(textoReconhecido.toUpperCase().contains("GIROSCÓPIO"))
                    {
                        txtvalor.setText(vGiroscopio);
                        narrar(""+vGiroscopio);
                    }
                    if(textoReconhecido.toUpperCase().contains("LIMPAR"))
                    {
                        txtvalor.setText("");
                    }
            }//if request code == ok
        }//if request code == 100
    }//protected void
}//class