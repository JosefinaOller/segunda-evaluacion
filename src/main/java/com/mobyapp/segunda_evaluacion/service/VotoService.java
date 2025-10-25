package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.model.Voto;
import com.mobyapp.segunda_evaluacion.repository.IVotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService  implements IVotoService {

    @Autowired
    private IVotoRepository repository;

    @Override
    public Voto registerVoto(Voto voto) {
        return repository.save(voto);
    }

    @Override
    public int countVotosByCandidato(Long idCandidato) { //Preguntar si quieren una lista o consulta individual
        return 0;
    }

    @Override
    public int countVotosByPartidoPolitico(Long idPartidoPolitico) {
        return 0;
    }
}
