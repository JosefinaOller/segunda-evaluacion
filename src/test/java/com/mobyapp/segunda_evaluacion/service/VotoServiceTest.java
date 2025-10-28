package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.model.Voto;
import com.mobyapp.segunda_evaluacion.repository.IVotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de la capa Service para Voto")
class VotoServiceTest {

    @InjectMocks
    private VotoService votoService;
    @Mock
    private IVotoRepository votoRepository;
    @Mock
    private ICandidatoService candidatoService;
    @Mock
    private IPartidoPoliticoService partidoPoliticoService;

    private Candidato existingCandidato;

    private Voto newVoto;
    private Voto existingVoto;
    private final Long ID_EXISTED = 1L;
    private final Long ID_NOT_EXISTED = 99L;
    private final Long PARTIDO_ID_EXISTED = 10L;
    private final int COUNT_VALUE = 5;

    @BeforeEach
    void setUp() {
        existingCandidato = new Candidato(1L,"Lionel Messi",null);

        newVoto = new Voto();
        newVoto.setCandidato(existingCandidato);
        newVoto.setFechaEmision(LocalDateTime.now());

        existingVoto = new Voto();
        existingVoto.setId(ID_EXISTED);
        existingVoto.setCandidato(existingCandidato);
        existingVoto.setFechaEmision(newVoto.getFechaEmision());
    }

    @Test
    @DisplayName("Debe registrar un Voto si existe el candidato")
    void registerVoto_CandidatoExists_SavesSuccessfully() throws RecursoNoEncontradoException {
        when(candidatoService.findCandidatoById(ID_EXISTED)).thenReturn(existingCandidato);
        when(votoRepository.save(newVoto)).thenReturn(existingVoto);

        Voto result = votoService.registerVoto(newVoto);

        assertNotNull(result, "El voto no debe ser nulo.");
        assertEquals(ID_EXISTED, result.getId(), "El ID del voto debe ser generado.");

        verify(candidatoService, times(1)).findCandidatoById(ID_EXISTED);
        verify(votoRepository, times(1)).save(newVoto);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Candidato no existe al registrar el voto")
    void registerVoto_CandidatoDoesNotExist_ThrowsException() throws RecursoNoEncontradoException {
        Candidato invalidCandidato = new Candidato(ID_NOT_EXISTED, "No Existe", null);
        Voto invalidVoto = new Voto();
        invalidVoto.setCandidato(invalidCandidato);

        when(candidatoService.findCandidatoById(ID_NOT_EXISTED))
                .thenThrow(new RecursoNoEncontradoException("Candidato no encontrado"));

        assertThrows(RecursoNoEncontradoException.class, () -> {
            votoService.registerVoto(invalidVoto);
        }, "Debe lanzar la excepción cuando el Candidato no existe.");

        verify(votoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe contar votos para un Candidato existente")
    void countVotosByCandidatoId_CandidatoExists_ReturnsCount() throws RecursoNoEncontradoException {
        when(candidatoService.findCandidatoById(ID_EXISTED)).thenReturn(existingCandidato);
        when(votoRepository.countVotosByCandidatoId(ID_EXISTED)).thenReturn(COUNT_VALUE);

        int result = votoService.countVotosByCandidatoId(ID_EXISTED);

        assertEquals(COUNT_VALUE, result, "El conteo de votos debe coincidir.");
        verify(candidatoService, times(1)).findCandidatoById(ID_EXISTED);
        verify(votoRepository, times(1)).countVotosByCandidatoId(ID_EXISTED);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Candidato no existe al contar")
    void countVotosByCandidatoId_CandidatoDoesNotExist_ThrowsException() throws RecursoNoEncontradoException {
        when(candidatoService.findCandidatoById(ID_NOT_EXISTED))
                .thenThrow(new RecursoNoEncontradoException("Candidato no encontrado"));

        assertThrows(RecursoNoEncontradoException.class, () -> {
            votoService.countVotosByCandidatoId(ID_NOT_EXISTED);
        }, "Debe lanzar la excepción si el Candidato no existe.");

        verify(votoRepository, never()).countVotosByCandidatoId(anyLong());
    }

    @Test
    @DisplayName("Debe contar votos para un Partido Político existente")
    void countVotosByPartidoId_PartidoExists_ReturnsCount() throws RecursoNoEncontradoException {
        PartidoPolitico existingPartido = new PartidoPolitico(PARTIDO_ID_EXISTED, "Partido de Messi", "PM");

        when(partidoPoliticoService.findPartidoPoliticoById(PARTIDO_ID_EXISTED)).thenReturn(existingPartido);
        when(votoRepository.countVotosByPartidoId(PARTIDO_ID_EXISTED)).thenReturn(COUNT_VALUE);

        int result = votoService.countVotosByPartidoId(PARTIDO_ID_EXISTED);

        assertEquals(COUNT_VALUE, result, "El conteo de votos debe coincidir.");
        verify(partidoPoliticoService, times(1)).findPartidoPoliticoById(PARTIDO_ID_EXISTED);
        verify(votoRepository, times(1)).countVotosByPartidoId(PARTIDO_ID_EXISTED);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Partido Político no existe al contar")
    void countVotosByPartidoId_PartidoDoesNotExist_ThrowsException() throws RecursoNoEncontradoException {
        when(partidoPoliticoService.findPartidoPoliticoById(ID_NOT_EXISTED))
                .thenThrow(new RecursoNoEncontradoException("Partido Político no encontrado"));

        assertThrows(RecursoNoEncontradoException.class, () -> {
            votoService.countVotosByPartidoId(ID_NOT_EXISTED);
        }, "Debe lanzar la excepción si el Partido no existe.");

        verify(votoRepository, never()).countVotosByPartidoId(anyLong());
    }

}
