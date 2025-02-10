package com.appreman.app.Repository;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    public static final String APP_PREFERENCES = "app_preferences";
    public static final String KEY_NOMBRE_EMPRESA = "nombre_empresa";
    public static final String KEY_ID_EMPRESA = "id_empresa";
    public static final String KEY_NOMBRE_ENCUESTADO = "nombre_encuestado";
    public static final String KEY_CARGO_ENCUESTADO = "cargo_encuestado";
    public static final String KEY_ID_OPERADOR = "id_operador"; // Nueva clave para el ID del operador

    private final SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    // Obtener el nombre de la empresa
    public String getNombreEmpresa() {
        return sharedPreferences.getString(KEY_NOMBRE_EMPRESA, "");
    }

    // Guardar el nombre de la empresa
    public void setNombreEmpresa(String nombreEmpresa) {
        sharedPreferences.edit().putString(KEY_NOMBRE_EMPRESA, nombreEmpresa).apply();
    }

    // Obtener el ID de la empresa
    public int getIdEmpresa() {
        return sharedPreferences.getInt(KEY_ID_EMPRESA, -1); // Devuelve -1 si no se ha guardado un ID
    }

    // Guardar el ID de la empresa
    public void setIdEmpresa(int idEmpresa) {
        sharedPreferences.edit().putInt(KEY_ID_EMPRESA, idEmpresa).apply();
    }

    // Obtener el nombre del encuestado
    public String getNombreEncuestado() {
        return sharedPreferences.getString(KEY_NOMBRE_ENCUESTADO, null); // Devuelve null si no está guardado
    }

    // Guardar el nombre del encuestado
    public void setNombreEncuestado(String nombreEncuestado) {
        sharedPreferences.edit().putString(KEY_NOMBRE_ENCUESTADO, nombreEncuestado).apply();
    }

    // Obtener el cargo del encuestado
    public String getCargoEncuestado() {
        return sharedPreferences.getString(KEY_CARGO_ENCUESTADO, null); // Devuelve null si no está guardado
    }

    // Guardar el cargo del encuestado
    public void setCargoEncuestado(String cargoEncuestado) {
        sharedPreferences.edit().putString(KEY_CARGO_ENCUESTADO, cargoEncuestado).apply();
    }

    // Obtener el ID del operador
    public int getIdOperador() {
        return sharedPreferences.getInt(KEY_ID_OPERADOR, -1); // Devuelve -1 si no se ha guardado un ID
    }

    // Guardar el ID del operador
    public void setIdOperador(int idOperador) {
        sharedPreferences.edit().putInt(KEY_ID_OPERADOR, idOperador).apply();
    }
}