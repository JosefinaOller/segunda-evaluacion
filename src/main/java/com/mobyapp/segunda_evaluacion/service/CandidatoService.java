package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.dto.CandidatoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.mapper.CandidatoMapper;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.repository.ICandidatoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidatoService implements ICandidatoService {

    private final ICandidatoRepository candidatoRepository;
    private final IPartidoPoliticoService partidoPoliticoService;
    private final CandidatoMapper candidatoMapper;
    private static final Logger log = LoggerFactory.getLogger(CandidatoService.class);

    @Autowired
    public CandidatoService(ICandidatoRepository candidatoRepository, IPartidoPoliticoService partidoPoliticoService, CandidatoMapper candidatoMapper) {
        this.candidatoRepository = candidatoRepository;
        this.partidoPoliticoService = partidoPoliticoService;
        this.candidatoMapper = candidatoMapper;
    }

    @Override
    public CandidatoDTO saveCandidato(Candidato candidato) throws RecursoNoEncontradoException, RecursoDuplicadoException {

        assignPartidoToCandidato(candidato);
        validateDuplicity(candidato);

        Candidato newCandidato = candidatoRepository.save(candidato);
        log.info("Candidato guardado correctamente con ID {}", newCandidato.getId());
        return candidatoMapper.toDTO(newCandidato);
    }

    private void assignPartidoToCandidato(Candidato candidato) throws RecursoNoEncontradoException {
        PartidoPolitico partido = partidoPoliticoService.findPartidoPoliticoEntityById(candidato.getPartido().getId());
        candidato.setPartido(partido);
    }

    private void validateDuplicity(Candidato candidato) throws RecursoDuplicadoException {
        Optional<Candidato> existingCandidato = candidatoRepository.findByNombreCompletoAndPartido(candidato.getNombreCompleto(), candidato.getPartido());
        if (existingCandidato.isPresent()) {
            log.warn("Intento de guardar candidato duplicado: {} del Partido Politico: {}", candidato.getNombreCompleto(), candidato.getPartido().getNombre());
            throw new RecursoDuplicadoException("Ya existe un candidato registrado con el nombre " + candidato.getNombreCompleto() + " en el partido " + candidato.getPartido().getNombre());
        }
    }

    @Override
    public CandidatoDTO findCandidatoById(Long id) throws RecursoNoEncontradoException {
        return  candidatoMapper.toDTO(getCandidato(id));
    }

    @Override
    public Candidato findCandidatoEntityById(Long id) throws RecursoNoEncontradoException {
        return getCandidato(id);
    }

    @Override
    public List<CandidatoDTO> getCandidatos() {
        return candidatoRepository.findAll()
                .stream()
                .map(candidatoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCandidato(Long id) throws RecursoNoEncontradoException {
        findCandidatoById(id);
        log.info("Eliminando el candidato con ID: {}", id);
        candidatoRepository.deleteById(id);
    }

    private Candidato getCandidato(Long id) throws RecursoNoEncontradoException {
        return candidatoRepository.findById(id).orElseThrow(()->{
            log.warn("No se encontró el candidato con ID: {}", id);
            return new RecursoNoEncontradoException("El candidato con ID " + id + " no existe");
        });
    }

}
