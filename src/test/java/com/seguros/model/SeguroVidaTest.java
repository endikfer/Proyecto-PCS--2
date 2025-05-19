package com.seguros.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SeguroVidaTest {
    @Test
    void constructorVacioDebeCrearObjetoSeguroVida() {
        SeguroVida seguroVida = new SeguroVida();
        assertNotNull(seguroVida);
    }

    @Test
    void constructorCompletoDebeInicializarCampos() {
        Seguro seguro = new Seguro("Vida Básico", "Cobertura total por fallecimiento", TipoSeguro.VIDA, 250.0);
        Cliente cliente = new Cliente();
        SeguroVida vida = new SeguroVida(seguro, cliente, 35, "Familia directa");

        assertEquals(seguro, vida.getSeguro());
        assertEquals(cliente, vida.getCliente());
        assertEquals(35, vida.getEdadAsegurado());
        assertEquals("Familia directa", vida.getBeneficiarios());
    }

    @Test
    void gettersYSettersFuncionanCorrectamente() {
        SeguroVida vida = new SeguroVida();

        vida.setPoliza(1010);
        vida.setEdadAsegurado(40);
        vida.setBeneficiarios("Cónyuge e hijos");

        Seguro seguro = new Seguro("Vida Plus", "Cobertura ampliada", TipoSeguro.VIDA, 320.0);
        vida.setSeguro(seguro);

        Cliente cliente = new Cliente();
        cliente.setId(123L);
        vida.setCliente(cliente);

        assertEquals(1010, vida.getPoliza());
        assertEquals(40, vida.getEdadAsegurado());
        assertEquals("Cónyuge e hijos", vida.getBeneficiarios());
        assertEquals(seguro, vida.getSeguro());
        assertEquals(cliente, vida.getCliente());
    }

    @Test
    void toStringDebeIncluirDatosEsperados() {
        Seguro seguro = new Seguro("Vida Senior", "Protección avanzada", TipoSeguro.VIDA, 450.0);
        seguro.setId(9L); // Asegúrate de que Seguro tenga setId()
        Cliente cliente = new Cliente();
        cliente.setId(5L);

        SeguroVida vida = new SeguroVida(seguro, cliente, 60, "Hijos");
        vida.setPoliza(3030);

        String toString = vida.toString();

        assertTrue(toString.contains("3030"));
        assertTrue(toString.contains("60"));
        assertTrue(toString.contains("Hijos"));
        assertTrue(toString.contains("9"));
        assertTrue(toString.contains("5"));
    }

    @Test
    void testToStringWithSeguroNullYClienteNull() {
        SeguroVida vida = new SeguroVida(null, null, 35, "Juan, Ana");
        vida.setPoliza(1);
        String str = vida.toString();
        assertTrue(str.contains("seguro=null"));
        assertTrue(str.contains("cliente=null"));
    }

    @Test
    void equalsYHashCodeFuncionanPorPoliza() {
        SeguroVida vida1 = new SeguroVida();
        vida1.setPoliza(1);

        SeguroVida vida2 = new SeguroVida();
        vida2.setPoliza(1);

        SeguroVida vida3 = new SeguroVida();
        vida3.setPoliza(2);

        assertEquals(vida1, vida2);
        assertNotEquals(vida1, vida3);
        assertEquals(vida1.hashCode(), vida2.hashCode());
        assertNotEquals(vida1.hashCode(), vida3.hashCode());
    }

    @Test
    void equalsDebeRetornarFalseParaNullYClaseDistinta() {
        SeguroVida vida = new SeguroVida();
        vida.setPoliza(300);

        assertNotEquals(vida, null);
        assertNotEquals(vida, "otra cosa");
    }

    @Test
    void equalsDebeRetornarTrueParaMismoObjeto() {
        SeguroVida vida = new SeguroVida();
        assertEquals(vida, vida);
    }

    @Test
    void equalsDebeRetornarFalseSiClaseDistintaONull() {
        SeguroVida vida = new SeguroVida();
        assertNotEquals(vida, null);
        assertNotEquals(vida, new Object());
    }

    @Test
    void equalsDebeRetornarFalseSiPolizasDiferentes() {
        SeguroVida vida1 = new SeguroVida();
        vida1.setPoliza(1);

        SeguroVida vida2 = new SeguroVida();
        vida2.setPoliza(2);

        assertNotEquals(vida1, vida2);
    }

    @Test
    void equalsDebeRetornarFalseSiEsDistintaClaseYNoNull() {
        Object obj = new Object();
        SeguroVida vida = new SeguroVida();
        vida.setPoliza(30);
        assertNotEquals(vida, obj);
    }

    @Test
    void equalsDebeRetornarFalseSiComparadoConInstanciaDeOtraClase() {
        SeguroVida vida = new SeguroVida();
        vida.setPoliza(1);
        assertNotEquals(vida, new Object());
    }
}
