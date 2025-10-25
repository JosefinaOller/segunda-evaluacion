package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.model.Candidato;
import java.util.List;

public interface ICandidatoService {

    public Candidato saveCandidato(Candidato candidato);
    public Candidato findCandidatoById (Long id);
    public List<Candidato> getCandidatos();
    public void deleteCandidato (Long id);
}
