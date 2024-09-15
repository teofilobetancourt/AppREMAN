package com.appreman.app.Models;

<<<<<<< HEAD
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

=======
public class Pregunta {

    private String numero;
    private String descripcion;
    private String elemento;


>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
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
<<<<<<< HEAD
=======


>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
}
