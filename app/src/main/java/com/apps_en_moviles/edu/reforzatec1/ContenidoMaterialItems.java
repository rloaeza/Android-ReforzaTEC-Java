package com.apps_en_moviles.edu.reforzatec1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Random;

/**
 * Created by roberto on 19/09/16.
 */
public class ContenidoMaterialItems extends BaseAdapter {


    private Context mContext;
    private final String[] titulos;
    private final int[] logos;
    private final Random random = new Random();

    private int[] fondos = new int[] {
            R.drawable.ovalo_azul,
            R.drawable.ovalo_magenta,
            R.drawable.ovalo_rojo,
            R.drawable.ovalo_verde,
            R.drawable.ovalo_cyan,
            R.drawable.ovalo_amarillo,
            R.drawable.ovalo_beige
    };

    public ContenidoMaterialItems(Context c, String[] textos, int[] logos) {
        this.mContext = c;
        this.titulos = textos;
        this.logos = logos;
    }

    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        View lv;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            lv = new View(mContext);
            lv = inflater.inflate(R.layout.contenido_material_item, null);

            TextView titulo = (TextView)lv.findViewById(R.id.titulo);

            titulo.setText(titulos[pos]);
            ImageView logo = (ImageView)lv.findViewById(R.id.logo);
            logo.setImageResource(logos[pos]);

        } else {
            lv = (View) convertView;
        }

        return lv;
    }
}
