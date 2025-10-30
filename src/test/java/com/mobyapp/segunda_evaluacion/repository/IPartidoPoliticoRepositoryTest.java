package com.mobyapp.segunda_evaluacion.repository;

import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Pruebas de la capa de Repository para PartidoPolitico")
class IPartidoPoliticoRepositoryTest {

    @Autowired
    private IPartidoPoliticoRepository partidoPoliticoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Debe guardar un PartidoPolitico y asignarle un ID")
    void savePartidoPolitico() {

        PartidoPolitico newPartidoPolitico = new PartidoPolitico(null, "Partido del Sol","PS");

        PartidoPolitico savedPartidoPolitico = partidoPoliticoRepository.save(newPartidoPolitico);

        assertNotNull(savedPartidoPolitico,"El objeto guardado no debe ser nulo.");
        assertNotNull(savedPartidoPolitico.getId(),"El ID debe ser generado por JPA.");
        assertEquals("PS",savedPartidoPolitico.getSigla());
    }

    @Test
    @DisplayName("Debe encontrar un PartidoPolitico por ID")
    void findPartidoPoliticoById() {

        PartidoPolitico persistedPartidoPolitico = entityManager.persistAndFlush(new PartidoPolitico(null, "Partido del Cambio","PC"));
        Long idFound =  persistedPartidoPolitico.getId();

        Optional<PartidoPolitico> result = partidoPoliticoRepository.findById(idFound);

        assertTrue(result.isPresent(),"Se debe encontrar un partido con el ID buscado.");
        assertEquals("Partido del Cambio",result.get().getNombre());
    }

    @Test
    @DisplayName("Debe retornar vacio si el ID no existe")
    void returnEmptyWhenIdNotFound() {

        Long idNotFound = 99L;

        Optional<PartidoPolitico> result = partidoPoliticoRepository.findById(idNotFound);

        assertTrue(result.isEmpty(), "Debe retornar vacio para un ID que no existe.");
    }

    @Test
    @DisplayName("Debe eliminar un PartidoPolitico por ID y confirmar su ausencia")
    void deletePartidoPoliticoById() {

        PartidoPolitico partidoToDelete = new PartidoPolitico(null, "Partido de Sol", "PS");
        entityManager.persistAndFlush(partidoToDelete);
        Long idToDelete = partidoToDelete.getId();

        partidoPoliticoRepository.deleteById(idToDelete);

        Optional<PartidoPolitico> result = partidoPoliticoRepository.findById(idToDelete);
        assertTrue(result.isEmpty(), "El partido debe haber sido eliminado.");
    }

    @Test
    @DisplayName("Debe encontrar un PartidoPolitico por Nombre y Sigla")
    void findByNombreAndSigla_ExistingPartido_ReturnsPartido() {
        PartidoPolitico partidoToPersist = new PartidoPolitico(null, "Partido para la Unidad", "PPU");
        PartidoPolitico persistedPartidoPolitico = entityManager.persistAndFlush(partidoToPersist);

        Optional<PartidoPolitico> result = partidoPoliticoRepository.findByNombreAndSigla("Partido para la Unidad", "PPU");

        assertTrue(result.isPresent(), "Se debe encontrar el partido que coincide con Nombre y Sigla.");
        assertEquals(persistedPartidoPolitico.getId(), result.get().getId(),"El ID debe ser generado por JPA.");
    }

    @Test
    @DisplayName("Debe retornar vacío si no existe un PartidoPolitico con el Nombre y Sigla dados")
    void findByNombreAndSigla_NonExistingPartido_ReturnsEmpty() {
        Optional<PartidoPolitico> result = partidoPoliticoRepository.findByNombreAndSigla("Partido Inexistente", "INEX");

        assertTrue(result.isEmpty(), "Debe retornar vacío si el partido no coincide.");
    }

}
