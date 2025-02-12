package com.appreman.app.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "asignar")
public class Asignar {

    @ColumnInfo(name = "id_empresa")
    private int idEmpresa;

    @PrimaryKey
    @ColumnInfo(name = "id_operador")
    private int idOperador;

    @ColumnInfo(name = "id_elemento")
    private String idElemento;

    @ColumnInfo(name = "nombre_empresa")
    private String nombreEmpresa;

    // Getters y setters
    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
    }

    public String getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(String idElemento) {
        this.idElemento = idElemento;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }
}