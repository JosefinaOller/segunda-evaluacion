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

    private Candidato candidato;

    @BeforeEach
    void setUp() {
        PartidoPolitico partidoPartido = new PartidoPolitico(null, "Partido de Libertad", "PL");
        entityManager.persist(partidoPartido);
        candidato = new Candidato(null, "Lionel Messi", partidoPartido);
        entityManager.persistAndFlush(candidato);
    }

    @Test
    @DisplayName("Debe guardar un Voto y registrar la fecha de emisión y el Candidato")
    void saveVoto() {

        LocalDateTime fechaEmision = LocalDateTime.now();
        Voto newVoto = new Voto(null, candidato, fechaEmision);

        Voto savedVoto = votoRepository.save(newVoto);

        assertNotNull(savedVoto.getId(), "El ID debe ser generado por JPA.");
        assertEquals(fechaEmision, savedVoto.getFechaEmision(), "La fecha de emisión debe coincidir.");
        assertEquals(candidato.getId(), savedVoto.getCandidato().getId(), "El ID del candidato en el voto guardado debe coincidir.");
    }

    @Test
    @DisplayName("Debe encontrar un Voto por ID y cargar su Candidato")
    void findVotoById() {

        LocalDateTime fechaEmision = LocalDateTime.now();
        Voto persistedVoto = new Voto(null, candidato, fechaEmision);
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

        Voto votoToDelete = new Voto(null, candidato, LocalDateTime.now());
        entityManager.persistAndFlush(votoToDelete);
        Long idToDelete = votoToDelete.getId();

        votoRepository.deleteById(idToDelete);

        Optional<Voto> result = votoRepository.findById(idToDelete);
        assertTrue(result.isEmpty(), "El voto debe haber sido eliminado.");
    }
}

