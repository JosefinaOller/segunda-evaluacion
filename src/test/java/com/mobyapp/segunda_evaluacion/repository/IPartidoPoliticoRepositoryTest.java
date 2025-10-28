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
    private TestEntityManager entityManager; //Para preparar un objeto válido en BD

    @Test
    @DisplayName("Debe guardar un PartidoPolitico y asignarle un ID")
    void savePartidoPolitico() {
        //Arrange
        PartidoPolitico newPartidoPolitico = new PartidoPolitico(null, "Partido del Sol","PS");
        //Act
        PartidoPolitico savedPartidoPolitico = partidoPoliticoRepository.save(newPartidoPolitico);
        //Assert
        assertNotNull(savedPartidoPolitico,"El objeto guardado no debe ser nulo.");
        assertNotNull(savedPartidoPolitico.getId(),"El ID debe ser generado por JPA.");
        assertEquals("PS",savedPartidoPolitico.getSigla());
    }

    @Test
    @DisplayName("Debe encontrar un PartidoPolitico por ID")
    void findPartidoPoliticoById() {
        //Arrange
        PartidoPolitico persistedPartidoPolitico = entityManager.persistAndFlush(new PartidoPolitico(null, "Partido del Cambio","PC"));
        Long idFound =  persistedPartidoPolitico.getId();
        //Act
        Optional<PartidoPolitico> result = partidoPoliticoRepository.findById(idFound);
        //Assert
        assertTrue(result.isPresent(),"Se debe encontrar un partido con el ID buscado.");
        assertEquals("Partido del Cambio",result.get().getNombre());
    }

    @Test
    @DisplayName("Debe retornar vacio si el ID no existe")
    void returnEmptyWhenIdNotFound() {
        //Arrange
        Long idNotFound = 99L;
        //Act
        Optional<PartidoPolitico> result = partidoPoliticoRepository.findById(idNotFound);
        //Assert
        assertTrue(result.isEmpty(), "Debe retornar vacio para un ID que no existe.");
    }

    @Test
    @DisplayName("Debe eliminar un PartidoPolitico por ID y confirmar su ausencia")
    void deletePartidoPoliticoById() {
        // Arrange
        PartidoPolitico partidoToDelete = new PartidoPolitico(null, "Partido de Sol", "PS");
        entityManager.persistAndFlush(partidoToDelete);
        Long idToDelete = partidoToDelete.getId();
        // Act
        partidoPoliticoRepository.deleteById(idToDelete);
        // Assert
        Optional<PartidoPolitico> result = partidoPoliticoRepository.findById(idToDelete);
        assertTrue(result.isEmpty(), "El partido debe haber sido eliminado.");
    }

}
