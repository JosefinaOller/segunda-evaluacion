package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import java.util.List;

public interface ICandidatoService {

    public Candidato saveCandidato(Candidato candidato) throws RecursoNoEncontradoException;
    public Candidato findCandidatoById (Long id) throws RecursoNoEncontradoException;
    public List<Candidato> getCandidatos();
    public void deleteCandidato (Long id) throws RecursoNoEncontradoException;
}
