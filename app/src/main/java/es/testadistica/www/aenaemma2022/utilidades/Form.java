package es.testadistica.www.aenaemma2022.utilidades;

import android.app.Activity;

import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;

public abstract class Form {

    Activity activity;
    CuePasajeros cue;
    DBHelper conn;
    int pregunta;

    public Form(Activity surveyAct, int pregunta, DBHelper conn){

        this.activity = surveyAct;
        this.pregunta = pregunta;
        this.conn = conn;
    }

    public abstract int getLayoutId();

    public abstract void initFormView();

    public abstract int onNextPressed(int p);

    public abstract int onPreviousPressed(int actual, int anterior);

    public abstract boolean checkQuestion(int q);
}
