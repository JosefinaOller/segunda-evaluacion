package com.mobyapp.segunda_evaluacion.controller;

import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.service.ICandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
public class CandidatoController {

    private final ICandidatoService service;

    @Autowired
    public CandidatoController(ICandidatoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Candidato createCandidato(@RequestBody Candidato candidato) {
        return service.saveCandidato(candidato);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Candidato> getCandidatos() {
        return service.getCandidatos();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Candidato findCandidatoById(@PathVariable Long id) {
        return service.findCandidatoById(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCandidatoById(@PathVariable Long id) {
        service.deleteCandidato(id);
    }
}
