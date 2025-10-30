package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.dto.PartidoPoliticoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.mapper.PartidoPoliticoMapper;
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
class PartidoPoliticoServiceTest {

    @InjectMocks
    private PartidoPoliticoService  service;

    @Mock
    private IPartidoPoliticoRepository repository;

    @Mock
    private PartidoPoliticoMapper mapper;

    private PartidoPolitico existingPartidoPolitico;
    private final Long idExisted = 1L;
    private final Long idNotExisted = 99L;

    private PartidoPoliticoDTO existingPartidoPoliticoDTO;

    @BeforeEach
    void setUp() {
        existingPartidoPolitico = new PartidoPolitico(idExisted, "Partido de Messi","PM");
        existingPartidoPoliticoDTO = new PartidoPoliticoDTO("Partido de Messi", "PM");
    }

    @Test
    @DisplayName("Debe guardar un Partido Politico y retornar un DTO")
    void savePartidoPolitico_SavesSuccessfully() throws RecursoDuplicadoException {
        PartidoPolitico newPartidoPolitico = new PartidoPolitico(null, "Partido nuevo","PN");
        PartidoPolitico savedPartidoPolitico = new PartidoPolitico(idExisted, "Partido nuevo","PN");
        PartidoPoliticoDTO savedPartidoPoliticoDTO = new PartidoPoliticoDTO("Partido nuevo", "PN");

        when(repository.findByNombreAndSigla(newPartidoPolitico.getNombre(), newPartidoPolitico.getSigla()))
                .thenReturn(Optional.empty());

        when(repository.save(newPartidoPolitico)).thenReturn(savedPartidoPolitico);
        when(mapper.toDTO(savedPartidoPolitico)).thenReturn(savedPartidoPoliticoDTO);

        PartidoPoliticoDTO result = service.savePartidoPolitico(newPartidoPolitico);

        assertNotNull(result, "El DTO del partido politico no debe ser nulo.");
        assertEquals("Partido nuevo", result.getNombre(), "El nombre del DTO debe coincidir.");
        assertEquals("PN", result.getSigla(), "La sigla del DTO debe coincidir.");

        verify(repository, times(1)).findByNombreAndSigla(newPartidoPolitico.getNombre(), newPartidoPolitico.getSigla());
        verify(repository, times(1)).save(newPartidoPolitico);
        verify(mapper, times(1)).toDTO(savedPartidoPolitico);
    }

    @Test
    @DisplayName("Debe lanzar RecursoDuplicadoException al intentar guardar un Partido Politico duplicado")
    void savePartidoPolitico_Duplicate_ThrowsException() {
        PartidoPolitico duplicatePartidoPolitico = new PartidoPolitico(null, "Partido de Messi", "PM");

        when(repository.findByNombreAndSigla(duplicatePartidoPolitico.getNombre(), duplicatePartidoPolitico.getSigla()))
                .thenReturn(Optional.of(existingPartidoPolitico));

        assertThrows(RecursoDuplicadoException.class, () -> {
            service.savePartidoPolitico(duplicatePartidoPolitico);
        }, "Debe lanzar RecursoDuplicadoException cuando el recurso ya existe.");

        verify(repository, times(1)).findByNombreAndSigla(duplicatePartidoPolitico.getNombre(), duplicatePartidoPolitico.getSigla());
        verify(repository, never()).save(any(PartidoPolitico.class));
        verify(mapper, never()).toDTO(any(PartidoPolitico.class));
    }

    @Test
    @DisplayName("Debe retornar un Partido Politico existente por ID y mapearlo a DTO")
    void findPartidoPoliticoById_ExistingId_ReturnsPartidoPoliticoDTO() throws RecursoNoEncontradoException {
        when(repository.findById(idExisted)).thenReturn(Optional.of(existingPartidoPolitico));
        when(mapper.toDTO(existingPartidoPolitico)).thenReturn(existingPartidoPoliticoDTO);

        PartidoPoliticoDTO result = service.findPartidoPoliticoById(idExisted);

        assertNotNull(result, "El Partido Politico DTO no debe ser nulo.");
        assertEquals("Partido de Messi", result.getNombre(), "El partido debe coincidir");

        verify(repository, times(1)).findById(idExisted);
        verify(mapper, times(1)).toDTO(existingPartidoPolitico);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Partido Politico no existe al buscar")
    void findPartidoPoliticoById_NonExistentId_ThrowsException() {
        when(repository.findById(idNotExisted)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {service.findPartidoPoliticoById(idNotExisted);
        }, "Debe lanzar la excepción cuando el Partido politico no existe.");

        verify(repository, times(1)).findById(idNotExisted);
        verify(mapper, never()).toDTO(any(PartidoPolitico.class));
    }

    @Test
    @DisplayName("Debe retornar una lista de Partidos Politicos DTOs")
    void getPartidosPoliticos_ReturnsListOfPartidosDTOs(){
        PartidoPolitico partidoPolitico2 = new PartidoPolitico(2L, "Partido de Paz", "PP");
        PartidoPoliticoDTO partidoPoliticoDTO2 = new PartidoPoliticoDTO("Partido de Paz", "PP");
        List<PartidoPolitico> listMock = Arrays.asList(existingPartidoPolitico, partidoPolitico2);

        when(repository.findAll()).thenReturn(listMock);
        when(mapper.toDTO(existingPartidoPolitico)).thenReturn(existingPartidoPoliticoDTO);
        when(mapper.toDTO(partidoPolitico2)).thenReturn(partidoPoliticoDTO2);

        List<PartidoPoliticoDTO> result = service.getPartidosPoliticos();
        assertNotNull(result, "La lista de partidos politicos DTOs no debe ser nula.");
        assertEquals(2, result.size(), "La cantidad de elementos de lista debe ser igual a 2");
        assertEquals("Partido de Messi", result.get(0).getNombre(),"El partido politico debe coincidir");

        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).toDTO(any(PartidoPolitico.class));
    }

    @Test
    @DisplayName("Debe buscar y eliminar un Partido Politico existente por ID (usa findDTO)")
    void deletePartidoPolitico_ExistingId_DeletesSuccessfully() throws RecursoNoEncontradoException {
        when(repository.findById(idExisted)).thenReturn(Optional.of(existingPartidoPolitico));
        when(mapper.toDTO(existingPartidoPolitico)).thenReturn(existingPartidoPoliticoDTO);

        service.deletePartidoPolitico(idExisted);

        verify(repository, times(1)).findById(idExisted);
        verify(mapper, times(1)).toDTO(existingPartidoPolitico);
        verify(repository, times(1)).deleteById(idExisted);
        verify(repository, never()).deleteById(idNotExisted);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Partido Politico a eliminar no existe")
    void deletePartidoPolitico_NonExistentId_ThrowsException() {
        when(repository.findById(idNotExisted)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            service.deletePartidoPolitico(idNotExisted);
        }, "Debe lanzar la excepción antes de intentar borrar algo que no existe.");

        verify(repository, never()).deleteById(anyLong());
        verify(repository, times(1)).findById(idNotExisted);
        verify(mapper, never()).toDTO(any(PartidoPolitico.class));
    }

}
