package es.testadistica.www.aenaemma2022.entidades;

import java.io.Serializable;

public class CuePasajerosListado implements Serializable {

    private int iden;
    private String fecha;
    private String horaInicio;
    private String aeropuerto;
    private String puerta;

    public CuePasajerosListado(int iden, String fecha, String horaInicio, String aeropuerto, String puerta) {
        this.iden = iden;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.aeropuerto = aeropuerto;
        this.puerta = puerta;
    }

    public int getIden() {
        return iden;
    }

    public void setIden(int iden) {
        this.iden = iden;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getAeropuerto() {
        return aeropuerto;
    }

    public void setAeropuerto(String aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }
}
