package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.dto.PartidoPoliticoDTO;
import com.mobyapp.segunda_evaluacion.exception.RecursoDuplicadoException;
import com.mobyapp.segunda_evaluacion.exception.RecursoNoEncontradoException;
import com.mobyapp.segunda_evaluacion.model.PartidoPolitico;
import java.util.List;

public interface IPartidoPoliticoService {

    PartidoPoliticoDTO savePartidoPolitico(PartidoPolitico partido) throws RecursoDuplicadoException;
    PartidoPoliticoDTO findPartidoPoliticoById(Long id) throws RecursoNoEncontradoException;
    PartidoPolitico findPartidoPoliticoEntityById(Long id) throws RecursoNoEncontradoException;
    List<PartidoPoliticoDTO> getPartidosPoliticos();
    void deletePartidoPolitico(Long id) throws RecursoNoEncontradoException;
}
