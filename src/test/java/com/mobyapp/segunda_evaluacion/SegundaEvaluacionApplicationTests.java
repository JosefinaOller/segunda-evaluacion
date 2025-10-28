package com.mobyapp.segunda_evaluacion;

import com.mobyapp.segunda_evaluacion.controller.VotoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SegundaEvaluacionApplicationTests {

    @Autowired
    private VotoController votoController;

    @Test
    void contextLoads() {
        assertThat(votoController).isNotNull();
    }

}
