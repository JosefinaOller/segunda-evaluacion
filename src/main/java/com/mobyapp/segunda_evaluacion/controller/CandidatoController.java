package com.mobyapp.segunda_evaluacion.controller;

import com.mobyapp.segunda_evaluacion.dto.CandidatoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.service.ICandidatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
@Tag(name = "Candidatos", description = "Operaciones CRUD para la gestión de candidatos electorales.")
public class CandidatoController {

    private final ICandidatoService service;

    @Autowired
    public CandidatoController(ICandidatoService service) {
        this.service = service;
    }

    @Operation(
            summary = "Crear un nuevo candidato",
            description = "Registra un nuevo candidato y lo asocia a un partido político existente. Retorna 404 si el partido no existe o 409 si ya existe un candidato con los mismos datos (por ejemplo, DNI)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidato creado exitosamente.",
                    content = @Content(schema = @Schema(implementation = CandidatoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Partido Político no encontrado para el candidato."),
            @ApiResponse(responseCode = "409", description = "Candidato duplicado (por ejemplo, ya existe un DNI registrado)."),
            @ApiResponse(responseCode = "400", description = "Datos de candidato inválidos.")
    })

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CandidatoDTO createCandidato(@Valid @RequestBody Candidato candidato) throws RecursoNoEncontradoException, RecursoDuplicadoException {
        return service.saveCandidato(candidato);
    }

    @Operation(
            summary = "Obtener todos los candidatos",
            description = "Retorna una lista de todos los candidatos registrados en el sistema."
    )
    @ApiResponse(responseCode = "200", description = "Lista de candidatos obtenida exitosamente.",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidatoDTO.class))))

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CandidatoDTO> getCandidatos() {
        return service.getCandidatos();
    }

    @Operation(
            summary = "Buscar candidato por ID",
            description = "Retorna un candidato específico por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato encontrado exitosamente.",
                    content = @Content(schema = @Schema(implementation = CandidatoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Candidato no encontrado (ID inexistente).")
    })

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CandidatoDTO findCandidatoById(@PathVariable Long id) throws RecursoNoEncontradoException {
        return service.findCandidatoById(id);
    }

    @Operation(
            summary = "Eliminar candidato por ID",
            description = "Elimina un candidato del sistema por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Candidato eliminado exitosamente (No Content)."),
            @ApiResponse(responseCode = "404", description = "Candidato no encontrado con el ID proporcionado.")
    })

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCandidatoById(@PathVariable Long id) throws RecursoNoEncontradoException {
        service.deleteCandidato(id);
    }
}
