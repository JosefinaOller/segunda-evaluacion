package com.mobyapp.segunda_evaluacion.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la entidad Partido Politico")
class PartidoPoliticoTest {

    @Test
    @DisplayName("Debe crear la instancia de Partido Politico usando el constructor con los argumentos")
    void createPartidoPoliticoWithAllArgs(){

        Long existingId = 1L;
        String existingName = "Partido de Libertad";
        String existingSigla = "PL";

        PartidoPolitico partidoPolitico = new PartidoPolitico(existingId,existingName,existingSigla);

        assertNotNull(partidoPolitico, "Partido Politico no debe ser nulo");
        assertEquals(existingId, partidoPolitico.getId(), "El ID debe coincidir.");
        assertEquals(existingName, partidoPolitico.getNombre(), "El nombre debe coincidir.");
        assertEquals(existingSigla, partidoPolitico.getSigla(), "La sigla debe coincidir.");
    }

    @Test
    @DisplayName("Debe crear la instancia de Partido Politico usando el constructor vacío")
    void createPartidoPoliticoWithNoArgs(){

        PartidoPolitico partidoPolitico = new PartidoPolitico();
        Long existingId = 2L;
        String existingName = "Partido de Argentina";
        String existingSigla = "PA";

        partidoPolitico.setId(existingId);
        partidoPolitico.setNombre(existingName);
        partidoPolitico.setSigla(existingSigla);

        assertNotNull(partidoPolitico, "Partido Politico no debe ser nulo");
        assertEquals(existingId, partidoPolitico.getId(), "El ID debe coincidir.");
        assertEquals(existingName, partidoPolitico.getNombre(), "El nombre debe coincidir.");
        assertEquals(existingSigla, partidoPolitico.getSigla(), "La sigla debe coincidir.");
    }

    @Test
    @DisplayName("Debe inicializar el ID como nulo usando el constructor NoArgsConstructor")
    void initializeIdAsNull(){

        PartidoPolitico partidoPolitico = new PartidoPolitico();

        assertNull(partidoPolitico.getId(), "El ID debe ser nulo al usar el constructor vacio. ");
    }
}
