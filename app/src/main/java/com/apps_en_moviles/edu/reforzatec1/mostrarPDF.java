package com.apps_en_moviles.edu.reforzatec1;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class mostrarPDF extends AppCompatActivity {


    //private String url = "https://docs.google.com/gview?embedded=true&url=201.134.65.227/reforzatec/materias/";
    private String url = "http://201.134.65.227/ViewerJS/#../reforzatec/materias/";

    private String idMateria;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_pdf);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(Herramientas.obtenerConf(
                getBaseContext(), "ActividadActual")+" "+getString(R.string.de)+" "+Herramientas.obtenerConf(getBaseContext(), "ContenidoActual") );




        idMateria = getIntent().getStringExtra("idMateria");
        nombre = getIntent().getStringExtra("nombre");

        Toast.makeText(this, getString(R.string.cargando)+" "+actionBar.getTitle(), Toast.LENGTH_LONG).show();

        WebView myWebView = (WebView) this.findViewById(R.id.contenidopdf);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.loadUrl(url+idMateria+"/"+nombre);
        //myWebView.loadUrl("https://google.com");

    }

}
