package com.appreman.app.Models;

public class Opcion {
    private String numero;
    private String nombre;
    private String pregunta;
    private boolean seleccionada; // Nuevo atributo para controlar la selección
    private int tipo; // Nuevo atributo para indicar el tipo de opción
    private String nombreOpcion; // Nuevo atributo para indicar el nombre de la opción

    public Opcion() {
        this.numero = "";
        this.nombre = "";
        this.pregunta = "";
        this.seleccionada = false;
        this.tipo = 0; // Inicializamos el tipo como 0 (ningún tipo)
        this.nombreOpcion = ""; // Inicializamos el nombre de la opción como cadena vacía
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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getNombreOpcion() {
        return nombreOpcion;
    }

    public void setNombreOpcion(String nombreOpcion) {
        this.nombreOpcion = nombreOpcion;
    }
}
