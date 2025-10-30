package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.dto.CandidatoDTO;
import com.mobyapp.segunda_evaluacion.dto.PartidoPoliticoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.mapper.CandidatoMapper;
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

    @Mock
    private CandidatoMapper candidatoMapper;

    private Candidato existingCandidato;
    private PartidoPolitico existingPartido;
    private final Long idExisted = 1L;
    private final Long idNotExisted = 99L;
    private CandidatoDTO existingCandidatoDTO;

    @BeforeEach
    void setUp() {
        existingPartido = new PartidoPolitico(1L, "Unión por Messi", "UM");
        existingCandidato = new Candidato(idExisted, "Lionel Messi", existingPartido);
        existingCandidatoDTO = new CandidatoDTO("Lionel Messi", new PartidoPoliticoDTO("Unión por Messi", "UM"));
    }

    @Test
    @DisplayName("Debe guardar un Candidato si su partido politico existe y retornar un DTO")
    void saveCandidato_WithExistingParty_SavesSuccessfully() throws RecursoNoEncontradoException, RecursoDuplicadoException {
        Candidato newCandidato = new Candidato(null, "Leo Messi", existingPartido);
        Candidato savedCandidato = new Candidato(idExisted, "Leo Messi", existingPartido);
        CandidatoDTO savedCandidatoDTO = new CandidatoDTO("Leo Messi", new PartidoPoliticoDTO("Unión por Messi", "UM"));

        when(partidoPoliticoService.findPartidoPoliticoEntityById(existingPartido.getId())).thenReturn(existingPartido);
        when(repository.findByNombreCompletoAndPartido(newCandidato.getNombreCompleto(), newCandidato.getPartido()))
                .thenReturn(Optional.empty());
        when(repository.save(any(Candidato.class))).thenReturn(savedCandidato);
        when(candidatoMapper.toDTO(savedCandidato)).thenReturn(savedCandidatoDTO);

        CandidatoDTO result = candidatoService.saveCandidato(newCandidato);

        assertNotNull(result, "El DTO del candidato no debe ser nulo.");
        assertEquals("Leo Messi", result.getNombreCompleto(),"El nombre de candidato debe coincidir");

        verify(partidoPoliticoService, times(1)).findPartidoPoliticoEntityById(existingPartido.getId());
        verify(repository, times(1)).findByNombreCompletoAndPartido(newCandidato.getNombreCompleto(), existingPartido);
        verify(repository, times(1)).save(newCandidato);
        verify(candidatoMapper, times(1)).toDTO(savedCandidato);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException al guardar si el Partido Político no existe")
    void saveCandidato_PartyDoesNotExist_ThrowsException() throws RecursoNoEncontradoException {
        Long partidoIdInvalid = 99L;
        PartidoPolitico partidoInvalid = new PartidoPolitico(partidoIdInvalid, "No Existe", "NE");
        Candidato candidatoPartidoInvalid = new Candidato(null, "Leo", partidoInvalid);

        when(partidoPoliticoService.findPartidoPoliticoEntityById(partidoIdInvalid))
                .thenThrow(new RecursoNoEncontradoException("El partido con ID 99 no existe"));

        assertThrows(RecursoNoEncontradoException.class, () -> {
            candidatoService.saveCandidato(candidatoPartidoInvalid);
        }, "Debe lanzar RecursoNoEncontradoException cuando el Partido no existe.");

        verify(partidoPoliticoService, times(1)).findPartidoPoliticoEntityById(partidoIdInvalid);
        verify(repository, never()).save(any());
        verify(repository, never()).findByNombreCompletoAndPartido(anyString(), any(PartidoPolitico.class));
    }

    @Test
    @DisplayName("Debe lanzar RecursoDuplicadoException si ya existe un Candidato con el mismo nombre en el mismo Partido")
    void saveCandidato_DuplicateCandidato_ThrowsException() throws RecursoNoEncontradoException {
        Candidato duplicateCandidato = new Candidato(null, "Lionel Messi", existingPartido);

        when(partidoPoliticoService.findPartidoPoliticoEntityById(existingPartido.getId())).thenReturn(existingPartido);
        when(repository.findByNombreCompletoAndPartido(duplicateCandidato.getNombreCompleto(), existingPartido))
                .thenReturn(Optional.of(existingCandidato));

        assertThrows(RecursoDuplicadoException.class, () -> {
            candidatoService.saveCandidato(duplicateCandidato);
        }, "Debe lanzar RecursoDuplicadoException cuando el candidato es duplicado.");

        verify(partidoPoliticoService, times(1)).findPartidoPoliticoEntityById(existingPartido.getId());
        verify(repository, times(1)).findByNombreCompletoAndPartido(duplicateCandidato.getNombreCompleto(), existingPartido);
        verify(repository, never()).save(any(Candidato.class));
        verify(candidatoMapper, never()).toDTO(any(Candidato.class));
    }

    @Test
    @DisplayName("Debe retornar un Candidato existente por ID mapeado a DTO")
    void findCandidatoById_ExistingId_ReturnsCandidatoDTO() throws RecursoNoEncontradoException {
        when(repository.findById(idExisted)).thenReturn(Optional.of(existingCandidato));
        when(candidatoMapper.toDTO(existingCandidato)).thenReturn(existingCandidatoDTO);

        CandidatoDTO result = candidatoService.findCandidatoById(idExisted);

        assertNotNull(result, "El candidato DTO no debe ser nulo.");
        assertEquals("Lionel Messi", result.getNombreCompleto(), "El nombre debe coincidir.");

        verify(repository, times(1)).findById(idExisted);
        verify(candidatoMapper, times(1)).toDTO(existingCandidato);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Candidato no existe")
    void findCandidatoById_NonExistentId_ThrowsException() {
        when(repository.findById(idNotExisted)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            candidatoService.findCandidatoById(idNotExisted);
        }, "Debe lanzar la excepción cuando el Optional está vacío.");

        verify(repository, times(1)).findById(idNotExisted);
        verify(candidatoMapper, never()).toDTO(any(Candidato.class));
    }

    @Test
    @DisplayName("Debe retornar una lista de Candidato DTOs")
    void getCandidatos_ReturnsListOfCandidatoDTOs(){
        Candidato candidato2 = new Candidato(2L, "Angel Di Maria", existingPartido);
        CandidatoDTO candidatoDTO2 = new CandidatoDTO("Angel Di Maria", new PartidoPoliticoDTO("Unión por Messi", "UM"));
        List<Candidato> listMock = Arrays.asList(existingCandidato, candidato2);

        when(repository.findAll()).thenReturn(listMock);
        when(candidatoMapper.toDTO(existingCandidato)).thenReturn(existingCandidatoDTO);
        when(candidatoMapper.toDTO(candidato2)).thenReturn(candidatoDTO2);

        List<CandidatoDTO> result = candidatoService.getCandidatos();
        assertNotNull(result, "La lista de candidatos DTOs no debe ser nula.");
        assertEquals(2, result.size(), "La cantidad de elementos de lista debe ser igual a 2");
        assertEquals("Lionel Messi", result.get(0).getNombreCompleto(), "El nombre de candidato debe coincidir");

        verify(repository, times(1)).findAll();
        verify(candidatoMapper, times(2)).toDTO(any(Candidato.class));
    }

    @Test
    @DisplayName("Debe buscar y eliminar un Candidato existente por ID")
    void deleteCandidato_ExistingId_DeletesSuccessfully() throws RecursoNoEncontradoException {
        when(repository.findById(idExisted)).thenReturn(Optional.of(existingCandidato));
        when(candidatoMapper.toDTO(existingCandidato)).thenReturn(existingCandidatoDTO);

        candidatoService.deleteCandidato(idExisted);

        verify(repository, times(1)).findById(idExisted);
        verify(candidatoMapper, times(1)).toDTO(existingCandidato);
        verify(repository, times(1)).deleteById(idExisted);
        verify(repository, never()).deleteById(idNotExisted);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el Candidato a eliminar no existe")
    void deleteCandidato_NonExistentId_ThrowsException() {
        when(repository.findById(idNotExisted)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            candidatoService.deleteCandidato(idNotExisted);
        }, "Debe lanzar la excepción antes de intentar borrar.");

        verify(repository, never()).deleteById(anyLong());
        verify(repository, times(1)).findById(idNotExisted);
        verify(candidatoMapper, never()).toDTO(any(Candidato.class));
    }

}
