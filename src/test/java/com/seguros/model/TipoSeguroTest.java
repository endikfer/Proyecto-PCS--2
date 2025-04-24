package com.seguros.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TipoSeguroTest {
    @Test
    void enumDebeContenerLosValoresEsperados() {
        assertEquals(TipoSeguro.VIDA, TipoSeguro.valueOf("VIDA"));
        assertEquals(TipoSeguro.CASA, TipoSeguro.valueOf("CASA"));
        assertEquals(TipoSeguro.COCHE, TipoSeguro.valueOf("COCHE"));
    }

    @Test
    void valuesDebeRetornarTodosLosTiposEnOrden() {
        TipoSeguro[] tipos = TipoSeguro.values();
        assertArrayEquals(new TipoSeguro[]{TipoSeguro.VIDA, TipoSeguro.CASA, TipoSeguro.COCHE}, tipos);
    }

    @Test
    void valueOfLanzaExcepcionConValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            TipoSeguro.valueOf("HOGAR"); // no existe
        });
    }

    @Test
    void todosLosTiposSonDistintosYNoNulos() {
        for (TipoSeguro tipo : TipoSeguro.values()) {
            assertNotNull(tipo);
        }

        assertNotEquals(TipoSeguro.VIDA, TipoSeguro.CASA);
        assertNotEquals(TipoSeguro.CASA, TipoSeguro.COCHE);
    }
}
