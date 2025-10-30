package com.mobyapp.segunda_evaluacion.mapper;

import com.mobyapp.segunda_evaluacion.dto.CandidatoDTO;
import com.mobyapp.segunda_evaluacion.dto.PartidoPoliticoDTO;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CandidatoMapper {

    private final PartidoPoliticoMapper partidoMapper;

    @Autowired
    public CandidatoMapper(PartidoPoliticoMapper partidoMapper) {
        this.partidoMapper = partidoMapper;
    }

    public CandidatoDTO toDTO(Candidato candidato) {
        PartidoPoliticoDTO partidoDTO = partidoMapper.toDTO(candidato.getPartido());
        return new CandidatoDTO(candidato.getNombreCompleto(),partidoDTO);
    }

}
