package com.appreman.app.Api.Response;


public class ActualizarAsignarResponse {
    private String mensaje;

    // Constructor
    public ActualizarAsignarResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    // Getter y Setter
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}