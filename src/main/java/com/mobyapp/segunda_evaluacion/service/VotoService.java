package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.model.Voto;
import com.mobyapp.segunda_evaluacion.repository.IVotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService  implements IVotoService {

    private final IVotoRepository repository;

    @Autowired
    public VotoService(IVotoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Voto registerVoto(Voto voto) {
        return repository.save(voto);
    }

    @Override
    public int countVotosByCandidatoId(Long candidatoId) {
        return repository.countVotosByCandidatoId(candidatoId);
    }

    @Override
    public int countVotosByPartidoId(Long partidoId) {
        return repository.countVotosByPartidoId(partidoId);
    }

}
