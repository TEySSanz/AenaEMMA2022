package es.testadistica.www.aenaemma2022.entidades;

import java.io.Serializable;

public class CueTrabajadoresListado implements Serializable {

    private int iden;
    private String encuestador;
    private String fecha;
    private String idioma;

    public CueTrabajadoresListado(int iden, String encuestador, String fecha, String idioma) {
        this.iden = iden;
        this.encuestador = encuestador;
        this.fecha = fecha;
        this.idioma = idioma;
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
}
