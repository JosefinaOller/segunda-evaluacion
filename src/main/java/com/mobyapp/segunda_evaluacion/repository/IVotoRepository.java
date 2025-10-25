package com.mobyapp.segunda_evaluacion.repository;

import com.mobyapp.segunda_evaluacion.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVotoRepository extends JpaRepository<Voto,Long> {}
