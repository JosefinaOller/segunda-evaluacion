package com.mobyapp.segunda_evaluacion.service;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de la capa Service para Partido Politico")
public class PartidoPoliticoTest {

    @InjectMocks
    private PartidoPoliticoService  service;

    @Mock
    private IPartidoPoliticoRepository repository;

    private PartidoPolitico partidoPoliticoMock;
    private final Long idExisted = 1L;
    private final Long idNotExisted = 99L;

    @BeforeEach
    void setUp() {
        partidoPoliticoMock = new PartidoPolitico(idExisted, "Partido de Messi","PM");
    }

    @Test
    @DisplayName("Debe guardar un Partido Politico usando el Repositorio")
    void savePartidoPolitico(){
        PartidoPolitico newPartidoPolitico = new PartidoPolitico(null, "Partido nuevo","PN");

        when(repository.save(newPartidoPolitico)).thenReturn(partidoPoliticoMock);

        PartidoPolitico result = service.savePartidoPolitico(newPartidoPolitico);

        assertNotNull(result, "El partido politico no debe ser nulo.");
        assertEquals(idExisted, result.getId(), "El ID debe coincidir.");

        verify(repository, times(1)).save(newPartidoPolitico);
    }

    @Test
    @DisplayName("Debe retornar un Partido Politico existente por ID")
    void findPartidoPoliticoById(){
        when(repository.findById(idExisted)).thenReturn(Optional.of(partidoPoliticoMock));

        PartidoPolitico result = service.findPartidoPoliticoById(idExisted);

        assertNotNull(result, "El Partido Politico no debe ser nulo.");
        assertEquals("Partido de Messi", result.getNombre());

        verify(repository, times(1)).findById(idExisted);
    }

    @Test
    @DisplayName("Debe retornar null si el Partido Politico no existe")
    void findPartidoPoliticoByIdNull(){
        when(repository.findById(idNotExisted)).thenReturn(Optional.empty());

        PartidoPolitico result = service.findPartidoPoliticoById(idNotExisted);

        assertNull(result, "El partido politico debe ser nulo si el ID no existe.");

        verify(repository, times(1)).findById(idNotExisted);
    }

    @Test
    @DisplayName("Debe retornar una lista de Partidos Politicos")
    void findAllPartidosPoliticos(){
        PartidoPolitico partidoPolitico2 = new PartidoPolitico(2L, "Partido de Paz", "PP");
        List<PartidoPolitico> listMock = Arrays.asList(partidoPoliticoMock, partidoPolitico2);

        when(repository.findAll()).thenReturn(listMock);

        List<PartidoPolitico> result = service.getPartidosPoliticos();
        assertNotNull(result, "La lista de partidos politicos no debe ser nula.");
        assertEquals(2, result.size(), "La cantidad de elementos de lista debe ser igual a 2");
        assertEquals("Partido de Messi", result.get(0).getNombre());

        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar y eliminar un Partido Politico existente por ID")
    void deletePartidoPoliticoById(){
        when(repository.findById(idExisted)).thenReturn(Optional.of(partidoPoliticoMock));

        service.deletePartidoPolitico(idExisted);

        verify(repository, times(1)).findById(idExisted);
        verify(repository, times(1)).deleteById(idExisted);
        //Verifica que nunca se llamó al deleteById con un id que no existe
        verify(repository, never()).deleteById(idNotExisted);
    }

}
