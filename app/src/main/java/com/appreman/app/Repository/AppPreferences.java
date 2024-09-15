package com.appreman.app.Repository;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    public static final String APP_PREFERENCES = "app_preferences";
    public static final String KEY_NOMBRE_EMPRESA = "nombre_empresa";
<<<<<<< HEAD
    public static final String KEY_ID_EMPRESA = "id_empresa";
=======
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30

    private final SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

<<<<<<< HEAD
    // Obtener el nombre de la empresa
=======
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
    public String getNombreEmpresa() {
        return sharedPreferences.getString(KEY_NOMBRE_EMPRESA, "");
    }

<<<<<<< HEAD
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
=======
    public void setNombreEmpresa(String nombreEmpresa) {
        sharedPreferences.edit().putString(KEY_NOMBRE_EMPRESA, nombreEmpresa).apply();
    }
>>>>>>> a21008206cf1f372d46ed21e6732f650f9060c30
}
