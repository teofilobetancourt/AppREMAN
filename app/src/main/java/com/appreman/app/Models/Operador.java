package com.appreman.app.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "operador")
public class Operador {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String apellido;
    private String cedula;
    private String email;
    private String password;

    public Operador(String nombre, String apellido, String cedula, String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.email = email;
        this.password = password;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
