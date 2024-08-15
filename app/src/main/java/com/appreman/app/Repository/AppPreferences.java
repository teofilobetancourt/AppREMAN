package com.appreman.app.Repository;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    public static final String APP_PREFERENCES = "app_preferences";
    public static final String KEY_NOMBRE_EMPRESA = "nombre_empresa";

    private final SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getNombreEmpresa() {
        return sharedPreferences.getString(KEY_NOMBRE_EMPRESA, "");
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        sharedPreferences.edit().putString(KEY_NOMBRE_EMPRESA, nombreEmpresa).apply();
    }
}
