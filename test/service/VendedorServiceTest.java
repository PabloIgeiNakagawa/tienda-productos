package service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exception.VendedorException;
import model.Venta;

public class VendedorServiceTest {
    private VendedorService service;

    @BeforeEach
    void setUp() {
        service = new VendedorService(new ArrayList<>());
    }

    @Test
    void testRegistrar_ok() throws VendedorException {
        service.registrar("Ana", 300000);

        assertEquals(1, service.obtenerTodos().size());
        assertEquals("V001", service.obtenerTodos().get(0).getCodigo());
        assertEquals("Ana", service.obtenerTodos().get(0).getNombre());
    }

    @Test
    void testRegistrar_autoGeneraCodigo() throws VendedorException {
        service.registrar("Ana", 300000);
        service.registrar("Carlos", 320000);

        assertEquals("V001", service.obtenerTodos().get(0).getCodigo());
        assertEquals("V002", service.obtenerTodos().get(1).getCodigo());
    }

    @Test
    void testRegistrar_nombreVacio_lanzaExcepcion() {
        assertThrows(VendedorException.class,
            () -> service.registrar("", 300000));
    }

    @Test
    void testRegistrar_sueldoNegativo_lanzaExcepcion() {
        assertThrows(VendedorException.class,
            () -> service.registrar("Ana", -300000));
    }

    @Test
    void testBuscarPorCodigo_ok() throws Exception {
        service.registrar("Ana", 300000);

        assertEquals("V001", service.buscarPorCodigo("V001").getCodigo());
        assertEquals("Ana", service.buscarPorCodigo("V001").getNombre());
    }

    @Test
    void testBuscarPorCodigo_noEncontrado() {
        assertThrows(VendedorException.class, () -> service.buscarPorCodigo("V999"));
    }

    @Test
    void testCalcularComision_sinVentas_returns0() throws Exception {
        service.registrar("Ana", 300000);

        assertEquals(0.0, service.calcularComision("V001"), 0.01);
    }

    @Test
    void testCalcularComision_vendedorNoEncontrado_lanzaExcepcion() {
        assertThrows(VendedorException.class, () -> service.calcularComision("V999"));
    }
}
