package service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exception.VendedorException;
import model.Producto;
import model.Vendedor;
import model.Venta;

public class VendedorServiceTest {
    private VendedorService service;
    private ProductoService productoService;
    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        productoService = new ProductoService();
        service = new VendedorService();
        ventaService = new VentaService(productoService, service);
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

    // ==================== COMISIONES ====================

    @Test
    void testComision_unProducto_5porciento() throws Exception {
        productoService.registrar("Mouse", 100000, "Tecnologia");
        service.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 1);
        ventaService.confirmar(venta);

        double comision = service.calcularComision("V001");
        assertEquals(5000.0, comision, 0.01);
    }

    @Test
    void testComision_dosProductos_5porciento() throws Exception {
        productoService.registrar("Mouse", 50000, "Tecnologia");
        service.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 2);
        ventaService.confirmar(venta);

        double comision = service.calcularComision("V001");
        assertEquals(5000.0, comision, 0.01);
    }

    @Test
    void testComision_tresProductos_10porciento() throws Exception {
        productoService.registrar("Mouse", 50000, "Tecnologia");
        service.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 3);
        ventaService.confirmar(venta);

        double comision = service.calcularComision("V001");
        assertEquals(15000.0, comision, 0.01);
    }

    @Test
    void testComision_multiplesVentas_cadaUnaEvaluaIndividualmente() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Teclado", 45000, "Tecnologia");
        service.registrar("Ana", 300000);

        Venta venta1 = ventaService.crear("V001");
        ventaService.agregarDetalle(venta1, "P001", 1);
        ventaService.agregarDetalle(venta1, "P002", 1);
        ventaService.confirmar(venta1);

        Venta venta2 = ventaService.crear("V001");
        ventaService.agregarDetalle(venta2, "P001", 1);
        ventaService.confirmar(venta2);

        double comision = service.calcularComision("V001");
        assertEquals(4750.0, comision, 0.01);
    }

    @Test
    void testComision_mixto_unaVenta5_otra10() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Teclado", 45000, "Tecnologia");
        service.registrar("Ana", 300000);

        Venta venta1 = ventaService.crear("V001");
        ventaService.agregarDetalle(venta1, "P001", 1);
        ventaService.confirmar(venta1);

        Venta venta2 = ventaService.crear("V001");
        ventaService.agregarDetalle(venta2, "P001", 2);
        ventaService.agregarDetalle(venta2, "P002", 1);
        ventaService.confirmar(venta2);

        double comision = service.calcularComision("V001");
        assertEquals(10750.0, comision, 0.01);
    }

    @Test
    void testComision_vendedoresIndependientes() throws Exception {
        productoService.registrar("Mouse", 100000, "Tecnologia");
        service.registrar("Ana", 300000);
        service.registrar("Carlos", 320000);

        Venta ventaAna = ventaService.crear("V001");
        ventaService.agregarDetalle(ventaAna, "P001", 1);
        ventaService.confirmar(ventaAna);

        Venta ventaCarlos = ventaService.crear("V002");
        ventaService.agregarDetalle(ventaCarlos, "P001", 3);
        ventaService.confirmar(ventaCarlos);

        assertEquals(5000.0, service.calcularComision("V001"), 0.01);
        assertEquals(30000.0, service.calcularComision("V002"), 0.01);
    }

    @Test
    void testComision_conCantidadMayor_10porciento() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        service.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 5);
        ventaService.confirmar(venta);

        double comision = service.calcularComision("V001");
        assertEquals(12500.0, comision, 0.01);
    }
}
