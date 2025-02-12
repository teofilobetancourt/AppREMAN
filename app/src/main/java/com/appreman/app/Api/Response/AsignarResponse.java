package com.appreman.app.Api.Response;


import java.util.List;
import com.appreman.app.Models.Asignar;

public class AsignarResponse {
    private List<Asignar> asignaciones;

    // Getters y setters
    public List<Asignar> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<Asignar> asignaciones) {
        this.asignaciones = asignaciones;
    }
}