package com.seguros.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SeguroCocheTest {
    @Test
    void constructorVacioDebeCrearObjetoSeguroCoche() {
        SeguroCoche coche = new SeguroCoche();
        assertNotNull(coche);
    }

    @Test
    void constructorCompletoDebeInicializarCampos() {
        Seguro seguro = new Seguro("Seguro Auto", "Cobertura total", TipoSeguro.COCHE, 300.0);
        SeguroCoche coche = new SeguroCoche(seguro, "1234ABC", "Model S", "Tesla");

        assertEquals("1234ABC", coche.getMatricula());
        assertEquals("Model S", coche.getModelo());
        assertEquals("Tesla", coche.getMarca());
        assertEquals(seguro, coche.getSeguro());
    }

    @Test
    void gettersYSettersFuncionanCorrectamente() {
        SeguroCoche coche = new SeguroCoche();

        coche.setPoliza(101);
        coche.setMatricula("5678DEF");
        coche.setModelo("Civic");
        coche.setMarca("Honda");

        Seguro seguro = new Seguro("Auto Avanzado", "Todo riesgo", TipoSeguro.COCHE, 280.0);
        coche.setSeguro(seguro);

        assertEquals(101, coche.getPoliza());
        assertEquals("5678DEF", coche.getMatricula());
        assertEquals("Civic", coche.getModelo());
        assertEquals("Honda", coche.getMarca());
        assertEquals(seguro, coche.getSeguro());
    }

    @Test
    void toStringDebeContenerDatosEsperados() {
        Seguro seguro = new Seguro("Coche Premium", "Cobertura ampliada", TipoSeguro.COCHE, 450.0);
        seguro.setId(5L); // Asegúrate de que tu clase Seguro tenga setId()

        SeguroCoche coche = new SeguroCoche(seguro, "0000ZZZ", "Mustang", "Ford");
        coche.setPoliza(202);

        String toString = coche.toString();
        assertTrue(toString.contains("202"));
        assertTrue(toString.contains("0000ZZZ"));
        assertTrue(toString.contains("Mustang"));
        assertTrue(toString.contains("Ford"));
        assertTrue(toString.contains("5"));
    }

    @Test
    void testToStringWithSeguroNull() {
        SeguroCoche coche = new SeguroCoche(null, "1234ABC", "Focus", "Ford");
        coche.setPoliza(1);
        String str = coche.toString();
        assertTrue(str.contains("seguro=null"));
    }

    @Test
    void equalsYHashCodeFuncionanCorrectamente() {
        SeguroCoche coche1 = new SeguroCoche();
        coche1.setPoliza(1);

        SeguroCoche coche2 = new SeguroCoche();
        coche2.setPoliza(1);

        SeguroCoche coche3 = new SeguroCoche();
        coche3.setPoliza(2);

        assertEquals(coche1, coche2);
        assertNotEquals(coche1, coche3);
        assertEquals(coche1.hashCode(), coche2.hashCode());
        assertNotEquals(coche1.hashCode(), coche3.hashCode());
    }

    @Test
    void equalsDebeRetornarFalseParaNullYClaseDistinta() {
        SeguroCoche coche = new SeguroCoche();
        coche.setPoliza(200);

        assertNotEquals(coche, null);
        assertNotEquals(coche, "otro objeto");
    }

    @Test
    void equalsDebeRetornarTrueParaMismoObjeto() {
        SeguroCoche coche = new SeguroCoche();
        assertEquals(coche, coche);
    }

    @Test
    void equalsDebeRetornarFalseSiClaseDistintaONull() {
        SeguroCoche coche = new SeguroCoche();
        assertNotEquals(coche, null);
        assertNotEquals(coche, new Object());
    }

    @Test
    void equalsDebeRetornarFalseSiPolizasDiferentes() {
        SeguroCoche coche1 = new SeguroCoche();
        coche1.setPoliza(1);

        SeguroCoche coche2 = new SeguroCoche();
        coche2.setPoliza(2);

        assertNotEquals(coche1, coche2);
    }

    @Test
    void equalsDebeRetornarFalseSiEsDistintaClaseYNoNull() {
        Object obj = new Object();
        SeguroCoche coche = new SeguroCoche();
        coche.setPoliza(20);
        assertNotEquals(coche, obj);
    }

    @Test
    void equalsDebeRetornarFalseSiComparadoConInstanciaDeOtraClase() {
        SeguroCoche coche = new SeguroCoche();
        coche.setPoliza(1);
        assertNotEquals(coche, new Object());
    }
}
