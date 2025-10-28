package com.mobyapp.segunda_evaluacion.controller;

import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.Voto;
import com.mobyapp.segunda_evaluacion.service.IVotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votos")
public class VotoController {

    private final IVotoService service;

    @Autowired
    public VotoController(IVotoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Voto registerVoto(@RequestBody Voto voto) throws RecursoNoEncontradoException {
        return service.registerVoto(voto);
    }

    //Endpoints de countVotos
}
