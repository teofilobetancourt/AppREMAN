package com.appreman.app.Models;

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
    private boolean respondida;

    @ColumnInfo(name = "comentario")
    private String comentario;  // Nueva columna para el comentario

    public Opcion() {
        this.numero = "";
        this.nombre = "";
        this.pregunta = "";
        this.seleccionada = false;
        this.nombreOpcion = "";
        this.respondida = false;
        this.comentario = "";  // Inicializar el comentario
    }

    public Opcion(String numero, String nombre, String pregunta, boolean seleccionada, boolean respondida, String nombreOpcion, String comentario) {
        this.numero = numero;
        this.nombre = nombre;
        this.pregunta = pregunta;
        this.seleccionada = seleccionada;
        this.respondida = respondida;
        this.nombreOpcion = nombreOpcion;
        this.comentario = comentario;  // Inicializar el comentario
    }

    // Getters y setters
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}