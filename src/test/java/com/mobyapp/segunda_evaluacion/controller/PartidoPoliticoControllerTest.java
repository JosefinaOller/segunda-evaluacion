package com.mobyapp.segunda_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
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
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    private final Long idExisted = 1L;
    private final Long idNotExisted = 99L;

    @BeforeEach
    void setUp() {
        partido = new PartidoPolitico(1L, "Unidos por Messi", "UM");
    }

    @Test
    @DisplayName("POST - Debe crear un partido politico y retornar el mismo creado")
    void postPartidoPolitico_SavesSuccessfully_Returns201Created() throws Exception {
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
    void getPartidosPoliticos_ReturnsListOfPartidos_Returns200OK() throws Exception {
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
    void getPartidoPoliticoById_ExistingId_Returns200OK() throws Exception {
        given(service.findPartidoPoliticoById(idExisted)).willReturn(partido);

        ResultActions response = mockMvc.perform(get("/api/partidos/{id}", idExisted));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(partido.getNombre())));
    }

    @Test
    @DisplayName("DELETE - Debe eliminar el candidato por ID")
    void deletePartidoPoliticoById_DeletesSuccessfully_Returns204NoContent() throws Exception {
        willDoNothing().given(service).deletePartidoPolitico(idExisted);

        ResultActions response = mockMvc.perform(delete("/api/partidos/{id}", idExisted));

        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET - Debe retornar 404 NOT FOUND si el partido politico no existe al buscar por ID")
    void getPartidoPoliticoById_NonExistentId_Returns404NotFound() throws Exception {
        given(service.findPartidoPoliticoById(idNotExisted))
                .willThrow(new RecursoNoEncontradoException("Partido con ID " + idNotExisted + " no encontrado"));

        mockMvc.perform(get("/api/partidos/{id}", idNotExisted))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(service, times(1)).findPartidoPoliticoById(idNotExisted);
    }

    @Test
    @DisplayName("DELETE - Debe retornar 404 NOT FOUND si el partido politico a eliminar no existe")
    void deletePartidoPoliticoById_NonExistentId_Returns404NotFound() throws Exception {
        willThrow(new RecursoNoEncontradoException("Partido con ID " + idNotExisted + " no encontrado"))
                .given(service).deletePartidoPolitico(idNotExisted);

        mockMvc.perform(delete("/api/partidos/{id}", idNotExisted))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(service, times(1)).deletePartidoPolitico(idNotExisted);
    }
}
