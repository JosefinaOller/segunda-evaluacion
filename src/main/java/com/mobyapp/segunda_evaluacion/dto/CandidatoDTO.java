package com.mobyapp.segunda_evaluacion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CandidatoDTO {
    private String nombreCompleto;
    private PartidoPoliticoDTO partido;
}
