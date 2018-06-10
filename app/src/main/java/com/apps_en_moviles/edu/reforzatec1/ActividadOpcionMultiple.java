package com.apps_en_moviles.edu.reforzatec1;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ActividadOpcionMultiple extends AppCompatActivity implements View.OnClickListener{


    String [][] ejercicios;

    private Random random= new Random();
    private int respuesta=-1;
    private int pregunta=-1;

    private String idMateria;


    private long tiempoRevizar = 5*60*1000;
    private Button evaluar;
    private Button[] r;
    private TextView preguntaTXT;
    private ProgressBar tiempoRestate;
    private int tiempoTranscurrido;
    private int tiempoMax = 5*60;
    CountDownTimer countDownTimer;
    private long tiempoDespuesDeRevizar = 5*1000;

    private Vibrator vibrator;
    long [] patronVibratorError = {0, 200, 100,200};
    long [] patronVibratorCorrecto = {0, 50};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_opcion_multiple);

        idMateria=getIntent().getStringExtra("idMateria");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(Herramientas.obtenerConf(
                getBaseContext(), "ActividadActual")+" "+getString(R.string.de)+" "+Herramientas.obtenerConf(getBaseContext(), "ContenidoActual") );

        preguntaTXT = (TextView)findViewById(R.id.ejercicios);
        r = new Button[4];
        (r[0] =(Button)findViewById(R.id.respuesta1)).setOnClickListener(this);
        (r[1] =(Button)findViewById(R.id.respuesta2)).setOnClickListener(this);
        (r[2] =(Button)findViewById(R.id.respuesta3)).setOnClickListener(this);
        (r[3] =(Button)findViewById(R.id.respuesta4)).setOnClickListener(this);
        vibrator =  (Vibrator)getSystemService(VIBRATOR_SERVICE);
       cargarDatos();


    }

    private void cargarDatos() {
        try {

            Preguntas ps = (Preguntas)Herramientas.fromString(Herramientas.obtenerConf(getBaseContext(), "M" + idMateria));
            ejercicios =new String[ps.respuestas.size()][];

            for(int i=0; i<ps.getRespuestas().size(); i++) {
                ejercicios[i]  = (ps.getPreguntas().get(i)+"|"+ps.getRespuestas().get(i)).toString().split("\\|");

            }

            pregunta = random.nextInt(ejercicios.length);
            tiempoRestate = (ProgressBar)findViewById(R.id.tiempo_restante);

            restaurar();
            iniciarTemporizador(tiempoRestate);


        }catch (IOException|ClassNotFoundException|ArrayIndexOutOfBoundsException e) {
            Log.e("Error", e.toString());
        }

    }



    private void iniciarTemporizador(final View view) {
        tiempoTranscurrido=0;
        int inc=50;
        tiempoRestate.setMax(tiempoMax*(1000/inc));

        countDownTimer = new CountDownTimer(tiempoMax*1000, inc) {
            @Override
            public void onTick(long l) {
                tiempoTranscurrido++;
                tiempoRestate.setProgress(tiempoTranscurrido);
            }

            @Override
            public void onFinish() {
                tiempoRestate.setProgress(tiempoRestate.getMax());

                sigEvaluacion(view);
            }
        };
        countDownTimer.start();
    }

    private void restaurar() {
        respuesta = -1;
        preguntaTXT.setText(ejercicios[pregunta][0]);

        for(int pos =0; pos<4; pos++) {
            r[pos].setText(ejercicios[pregunta][pos+1].replace("@",""));
            r[pos].setBackgroundResource(R.color.white);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.respuesta1:
                restaurar();
                respuesta = 0;
                break;
            case R.id.respuesta2:
                restaurar();
                respuesta = 1;
                break;
            case R.id.respuesta3:
                restaurar();
                respuesta = 2;
                break;
            case R.id.respuesta4:
                restaurar();
                respuesta = 3;
                break;

        }
        if(respuesta!=-1) {
            colorearOpcion(respuesta, R.color.verdeClaro);
            sigEvaluacion(view);

        }



    }
    private void sigEvaluacion(View view) {
        String msg;
        if(respuesta==-1) {
            msg = getString(R.string.no_seleccion);

        }
        else {
            if (ejercicios[pregunta][respuesta + 1].startsWith("@")) {

                msg = getString(R.string.correcto);
                colorearOpcion(respuesta, R.color.verdeFuerte);
                vibrator.vibrate(patronVibratorCorrecto, -1);

            } else {
                msg = getString(R.string.incorrecto);
                int respuestaCorrecta = buscarRespuesta();
                colorearOpcion(respuesta, R.color.rojoFuerte);
                colorearOpcion(respuestaCorrecta, R.color.verdeClaro);
                vibrator.vibrate(patronVibratorError, -1);

            }
        }





        final Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();




       // evaluar.setEnabled(false);
        for (Button b : r)
            b.setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (snackbar.isShown()) {
                    snackbar.dismiss();

                }
                pregunta = random.nextInt(ejercicios.length);
                restaurar();
               // evaluar.setEnabled(true);
                for (Button b : r)
                    b.setEnabled(true);
                tiempoTranscurrido=0;
                countDownTimer.start();

            }
        }, respuesta==-1?tiempoDespuesDeRevizar/2:tiempoDespuesDeRevizar);
        countDownTimer.cancel();
        respuesta = -1;





    }
    protected int buscarRespuesta() {
        for(int i=0; i<ejercicios[pregunta].length; i++)
            if (ejercicios[pregunta][i].startsWith("@")) {
                return (i-1);
        }
        return -1;
    }

    private void colorearOpcion(int pos, int color) {
        r[pos].setBackgroundResource(color);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(countDownTimer!=null)
            countDownTimer.cancel();
    }
}
