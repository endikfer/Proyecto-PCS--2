package com.seguros.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class SeguroCasaTest {
    @Test
    void constructorVacioDebeCrearObjetoSeguroCasa() {
        SeguroCasa seguroCasa = new SeguroCasa();
        assertNotNull(seguroCasa);
    }

    @Test
    void constructorCompletoDebeInicializarCampos() {
        Seguro seguro = new Seguro("Seguro Hogar", "Protección completa", TipoSeguro.CASA, 180.0);
        SeguroCasa seguroCasa = new SeguroCasa(seguro, "Calle Falsa 123", 250000.0, "Piso");

        assertEquals("Calle Falsa 123", seguroCasa.getDireccion());
        assertEquals(250000.0, seguroCasa.getValorInmueble());
        assertEquals("Piso", seguroCasa.getTipoVivienda());
        assertEquals(seguro, seguroCasa.getSeguro());
    }

    @Test
    void gettersYSettersFuncionanCorrectamente() {
        SeguroCasa seguroCasa = new SeguroCasa();

        seguroCasa.setPoliza(1234);
        seguroCasa.setDireccion("Av. Principal 456");
        seguroCasa.setValorInmueble(300000.0);
        seguroCasa.setTipoVivienda("Chalet");

        Seguro seguro = new Seguro("Casa Premium", "Cobertura total", TipoSeguro.CASA, 220.0);
        seguroCasa.setSeguro(seguro);

        assertEquals(1234, seguroCasa.getPoliza());
        assertEquals("Av. Principal 456", seguroCasa.getDireccion());
        assertEquals(300000.0, seguroCasa.getValorInmueble());
        assertEquals("Chalet", seguroCasa.getTipoVivienda());
        assertEquals(seguro, seguroCasa.getSeguro());
    }

    @Test
    void toStringDebeContenerDatosEsperados() {
        Seguro seguro = new Seguro("Seguro Casa", "Cobertura básica", TipoSeguro.CASA, 100.0);
        seguro.setId(10L); // Asegúrate de que Seguro tenga un setId()

        SeguroCasa seguroCasa = new SeguroCasa(seguro, "Calle Luna 33", 150000.0, "Ático");
        seguroCasa.setPoliza(5678);

        String toString = seguroCasa.toString();
        assertTrue(toString.contains("5678"));
        assertTrue(toString.contains("Calle Luna 33"));
        assertTrue(toString.contains("150000.0"));
        assertTrue(toString.contains("Ático"));
        assertTrue(toString.contains("10"));
    }

    @Test
    void testToStringWithSeguroNull() {
        SeguroCasa casa = new SeguroCasa(null, "Calle Falsa", 100000.0, "Piso");
        casa.setPoliza(1);
        String str = casa.toString();
        assertTrue(str.contains("seguro=null"));
    }

    @Test
    void equalsYHashCodeFuncionanPorPoliza() {
        SeguroCasa casa1 = new SeguroCasa();
        casa1.setPoliza(1);

        SeguroCasa casa2 = new SeguroCasa();
        casa2.setPoliza(1);

        SeguroCasa casa3 = new SeguroCasa();
        casa3.setPoliza(2);

        assertEquals(casa1, casa2);
        assertNotEquals(casa1, casa3);
        assertEquals(casa1.hashCode(), casa2.hashCode());
        assertNotEquals(casa1.hashCode(), casa3.hashCode());
    }

    @Test
    void equalsDebeRetornarFalseParaNullYClaseDistinta() {
        SeguroCasa casa = new SeguroCasa();
        casa.setPoliza(100);

        assertNotEquals(casa, null); // null check
        assertNotEquals(casa, "otro objeto"); // tipo distinto
    }

    @Test
    void equalsDebeRetornarTrueParaMismoObjeto() {
        SeguroCasa casa = new SeguroCasa();
        assertEquals(casa, casa); // this == o
    }

    @Test
    void equalsDebeRetornarFalseSiClaseDistintaONull() {
        SeguroCasa casa = new SeguroCasa();
        assertNotEquals(casa, null); // o == null
        assertNotEquals(casa, new Object()); // clase distinta
    }

    @Test
    void equalsDebeRetornarFalseSiPolizasDiferentes() {
        SeguroCasa casa1 = new SeguroCasa();
        casa1.setPoliza(1);

        SeguroCasa casa2 = new SeguroCasa();
        casa2.setPoliza(2);

        assertNotEquals(casa1, casa2);
    }

    @Test
    void equalsDebeRetornarFalseSiEsDistintaClaseYNoNull() {
        Object obj = new Object();
        SeguroCasa casa = new SeguroCasa();
        casa.setPoliza(10);
        assertNotEquals(casa, obj); // fuerza la rama del `getClass() !=`
    }

    @Test
    void equalsDebeRetornarFalseSiComparadoConInstanciaDeOtraClase() {
        SeguroCasa casa = new SeguroCasa();
        casa.setPoliza(1);
        assertNotEquals(casa, new Object()); // fuerza rama getClass() !=
    }
}
