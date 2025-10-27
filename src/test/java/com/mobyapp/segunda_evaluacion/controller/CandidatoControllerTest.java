package com.mobyapp.segunda_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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

    private Candidato candidato;
    private PartidoPolitico partido;

    @BeforeEach
    void setUp() {
        partido = new PartidoPolitico(1L, "Unidos por Messi", "UM");
        candidato = new Candidato(1L,"Angel di Maria", partido);
    }

    @Test
    @DisplayName("POST - Debe crear un candidato y retornar el mismo creado")
    void createCandidato() throws Exception {
        given(service.saveCandidato(any(Candidato.class))).willReturn(candidato);

        ResultActions response = mockMvc.perform(post("/api/candidatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(candidato)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreCompleto", is(candidato.getNombreCompleto())))
                .andExpect(jsonPath("$.partido.nombre", is(partido.getNombre())));
    }

    @Test
    @DisplayName("GET - Debe retornar una lista de candidatos")
    void getCandidatos() throws Exception {
        Candidato candidato2 = new Candidato(2L, "Rodrigo de Paul", partido);
        List<Candidato> candidatos = Arrays.asList(candidato, candidato2);
        given(service.getCandidatos()).willReturn(candidatos);

        ResultActions response = mockMvc.perform(get("/api/candidatos"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(candidatos.size())))
                .andExpect(jsonPath("$[0].nombreCompleto", is(candidato.getNombreCompleto())))
                .andExpect(jsonPath("$[1].nombreCompleto", is(candidato2.getNombreCompleto())));
    }

    @Test
    @DisplayName("GET - Debe buscar el candidato por ID y retornar el mismo")
    void findCandidatoById() throws Exception {
        Long candidatoId = 1L;
        given(service.findCandidatoById(candidatoId)).willReturn(candidato);

        ResultActions response = mockMvc.perform(get("/api/candidatos/{id}", candidatoId));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto", is(candidato.getNombreCompleto())));
    }

    @Test
    @DisplayName("DELETE - Debe eliminar el candidato por ID")
    void deleteCandidatoById() throws Exception {
        Long candidatoId = 1L;
        willDoNothing().given(service).deleteCandidato(candidatoId);

        ResultActions response = mockMvc.perform(delete("/api/candidatos/{id}", candidatoId));

        response.andDo(print())
                .andExpect(status().isNoContent());
    }
}