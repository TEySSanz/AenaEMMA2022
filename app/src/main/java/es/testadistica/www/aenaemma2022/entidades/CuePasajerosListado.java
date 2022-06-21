package es.testadistica.www.aenaemma2022.entidades;

import java.io.Serializable;

public class CuePasajerosListado implements Serializable {

    private int iden;
    private String encuestador;
    private String fecha;
    private String idioma;
    private String vuelo;
    private String puerta;
    private String enviado;

    public CuePasajerosListado(int iden, String encuestador, String fecha, String idioma, String vuelo, String puerta, String enviado) {
        this.iden = iden;
        this.encuestador = encuestador;
        this.fecha = fecha;
        this.idioma = idioma;
        this.vuelo = vuelo;
        this.puerta = puerta;
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

    public String getVuelo() {
        return vuelo;
    }

    public void setVuelo(String vuelo) {
        this.vuelo = vuelo;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public String getEnviado() {
        return enviado;
    }

    public void setEnviado(String enviado) {
        this.enviado = enviado;
    }
}
