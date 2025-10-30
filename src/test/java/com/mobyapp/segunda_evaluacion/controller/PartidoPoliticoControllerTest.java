package com.mobyapp.segunda_evaluacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobyapp.segunda_evaluacion.dto.PartidoPoliticoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
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

    private PartidoPolitico partidoEntity;
    private PartidoPoliticoDTO partidoDTO;

    private final Long idExisted = 1L;
    private final Long idNotExisted = 99L;

    @BeforeEach
    void setUp() {
        partidoDTO = new PartidoPoliticoDTO("Unidos por Messi", "UM");
        partidoEntity = new PartidoPolitico(idExisted, "Unidos por Messi", "UM");
    }

    @Test
    @DisplayName("POST - Debe crear un partido politico y retornar el DTO creado con 201 Created")
    void postPartidoPolitico_SavesSuccessfully_Returns201Created() throws Exception {
        given(service.savePartidoPolitico(any(PartidoPolitico.class))).willReturn(partidoDTO);

        ResultActions response = mockMvc.perform(post("/api/partidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partidoEntity)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(partidoDTO.getNombre())))
                .andExpect(jsonPath("$.sigla", is(partidoDTO.getSigla())));

        verify(service, times(1)).savePartidoPolitico(any(PartidoPolitico.class));
    }

    @Test
    @DisplayName("POST - Debe retornar 409 Conflict si el partido politico ya existe (RecursoDuplicadoException)")
    void postPartidoPolitico_DuplicateResource_Returns409Conflict() throws Exception {
        given(service.savePartidoPolitico(any(PartidoPolitico.class)))
                .willThrow(new RecursoDuplicadoException("El Partido Politico ya existe"));

        mockMvc.perform(post("/api/partidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partidoEntity)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("GET - Debe retornar una lista de partidos politicos con 200 OK")
    void getPartidosPoliticos_ReturnsListOfPartidos_Returns200OK() throws Exception {
        PartidoPoliticoDTO partidoDTO2 = new PartidoPoliticoDTO("Unidos por la Argentina", "UPLA");
        List<PartidoPoliticoDTO> partidos = Arrays.asList(partidoDTO, partidoDTO2);

        given(service.getPartidosPoliticos()).willReturn(partidos);

        ResultActions response = mockMvc.perform(get("/api/partidos"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(partidos.size())))
                .andExpect(jsonPath("$[0].nombre", is(partidoDTO.getNombre())))
                .andExpect(jsonPath("$[1].nombre", is(partidoDTO2.getNombre())));
    }

    @Test
    @DisplayName("GET - Debe buscar el partido por ID y retornar el DTO con 200 OK")
    void getPartidoPoliticoById_ExistingId_Returns200OK() throws Exception {
        given(service.findPartidoPoliticoById(idExisted)).willReturn(partidoDTO);

        ResultActions response = mockMvc.perform(get("/api/partidos/{id}", idExisted));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(partidoDTO.getNombre())));
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
    @DisplayName("DELETE - Debe eliminar el partido por ID y retornar 204 No Content")
    void deletePartidoPoliticoById_DeletesSuccessfully_Returns204NoContent() throws Exception {
        willDoNothing().given(service).deletePartidoPolitico(idExisted);

        ResultActions response = mockMvc.perform(delete("/api/partidos/{id}", idExisted));

        response.andDo(print())
                .andExpect(status().isNoContent());

        verify(service, times(1)).deletePartidoPolitico(idExisted);
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
