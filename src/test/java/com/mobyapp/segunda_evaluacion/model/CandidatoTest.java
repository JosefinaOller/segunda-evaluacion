package com.mobyapp.segunda_evaluacion.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la entidad Candidato")
public class CandidatoTest {

    private final PartidoPolitico partidoPoliticoMock = new PartidoPolitico(1L,"Partido de Libertad", "PL");

    @Test
    @DisplayName("Debe crear la instancia de Candidato usando el constructor con los argumentos")
    void createCandidatoWithAllArgs(){
        //Arrange
        Long idExpected = 1L;
        String nameExpected = "Lionel Messi";
        //Act
        Candidato candidato = new Candidato(idExpected,nameExpected,partidoPoliticoMock);
        //Assert
        assertNotNull(candidato, "Candidato no debe ser nulo");
        assertEquals(idExpected, candidato.getId(), "El ID debe coincidir.");
        assertEquals(nameExpected, candidato.getNombreCompleto(), "El nombre debe coincidir.");
        assertEquals(partidoPoliticoMock, candidato.getPartido(), "El partido debe coincidir.");
        assertEquals("Partido de Libertad", candidato.getPartido().getNombre(), "Debe acceder al nombre del partido.");
    }

    @Test
    @DisplayName("Debe crear la instancia de Candidato usando el constructor vacío")
    void createCandidatoWithNoArgs(){
        //Arrange
        Candidato candidato = new Candidato();
        Long idExpected = 2L;
        String nameExpected = "Angel Di Maria";
        //Act
        candidato.setId(idExpected);
        candidato.setNombreCompleto(nameExpected);
        candidato.setPartido(partidoPoliticoMock);
        //Asset
        assertNotNull(candidato, "Candidato no debe ser nulo");
        assertEquals(idExpected, candidato.getId(), "El ID seteado debe coincidir.");
        assertEquals(nameExpected, candidato.getNombreCompleto(), "El nombre seteado debe coincidir.");
        assertEquals(partidoPoliticoMock, candidato.getPartido(), "El partido debe coincidir.");
        assertEquals("Partido de Libertad", candidato.getPartido().getNombre(), "El partido no debe ser nulo.");
    }

    @Test
    @DisplayName("Debe inicializar el ID como nulo usando el constructor NoArgsConstructor")
    void initializeIdAsNull(){
        //Arrange
        Candidato candidato = new Candidato();
        //Assert
        assertNull(candidato.getId(), "El ID debe ser nulo al usar el constructor vacio. ");
    }
}
