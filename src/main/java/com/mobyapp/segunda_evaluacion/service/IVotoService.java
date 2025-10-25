package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.model.Voto;

public interface IVotoService {

    public Voto registerVoto(Voto voto);
    public int countVotosByCandidato(Long idCandidato);
    public int countVotosByPartidoPolitico(Long idPartidoPolitico);
}
