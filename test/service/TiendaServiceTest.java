package service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exception.ProductoException;
import exception.VendedorException;
import model.Producto;
import model.Venta;

public class TiendaServiceTest {
    private Tienda tienda;
    private ProductoService productoService;
    private VendedorService vendedorService;
    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        tienda = Tienda.crear();
        productoService = tienda.getProductoService();
        vendedorService = tienda.getVendedorService();
        ventaService = tienda.getVentaService();
    }

    // ==================== REGISTRO DE PRODUCTOS ====================

    @Test
    void testRegistrarProducto_ok() throws ProductoException {
        productoService.registrar("Notebook", 850000, "Tecnologia");

        assertEquals(1, productoService.obtenerTodos().size());
        assertEquals("P001", productoService.obtenerTodos().get(0).getCodigo());
        assertEquals("Notebook", productoService.obtenerTodos().get(0).getNombre());
    }

    @Test
    void testRegistrarProducto_autoGeneraCodigo() throws ProductoException {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Teclado", 45000, "Tecnologia");

        assertEquals(2, productoService.obtenerTodos().size());
        assertEquals("P001", productoService.obtenerTodos().get(0).getCodigo());
        assertEquals("P002", productoService.obtenerTodos().get(1).getCodigo());
    }

    @Test
    void testRegistrarProducto_nombreVacio_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> productoService.registrar("", 850000, "Tecnologia"));
    }

    @Test
    void testRegistrarProducto_nombreNulo_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> productoService.registrar(null, 850000, "Tecnologia"));
    }

    @Test
    void testRegistrarProducto_precioNegativo_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> productoService.registrar("Notebook", -100, "Tecnologia"));
    }

    @Test
    void testRegistrarProducto_precioCero_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> productoService.registrar("Notebook", 0, "Tecnologia"));
    }

    @Test
    void testRegistrarProducto_categoriaNula_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> productoService.registrar("Notebook", 850000, null));
    }

    @Test
    void testRegistrarProducto_categoriaVacia_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> productoService.registrar("Notebook", 850000, ""));
    }

    // ==================== REGISTRO DE VENDEDORES ====================

    @Test
    void testRegistrarVendedor_ok() throws VendedorException {
        vendedorService.registrar("Ana", 300000);

        assertEquals(1, vendedorService.obtenerTodos().size());
        assertEquals("V001", vendedorService.obtenerTodos().get(0).getCodigo());
        assertEquals("Ana", vendedorService.obtenerTodos().get(0).getNombre());
    }

    @Test
    void testRegistrarVendedor_autoGeneraCodigo() throws VendedorException {
        vendedorService.registrar("Ana", 300000);
        vendedorService.registrar("Carlos", 320000);

        assertEquals(2, vendedorService.obtenerTodos().size());
        assertEquals("V001", vendedorService.obtenerTodos().get(0).getCodigo());
        assertEquals("V002", vendedorService.obtenerTodos().get(1).getCodigo());
    }

    @Test
    void testRegistrarVendedor_nombreVacio_lanzaExcepcion() {
        assertThrows(VendedorException.class,
            () -> vendedorService.registrar("", 300000));
    }

    @Test
    void testRegistrarVendedor_sueldoNegativo_lanzaExcepcion() {
        assertThrows(VendedorException.class,
            () -> vendedorService.registrar("Ana", -300000));
    }

    // ==================== REGISTRO DE VENTAS ====================

    @Test
    void testCrearVenta_ok() throws Exception {
        vendedorService.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");

        assertNotNull(venta);
        assertEquals("V001", venta.getVendedor().getCodigo());
        assertTrue(venta.getDetalles().isEmpty());
    }

    @Test
    void testCrearVenta_vendedorNoEncontrado_lanzaExcepcion() {
        assertThrows(VendedorException.class, () -> ventaService.crear("V999"));
    }

    @Test
    void testAgregarDetalleAVenta_ok() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 2);

        assertEquals(1, venta.getDetalles().size());
        assertEquals(2, venta.getDetalles().get(0).getCantidad());
        assertEquals(50000, venta.getTotal(), 0.01);
    }

    @Test
    void testAgregarDetalleAVenta_productoNoEncontrado_lanzaExcepcion() throws Exception {
        vendedorService.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");

        assertThrows(ProductoException.class,
            () -> ventaService.agregarDetalle(venta, "P999", 1));
    }

    @Test
    void testAgregarMultiplesDetalles_ok() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Teclado", 45000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 2);
        ventaService.agregarDetalle(venta, "P002", 1);

        assertEquals(2, venta.getDetalles().size());
        assertEquals(3, venta.getCantidadTotalProductos());
        assertEquals(95000, venta.getTotal(), 0.01);
    }

    @Test
    void testConfirmarVenta_ok() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 1);
        ventaService.confirmar(venta);

        assertEquals(1, ventaService.obtenerTodas().size());
    }

    @Test
    void testConfirmarVenta_vendedorTieneVentaEnSuLista() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 1);
        ventaService.confirmar(venta);

        List<Venta> ventasAna = vendedorService.buscarPorCodigo("V001").getVentas();
        assertEquals(1, ventasAna.size());
        assertEquals("VENT-1", ventasAna.get(0).getCodigoVenta());
    }

    // ==================== BUSQUEDAS ====================

    @Test
    void testBuscarPorCategoria() throws ProductoException {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Cafetera", 120000, "Electro");
        productoService.registrar("Teclado", 45000, "Tecnologia");

        List<Producto> resultado = productoService.buscarPorCategoria("Tecnologia");

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getCategoria().equalsIgnoreCase("Tecnologia")));
    }

    @Test
    void testBuscarPorCategoria_caseInsensitive() throws ProductoException {
        productoService.registrar("Mouse", 25000, "Tecnologia");

        List<Producto> resultado = productoService.buscarPorCategoria("tecnologia");

        assertEquals(1, resultado.size());
    }

    @Test
    void testBuscarPorCategoria_sinResultados() throws ProductoException {
        productoService.registrar("Mouse", 25000, "Tecnologia");

        List<Producto> resultado = productoService.buscarPorCategoria("Hogar");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarPorNombre() throws ProductoException {
        productoService.registrar("Mouse Logitech", 25000, "Tecnologia");
        productoService.registrar("Mouse Genius", 15000, "Tecnologia");
        productoService.registrar("Teclado Redragon", 45000, "Tecnologia");

        List<Producto> resultado = productoService.buscarPorNombre("mouse");

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getNombre().toLowerCase().contains("mouse")));
    }

    @Test
    void testBuscarPorRangoPrecio() throws ProductoException {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Teclado", 45000, "Tecnologia");
        productoService.registrar("Notebook", 850000, "Tecnologia");

        List<Producto> resultado = productoService.buscarPorRangoPrecio(20000, 50000);

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getPrecio() >= 20000 && p.getPrecio() <= 50000));
    }

    @Test
    void testBuscarPorCodigo() throws ProductoException {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Teclado", 45000, "Tecnologia");

        List<Producto> resultado = productoService.buscarPorCodigoLista("P002");

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNombre());
    }

    @Test
    void testObtenerCategorias() throws ProductoException {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Cafetera", 120000, "Electro");
        productoService.registrar("Teclado", 45000, "Tecnologia");

        List<String> categorias = productoService.obtenerCategorias();

        assertEquals(2, categorias.size());
        assertTrue(categorias.contains("Tecnologia"));
        assertTrue(categorias.contains("Electro"));
    }

    // ==================== COMISIONES ====================

    @Test
    void testComision_unProducto_5porciento() throws Exception {
        productoService.registrar("Mouse", 100000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 1);
        ventaService.confirmar(venta);

        double comision = vendedorService.calcularComision("V001");
        assertEquals(5000.0, comision, 0.01);
    }

    @Test
    void testComision_dosProductos_5porciento() throws Exception {
        productoService.registrar("Mouse", 50000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 2);
        ventaService.confirmar(venta);

        double comision = vendedorService.calcularComision("V001");
        assertEquals(5000.0, comision, 0.01);
    }

    @Test
    void testComision_tresProductos_10porciento() throws Exception {
        productoService.registrar("Mouse", 50000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 3);
        ventaService.confirmar(venta);

        double comision = vendedorService.calcularComision("V001");
        assertEquals(15000.0, comision, 0.01);
    }

    @Test
    void testComision_multiplesVentas_cadaUnaEvaluaIndividualmente() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Teclado", 45000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta1 = ventaService.crear("V001");
        ventaService.agregarDetalle(venta1, "P001", 1);
        ventaService.agregarDetalle(venta1, "P002", 1);
        ventaService.confirmar(venta1);

        Venta venta2 = ventaService.crear("V001");
        ventaService.agregarDetalle(venta2, "P001", 1);
        ventaService.confirmar(venta2);

        double comision = vendedorService.calcularComision("V001");
        assertEquals(4750.0, comision, 0.01);
    }

    @Test
    void testComision_mixto_unaVenta5_oltra10() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Teclado", 45000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta1 = ventaService.crear("V001");
        ventaService.agregarDetalle(venta1, "P001", 1);
        ventaService.confirmar(venta1);

        Venta venta2 = ventaService.crear("V001");
        ventaService.agregarDetalle(venta2, "P001", 2);
        ventaService.agregarDetalle(venta2, "P002", 1);
        ventaService.confirmar(venta2);

        double comision = vendedorService.calcularComision("V001");
        assertEquals(10750.0, comision, 0.01);
    }

    @Test
    void testComision_vendedorSinVentas_returns0() throws Exception {
        vendedorService.registrar("Ana", 300000);

        double comision = vendedorService.calcularComision("V001");

        assertEquals(0.0, comision, 0.01);
    }

    @Test
    void testComision_vendedorNoEncontrado_lanzaExcepcion() {
        assertThrows(VendedorException.class, () -> vendedorService.calcularComision("V999"));
    }

    @Test
    void testComision_vendedoresIndependientes() throws Exception {
        productoService.registrar("Mouse", 100000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);
        vendedorService.registrar("Carlos", 320000);

        Venta ventaAna = ventaService.crear("V001");
        ventaService.agregarDetalle(ventaAna, "P001", 1);
        ventaService.confirmar(ventaAna);

        Venta ventaCarlos = ventaService.crear("V002");
        ventaService.agregarDetalle(ventaCarlos, "P001", 3);
        ventaService.confirmar(ventaCarlos);

        assertEquals(5000.0, vendedorService.calcularComision("V001"), 0.01);
        assertEquals(30000.0, vendedorService.calcularComision("V002"), 0.01);
    }

    @Test
    void testComision_conCantidadMayor_10porciento() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = ventaService.crear("V001");
        ventaService.agregarDetalle(venta, "P001", 5);
        ventaService.confirmar(venta);

        double comision = vendedorService.calcularComision("V001");
        assertEquals(12500.0, comision, 0.01);
    }
}
