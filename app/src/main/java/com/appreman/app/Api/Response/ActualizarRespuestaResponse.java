package com.appreman.app.Api.Response;

public class ActualizarRespuestaResponse {
    private String mensaje;

    // Constructor
    public ActualizarRespuestaResponse(String mensaje) {
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