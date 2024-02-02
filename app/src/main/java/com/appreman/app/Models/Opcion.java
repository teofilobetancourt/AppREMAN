package com.appreman.app.Models;

public class Opcion {
    private String numero;
    private String nombre;
    private String pregunta;
    private boolean seleccionada;
    private String nombreOpcion;
    private String opcionAct;
    private String opcionPot;
    private String tipo; // Nuevo atributo para el tipo de opción

    public Opcion() {
        this.numero = "";
        this.nombre = "";
        this.pregunta = "";
        this.seleccionada = false;
        this.nombreOpcion = "";
        this.opcionAct = "";
        this.opcionPot = "";
        this.tipo = "";
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
