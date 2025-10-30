package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.dto.CandidatoDTO;
import com.mobyapp.segunda_evaluacion.dto.VotoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.mapper.VotoMapper;
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
        @Mock
        private VotoMapper votoMapper;

        private Candidato existingCandidato;
        private Voto newVoto;
        private Voto existingVoto;
        private VotoDTO votoDTO;
        private final Long idExisted = 1L;
        private final Long idNotExisted = 99L;
        private final int countValue = 5;

        @BeforeEach
        void setUp() {
            existingCandidato = new Candidato(1L, "Lionel Messi", null);

            newVoto = new Voto();
            newVoto.setCandidato(existingCandidato);
            newVoto.setFechaEmision(LocalDateTime.now());

            existingVoto = new Voto();
            existingVoto.setId(idExisted);
            existingVoto.setCandidato(existingCandidato);
            existingVoto.setFechaEmision(newVoto.getFechaEmision());

            CandidatoDTO candidatoDTO = new CandidatoDTO();
            candidatoDTO.setNombreCompleto(existingCandidato.getNombreCompleto());

            votoDTO = new VotoDTO();
            votoDTO.setCandidato(candidatoDTO);
            votoDTO.setFechaEmision(newVoto.getFechaEmision());
        }

        @Test
        @DisplayName("Debe registrar un VotoDTO si existe el candidato")
        void registerVoto_CandidatoExists_SavesSuccessfully() throws RecursoNoEncontradoException {
            when(candidatoService.findCandidatoEntityById(idExisted)).thenReturn(existingCandidato);
            when(votoRepository.save(newVoto)).thenReturn(existingVoto);
            when(votoMapper.toDTO(existingVoto)).thenReturn(votoDTO);

            VotoDTO result = votoService.registerVoto(newVoto);

            assertNotNull(result, "El VotoDTO no debe ser nulo");
            assertEquals(votoDTO.getCandidato().getNombreCompleto(), result.getCandidato().getNombreCompleto(),"El nombre de candidato debe coincidir");

            verify(candidatoService, times(1)).findCandidatoEntityById(idExisted);
            verify(votoRepository, times(1)).save(newVoto);
            verify(votoMapper, times(1)).toDTO(existingVoto);
        }

        @Test
        @DisplayName("Debe lanzar RecursoNoEncontradoException si el Candidato no existe al registrar el voto")
        void registerVoto_CandidatoDoesNotExist_ThrowsException() throws RecursoNoEncontradoException {
            Candidato invalidCandidato = new Candidato(idNotExisted, "No Existe", null);
            Voto invalidVoto = new Voto();
            invalidVoto.setCandidato(invalidCandidato);

            when(candidatoService.findCandidatoEntityById(idNotExisted))
                    .thenThrow(new RecursoNoEncontradoException("Candidato no encontrado"));

            assertThrows(RecursoNoEncontradoException.class, () -> votoService.registerVoto(invalidVoto));
            verify(votoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe contar votos para un Candidato existente")
        void countVotosByCandidatoId_CandidatoExists_ReturnsCount() throws RecursoNoEncontradoException {
            when(candidatoService.findCandidatoEntityById(idExisted)).thenReturn(existingCandidato);
            when(votoRepository.countVotosByCandidatoId(idExisted)).thenReturn(countValue);

            int result = votoService.countVotosByCandidatoId(idExisted);

            assertEquals(countValue, result, "La cantidad de votos debe coincidir");

            verify(candidatoService, times(1)).findCandidatoEntityById(idExisted);
            verify(votoRepository, times(1)).countVotosByCandidatoId(idExisted);
        }

        @Test
        @DisplayName("Debe lanzar RecursoNoEncontradoException si el Candidato no existe al contar")
        void countVotosByCandidatoId_CandidatoDoesNotExist_ThrowsException() throws RecursoNoEncontradoException {
            when(candidatoService.findCandidatoEntityById(idNotExisted))
                    .thenThrow(new RecursoNoEncontradoException("Candidato no encontrado"));

            assertThrows(RecursoNoEncontradoException.class, () -> votoService.countVotosByCandidatoId(idNotExisted));
            verify(votoRepository, never()).countVotosByCandidatoId(anyLong());
        }

        @Test
        @DisplayName("Debe contar votos para un Partido Político existente")
        void countVotosByPartidoId_PartidoExists_ReturnsCount() throws RecursoNoEncontradoException {
            Long partidoIdExisted = 10L;
            PartidoPolitico existingPartido = new PartidoPolitico(partidoIdExisted, "Partido de Messi", "PM");

            when(partidoPoliticoService.findPartidoPoliticoEntityById(partidoIdExisted)).thenReturn(existingPartido);
            when(votoRepository.countVotosByPartidoId(partidoIdExisted)).thenReturn(countValue);

            int result = votoService.countVotosByPartidoId(partidoIdExisted);

            assertEquals(countValue, result,"La cantidad de votos debe coincidir");
            verify(partidoPoliticoService, times(1)).findPartidoPoliticoEntityById(partidoIdExisted);
            verify(votoRepository, times(1)).countVotosByPartidoId(partidoIdExisted);
        }

        @Test
        @DisplayName("Debe lanzar RecursoNoEncontradoException si el Partido Político no existe al contar")
        void countVotosByPartidoId_PartidoDoesNotExist_ThrowsException() throws RecursoNoEncontradoException {
            when(partidoPoliticoService.findPartidoPoliticoEntityById(idNotExisted))
                    .thenThrow(new RecursoNoEncontradoException("Partido Político no encontrado"));

            assertThrows(RecursoNoEncontradoException.class, () -> votoService.countVotosByPartidoId(idNotExisted),"La excepción se debe lanzar cuando no existe el partido politico");
            verify(votoRepository, never()).countVotosByPartidoId(anyLong());
        }
}

