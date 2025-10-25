package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.repository.ICandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoService implements ICandidatoService {

    @Autowired
    private ICandidatoRepository repository;

    @Override
    public Candidato saveCandidato(Candidato candidato) {
        return repository.save(candidato);
    }

    @Override
    public Candidato findCandidatoById(Long id) {
        return repository.findById(id).orElse(null); //Agregar excepciones
    }

    @Override
    public List<Candidato> getCandidatos() {
        return repository.findAll();
    }

    @Override
    public void deleteCandidato(Long id) {
        this.findCandidatoById(id);
        repository.deleteById(id);
    }
}
