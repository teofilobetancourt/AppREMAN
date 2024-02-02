package com.appreman.app.Models;

public class Opcion {
    private String numero;
    private String nombre;
    private String pregunta;
    private boolean seleccionada; // Nuevo atributo para controlar la selección
    private String nombreOpcion; // Nuevo atributo para indicar el nombre de la opción
    private String opcionAct; // Nuevo atributo para opcionAct
    private String opcionPot; // Nuevo atributo para opcionPot

    public Opcion() {
        this.numero = "";
        this.nombre = "";
        this.pregunta = "";
        this.seleccionada = false;
        this.nombreOpcion = "";
        this.opcionAct = "";
        this.opcionPot = "";
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

    public String getOpcionAct() {
        return opcionAct;
    }

    public void setOpcionAct(String opcionAct) {
        this.opcionAct = opcionAct;
    }

    public String getOpcionPot() {
        return opcionPot;
    }

    public void setOpcionPot(String opcionPot) {
        this.opcionPot = opcionPot;
    }
}
