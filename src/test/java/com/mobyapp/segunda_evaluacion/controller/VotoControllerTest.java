package com.mobyapp.segunda_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.model.Voto;
import com.mobyapp.segunda_evaluacion.service.IVotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;
import java.time.LocalDateTime;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(VotoController.class)
@DisplayName("Pruebas de la capa controller para Voto")
class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IVotoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Voto voto;
    private Candidato candidato;

    private final Long idExisted = 1L;
    private final int countValue = 5;

    @BeforeEach
    void setUp() {

        objectMapper.registerModule(new JavaTimeModule());

        PartidoPolitico partido = new PartidoPolitico(idExisted, "Unión por Messi", "UM");
        candidato = new Candidato(idExisted, "Angel Di Maria", partido);

        voto = new Voto(idExisted, candidato, LocalDateTime.now().withNano(0)); //Para que no falle en la comparacion de fecha
    }

    @Test
    @DisplayName("POST - Debe registrar un voto y retornar 201 CREATED")
    void postVoto_SavesSuccessfully_Returns201Created() throws Exception {

        given(service.registerVoto(any(Voto.class)))
                .willReturn(voto);

        ResultActions response = mockMvc.perform(post("/api/votos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voto)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(voto.getId().intValue())))
                .andExpect(jsonPath("$.candidato.nombreCompleto", is(candidato.getNombreCompleto())))
                .andExpect(jsonPath("$.fechaEmision").exists());

        verify(service, times(1)).registerVoto(any(Voto.class));
    }

    @Test
    @DisplayName("POST - Debe retornar 404 NOT FOUND si el Candidato asociado no existe")
    void postVoto_CandidatoDoesNotExist_Returns404NotFound() throws Exception {

        given(service.registerVoto(any(Voto.class)))
                .willThrow(new RecursoNoEncontradoException("Candidato no encontrado"));

        ResultActions response = mockMvc.perform(post("/api/votos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voto)));

        response.andDo(print())
                .andExpect(status().isNotFound());

        verify(service, times(1)).registerVoto(any(Voto.class));
    }

    @Test
    @DisplayName("GET /candidato/{id} - Debe retornar el conteo y 200 OK")
    void countVotosByCandidatoId_CandidatoExists_Returns200OK() throws Exception {

        given(service.countVotosByCandidatoId(idExisted))
                .willReturn(countValue);

        mockMvc.perform(get("/api/votos/candidato/{id}", idExisted))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(countValue)));

        verify(service, times(1)).countVotosByCandidatoId(idExisted);
    }

    @Test
    @DisplayName("GET /candidato/{id} - Debe retornar 404 NOT FOUND si el Candidato no existe")
    void countVotosByCandidatoId_CandidatoDoesNotExist_Returns404NotFound() throws Exception {

        Long idNotExisted = 99L;
        given(service.countVotosByCandidatoId(idNotExisted))
                .willThrow(new RecursoNoEncontradoException("Candidato no encontrado"));

        mockMvc.perform(get("/api/votos/candidato/{id}", idNotExisted))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(service, times(1)).countVotosByCandidatoId(idNotExisted);
    }

    @Test
    @DisplayName("GET /partido/{id} - Debe retornar el conteo y 200 OK")
    void countVotosByPartidoId_PartidoExists_Returns200OK() throws Exception {

        Long partidoIdExisted = 10L;
        given(service.countVotosByPartidoId(partidoIdExisted))
                .willReturn(countValue);

        mockMvc.perform(get("/api/votos/partido/{id}", partidoIdExisted))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(countValue)));

        verify(service, times(1)).countVotosByPartidoId(partidoIdExisted);
    }

    @Test
    @DisplayName("GET /partido/{id} - Debe retornar 404 NOT FOUND si el Partido no existe")
    void countVotosByPartidoId_PartidoDoesNotExist_Returns404NotFound() throws Exception {

        Long partidoIdNotExisted = 100L;
        given(service.countVotosByPartidoId(partidoIdNotExisted))
                .willThrow(new RecursoNoEncontradoException("Partido Político no encontrado"));

        mockMvc.perform(get("/api/votos/partido/{id}", partidoIdNotExisted))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(service, times(1)).countVotosByPartidoId(partidoIdNotExisted);
    }
}
