package com.appreman.app.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "elemento")
public class Elemento {

    @PrimaryKey
    @ColumnInfo(name = "numero")
    @NonNull
    private String mNumero;
    @ColumnInfo(name = "nombre")
    private String mNombre;

    @ColumnInfo(name = "grupo")
    private Integer mGrupo;


    public Elemento() {

        mNumero = null;
    }


    @NonNull
    public String getNumero() {
        return mNumero;
    }

    public String getNombre() {
        return mNombre;
    }

    public Integer getGrupo() {
        return mGrupo;
    }

    public void setGrupo(Integer grupo) {
        mGrupo = grupo;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

    public void setNumero(String numero) {
        mNumero = numero;
    }

}
