package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.repository.IPartidoPoliticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidoPoliticoService implements IPartidoPoliticoService {

    private final IPartidoPoliticoRepository repository;

    @Autowired
    public PartidoPoliticoService(IPartidoPoliticoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PartidoPolitico savePartidoPolitico(PartidoPolitico partido) {
        return repository.save(partido);
    }

    @Override
    public PartidoPolitico findPartidoPoliticoById(Long id) {
        return repository.findById(id).orElse(null); //Agregar el manejo de excepciones
    }

    @Override
    public List<PartidoPolitico> getPartidosPoliticos() {
        return repository.findAll();
    }

    @Override
    public void deletePartidoPolitico(Long id) {
        this.findPartidoPoliticoById(id);
        repository.deleteById(id);
    }
}
