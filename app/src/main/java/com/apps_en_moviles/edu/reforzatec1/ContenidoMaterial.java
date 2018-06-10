package com.apps_en_moviles.edu.reforzatec1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class ContenidoMaterial extends AppCompatActivity {

    String[] Prueba = new String[]{
            "Teoría y conceptos",
            "Ejemplos",
            "Ejercicios",
            "Referencias"
            };


    int[] IconosId = new int[]{
            R.drawable.teoria,
            R.drawable.ejemplos,
            R.drawable.ejercicios,
            R.drawable.referencias
    };

    private String idMateria;
    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contenido_material);
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Herramientas.obtenerConf(getBaseContext(), "ContenidoActual"));
        idMateria = getIntent().getStringExtra("idMateria");

        ContenidoMaterialItems adapter = new ContenidoMaterialItems(getBaseContext(),Prueba, IconosId );
        ListView lv = (ListView)findViewById(R.id.contenidoMaterial);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it;
                switch (i){
                    case 0:

                        it = new Intent(getBaseContext(), mostrarPDF.class);
                        it.putExtra("idMateria", idMateria);
                        it.putExtra("nombre", "teoria.pdf");
                        Herramientas.fijarConf(getBaseContext(), new String[][]{{"ActividadActual", "Teoria y conceptos"}});
                        startActivity(it);
                        break;
                    case 1:

                        it = new Intent(getBaseContext(), mostrarPDF.class);
                        it.putExtra("idMateria", idMateria);
                        it.putExtra("nombre", "ejemplos.pdf");
                        Herramientas.fijarConf(getBaseContext(), new String[][]{{"ActividadActual", "Ejemplos"}});
                        startActivity(it);
                        break;
                    case 2:
                        it = new Intent(getBaseContext(), ActividadOpcionMultiple.class);
                        it.putExtra("idMateria", idMateria);
                        Herramientas.fijarConf(getBaseContext(), new String[][]{{"ActividadActual", "Ejercicios"}});

                        /*it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        it.putExtra("titulo", getTitle());*/
                        startActivity(it);
                        break;
                }
            }
        });
    }

}


