package com.appreman.app.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "asignar")
public class Asignar implements Parcelable {

    @SerializedName("id_empresa")
    private int idEmpresa;

    @SerializedName("id_elemento")
    private String idElemento;

    @SerializedName("id_operador")
    private int idOperador;

    @SerializedName("nombre_empresa")
    private String nombreEmpresa;

    // Constructor vacío
    public Asignar() {}

    // Constructor para Parcelable
    protected Asignar(Parcel in) {
        idEmpresa = in.readInt();
        idElemento = in.readString();
        idOperador = in.readInt();
        nombreEmpresa = in.readString();
    }

    public static final Creator<Asignar> CREATOR = new Creator<Asignar>() {
        @Override
        public Asignar createFromParcel(Parcel in) {
            return new Asignar(in);
        }

        @Override
        public Asignar[] newArray(int size) {
            return new Asignar[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idEmpresa);
        dest.writeString(idElemento);
        dest.writeInt(idOperador);
        dest.writeString(nombreEmpresa);
    }

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