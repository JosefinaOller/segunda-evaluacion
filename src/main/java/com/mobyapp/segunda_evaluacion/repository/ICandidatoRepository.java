package com.mobyapp.segunda_evaluacion.repository;

import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICandidatoRepository extends JpaRepository<Candidato,Long> {

    Optional<Candidato> findByNombreCompletoAndPartido(String nombreCompleto, PartidoPolitico partido);
}
