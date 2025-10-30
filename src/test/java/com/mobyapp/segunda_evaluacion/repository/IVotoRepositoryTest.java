package com.mobyapp.segunda_evaluacion.repository;

import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.model.Voto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DisplayName("Pruebas de la capa de Repository para Voto")
class IVotoRepositoryTest {
    @Autowired
    private IVotoRepository votoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Candidato candidatoMessi;
    private Candidato candidatoDiMaria;
    private PartidoPolitico partidoPL;
    private PartidoPolitico partidoPD;

    @BeforeEach
    void setUp() {
        partidoPL = new PartidoPolitico(null, "Partido de Libertad", "PL");
        partidoPD = new PartidoPolitico(null, "Partido del Duelo", "PD");

        entityManager.persist(partidoPL);
        entityManager.persist(partidoPD);

        candidatoMessi = new Candidato(null, "Lionel Messi", partidoPL);
        candidatoDiMaria = new Candidato(null, "Angel Di Maria", partidoPD);

        entityManager.persist(candidatoMessi);
        entityManager.persistAndFlush(candidatoDiMaria);

        votoRepository.save(new Voto(null, candidatoMessi, LocalDateTime.now()));
        votoRepository.save(new Voto(null, candidatoMessi, LocalDateTime.now()));
        votoRepository.save(new Voto(null, candidatoDiMaria, LocalDateTime.now()));
    }

    @Test
    @DisplayName("Debe guardar un Voto y registrar la fecha de emisión y el Candidato")
    void saveVoto() {

        LocalDateTime fechaEmision = LocalDateTime.now();
        Voto newVoto = new Voto(null, candidatoMessi, fechaEmision);

        Voto savedVoto = votoRepository.save(newVoto);

        assertNotNull(savedVoto.getId(), "El ID debe ser generado por JPA.");
        assertEquals(fechaEmision, savedVoto.getFechaEmision(), "La fecha de emisión debe coincidir.");
        assertEquals(candidatoMessi.getId(), savedVoto.getCandidato().getId(), "El ID del candidato en el voto guardado debe coincidir.");
    }

    @Test
    @DisplayName("Debe encontrar un Voto por ID y cargar su Candidato")
    void findVotoById() {

        LocalDateTime fechaEmision = LocalDateTime.now();
        Voto persistedVoto = new Voto(null, candidatoMessi, fechaEmision);
        entityManager.persistAndFlush(persistedVoto);
        Long idFound = persistedVoto.getId();

        Optional<Voto> result = votoRepository.findById(idFound);

        assertTrue(result.isPresent(), "Se debe encontrar el voto.");
        assertEquals(fechaEmision, result.get().getFechaEmision());
        assertNotNull(result.get().getCandidato(), "El Candidato no debe ser nulo.");
        assertEquals("Lionel Messi", result.get().getCandidato().getNombreCompleto());
    }

    @Test
    @DisplayName("Debe retornar vacio si el ID no existe")
    void returnEmptyWhenIdNotFound() {

        Long idNotFound = 99L;

        Optional<Voto> result = votoRepository.findById(idNotFound);

        assertTrue(result.isEmpty(), "Debe retornar vacio para un ID que no existe.");
    }

    @Test
    @DisplayName("Debe eliminar un Voto por ID y confirmar su ausencia")
    void deleteVotoById() {

        Voto votoToDelete = new Voto(null, candidatoMessi, LocalDateTime.now());
        entityManager.persistAndFlush(votoToDelete);
        Long idToDelete = votoToDelete.getId();

        votoRepository.deleteById(idToDelete);

        Optional<Voto> result = votoRepository.findById(idToDelete);
        assertTrue(result.isEmpty(), "El voto debe haber sido eliminado.");
    }

    @Test
    @DisplayName("Debe contar el número de votos para un candidato específico")
    void countVotosByCandidato() {
        int votosMessi = votoRepository.countVotosByCandidatoId(candidatoMessi.getId());
        int votosDiMaria = votoRepository.countVotosByCandidatoId(candidatoDiMaria.getId());

        assertEquals(2, votosMessi, "Messi debe tener 2 votos.");
        assertEquals(1, votosDiMaria, "Di Maria debe tener 1 voto.");
    }

    @Test
    @DisplayName("Debe contar el número de votos para un partido específico")
    void countVotosByPartido() {
        int votosPL = votoRepository.countVotosByPartidoId(partidoPL.getId());
        int votosPD = votoRepository.countVotosByPartidoId(partidoPD.getId());

        assertEquals(2, votosPL, "Partido de Libertad debe tener 2 votos.");
        assertEquals(1, votosPD, "Partido del Duelo debe tener 1 voto.");
    }
}

