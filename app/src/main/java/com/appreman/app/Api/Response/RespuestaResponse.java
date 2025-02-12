package com.appreman.app.Api.Response;


import java.util.List;
import com.appreman.app.Models.Respuesta;

public class RespuestaResponse {
    private List<Respuesta> respuestas;

    // Getters y setters
    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}