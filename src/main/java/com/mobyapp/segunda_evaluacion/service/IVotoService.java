package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Voto;

public interface IVotoService {

    Voto registerVoto(Voto voto) throws RecursoNoEncontradoException;
    int countVotosByCandidatoId(Long candidatoId) throws RecursoNoEncontradoException;
    int countVotosByPartidoId(Long partidoId) throws RecursoNoEncontradoException;
}
