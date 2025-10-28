package com.mobyapp.segunda_evaluacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre completo no puede estar vacío.")
    @Size(min = 2, max = 60, message = "El nombre completo debe tener entre 2 y 60 caracteres.")
    private String nombreCompleto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="partido_id")
    @NotNull(message = "El partido politico no debe ser nulo")
    private PartidoPolitico partido;
}
