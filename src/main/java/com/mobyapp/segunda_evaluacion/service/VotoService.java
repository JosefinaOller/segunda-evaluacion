package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.dto.VotoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.mapper.VotoMapper;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.Voto;
import com.mobyapp.segunda_evaluacion.repository.IVotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService  implements IVotoService {

    private final IVotoRepository votoRepository;
    private final ICandidatoService candidatoService;
    private final IPartidoPoliticoService partidoPoliticoService;
    private final VotoMapper votoMapper;
    private static final Logger log = LoggerFactory.getLogger(VotoService.class);

    @Autowired
    public VotoService(IVotoRepository votoRepository, ICandidatoService candidatoService, IPartidoPoliticoService partidoPoliticoService, VotoMapper votoMapper) {
        this.votoRepository = votoRepository;
        this.candidatoService = candidatoService;
        this.partidoPoliticoService = partidoPoliticoService;
        this.votoMapper = votoMapper;
    }

    @Override
    public VotoDTO registerVoto(Voto voto) throws RecursoNoEncontradoException {
        assignCandidatoToVoto(voto);
        log.info("Voto registrado correctamente para el candidato ID {}",voto.getCandidato().getId());
        Voto newVoto = votoRepository.save(voto);
        return votoMapper.toDTO(newVoto);
    }

    private void assignCandidatoToVoto(Voto voto) throws RecursoNoEncontradoException {
        Candidato candidato= candidatoService.findCandidatoEntityById(voto.getCandidato().getId());
        voto.setCandidato(candidato);
    }

    @Override
    public int countVotosByCandidatoId(Long candidatoId) throws RecursoNoEncontradoException {
        candidatoService.findCandidatoEntityById(candidatoId);
        log.info("Contando votos para el candidato ID {}", candidatoId);
        return votoRepository.countVotosByCandidatoId(candidatoId);
    }

    @Override
    public int countVotosByPartidoId(Long partidoId) throws RecursoNoEncontradoException {
        partidoPoliticoService.findPartidoPoliticoEntityById(partidoId);
        log.info("Contando votos para el partido ID {}", partidoId);
        return votoRepository.countVotosByPartidoId(partidoId);
    }

}
