package com.appreman.app.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pregunta")
public class Pregunta {

    @PrimaryKey
    @ColumnInfo(name = "numero")
    @NonNull
    private String numero;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "elemento")
    private String elemento;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }
}
