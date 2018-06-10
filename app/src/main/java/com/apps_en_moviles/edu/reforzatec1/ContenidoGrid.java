package com.apps_en_moviles.edu.reforzatec1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by roberto on 19/09/16.
 */
public class ContenidoGrid extends ArrayAdapter<String> {


    private Context mContext;
    private ArrayList<String> data;

    public ContenidoGrid(Context c, ArrayList<String> data) {
        super(c, R.layout.contenido_item, data);
        this.mContext = c;
        this.data = data;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        grid = inflater.inflate(R.layout.contenido_item, null);

        TextView titulo = (TextView) grid.findViewById(R.id.titulo);
        titulo.setText(data.get(pos));

        TextView abreviatura = (TextView)grid.findViewById(R.id.abreviatura);
        abreviatura.setText(Herramientas.Abreviar(data.get(pos)));
        abreviatura.setBackgroundResource(Herramientas.getColorOvalo(pos));



        return grid;
    }

    public void actualizarGrid(ArrayList<String> models) {
        this.data = models;
        notifyDataSetChanged();
    }
}
