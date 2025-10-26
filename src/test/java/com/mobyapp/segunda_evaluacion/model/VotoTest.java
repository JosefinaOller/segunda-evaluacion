package com.mobyapp.segunda_evaluacion.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la entidad Voto")
public class VotoTest {

    private final PartidoPolitico partidoPoliticoMock = new PartidoPolitico(1L,"Partido de Libertad","PL");
    private final Candidato candidatoMock = new Candidato(1L,"Lionel Messi",partidoPoliticoMock);

    @Test
    @DisplayName("Debe crear la instancia de Voto usando el constructor con los argumentos")
    void createVotoWithAllArgs(){
        //Arrange
        Long idExpected = 1L;
        LocalDateTime fechaEmisionExpected = LocalDateTime.now();
        //Act
        Voto voto = new Voto(idExpected,candidatoMock,fechaEmisionExpected);
        //Assert
        assertNotNull(voto, "Voto no debe ser nulo");
        assertEquals(idExpected, voto.getId(), "El ID debe coincidir.");
        assertEquals(fechaEmisionExpected, voto.getFechaEmision(), "La fecha de emision debe coincidir.");
        assertEquals(candidatoMock, voto.getCandidato(), "El candidato debe coincidir.");
        assertEquals("Lionel Messi", voto.getCandidato().getNombreCompleto(), "Debe acceder al nombre del candidato.");
    }

    @Test
    @DisplayName("Debe crear la instancia de Voto usando el constructor vacío")
    void createVotoWithNoArgs(){
        //Arrange
        Voto voto = new Voto();
        Long idExpected = 2L;
        LocalDateTime fechaEmisionExpected = LocalDateTime.now();
        //Act
        voto.setId(idExpected);
        voto.setFechaEmision(fechaEmisionExpected);
        voto.setCandidato(candidatoMock);
        //Assert
        assertNotNull(voto, "Voto no debe ser nulo");
        assertEquals(idExpected, voto.getId(), "El ID seteado debe coincidir.");
        assertEquals(fechaEmisionExpected, voto.getFechaEmision(), "La fecha de emision seteada debe coincidir.");
        assertEquals(candidatoMock, voto.getCandidato(), "El candidato debe coincidir.");
        assertEquals("Lionel Messi", voto.getCandidato().getNombreCompleto(), "Debe acceder al nombre del candidato.");
    }

    @Test
    @DisplayName("Debe inicializar el ID como nulo usando el constructor NoArgsConstructor")
    void initializeIdAsNull(){
        //Arrange
        Voto voto = new Voto();
        //Assert
        assertNull(voto.getId(), "El ID debe ser nulo al usar el constructor vacio. ");
    }

}
