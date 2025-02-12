package com.appreman.app.Api.Response;

import java.util.List;
import com.appreman.app.Models.Operador;

public class OperadorResponse {
    private List<Operador> operadores;

    // Getters y setters
    public List<Operador> getOperadores() {
        return operadores;
    }

    public void setOperadores(List<Operador> operadores) {
        this.operadores = operadores;
    }
}