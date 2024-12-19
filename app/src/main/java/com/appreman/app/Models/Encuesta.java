package com.appreman.app.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "encuesta")
public class Encuesta {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nombreEmpresa;
    public String idOperador;
    public String fechaRespuesta;

    public Encuesta(String nombreEmpresa, String idOperador, String fechaRespuesta) {
        this.nombreEmpresa = nombreEmpresa;
        this.idOperador = idOperador;
        this.fechaRespuesta = fechaRespuesta;
    }

    // Getters y setters
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

    public String getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(String idOperador) {
        this.idOperador = idOperador;
    }

    public String getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(String fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }
}