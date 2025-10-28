package com.mobyapp.segunda_evaluacion.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la entidad Candidato")
class CandidatoTest {

    private final PartidoPolitico existingPartidoPolitico = new PartidoPolitico(1L,"Partido de Libertad", "PL");

    @Test
    @DisplayName("Debe crear la instancia de Candidato usando el constructor con los argumentos")
    void createCandidatoWithAllArgs(){

        Long existingId = 1L;
        String existingName = "Lionel Messi";

        Candidato candidato = new Candidato(existingId,existingName, existingPartidoPolitico);

        assertNotNull(candidato, "Candidato no debe ser nulo");
        assertEquals(existingId, candidato.getId(), "El ID debe coincidir.");
        assertEquals(existingName, candidato.getNombreCompleto(), "El nombre debe coincidir.");
        assertEquals(existingPartidoPolitico, candidato.getPartido(), "El partido debe coincidir.");
        assertEquals("Partido de Libertad", candidato.getPartido().getNombre(), "Debe acceder al nombre del partido.");
    }

    @Test
    @DisplayName("Debe crear la instancia de Candidato usando el constructor vacío")
    void createCandidatoWithNoArgs(){

        Candidato candidato = new Candidato();
        Long existingId = 2L;
        String existingName = "Angel Di Maria";

        candidato.setId(existingId);
        candidato.setNombreCompleto(existingName);
        candidato.setPartido(existingPartidoPolitico);

        assertNotNull(candidato, "Candidato no debe ser nulo");
        assertEquals(existingId, candidato.getId(), "El ID seteado debe coincidir.");
        assertEquals(existingName, candidato.getNombreCompleto(), "El nombre seteado debe coincidir.");
        assertEquals(existingPartidoPolitico, candidato.getPartido(), "El partido debe coincidir.");
        assertEquals("Partido de Libertad", candidato.getPartido().getNombre(), "El partido no debe ser nulo.");
    }

    @Test
    @DisplayName("Debe inicializar el ID como nulo usando el constructor NoArgsConstructor")
    void initializeIdAsNull(){

        Candidato candidato = new Candidato();

        assertNull(candidato.getId(), "El ID debe ser nulo al usar el constructor vacio. ");
    }
}
