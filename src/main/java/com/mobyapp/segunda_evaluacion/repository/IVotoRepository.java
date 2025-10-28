package com.mobyapp.segunda_evaluacion.repository;

import com.mobyapp.segunda_evaluacion.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IVotoRepository extends JpaRepository<Voto,Long> {

    @Query("SELECT COUNT(v.id) FROM Voto v WHERE v.candidato.id =:candidatoId")
    int countVotosByCandidatoId(@Param("candidatoId") Long candidatoId);

    @Query("SELECT COUNT(v.id) FROM Voto v WHERE v.candidato.partido.id = :partidoId")
    int countVotosByPartidoId(@Param("partidoId") Long partidoId);

}
