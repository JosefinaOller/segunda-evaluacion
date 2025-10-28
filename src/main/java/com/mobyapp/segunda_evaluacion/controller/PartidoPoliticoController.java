package com.mobyapp.segunda_evaluacion.controller;

import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import com.mobyapp.segunda_evaluacion.service.IPartidoPoliticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/partidos")
public class PartidoPoliticoController {

    private final IPartidoPoliticoService service;

    @Autowired
    public PartidoPoliticoController(IPartidoPoliticoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartidoPolitico createPartidoPolitico (@RequestBody PartidoPolitico partidoPolitico) {
        return service.savePartidoPolitico(partidoPolitico);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PartidoPolitico> findAllPartidoPolitico() {
        return service.getPartidosPoliticos();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PartidoPolitico findPartidoPoliticoById(@PathVariable Long id) {
        return service.findPartidoPoliticoById(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePartidoPolitico (@PathVariable Long id) {
        service.deletePartidoPolitico(id);
    }

}
