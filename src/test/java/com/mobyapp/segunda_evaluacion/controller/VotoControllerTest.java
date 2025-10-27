package com.mobyapp.segunda_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        PartidoPolitico partido = new PartidoPolitico(1L,"Unión por Messi","UM");
        candidato = new Candidato(1L,"Angel Di Maria",partido);

        voto = new Voto(1L,candidato, LocalDateTime.now().withNano(0)); //en nanos para evitar fallos de comparación
    }

    @Test
    @DisplayName("POST - Debe registrar un voto y retornar el objeto creado con 201 CREATED")
    void registerVoto() throws Exception {
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
    }
}
