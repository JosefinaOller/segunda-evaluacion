package com.mobyapp.segunda_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobyapp.segunda_evaluacion.dto.CandidatoDTO;
import com.mobyapp.segunda_evaluacion.dto.PartidoPoliticoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.service.ICandidatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CandidatoController.class)
@DisplayName("Pruebas de la capa controller para Candidato")
class CandidatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICandidatoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Candidato candidatoEntity;
    private PartidoPolitico partidoEntity;
    private CandidatoDTO candidatoDTO;
    private PartidoPoliticoDTO partidoDTOMock;

    @BeforeEach
    void setUp() {
        partidoEntity = new PartidoPolitico(1L, "Unidos por Messi", "UM");
        candidatoEntity = new Candidato(1L,"Angel di Maria", partidoEntity);
        partidoDTOMock = new PartidoPoliticoDTO("Unidos por Messi", "UM");
        candidatoDTO = new CandidatoDTO("Angel di Maria", partidoDTOMock);
    }

    @Test
    @DisplayName("POST - Debe crear un candidato y retornar el DTO creado con 201 Created")
    void postCandidato_SavesSuccessfully_Returns201Created() throws Exception {
        given(service.saveCandidato(any(Candidato.class))).willReturn(candidatoDTO);

        ResultActions response = mockMvc.perform(post("/api/candidatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(candidatoEntity)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreCompleto", is(candidatoDTO.getNombreCompleto())))
                .andExpect(jsonPath("$.partido.nombre", is(partidoDTOMock.getNombre())));

        verify(service, times(1)).saveCandidato(any(Candidato.class));
    }

    @Test
    @DisplayName("POST - Debe retornar 409 CONFLICT si el Candidato ya existe (duplicado)")
    void postCandidato_IsDuplicate_Returns409Conflict() throws Exception {
        given(service.saveCandidato(any(Candidato.class)))
                .willThrow(new RecursoDuplicadoException("Ya existe un candidato registrado."));

        mockMvc.perform(post("/api/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidatoEntity)))
                .andDo(print())
                .andExpect(status().isConflict());

        verify(service, times(1)).saveCandidato(any(Candidato.class));
    }

    @Test
    @DisplayName("POST - Debe retornar 404 NOT FOUND si el Partido Político en el Candidato no existe")
    void createCandidato_PartyDoesNotExist_Returns404NotFound() throws Exception {
        Candidato candidatoInvalido = new Candidato(null, "Candidato sin Partido", new PartidoPolitico(99L, "Inválido", "INV"));

        given(service.saveCandidato(any(Candidato.class)))
                .willThrow(new RecursoNoEncontradoException("Partido Político con ID 99 no encontrado"));

        mockMvc.perform(post("/api/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidatoInvalido)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(service, times(1)).saveCandidato(any(Candidato.class));
    }

    @Test
    @DisplayName("GET - Debe retornar una lista de DTOs de candidatos con 200 OK")
    void getCandidatos_ReturnsListOfCandidatoDTOs_Returns200OK() throws Exception {
        CandidatoDTO candidatoDTO2 = new CandidatoDTO("Rodrigo de Paul", partidoDTOMock);
        List<CandidatoDTO> candidatosDTOs = Arrays.asList(candidatoDTO, candidatoDTO2);

        given(service.getCandidatos()).willReturn(candidatosDTOs);

        ResultActions response = mockMvc.perform(get("/api/candidatos"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(candidatosDTOs.size())))
                .andExpect(jsonPath("$[0].nombreCompleto", is(candidatoDTO.getNombreCompleto())))
                .andExpect(jsonPath("$[1].nombreCompleto", is(candidatoDTO2.getNombreCompleto())));

        verify(service, times(1)).getCandidatos();
    }

    @Test
    @DisplayName("GET - Debe buscar el candidato por ID y retornar el DTO con 200 OK")
    void getCandidatoById_ExistingId_Returns200OK() throws Exception {
        Long candidatoId = 1L;
        given(service.findCandidatoById(candidatoId)).willReturn(candidatoDTO);

        ResultActions response = mockMvc.perform(get("/api/candidatos/{id}", candidatoId));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto", is(candidatoDTO.getNombreCompleto())));

        verify(service, times(1)).findCandidatoById(candidatoId);
    }

    @Test
    @DisplayName("GET - Debe retornar 404 NOT FOUND si el candidato no existe al buscar por ID")
    void findCandidatoById_NonExistentId_Returns404NotFound() throws Exception {
        Long nonExistentId = 99L;

        given(service.findCandidatoById(nonExistentId))
                .willThrow(new RecursoNoEncontradoException("Candidato con ID " + nonExistentId + " no encontrado"));

        mockMvc.perform(get("/api/candidatos/{id}", nonExistentId))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(service, times(1)).findCandidatoById(nonExistentId);
    }

    @Test
    @DisplayName("DELETE - Debe eliminar el candidato por ID y retornar 204 No Content")
    void deleteCandidatoById_DeletesSuccessfully_Returns204NoContent() throws Exception {
        Long candidatoId = 1L;
        willDoNothing().given(service).deleteCandidato(candidatoId);

        mockMvc.perform(delete("/api/candidatos/{id}", candidatoId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteCandidato(candidatoId);
    }

    @Test
    @DisplayName("DELETE - Debe retornar 404 NOT FOUND si el candidato a eliminar no existe")
    void deleteCandidatoById_NonExistentId_Returns404NotFound() throws Exception {
        Long nonExistentId = 99L;

        willThrow(new RecursoNoEncontradoException("Candidato con ID " + nonExistentId + " no encontrado"))
                .given(service).deleteCandidato(nonExistentId);

        mockMvc.perform(delete("/api/candidatos/{id}", nonExistentId))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(service, times(1)).deleteCandidato(nonExistentId);
    }
}