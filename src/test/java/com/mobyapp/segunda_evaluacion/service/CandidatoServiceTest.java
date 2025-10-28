package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de la capa Service para Candidato")
class CandidatoServiceTest {

    @InjectMocks
    private CandidatoService candidatoService;

    @Mock
    private ICandidatoRepository repository;

    @Mock
    private IPartidoPoliticoService partidoPoliticoService;

    private Candidato existingCandidato;
    private final Long ID_EXISTED = 1L;
    private final Long ID_NOT_EXISTED = 99L;

    @BeforeEach
    void setUp() {
        existingCandidato = new Candidato(ID_EXISTED, "Lionel Messi",null);
    }

    @Test
    @DisplayName("Debe guardar un Candidato si su partido politico existe")
    void saveCandidato_WithExistingParty_SavesSuccessfully() throws RecursoNoEncontradoException {

        PartidoPolitico partidoMock = new PartidoPolitico(1L, "Unión por Messi", "UM");
        existingCandidato.setPartido(partidoMock);

        when(partidoPoliticoService.findPartidoPoliticoById(1L)).thenReturn(partidoMock);
        when(repository.save(any(Candidato.class))).thenReturn(existingCandidato);

        candidatoService.saveCandidato(existingCandidato);

        verify(partidoPoliticoService, times(1)).findPartidoPoliticoById(1L);
        verify(repository, times(1)).save(existingCandidato);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException al guardar si el Partido Político no existe")
    void saveCandidato_PartyDoesNotExist_ThrowsException() throws RecursoNoEncontradoException {
        Long partidoIdInvalid = 99L;
        PartidoPolitico partidoInvalid = new PartidoPolitico(partidoIdInvalid, "No Existe", "NE");
        Candidato candidatoPartidoInvalid = new Candidato(null, "Leo", partidoInvalid);

        when(partidoPoliticoService.findPartidoPoliticoById(partidoIdInvalid))
                .thenThrow(new RecursoNoEncontradoException("El partido con ID 99 no existe"));

        assertThrows(RecursoNoEncontradoException.class, () -> {
            candidatoService.saveCandidato(candidatoPartidoInvalid);
        }, "Debe lanzar RecursoNoEncontradoException cuando el Partido no existe.");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar un Candidato existente por ID")
    void findCandidatoById_ExistingId_ReturnsCandidato() throws RecursoNoEncontradoException {
        when(repository.findById(ID_EXISTED)).thenReturn(Optional.of(existingCandidato));

        Candidato result = candidatoService.findCandidatoById(ID_EXISTED);

        assertNotNull(result, "El candidato no debe ser nulo.");
        assertEquals("Lionel Messi", result.getNombreCompleto(), "El nombre debe coincidir.");

        verify(repository, times(1)).findById(ID_EXISTED);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Candidato no existe")
    void findCandidatoById_NonExistentId_ThrowsException() throws RecursoNoEncontradoException {
        when(repository.findById(ID_NOT_EXISTED)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            candidatoService.findCandidatoById(ID_NOT_EXISTED);
        }, "Debe lanzar la excepción cuando el Optional está vacío.");

        verify(repository, times(1)).findById(ID_NOT_EXISTED);
    }

    @Test
    @DisplayName("Debe retornar una lista de Candidatos")
    void getCandidatos_ReturnsListOfCandidatos(){
        Candidato candidato2 = new Candidato(2L, "Angel Di Maria", null);
        List<Candidato> listMock = Arrays.asList(existingCandidato, candidato2);

        when(repository.findAll()).thenReturn(listMock);

        List<Candidato> result = candidatoService.getCandidatos();
        assertNotNull(result, "La lista de candidatos no debe ser nula.");
        assertEquals(2, result.size(), "La cantidad de elementos de lista debe ser igual a 2");
        assertEquals("Lionel Messi", result.get(0).getNombreCompleto());

        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar y eliminar un Candidato existente por ID")
    void deleteCandidato_ExistingId_DeletesSuccessfully() throws RecursoNoEncontradoException {
        when(repository.findById(ID_EXISTED)).thenReturn(Optional.of(existingCandidato));

        candidatoService.deleteCandidato(ID_EXISTED);

        verify(repository, times(1)).findById(ID_EXISTED);
        verify(repository, times(1)).deleteById(ID_EXISTED);

        verify(repository, never()).deleteById(ID_NOT_EXISTED);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Candidato a eliminar no existe")
    void deleteCandidato_NonExistentId_ThrowsException() throws RecursoNoEncontradoException {
        when(repository.findById(ID_NOT_EXISTED)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            candidatoService.deleteCandidato(ID_NOT_EXISTED);
        }, "Debe lanzar la excepción antes de intentar borrar.");

        verify(repository, never()).deleteById(anyLong());
        verify(repository, times(1)).findById(ID_NOT_EXISTED);
    }

}
