package com.appreman.app.Models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "empresa")
public class Empresa {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nombre;
    public String pais;
    public String region;
    public String sitio;
    public String sector;
    public String planta;
    public String representante;
    public String telefono;
    public String email;

    public String clienteAct;
    public String numeroDePlant;
    public String numeroDePlantIm;

    public Empresa() {
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setSitio(String sitio) {
        this.sitio = sitio;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setClienteAct(String clienteAct ) {
        this.clienteAct = clienteAct;
    }

    public void setNumeroDePlant(String numeroDePlant) {
        this.numeroDePlant = numeroDePlant;
    }

    public void setNumeroDePlantIm(String numeroDePlantIm) {
        this.numeroDePlantIm = numeroDePlantIm;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    public String getRegion() {
        return region;
    }

    public String getSitio() {
        return sitio;
    }

    public String getSector() {
        return sector;
    }

    public String getPlanta() {
        return planta;
    }

    public String getRepresentante() {
        return representante;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getClienteAct() {
        return clienteAct;
    }

    public String getNumeroDePlant() {
        return numeroDePlant;
    }

    public String getNumeroDePlantIm() {
        return numeroDePlantIm;
    }

    public int getId() {
        return id;
    }

}


