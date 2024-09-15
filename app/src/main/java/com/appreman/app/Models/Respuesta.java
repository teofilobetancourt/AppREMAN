package com.appreman.app.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "respuestas")
public class Respuesta {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "empresa")
    private String nombreEmpresa;

    @ColumnInfo(name = "pregunta")
    private String pregunta;

    @ColumnInfo(name = "opcionActual")
    private String opcionActual;

    @ColumnInfo(name = "opcionPotencial")
    private String opcionPotencial;

    @ColumnInfo(name = "elemento")
    private String elemento;

    // Constructor sin parámetros requerido por Room
    public Respuesta() {
    }

    // Constructor con parámetros
    public Respuesta(String nombreEmpresa, String pregunta, String opcionActual, String opcionPotencial, String elemento) {
        this.nombreEmpresa = nombreEmpresa;
        this.pregunta = pregunta;
        this.opcionActual = opcionActual;
        this.opcionPotencial = opcionPotencial;
        this.elemento = elemento;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getOpcionActual() {
        return opcionActual;
    }

    public void setOpcionActual(String opcionActual) {
        this.opcionActual = opcionActual;
    }

    public String getOpcionPotencial() {
        return opcionPotencial;
    }

    public void setOpcionPotencial(String opcionPotencial) {
        this.opcionPotencial = opcionPotencial;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }
}
