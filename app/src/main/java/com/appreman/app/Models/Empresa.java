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
    public String fechaRegistro;
    public String idOperador; // Nuevo campo

    // Nuevas variables para gestionar la visibilidad de los campos adicionales
    private boolean camposAdicionalesVisible = false;

    public Empresa() {
    }

    public Empresa(String nombre, String pais, String region, String sitio) {
        this.nombre = nombre;
        this.pais = pais;
        this.region = region;
        this.sitio = sitio;
    }

    // Nuevo constructor
    public Empresa(String nombre, String pais, String region, String sitio, String representante, String planta, String idOperador) {
        this.nombre = nombre;
        this.pais = pais;
        this.region = region;
        this.sitio = sitio;
        this.representante = representante;
        this.planta = planta;
        this.idOperador = idOperador;
    }

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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSitio() {
        return sitio;
    }

    public void setSitio(String sitio) {
        this.sitio = sitio;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClienteAct() {
        return clienteAct;
    }

    public void setClienteAct(String clienteAct) {
        this.clienteAct = clienteAct;
    }

    public String getNumeroDePlant() {
        return numeroDePlant;
    }

    public void setNumeroDePlant(String numeroDePlant) {
        this.numeroDePlant = numeroDePlant;
    }

    public String getNumeroDePlantIm() {
        return numeroDePlantIm;
    }

    public void setNumeroDePlantIm(String numeroDePlantIm) {
        this.numeroDePlantIm = numeroDePlantIm;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(String idOperador) {
        this.idOperador = idOperador;
    }

    // Función para cambiar la visibilidad de los campos adicionales
    public void toggleVisibilidadCamposAdicionales() {
        camposAdicionalesVisible = !camposAdicionalesVisible;
    }

    // Función para obtener el estado actual de visibilidad de los campos adicionales
    public boolean areCamposAdicionalesVisible() {
        return camposAdicionalesVisible;
    }
}