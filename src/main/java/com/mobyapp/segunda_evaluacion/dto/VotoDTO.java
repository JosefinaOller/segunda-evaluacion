package com.mobyapp.segunda_evaluacion.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotoDTO {
    private CandidatoDTO candidato;
    private LocalDateTime fechaEmision;
}
