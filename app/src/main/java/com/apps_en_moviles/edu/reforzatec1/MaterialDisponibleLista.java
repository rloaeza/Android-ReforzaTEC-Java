package com.apps_en_moviles.edu.reforzatec1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class MaterialDisponibleLista extends AppCompatActivity {

    ArrayList<String> nombres, ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_disponible_lista);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        String titulo = getIntent().getStringExtra("titulo");
        actionBar.setTitle(titulo);

        nombres = new ArrayList<String>();
        ids = new ArrayList<String>();


        final MaterialDisponibleListaItems adapter = new MaterialDisponibleListaItems(this,nombres, ids);
        ListView lv = (ListView)findViewById(R.id.lista);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://201.134.65.227/reforzatec/reforzatec.php?Actividad=1";
        JsonArrayRequest json = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {


                            for (int i = 0; i < response.length(); i++) {
                                JSONObject materia = response.getJSONObject(i);
                                nombres.add(materia.getString("nombre"));
                                ids.add(materia.getString("idMaterias"));
                                adapter.notifyDataSetChanged();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );
        queue.add(json);
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, "Usa el boton de regresar de arriba", Toast.LENGTH_SHORT).show();
    }
}
