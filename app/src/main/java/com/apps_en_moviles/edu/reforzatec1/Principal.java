package com.apps_en_moviles.edu.reforzatec1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

public class Principal extends AppCompatActivity implements View.OnClickListener {

    private Snackbar snackbar;
    private FloatingActionsMenu fam;
    private View vista;

    private ArrayList<String> datos;
    private ArrayList<String> ids;
    private ContenidoGrid adapter;
    private GridView gvContenido;


    private String versionCode = "-1";
    private boolean appGenuina=true;
    private boolean appDepurar = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        if (!("com.android.vending".equals(getPackageManager().getInstallerPackageName(this.getPackageName())))) {
            appGenuina = false;
           // Toast.makeText(Principal.this, getString(R.string.no_app_genuina), Toast.LENGTH_SHORT).show();

        }


        try {
            String versionCode =getPackageManager().getPackageInfo(this.getPackageName(),0).versionCode+"";
            if(!Herramientas.obtenerConf(getBaseContext(), "versionCode").equalsIgnoreCase(versionCode)) {
                Herramientas.limpiarConf(getBaseContext());
                Herramientas.fijarConf(getBaseContext(), new String[][]{{"versionCode", versionCode}});
            }

        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        fam = (FloatingActionsMenu)findViewById(R.id.fam_prinicipal);
        ((FloatingActionButton)findViewById(R.id.fab_sugerencias)).setOnClickListener(this) ;
        ((FloatingActionButton)findViewById(R.id.fab_comparteme)).setOnClickListener(this);
        ((FloatingActionButton)findViewById(R.id.fab_mascontenido)).setOnClickListener(this);



        cargarDatos();


         adapter = new ContenidoGrid(getBaseContext(), datos);

         gvContenido = (GridView)findViewById(R.id.gridView);



        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ) {
            gvContenido.setNumColumns(2);
        }
         else {
            gvContenido.setNumColumns(3);
        }

        gvContenido.setAdapter(adapter);

        gvContenido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if(fam.isExpanded())
                    fam.collapse();

                snackbar = Snackbar.make(view, "Cargando: " + datos.get(i) + "...", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (snackbar.isShown()) {
                            snackbar.dismiss();

                        }
                        Intent it = new Intent(getBaseContext(), ContenidoMaterial.class);
                        it.putExtra("idMateria", ids.get(i));
                        Herramientas.fijarConf(getBaseContext(), new String[][]{{"ContenidoActual", datos.get(i)}});
                        startActivity(it);
                    }
                }, 1000);



            }
        });


    }











    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.fab_sugerencias:
                final AlertDialog.Builder inputAlert = new AlertDialog.Builder(view.getContext());
                inputAlert.setTitle(getString(R.string.sugerencias));
                inputAlert.setMessage(getString(R.string.sugerencia));
                final EditText userInput = new EditText(view.getContext());
                inputAlert.setView(userInput);
                inputAlert.setPositiveButton(getString(R.string.enviar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = userInput.getText().toString();
                        RequestQueue queue = Volley.newRequestQueue(view.getContext());
                        String url = Herramientas.url+"reforzatec.php?Actividad=0&comentario="+userInputValue;


                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Do something with the response
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Handle error
                                    }
                                });


                        queue.add(stringRequest);


                    }
                });
                inputAlert.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = inputAlert.create();
                alertDialog.show();
                break;

            case R.id.fab_comparteme:

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.compartirTexto));
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.compartir)));

                break;
            case R.id.fab_mascontenido:
                if(appGenuina || appDepurar ) {
                    Intent it = new Intent(getBaseContext(), MaterialDisponibleLista.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    it.putExtra("titulo", getString(R.string.materialdisponible));
                    startActivity(it);
                }
                else {
                    Toast.makeText(this, getString(R.string.no_app_genuina), Toast.LENGTH_SHORT).show();
                }

                break;
        }
        fam.collapse();
    }


    private void cargarDatos() {
        datos = new ArrayList<String>();
        ids = new ArrayList<String>();
        for(String s: Herramientas.obtenerConf(getBaseContext(),"ContenidoDisponible").split("\\|")) {
            if(!s.isEmpty()) {

                 datos.add(s.substring(s.indexOf('#') + 1));
                ids.add(s.substring(0, s.indexOf('#')  ));
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
        adapter.actualizarGrid(datos);




    }


}
