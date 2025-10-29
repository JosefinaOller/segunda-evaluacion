package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import java.util.List;

public interface IPartidoPoliticoService {

    PartidoPolitico savePartidoPolitico(PartidoPolitico partido) throws RecursoDuplicadoException;
    PartidoPolitico findPartidoPoliticoById(Long id) throws RecursoNoEncontradoException;
    List<PartidoPolitico> getPartidosPoliticos();
    void deletePartidoPolitico(Long id) throws RecursoNoEncontradoException;
}
