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
    private int idElemento;

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

    public int getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(int idElemento) {
        this.idElemento = idElemento;
    }
}