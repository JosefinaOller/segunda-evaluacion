package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.repository.ICandidatoRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de la capa Service para Candidato")
public class CandidatoServiceTest {

    @InjectMocks //Pruebo candidato
    private CandidatoService service;

    @Mock //Mockeo el repositorio
    private ICandidatoRepository repository;

    private Candidato candidatoMock;
    private final Long idExisted = 1L;
    private final Long idNotExisted = 99L;

    @BeforeEach
    void setUp() {
        candidatoMock = new Candidato(idExisted, "Lionel Messi",null);
    }

    @Test
    @DisplayName("Debe guardar un Candidato usando el Repositorio")
    void saveCandidato(){
        Candidato newCandidato = new Candidato(null, "Lionel Messi",null);
        when(repository.save(newCandidato)).thenReturn(candidatoMock);

        Candidato result = service.saveCandidato(newCandidato);

        assertNotNull(result, "El candidato no debe ser nulo.");
        assertEquals(idExisted, result.getId(), "El ID debe coincidir.");

        verify(repository, times(1)).save(newCandidato);
    }

    @Test
    @DisplayName("Debe retornar un Candidato existente por ID")
    void findCandidatoById(){
        when(repository.findById(idExisted)).thenReturn(Optional.of(candidatoMock));

        Candidato result = service.findCandidatoById(idExisted);

        assertNotNull(result, "El candidato no debe ser nulo.");
        assertEquals("Lionel Messi", result.getNombreCompleto(), "El nombre debe coincidir.");

        verify(repository, times(1)).findById(idExisted);
    }

    @Test
    @DisplayName("Debe retornar null si el Candidato no existe")
    void findCandidatoByIdNull(){
        when(repository.findById(idNotExisted)).thenReturn(Optional.empty());

        Candidato result = service.findCandidatoById(idNotExisted);

        assertNull(result, "El candidato debe ser nulo si el ID no existe.");

        verify(repository, times(1)).findById(idNotExisted);
    }

    @Test
    @DisplayName("Debe retornar una lista de Candidatos")
    void findAllCandidatos(){
        Candidato candidato2 = new Candidato(2L, "Angel Di Maria", null);
        List<Candidato> listMock = Arrays.asList(candidatoMock, candidato2);

        when(repository.findAll()).thenReturn(listMock);

        List<Candidato> result = service.getCandidatos();
        assertNotNull(result, "La lista de candidatos no debe ser nula.");
        assertEquals(2, result.size(), "La cantidad de elementos de lista debe ser igual a 2");
        assertEquals("Lionel Messi", result.get(0).getNombreCompleto());

        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar y eliminar un Candidato existente por ID")
    void deleteCandidatoById(){
        when(repository.findById(idExisted)).thenReturn(Optional.of(candidatoMock));

        service.deleteCandidato(idExisted);

        verify(repository, times(1)).findById(idExisted);
        verify(repository, times(1)).deleteById(idExisted);
        //Verifica que nunca se llamó al deleteById con un id que no existe
        verify(repository, never()).deleteById(idNotExisted);
    }

}
