package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.dto.CandidatoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Candidato;

import java.util.List;

public interface ICandidatoService {

    CandidatoDTO saveCandidato(Candidato candidato) throws RecursoNoEncontradoException, RecursoDuplicadoException;
    CandidatoDTO findCandidatoById(Long id) throws RecursoNoEncontradoException;
    Candidato findCandidatoEntityById(Long id) throws RecursoNoEncontradoException;
    List<CandidatoDTO> getCandidatos();
    void deleteCandidato(Long id) throws RecursoNoEncontradoException;
}
