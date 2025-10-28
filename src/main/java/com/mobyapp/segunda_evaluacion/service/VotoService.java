package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.Voto;
import com.mobyapp.segunda_evaluacion.repository.ICandidatoRepository;
import com.mobyapp.segunda_evaluacion.repository.IVotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VotoService  implements IVotoService {

    private final IVotoRepository votoRepository;
    private final ICandidatoService candidatoService;
    private final IPartidoPoliticoService partidoPoliticoService;
    private static final Logger log = LoggerFactory.getLogger(VotoService.class);

    @Autowired
    public VotoService(IVotoRepository votoRepository, ICandidatoService candidatoService, IPartidoPoliticoService partidoPoliticoService) {
        this.votoRepository = votoRepository;
        this.candidatoService = candidatoService;
        this.partidoPoliticoService = partidoPoliticoService;
    }

    @Override
    public Voto registerVoto(Voto voto) throws RecursoNoEncontradoException {
        Candidato candidato= candidatoService.findCandidatoById(voto.getCandidato().getId());
        voto.setCandidato(candidato);
        log.info("Voto registrado correctamente para el candidato ID {}",candidato.getId());
        return votoRepository.save(voto);
    }

    @Override
    public int countVotosByCandidatoId(Long candidatoId) throws RecursoNoEncontradoException {
        candidatoService.findCandidatoById(candidatoId);
        log.info("Contando votos para el candidato ID {}", candidatoId);
        return votoRepository.countVotosByCandidatoId(candidatoId);
    }

    @Override
    public int countVotosByPartidoId(Long partidoId) throws RecursoNoEncontradoException {
        partidoPoliticoService.findPartidoPoliticoById(partidoId);
        log.info("Contando votos para el partido ID {}", partidoId);
        return votoRepository.countVotosByPartidoId(partidoId);
    }

}
