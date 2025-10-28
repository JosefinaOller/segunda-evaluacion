package com.mobyapp.segunda_evaluacion.controller;

import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Voto;
import com.mobyapp.segunda_evaluacion.service.IVotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votos")
@Tag(name = "Votos", description = "Operaciones para el registro y conteo de votos.")
public class VotoController {

    private final IVotoService service;

    @Autowired
    public VotoController(IVotoService service) {
        this.service = service;
    }

    @Operation(
            summary = "Registrar un nuevo voto",
            description = "Registra un voto asignado a un candidato. Retorna 404 si el candidato no existe."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voto registrado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Candidato asociado al voto no encontrado."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. cuerpo vacío).")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Voto registerVoto(@RequestBody Voto voto) throws RecursoNoEncontradoException {
        return service.registerVoto(voto);
    }

    @Operation(
            summary = "Contar votos por Candidato",
            description = "Retorna el número total de votos recibidos por un candidato específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo exitoso."),
            @ApiResponse(responseCode = "404", description = "Candidato no encontrado con el ID proporcionado.")
    })
    @GetMapping("/candidato/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int countVotosByCandidatoId (@PathVariable Long id) throws RecursoNoEncontradoException {
        return service.countVotosByCandidatoId(id);
    }


    @Operation(
            summary = "Contar votos por Partido Politico",
            description = "Retorna el número total de votos recibidos por un partido político específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo exitoso."),
            @ApiResponse(responseCode = "404", description = "Partido Politico no encontrado con el ID proporcionado.")
    })
    @GetMapping("/partido/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int countVotosByPartidoId (@PathVariable Long id) throws RecursoNoEncontradoException {
        return service.countVotosByPartidoId(id);
    }

}
