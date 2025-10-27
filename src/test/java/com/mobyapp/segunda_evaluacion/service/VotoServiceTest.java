package com.mobyapp.segunda_evaluacion.service;

import com.mobyapp.segunda_evaluacion.model.Candidato;
import com.mobyapp.segunda_evaluacion.model.Voto;
import com.mobyapp.segunda_evaluacion.repository.IVotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de la capa Service para Voto")
public class VotoServiceTest {

    @InjectMocks
    private VotoService service;
    @Mock
    private IVotoRepository repository;

    private Candidato candidatoMock;

    private Voto newVoto;
    private Voto votoMock;
    private final Long idExisted = 1L;

    @BeforeEach
    public void setUp() {
        candidatoMock = new Candidato(1L,"Lionel Messi",null);

        newVoto = new Voto();
        newVoto.setCandidato(candidatoMock);
        newVoto.setFechaEmision(LocalDateTime.now());

        votoMock = new Voto();
        votoMock.setId(idExisted);
        votoMock.setCandidato(candidatoMock);
        votoMock.setFechaEmision(newVoto.getFechaEmision());
    }

    @Test
    @DisplayName("Debe registrar un Voto y retornarlo con el ID generado")
    void registerVoto() {
        when(repository.save(newVoto)).thenReturn(votoMock);

        Voto result = service.registerVoto(newVoto);

        assertNotNull(result, "El voto no debe ser nulo.");
        assertEquals(idExisted, result.getId(), "El ID del voto debe ser el generado por el repositorio.");
        assertEquals(candidatoMock.getId(), result.getCandidato().getId(), "El Candidato debe ser el mismo.");

        verify(repository, times(1)).save(newVoto);
    }

    //Tests para countVoto
}
