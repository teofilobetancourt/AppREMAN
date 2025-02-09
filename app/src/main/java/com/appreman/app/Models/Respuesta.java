package com.appreman.app.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "respuesta")
public class Respuesta {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "empresa")
    private String nombreEmpresa;

    @ColumnInfo(name = "pregunta")
    private String pregunta;

    @ColumnInfo(name = "opcion_actual")
    private String opcionActual;

    @ColumnInfo(name = "opcion_potencial")
    private String opcionPotencial;

    @ColumnInfo(name = "elemento")
    private String elemento;

    @ColumnInfo(name = "fecha_respuesta")
    private String fechaRespuesta;

    @ColumnInfo(name = "comentario")
    private String comentario;

    @ColumnInfo(name = "nombre_encuestado")
    private String nombreEncuestado;

    @ColumnInfo(name = "cargo_encuestado")
    private String cargoEncuestado;

    // Constructor sin parámetros requerido por Room
    public Respuesta() {
    }

    // Constructor con parámetros
    public Respuesta(String nombreEmpresa, String pregunta, String opcionActual, String opcionPotencial, String elemento, String fechaRespuesta, String comentario, String nombreEncuestado, String cargoEncuestado) {
        this.nombreEmpresa = nombreEmpresa;
        this.pregunta = pregunta;
        this.opcionActual = opcionActual;
        this.opcionPotencial = opcionPotencial;
        this.elemento = elemento;
        this.fechaRespuesta = fechaRespuesta;
        this.comentario = comentario;
        this.nombreEncuestado = nombreEncuestado;
        this.cargoEncuestado = cargoEncuestado;
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

    public String getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(String fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNombreEncuestado() {
        return nombreEncuestado;
    }

    public void setNombreEncuestado(String nombreEncuestado) {
        this.nombreEncuestado = nombreEncuestado;
    }

    public String getCargoEncuestado() {
        return cargoEncuestado;
    }

    public void setCargoEncuestado(String cargoEncuestado) {
        this.cargoEncuestado = cargoEncuestado;
    }
}