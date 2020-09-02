package com.example.alunos.testesensores;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity {
    Button btnListar, btnAvancar;
    TextView txtSensores;
    public static SensorManager mSensores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mSensores = (SensorManager)getSystemService(SENSOR_SERVICE);

        txtSensores = findViewById(R.id.txtSensores);
        btnListar = findViewById(R.id.btnListar);
        btnAvancar = findViewById(R.id.btnAvancar);

        txtSensores.setMovementMethod(new ScrollingMovementMethod());
        //método scroll no txtSensores
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarSensores();

            }
        });

        btnListar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                txtSensores.setText("");
                return false;
            }
        });

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proximaJanela();
            }
        });

    }

    private void proximaJanela(){
        Intent janela = new Intent(this, SensoresActivity.class);
        startActivity(janela);
    }


    private void listarSensores(){
        List<Sensor> listaSensores = mSensores.getSensorList(Sensor.TYPE_ALL);
        String descobertos = "TOTAL DE SENSORES: " + listaSensores.size() + "\n\n";
        for (Sensor s:listaSensores){
            descobertos += s.getName() + "\n";
        }

        txtSensores.setText(descobertos);
        }
    }
