package com.appreman.app.Models;

<<<<<<< HEAD
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "opcion")
public class Opcion {

    @PrimaryKey
    @ColumnInfo(name = "numero")
    @NonNull
    private String numero;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "pregunta")
    private String pregunta;

    @ColumnInfo(name = "seleccionada")
    private boolean seleccionada;

    @ColumnInfo(name = "nombre_opcion")
    private String nombreOpcion;

    @ColumnInfo(name = "respondida")
=======
public class Opcion {
    private String numero;
    private String nombre;
    private String pregunta;
    private boolean seleccionada;
    private String nombreOpcion;
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
    private boolean respondida;

    public Opcion() {
        this.numero = "";
        this.nombre = "";
        this.pregunta = "";
        this.seleccionada = false;
        this.nombreOpcion = "";
        this.respondida = false;
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

    public boolean isRespondida() {
        return respondida;
    }

    public void setRespondida(boolean respondida) {
        this.respondida = respondida;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
