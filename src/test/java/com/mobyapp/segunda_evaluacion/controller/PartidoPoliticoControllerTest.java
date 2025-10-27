package com.mobyapp.segunda_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.service.IPartidoPoliticoService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartidoPoliticoController.class)
@DisplayName("Pruebas de la capa controller para Partido Politico")
class PartidoPoliticoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPartidoPoliticoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private PartidoPolitico partido;

    @BeforeEach
    void setUp() {
        partido = new PartidoPolitico(1L, "Unidos por Messi", "UM");
    }

    @Test
    @DisplayName("POST - Debe crear un partido politico y retornar el mismo creado")
    void createPartidoPolitico() throws Exception {
        given(service.savePartidoPolitico(any(PartidoPolitico.class))).willReturn(partido);

        ResultActions response = mockMvc.perform(post("/api/partidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partido)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(partido.getNombre())))
                .andExpect(jsonPath("$.sigla", is(partido.getSigla())));
    }

    @Test
    @DisplayName("GET - Debe retornar una lista de partidos politicos")
    void getPartidosPoliticos() throws Exception {
        PartidoPolitico partido2 = new PartidoPolitico(2L,"Unidos por la Argentina", "UPLA");
        List<PartidoPolitico> partidos = Arrays.asList(partido, partido2);
        given(service.getPartidosPoliticos()).willReturn(partidos);

        ResultActions response = mockMvc.perform(get("/api/partidos"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(partidos.size())))
                .andExpect(jsonPath("$[0].nombre", is(partido.getNombre())))
                .andExpect(jsonPath("$[1].nombre", is(partido2.getNombre())));
    }

    @Test
    @DisplayName("GET - Debe buscar el candidato por ID y retornar el mismo")
    void findPartidoPoliticoById() throws Exception {
        Long partidoId = 1L;
        given(service.findPartidoPoliticoById(partidoId)).willReturn(partido);

        ResultActions response = mockMvc.perform(get("/api/partidos/{id}", partidoId));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(partido.getNombre())));
    }

    @Test
    @DisplayName("DELETE - Debe eliminar el candidato por ID")
    void deletePartidoPoliticoById() throws Exception {
        Long partidoId = 1L;
        willDoNothing().given(service).deletePartidoPolitico(partidoId);

        ResultActions response = mockMvc.perform(delete("/api/partidos/{id}", partidoId));

        response.andDo(print())
                .andExpect(status().isNoContent());
    }
}
