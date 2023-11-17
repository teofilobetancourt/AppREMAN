package com.appreman.app.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grupo")
public class Grupo {

    @PrimaryKey
    @ColumnInfo(name = "numero")
    private Integer mNumero;
    @ColumnInfo(name = "nombre")
    private String mNombre;

    public Grupo(Integer numero, String nombre) {
        this.mNumero = numero;
        this.mNombre = nombre;
    }

    public Grupo() {

    }



    public String getNombre() {
        return mNombre;
    }

    @NonNull
    public Integer getNumero() {
        return mNumero;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

    public void setNumero(int numero) {
        mNumero = numero;
    }
}
