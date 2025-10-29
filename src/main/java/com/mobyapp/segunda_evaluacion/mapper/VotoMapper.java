package com.mobyapp.segunda_evaluacion.mapper;

import com.mobyapp.segunda_evaluacion.dto.CandidatoDTO;
import com.mobyapp.segunda_evaluacion.dto.VotoDTO;
import com.mobyapp.segunda_evaluacion.model.Voto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VotoMapper {

    private final CandidatoMapper candidatoMapper;

    @Autowired
    public VotoMapper(CandidatoMapper candidatoMapper) {
        this.candidatoMapper = candidatoMapper;
    }

    public VotoDTO toDTO(Voto voto) {
        CandidatoDTO candidatoDTO = candidatoMapper.toDTO(voto.getCandidato());
        return new VotoDTO(candidatoDTO, voto.getFechaEmision());
    }
}
