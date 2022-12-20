package es.testadistica.www.aenaemma2022.entidades;

import java.io.Serializable;

public class CueAutobusesListado implements Serializable {

    private int iden;
    private String encuestador;
    private String fecha;
    private String idioma;
    private String enviado;
    private String numdarsena;
    private String numbus;


    public CueAutobusesListado(int iden, String encuestador, String fecha, String idioma, String numdarsena, String numbus, String enviado) {
        this.iden = iden;
        this.encuestador = encuestador;
        this.fecha = fecha;
        this.idioma = idioma;
        this.numdarsena = numdarsena;
        this.numbus = numbus;
        this.enviado = enviado;
    }

    public int getIden() {
        return iden;
    }

    public void setIden(int iden) {
        this.iden = iden;
    }

    public String getEncuestador() {
        return encuestador;
    }

    public void setEncuestador(String encuestador) {
        this.encuestador = encuestador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getNumdarsena() {
        return numdarsena;
    }

    public void setNumdarsena(String numdarsena) {
        this.numdarsena = numdarsena;
    }

    public String getNumbus() {
        return numbus;
    }

    public void setNumbus(String numbus) {
        this.numbus = numbus;
    }

    public String getEnviado() {
        return enviado;
    }

    public void setEnviado(String enviado) {
        this.enviado = enviado;
    }


}
