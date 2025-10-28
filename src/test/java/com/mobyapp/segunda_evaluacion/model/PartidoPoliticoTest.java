package com.mobyapp.segunda_evaluacion.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la entidad Partido Politico")
class PartidoPoliticoTest {

    @Test
    @DisplayName("Debe crear la instancia de Partido Politico usando el constructor con los argumentos")
    void createPartidoPoliticoWithAllArgs(){
        //Arrange
        Long idExpected = 1L;
        String nombreExpected = "Partido de Libertad";
        String siglaExpected = "PL";
        //Act
        PartidoPolitico partidoPolitico = new PartidoPolitico(idExpected,nombreExpected,siglaExpected);
        //Assert
        assertNotNull(partidoPolitico, "Partido Politico no debe ser nulo");
        assertEquals(idExpected, partidoPolitico.getId(), "El ID debe coincidir.");
        assertEquals(nombreExpected, partidoPolitico.getNombre(), "El nombre debe coincidir.");
        assertEquals(siglaExpected, partidoPolitico.getSigla(), "La sigla debe coincidir.");
    }

    @Test
    @DisplayName("Debe crear la instancia de Partido Politico usando el constructor vacío")
    void createPartidoPoliticoWithNoArgs(){
        //
        PartidoPolitico partidoPolitico = new PartidoPolitico();
        Long idExpected = 2L;
        String nombreExpected = "Partido de Argentina";
        String siglaExpected = "PA";
        //Act
        partidoPolitico.setId(idExpected);
        partidoPolitico.setNombre(nombreExpected);
        partidoPolitico.setSigla(siglaExpected);
        //Assert
        assertNotNull(partidoPolitico, "Partido Politico no debe ser nulo");
        assertEquals(idExpected, partidoPolitico.getId(), "El ID debe coincidir.");
        assertEquals(nombreExpected, partidoPolitico.getNombre(), "El nombre debe coincidir.");
        assertEquals(siglaExpected, partidoPolitico.getSigla(), "La sigla debe coincidir.");
    }

    @Test
    @DisplayName("Debe inicializar el ID como nulo usando el constructor NoArgsConstructor")
    void initializeIdAsNull(){
        //Arrange
        PartidoPolitico partidoPolitico = new PartidoPolitico();
        //Assert
        assertNull(partidoPolitico.getId(), "El ID debe ser nulo al usar el constructor vacio. ");
    }
}
