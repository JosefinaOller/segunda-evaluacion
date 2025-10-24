package com.mobyapp.segunda_evaluacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreCompleto;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="partido_id")
    private PartidoPolitico partido;

}
