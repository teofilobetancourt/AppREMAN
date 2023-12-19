package com.appreman.app.Models;

public class Opcion {
    private String numero;
    private String nombre;
    private String pregunta;
    private boolean seleccionada; // Nuevo atributo para controlar la selección

    public Opcion() {
        this.numero = numero;
        this.nombre = nombre;
        this.pregunta = pregunta;
        this.seleccionada = false; // Por defecto, la opción no está seleccionada al inicio
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
}
