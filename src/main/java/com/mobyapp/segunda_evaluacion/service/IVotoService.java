package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.model.Voto;

public interface IVotoService {

    public Voto registerVoto(Voto voto);
    public int countVotosByCandidatoId(Long candidatoId);
    public int countVotosByPartidoId(Long partidoId);
}
