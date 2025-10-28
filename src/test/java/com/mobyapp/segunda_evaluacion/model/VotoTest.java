package com.mobyapp.segunda_evaluacion.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de la entidad Voto")
class VotoTest {

    private final PartidoPolitico existingPartidoPolitico = new PartidoPolitico(1L,"Partido de Libertad","PL");
    private final Candidato existingCandidato = new Candidato(1L,"Lionel Messi", existingPartidoPolitico);

    @Test
    @DisplayName("Debe crear la instancia de Voto usando el constructor con los argumentos")
    void createVotoWithAllArgs(){

        Long existingId = 1L;
        LocalDateTime existingFechaEmision = LocalDateTime.now();

        Voto voto = new Voto(existingId, existingCandidato,existingFechaEmision);

        assertNotNull(voto, "Voto no debe ser nulo");
        assertEquals(existingId, voto.getId(), "El ID debe coincidir.");
        assertEquals(existingFechaEmision, voto.getFechaEmision(), "La fecha de emision debe coincidir.");
        assertEquals(existingCandidato, voto.getCandidato(), "El candidato debe coincidir.");
        assertEquals("Lionel Messi", voto.getCandidato().getNombreCompleto(), "Debe acceder al nombre del candidato.");
    }

    @Test
    @DisplayName("Debe crear la instancia de Voto usando el constructor vacío")
    void createVotoWithNoArgs(){

        Voto voto = new Voto();
        Long existingId = 2L;
        LocalDateTime existingFechaEmision = LocalDateTime.now();

        voto.setId(existingId);
        voto.setFechaEmision(existingFechaEmision);
        voto.setCandidato(existingCandidato);

        assertNotNull(voto, "Voto no debe ser nulo");
        assertEquals(existingId, voto.getId(), "El ID seteado debe coincidir.");
        assertEquals(existingFechaEmision, voto.getFechaEmision(), "La fecha de emision seteada debe coincidir.");
        assertEquals(existingCandidato, voto.getCandidato(), "El candidato debe coincidir.");
        assertEquals("Lionel Messi", voto.getCandidato().getNombreCompleto(), "Debe acceder al nombre del candidato.");
    }

    @Test
    @DisplayName("Debe inicializar el ID como nulo usando el constructor NoArgsConstructor")
    void initializeIdAsNull(){

        Voto voto = new Voto();

        assertNull(voto.getId(), "El ID debe ser nulo al usar el constructor vacio. ");
    }

}
