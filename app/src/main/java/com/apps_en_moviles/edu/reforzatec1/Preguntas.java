package com.apps_en_moviles.edu.reforzatec1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by roberto on 30/05/17.
 */

public class Preguntas implements Serializable {
    public ArrayList<String> preguntas;

    public ArrayList<String> respuestas;
    public ArrayList<Integer> tipos;


    public Preguntas() {

    }
    public Preguntas(ArrayList<String> preguntas, ArrayList<String> respuestas, ArrayList<Integer> tipos) {
        this.preguntas = preguntas;
        this.respuestas = respuestas;
        this.tipos = tipos;
    }


    public ArrayList<String> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(ArrayList<String> preguntas) {
        this.preguntas = preguntas;
    }


    public ArrayList<String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<String> respuestas) {
        this.respuestas = respuestas;
    }



    public ArrayList<Integer> getTipos() {
        return tipos;
    }

    public void setTipos(ArrayList<Integer> tipos) {
        this.tipos = tipos;
    }





}
