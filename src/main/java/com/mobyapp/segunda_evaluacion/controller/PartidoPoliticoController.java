package com.mobyapp.segunda_evaluacion.controller;

import com.mobyapp.segunda_evaluacion.dto.PartidoPoliticoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.service.IPartidoPoliticoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/partidos")
@Tag(name = "Partidos Políticos", description = "Gestión de las entidades de partidos politicos.")
public class PartidoPoliticoController {

    private final IPartidoPoliticoService service;

    @Autowired
    public PartidoPoliticoController(IPartidoPoliticoService service) {
        this.service = service;
    }

    @Operation(
            summary = "Registrar un nuevo Partido Político",
            description = "Crea un nuevo partido con un nombre y una sigla únicos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partido creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes o incorrectos).")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SuppressWarnings("java:S1130")
    public PartidoPoliticoDTO createPartidoPolitico (@RequestBody PartidoPolitico partidoPolitico) throws RecursoDuplicadoException {
        // Tuve que ignorar la warning de Sonarqube: Se mantiene 'throws RecursoDuplicadoException' por requisito del compilador
        // (Checked Exception). El error S1130 es un falso positivo, ya que la excepción es manejada
        // globalmente por el @RestControllerAdvice, cumpliendo con la arquitectura REST/Spring.
        return service.savePartidoPolitico(partidoPolitico);
    }

    @Operation(
            summary = "Obtener todos los Partidos Políticos",
            description = "Retorna una lista de todos los partidos políticos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de partidos obtenida exitosamente.")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PartidoPoliticoDTO> findAllPartidoPolitico() {
        return service.getPartidosPoliticos();
    }

    @Operation(
            summary = "Obtener Partido Político por ID",
            description = "Busca un partido por su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partido encontrado."),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado con el ID proporcionado.")
    })
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PartidoPoliticoDTO findPartidoPoliticoById(@PathVariable Long id) throws RecursoNoEncontradoException {
        return service.findPartidoPoliticoById(id);
    }

    @Operation(
            summary = "Eliminar Partido Político por ID",
            description = "Elimina un partido del sistema permanentemente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partido eliminado exitosamente (No Content)."),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado con el ID proporcionado.")
    })
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePartidoPolitico (@PathVariable Long id) throws RecursoNoEncontradoException {
        service.deletePartidoPolitico(id);
    }

}
