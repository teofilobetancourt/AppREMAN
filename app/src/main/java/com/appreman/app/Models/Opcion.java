package com.appreman.app.Models;

public class Opcion {
    private String numero;
    private String nombre;
    private String pregunta;
    private boolean seleccionada;
    private String nombreOpcion;
    private boolean respondida;

    public Opcion() {
        this.numero = "";
        this.nombre = "";
        this.pregunta = "";
        this.seleccionada = false;
        this.nombreOpcion = "";
        this.respondida = false;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public boolean isSeleccionada() {
        return seleccionada;
    }

    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
    }

    public String getNombreOpcion() {
        return nombreOpcion;
    }

    public void setNombreOpcion(String nombreOpcion) {
        this.nombreOpcion = nombreOpcion;
    }

    public boolean isRespondida() {
        return respondida;
    }

    public void setRespondida(boolean respondida) {
        this.respondida = respondida;
    }
}
