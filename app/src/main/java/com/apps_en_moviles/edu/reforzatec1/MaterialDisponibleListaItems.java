package com.apps_en_moviles.edu.reforzatec1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by roberto on 19/09/16.
 */
public class MaterialDisponibleListaItems extends ArrayAdapter<String> {


    private Activity mContext;

    private ArrayList<String> nombres;
    private ArrayList<String> ids;


    public MaterialDisponibleListaItems(Activity c, ArrayList<String> nombres, ArrayList<String> ids) {
        super(c, R.layout.material_disponible_lista_item, nombres);
        this.mContext = c;
        this.nombres = nombres;
        this.ids = ids;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup viewGroup) {
        View lv;
        LayoutInflater inflater = mContext.getLayoutInflater();
        lv = inflater.inflate(R.layout.material_disponible_lista_item, null, true);


        TextView titulo = (TextView)lv.findViewById(R.id.titulo);

        titulo.setText(nombres.get( pos ));

        TextView abreviatura = (TextView)lv.findViewById(R.id.abreviatura);
        abreviatura.setText(Herramientas.Abreviar(nombres.get(pos)));
        abreviatura.setBackgroundResource(Herramientas.getColorOvalo(pos));

        final ImageButton descargarEliminar = (ImageButton)lv.findViewById(R.id.descargareliminar);

        if(Herramientas.obtenerConf(getContext(), "ContenidoDisponible").contains(ids.get(pos)+"#"+nombres.get(pos))) {
            descargarEliminar.setImageResource(R.drawable.ic_delete_forever_black_24dp);
        }else {
            descargarEliminar.setImageResource(R.drawable.ic_file_download_black_24dp);
        }

        descargarEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargar_Eliminar(descargarEliminar, pos, view);
            }
        });

        titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descargar_Eliminar(descargarEliminar, pos, v );
            }
        });

        abreviatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descargar_Eliminar(descargarEliminar, pos, v);
            }
        });

        return lv;
    }




    private void descargar_Eliminar(ImageButton descargarEliminar, final int pos, View v) {
        if(Herramientas.obtenerConf(getContext(), "ContenidoDisponible").contains(ids.get(pos)+"#"+nombres.get(pos))) {
            descargarEliminar.setImageResource(R.drawable.ic_file_download_black_24dp);
            Herramientas.fijarConf(getContext(), new String[][]{{"ContenidoDisponible", Herramientas.obtenerConf(getContext(), "ContenidoDisponible").replace("|" + ids.get(pos)+"#"+ nombres.get(pos), "")}});
            Toast.makeText(mContext, mContext.getString(R.string.materia_eliminada).replace("#", nombres.get(pos)), Toast.LENGTH_SHORT).show();
        } else {
            descargarEliminar.setImageResource(R.drawable.ic_delete_forever_black_24dp);
            Herramientas.fijarConf(getContext(), new String[][]{{"ContenidoDisponible", Herramientas.obtenerConf(getContext(), "ContenidoDisponible") + "|" + ids.get(pos)+"#"+nombres.get(pos)}});
            Toast.makeText(mContext, mContext.getString(R.string.materia_agregada).replace("#", nombres.get(pos)), Toast.LENGTH_SHORT).show();

            final ArrayList<String> preguntas = new ArrayList<String>();
            final ArrayList<String> respuestas = new ArrayList<String>();
            final ArrayList<Integer> tipos = new ArrayList<Integer>();
            final ArrayList<String> ejerciciosJSON = new ArrayList<String>();

            final Preguntas ps = new Preguntas();

            final Snackbar snackbar = Snackbar.make(v, mContext.getString(R.string.materia_agregada).replace("#", nombres.get(pos)), Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "http://201.134.65.227/reforzatec/reforzatec.php?Actividad=2&idMaterias="+ids.get(pos);

            JsonArrayRequest json = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject pregunta = response.getJSONObject(i);
                                    preguntas.add(pregunta.getString("textos"));
                                    respuestas.add(pregunta.getString("respuestas"));
                                    tipos.add(pregunta.getInt("idEjerciciosTipo"));

                                    snackbar.setText((mContext.getString(R.string.cargando_ejercicios).replace("#1", (i+1)+"")).replace("#2", response.length()+"") );
                                }
                                ps.setPreguntas(preguntas);
                                ps.setRespuestas(respuestas);
                                ps.setTipos(tipos);
                                Herramientas.fijarConf(getContext(), new String[][]{{"M"+ids.get(pos), Herramientas.toString(ps)}});
                                snackbar.dismiss();

                            }catch (JSONException|IOException e){
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
    }











}
