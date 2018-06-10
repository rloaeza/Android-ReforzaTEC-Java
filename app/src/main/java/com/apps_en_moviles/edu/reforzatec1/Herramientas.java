package com.apps_en_moviles.edu.reforzatec1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by roberto on 21/09/16.
 */
public class Herramientas {


    private static int[] fondos = new int[] {
            R.drawable.ovalo_azul,
            R.drawable.ovalo_magenta,
            R.drawable.ovalo_rojo,
            R.drawable.ovalo_verde,
            R.drawable.ovalo_cyan,
            R.drawable.ovalo_amarillo,
            R.drawable.ovalo_beige
    };

    protected static int getColorOvalo(int id) {
        return fondos[id%fondos.length];
    }

    protected static String Abreviar(String titulo) {
        String abreviatura = "";
        for(String s : titulo.split(" ") ) {
            if( (s.charAt(0)+"").equals((s.charAt(0)+"").toUpperCase()) )
                abreviatura+=s.charAt(0);
        }
        return abreviatura.toUpperCase();
    }

    protected static void limpiarConf(Context c) {
        try {
            SharedPreferences settings = c.getSharedPreferences(c.getString(R.string.app_name), 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
        }catch (NullPointerException e) {
            Log.e("Shared preferences", e.toString());
        }
    }

    protected static void fijarConf(Context c, String[][] configuraciones) {
        try {
            SharedPreferences settings = c.getSharedPreferences(c.getString(R.string.app_name), 0);
            SharedPreferences.Editor editor = settings.edit();
            for (String[] configuracion : configuraciones) {
                editor.putString(configuracion[0], configuracion[1]);
            }
            editor.commit();
        }catch (NullPointerException e) {
            Log.e("Shared preferences", e.toString());
        }
    }
    protected static String obtenerConf(Context c, String configuracion) {
        SharedPreferences settings = c.getSharedPreferences(c.getString(R.string.app_name), 0);
        return settings.getString(configuracion, "");
    }

    protected static File getTempFile(Context context, String fileName) {
        File f = null;
        try {
            //String fileName = Uri.parse(url).getLastPathSegment();
            f = File.createTempFile(fileName, null, context.getCacheDir());
        }catch(IOException e) {
            e.printStackTrace();
        }
        return f;
    }






    /** Read the object from Base64 string. */
    protected static Object fromString( String s ) throws IOException ,
            ClassNotFoundException {

        byte [] data = Base64.decode(s, Base64.DEFAULT);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    /** Write the object to a Base64 string. */
    protected static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}
