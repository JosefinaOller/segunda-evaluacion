package com.mobyapp.segunda_evaluacion.repository;

import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPartidoPoliticoRepository extends JpaRepository<PartidoPolitico,Long> {

    Optional<PartidoPolitico> findByNombreAndSigla(String nombre, String sigla);
}
