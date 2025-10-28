package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import java.util.List;

public interface IPartidoPoliticoService {

    public PartidoPolitico savePartidoPolitico (PartidoPolitico partido);
    public PartidoPolitico findPartidoPoliticoById (Long id) throws RecursoNoEncontradoException;
    public List<PartidoPolitico> getPartidosPoliticos();
    public void deletePartidoPolitico (Long id) throws RecursoNoEncontradoException;
}
