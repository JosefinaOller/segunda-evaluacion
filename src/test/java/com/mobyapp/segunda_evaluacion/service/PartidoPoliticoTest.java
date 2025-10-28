package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.repository.IPartidoPoliticoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de la capa Service para Partido Politico")
class PartidoPoliticoTest {

    @InjectMocks
    private PartidoPoliticoService  service;

    @Mock
    private IPartidoPoliticoRepository repository;

    private PartidoPolitico existingPartidoPolitico;
    private final Long ID_EXISTED = 1L;
    private final Long ID_NOT_EXISTED = 99L;

    @BeforeEach
    void setUp() {
        existingPartidoPolitico = new PartidoPolitico(ID_EXISTED, "Partido de Messi","PM");
    }

    @Test
    @DisplayName("Debe guardar un Partido Politico usando el Repositorio")
    void savePartidoPolitico_SavesSuccessfully(){
        PartidoPolitico newPartidoPolitico = new PartidoPolitico(null, "Partido nuevo","PN");

        when(repository.save(newPartidoPolitico)).thenReturn(existingPartidoPolitico);

        PartidoPolitico result = service.savePartidoPolitico(newPartidoPolitico);

        assertNotNull(result, "El partido politico no debe ser nulo.");
        assertEquals(ID_EXISTED, result.getId(), "El ID debe coincidir.");

        verify(repository, times(1)).save(newPartidoPolitico);
    }

    @Test
    @DisplayName("Debe retornar un Partido Politico existente por ID")
    void findPartidoPoliticoById_ExistingId_ReturnsPartidoPolitico() throws RecursoNoEncontradoException {
        when(repository.findById(ID_EXISTED)).thenReturn(Optional.of(existingPartidoPolitico));

        PartidoPolitico result = service.findPartidoPoliticoById(ID_EXISTED);

        assertNotNull(result, "El Partido Politico no debe ser nulo.");
        assertEquals("Partido de Messi", result.getNombre());

        verify(repository, times(1)).findById(ID_EXISTED);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Partido Politico no existe al buscar")
    void findPartidoPoliticoById_NonExistentId_ThrowsException() throws RecursoNoEncontradoException {
        when(repository.findById(ID_NOT_EXISTED)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {service.findPartidoPoliticoById(ID_NOT_EXISTED);
        }, "Debe lanzar la excepción cuando el Optional está vacío.");

        verify(repository, times(1)).findById(ID_NOT_EXISTED);
    }

    @Test
    @DisplayName("Debe retornar una lista de Partidos Politicos")
    void getPartidosPoliticos_ReturnsListOfPartidos(){
        PartidoPolitico partidoPolitico2 = new PartidoPolitico(2L, "Partido de Paz", "PP");
        List<PartidoPolitico> listMock = Arrays.asList(existingPartidoPolitico, partidoPolitico2);

        when(repository.findAll()).thenReturn(listMock);

        List<PartidoPolitico> result = service.getPartidosPoliticos();
        assertNotNull(result, "La lista de partidos politicos no debe ser nula.");
        assertEquals(2, result.size(), "La cantidad de elementos de lista debe ser igual a 2");
        assertEquals("Partido de Messi", result.get(0).getNombre());

        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar y eliminar un Partido Politico existente por ID")
    void deletePartidoPolitico_ExistingId_DeletesSuccessfully() throws RecursoNoEncontradoException {
        when(repository.findById(ID_EXISTED)).thenReturn(Optional.of(existingPartidoPolitico));

        service.deletePartidoPolitico(ID_EXISTED);

        verify(repository, times(1)).findById(ID_EXISTED);
        verify(repository, times(1)).deleteById(ID_EXISTED);
        verify(repository, never()).deleteById(ID_NOT_EXISTED);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Partido Politico a eliminar no existe")
    void deletePartidoPolitico_NonExistentId_ThrowsException() {
        when(repository.findById(ID_NOT_EXISTED)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            service.deletePartidoPolitico(ID_NOT_EXISTED);
        }, "Debe lanzar la excepción antes de intentar borrar.");

        verify(repository, never()).deleteById(anyLong());
        verify(repository, times(1)).findById(ID_NOT_EXISTED);
    }

}
