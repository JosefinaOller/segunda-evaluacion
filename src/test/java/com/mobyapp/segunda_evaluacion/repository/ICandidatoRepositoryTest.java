package com.mobyapp.segunda_evaluacion.repository;

import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DisplayName("Pruebas de la capa de Repository para Candidato")
class ICandidatoRepositoryTest {
    @Autowired
    private ICandidatoRepository candidatoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PartidoPolitico partidoPolitico;

    @BeforeEach
    void setup() {
        partidoPolitico = new PartidoPolitico(null,"Partido de Libertad","PL");
        entityManager.persistAndFlush(partidoPolitico);
    }

    @Test
    @DisplayName("Debe guardar un Candidato y resolver su relación ManyToOne con PartidoPolitico")
    void saveCandidato() {
        Candidato newCandidato = new Candidato(null,"Lionel Messi",partidoPolitico);

        Candidato savedCandidato = candidatoRepository.save(newCandidato);

        assertNotNull(savedCandidato.getId(),"El ID debe ser generado por JPA.");
        assertEquals(partidoPolitico.getId(), savedCandidato.getPartido().getId(), "El ID en el candidato guardado debe coincidir.");
    }

    @Test
    @DisplayName("Debe encontrar un Candidato por ID y cargar su PartidoPolitico")
    void findCandidatoById() {

        Candidato persistedCandidato = new Candidato(null,"Rodrigo de Paul",partidoPolitico);
        entityManager.persistAndFlush(persistedCandidato);
        Long idFound = persistedCandidato.getId();

        Optional<Candidato> result = candidatoRepository.findById(idFound);

        assertTrue(result.isPresent(),"Se debe encontrar el candidato.");
        assertEquals("Rodrigo de Paul",result.get().getNombreCompleto());
        assertNotNull(result.get().getPartido(), "El PartidoPolitico no debe ser nulo.");
        assertEquals("Partido de Libertad",result.get().getPartido().getNombre());
    }

    @Test
    @DisplayName("Debe retornar vacio si el ID no existe")
    void returnEmptyWhenIdNotFound() {

        Long idNotFound = 99L;

        Optional<Candidato> result = candidatoRepository.findById(idNotFound);

        assertTrue(result.isEmpty(), "Debe retornar vacio para un ID que no existe.");
    }

    @Test
    @DisplayName("Debe eliminar un Candidato por ID y confirmar su ausencia")
    void deleteCandidatoById() {

        Candidato candidatoToDelete = new Candidato(null,"Emiliano Martinez", partidoPolitico);
        entityManager.persistAndFlush(candidatoToDelete);
        Long idToDelete = candidatoToDelete.getId();

        candidatoRepository.deleteById(idToDelete);

        Optional<Candidato> result = candidatoRepository.findById(idToDelete);
        assertTrue(result.isEmpty(), "El candidato debe haber sido eliminado.");
    }

    @Test
    @DisplayName("Debe encontrar un Candidato por Nombre Completo y Partido")
    void findByNombreCompletoAndPartido_ExistingCandidato_ReturnsCandidato() {
        Candidato existingCandidato = new Candidato(null,"Ángel Di María", partidoPolitico);
        entityManager.persistAndFlush(existingCandidato);

        Optional<Candidato> result = candidatoRepository.findByNombreCompletoAndPartido("Ángel Di María", partidoPolitico);

        assertTrue(result.isPresent(), "Se debe encontrar el candidato existente.");
        assertEquals(existingCandidato.getId(), result.get().getId(),"El ID debe ser generado por JPA.");
    }

    @Test
    @DisplayName("Debe retornar vacío si el Candidato por Nombre Completo y Partido no existe")
    void findByNombreCompletoAndPartido_NonExistingCandidato_ReturnsEmpty() {
        Optional<Candidato> result = candidatoRepository.findByNombreCompletoAndPartido("Candidato Inexistente", partidoPolitico);

        assertTrue(result.isEmpty(), "Debe retornar vacío si el candidato no existe.");
    }
}
