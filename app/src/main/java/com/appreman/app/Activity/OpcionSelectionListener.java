package com.appreman.app.Activity;

import com.appreman.app.Models.Opcion;

import java.util.List;

public interface OpcionSelectionListener {
    void onOpcionesSelected(String preguntaNumero, List<Opcion> opcionesSeleccionadas);

    void onOpcionSelected(Opcion opcionActual, Opcion opcionPotencial);

    void onOpcionSelected(String preguntaNumero, List<Opcion> opcionesSeleccionadas);
}
