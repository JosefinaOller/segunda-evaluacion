package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.repository.ICandidatoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoService implements ICandidatoService {

    private final ICandidatoRepository candidatoRepository;
    private final IPartidoPoliticoService partidoPoliticoService;
    private static final Logger log = LoggerFactory.getLogger(CandidatoService.class);

    @Autowired
    public CandidatoService(ICandidatoRepository candidatoRepository, IPartidoPoliticoService partidoPoliticoService) {
        this.candidatoRepository = candidatoRepository;
        this.partidoPoliticoService = partidoPoliticoService;
    }

    @Override
    public Candidato saveCandidato(Candidato candidato) throws RecursoNoEncontradoException {
        PartidoPolitico partido = partidoPoliticoService.findPartidoPoliticoById(candidato.getPartido().getId());
        candidato.setPartido(partido);
        log.info("Candidato guardado correctamente");
        return candidatoRepository.save(candidato);
    }

    @Override
    public Candidato findCandidatoById(Long id) throws RecursoNoEncontradoException {
        return candidatoRepository.findById(id).orElseThrow(()->{
            log.warn("No se encontró el candidato con ID: {}", id);
            return new RecursoNoEncontradoException("El candidato con ID " + id + " no existe");
        });
    }

    @Override
    public List<Candidato> getCandidatos() {
        return candidatoRepository.findAll();
    }

    @Override
    public void deleteCandidato(Long id) throws RecursoNoEncontradoException {
        this.findCandidatoById(id);
        log.info("Eliminando el candidato con ID: {}", id);
        candidatoRepository.deleteById(id);
    }
}
