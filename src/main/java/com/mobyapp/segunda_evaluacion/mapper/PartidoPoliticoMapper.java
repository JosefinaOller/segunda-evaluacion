package com.mobyapp.segunda_evaluacion.mapper;

import com.mobyapp.segunda_evaluacion.dto.PartidoPoliticoDTO;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import org.springframework.stereotype.Component;

@Component
public class PartidoPoliticoMapper {

    public PartidoPoliticoDTO toDTO(PartidoPolitico partido) {
        return new PartidoPoliticoDTO(partido.getNombre(), partido.getSigla());
    }
}
