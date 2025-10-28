package com.mobyapp.segunda_evaluacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="candidato_id")
    @NotNull(message = "El candidato no debe ser nulo")
    private Candidato candidato;

    @NotNull(message = "La fecha no debe ser nula")
    private LocalDateTime fechaEmision;

}
