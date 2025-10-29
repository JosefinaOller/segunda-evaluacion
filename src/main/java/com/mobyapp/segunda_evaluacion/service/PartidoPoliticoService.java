package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.repository.IPartidoPoliticoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartidoPoliticoService implements IPartidoPoliticoService {

    private final IPartidoPoliticoRepository repository;
    private static final Logger log = LoggerFactory.getLogger(PartidoPoliticoService.class);

    @Autowired
    public PartidoPoliticoService(IPartidoPoliticoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PartidoPolitico savePartidoPolitico(PartidoPolitico partido) throws RecursoDuplicadoException {
        validateDuplicity(partido);
        log.info("Partido Politico guardado correctamente");
        return repository.save(partido);
    }

    private void validateDuplicity(PartidoPolitico partido) throws RecursoDuplicadoException {
        Optional<PartidoPolitico> existingPartido = repository.findByNombreAndSigla(partido.getNombre(), partido.getSigla());
        if (existingPartido.isPresent()) {
            log.warn("Intento de guardar partido politico duplicado: Nombre {} y Sigla {}", partido.getNombre(), partido.getSigla());
            throw new RecursoDuplicadoException("Ya existe un partido político registrado con el Nombre " + partido.getNombre() + " y la Sigla " + partido.getSigla());
        }
    }

    @Override
    public PartidoPolitico findPartidoPoliticoById(Long id) throws RecursoNoEncontradoException {
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró el partido politico con ID: {}", id);
            return new RecursoNoEncontradoException("El partido politico con ID " + id + " no existe");
        });
    }

    @Override
    public List<PartidoPolitico> getPartidosPoliticos() {
        return repository.findAll();
    }

    @Override
    public void deletePartidoPolitico(Long id) throws RecursoNoEncontradoException {
        findPartidoPoliticoById(id);
        log.info("Eliminando partido politico con ID: {}", id);
        repository.deleteById(id);
    }
}
