package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Candidato;

import java.util.List;

public interface ICandidatoService {

    Candidato saveCandidato(Candidato candidato) throws RecursoNoEncontradoException, RecursoDuplicadoException;
    Candidato findCandidatoById(Long id) throws RecursoNoEncontradoException;
    List<Candidato> getCandidatos();
    void deleteCandidato(Long id) throws RecursoNoEncontradoException;
}
