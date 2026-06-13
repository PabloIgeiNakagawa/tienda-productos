package service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exception.ProductoException;
import exception.VendedorException;
import model.Producto;
import model.Vendedor;
import model.Venta;

public class TiendaServiceTest {
    private Tienda service;

    @BeforeEach
    void setUp() {
        service = new Tienda();
    }

    // ==================== REGISTRO DE PRODUCTOS ====================

    @Test
    void testRegistrarProducto_ok() throws ProductoException {
        service.registrarProducto("Notebook", 850000, "Tecnologia");

        assertEquals(1, service.obtenerTodosLosProductos().size());
        assertEquals("P001", service.obtenerTodosLosProductos().get(0).getCodigo());
        assertEquals("Notebook", service.obtenerTodosLosProductos().get(0).getNombre());
    }

    @Test
    void testRegistrarProducto_autoGeneraCodigo() throws ProductoException {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarProducto("Teclado", 45000, "Tecnologia");

        assertEquals(2, service.obtenerTodosLosProductos().size());
        assertEquals("P001", service.obtenerTodosLosProductos().get(0).getCodigo());
        assertEquals("P002", service.obtenerTodosLosProductos().get(1).getCodigo());
    }

    @Test
    void testRegistrarProducto_nombreVacio_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrarProducto("", 850000, "Tecnologia"));
    }

    @Test
    void testRegistrarProducto_nombreNulo_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrarProducto(null, 850000, "Tecnologia"));
    }

    @Test
    void testRegistrarProducto_precioNegativo_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrarProducto("Notebook", -100, "Tecnologia"));
    }

    @Test
    void testRegistrarProducto_precioCero_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrarProducto("Notebook", 0, "Tecnologia"));
    }

    @Test
    void testRegistrarProducto_categoriaNula_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrarProducto("Notebook", 850000, null));
    }

    @Test
    void testRegistrarProducto_categoriaVacia_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrarProducto("Notebook", 850000, ""));
    }

    // ==================== REGISTRO DE VENDEDORES ====================

    @Test
    void testRegistrarVendedor_ok() throws VendedorException {
        service.registrarVendedor("Ana", 300000);

        assertEquals(1, service.obtenerTodosLosVendedores().size());
        assertEquals("V001", service.obtenerTodosLosVendedores().get(0).getCodigo());
        assertEquals("Ana", service.obtenerTodosLosVendedores().get(0).getNombre());
    }

    @Test
    void testRegistrarVendedor_autoGeneraCodigo() throws VendedorException {
        service.registrarVendedor("Ana", 300000);
        service.registrarVendedor("Carlos", 320000);

        assertEquals(2, service.obtenerTodosLosVendedores().size());
        assertEquals("V001", service.obtenerTodosLosVendedores().get(0).getCodigo());
        assertEquals("V002", service.obtenerTodosLosVendedores().get(1).getCodigo());
    }

    @Test
    void testRegistrarVendedor_nombreVacio_lanzaExcepcion() {
        assertThrows(VendedorException.class,
            () -> service.registrarVendedor("", 300000));
    }

    @Test
    void testRegistrarVendedor_sueldoNegativo_lanzaExcepcion() {
        assertThrows(VendedorException.class,
            () -> service.registrarVendedor("Ana", -300000));
    }

    // ==================== REGISTRO DE VENTAS ====================

    @Test
    void testCrearVenta_ok() throws Exception {
        service.registrarVendedor("Ana", 300000);

        Venta venta = service.crearVenta("V001");

        assertNotNull(venta);
        assertEquals("V001", venta.getVendedor().getCodigo());
        assertTrue(venta.getDetalles().isEmpty());
    }

    @Test
    void testCrearVenta_vendedorNoEncontrado_lanzaExcepcion() {
        assertThrows(VendedorException.class, () -> service.crearVenta("V999"));
    }

    @Test
    void testAgregarDetalleAVenta_ok() throws Exception {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);

        Venta venta = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta, "P001", 2);

        assertEquals(1, venta.getDetalles().size());
        assertEquals(2, venta.getDetalles().get(0).getCantidad());
        assertEquals(50000, venta.getTotal(), 0.01);
    }

    @Test
    void testAgregarDetalleAVenta_productoNoEncontrado_lanzaExcepcion() throws Exception {
        service.registrarVendedor("Ana", 300000);

        Venta venta = service.crearVenta("V001");

        assertThrows(ProductoException.class,
            () -> service.agregarDetalleAVenta(venta, "P999", 1));
    }

    @Test
    void testAgregarMultiplesDetalles_ok() throws Exception {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarProducto("Teclado", 45000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);

        Venta venta = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta, "P001", 2);
        service.agregarDetalleAVenta(venta, "P002", 1);

        assertEquals(2, venta.getDetalles().size());
        assertEquals(3, venta.getCantidadTotalProductos());
        assertEquals(95000, venta.getTotal(), 0.01);
    }

    @Test
    void testConfirmarVenta_ok() throws Exception {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);

        Venta venta = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta, "P001", 1);
        service.confirmarVenta(venta);

        assertEquals(1, service.obtenerTodasLasVentas().size());
    }

    @Test
    void testConfirmarVenta_vendedorTieneVentaEnSuLista() throws Exception {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);

        Venta venta = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta, "P001", 1);
        service.confirmarVenta(venta);

        List<Venta> ventasAna = service.obtenerTodosLosVendedores().get(0).getVentas();
        assertEquals(1, ventasAna.size());
        assertEquals("VENT-1", ventasAna.get(0).getCodigoVenta());
    }

    // ==================== BUSQUEDAS ====================

    @Test
    void testBuscarPorCategoria() throws ProductoException {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarProducto("Cafetera", 120000, "Electro");
        service.registrarProducto("Teclado", 45000, "Tecnologia");

        List<Producto> resultado = service.buscarProductosPorCategoria("Tecnologia");

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getCategoria().equalsIgnoreCase("Tecnologia")));
    }

    @Test
    void testBuscarPorCategoria_caseInsensitive() throws ProductoException {
        service.registrarProducto("Mouse", 25000, "Tecnologia");

        List<Producto> resultado = service.buscarProductosPorCategoria("tecnologia");

        assertEquals(1, resultado.size());
    }

    @Test
    void testBuscarPorCategoria_sinResultados() throws ProductoException {
        service.registrarProducto("Mouse", 25000, "Tecnologia");

        List<Producto> resultado = service.buscarProductosPorCategoria("Hogar");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarPorNombre() throws ProductoException {
        service.registrarProducto("Mouse Logitech", 25000, "Tecnologia");
        service.registrarProducto("Mouse Genius", 15000, "Tecnologia");
        service.registrarProducto("Teclado Redragon", 45000, "Tecnologia");

        List<Producto> resultado = service.buscarProductosPorNombre("mouse");

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getNombre().toLowerCase().contains("mouse")));
    }

    @Test
    void testBuscarPorRangoPrecio() throws ProductoException {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarProducto("Teclado", 45000, "Tecnologia");
        service.registrarProducto("Notebook", 850000, "Tecnologia");

        List<Producto> resultado = service.buscarProductosPorRangoPrecio(20000, 50000);

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getPrecio() >= 20000 && p.getPrecio() <= 50000));
    }

    @Test
    void testBuscarPorCodigo() throws ProductoException {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarProducto("Teclado", 45000, "Tecnologia");

        List<Producto> resultado = service.buscarProductosPorCodigo("P002");

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNombre());
    }

    @Test
    void testObtenerCategorias() throws ProductoException {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarProducto("Cafetera", 120000, "Electro");
        service.registrarProducto("Teclado", 45000, "Tecnologia");

        List<String> categorias = service.obtenerCategorias();

        assertEquals(2, categorias.size());
        assertTrue(categorias.contains("Tecnologia"));
        assertTrue(categorias.contains("Electro"));
    }

    // ==================== COMISIONES ====================

    @Test
    void testComision_unProducto_5porciento() throws Exception {
        service.registrarProducto("Mouse", 100000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);

        Venta venta = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta, "P001", 1);
        service.confirmarVenta(venta);

        double comision = service.calcularComisionVendedor("V001");
        assertEquals(5000.0, comision, 0.01);
    }

    @Test
    void testComision_dosProductos_5porciento() throws Exception {
        service.registrarProducto("Mouse", 50000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);

        Venta venta = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta, "P001", 2);
        service.confirmarVenta(venta);

        double comision = service.calcularComisionVendedor("V001");
        assertEquals(5000.0, comision, 0.01);
    }

    @Test
    void testComision_tresProductos_10porciento() throws Exception {
        service.registrarProducto("Mouse", 50000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);

        Venta venta = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta, "P001", 3);
        service.confirmarVenta(venta);

        double comision = service.calcularComisionVendedor("V001");
        assertEquals(15000.0, comision, 0.01);
    }

    @Test
    void testComision_multiplesVentas_cadaUnaEvaluaIndividualmente() throws Exception {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarProducto("Teclado", 45000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);

        Venta venta1 = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta1, "P001", 1);
        service.agregarDetalleAVenta(venta1, "P002", 1);
        service.confirmarVenta(venta1);

        Venta venta2 = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta2, "P001", 1);
        service.confirmarVenta(venta2);

        double comision = service.calcularComisionVendedor("V001");
        assertEquals(4750.0, comision, 0.01);
    }

    @Test
    void testComision_mixto_unaVenta5_oltra10() throws Exception {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarProducto("Teclado", 45000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);

        Venta venta1 = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta1, "P001", 1);
        service.confirmarVenta(venta1);

        Venta venta2 = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta2, "P001", 2);
        service.agregarDetalleAVenta(venta2, "P002", 1);
        service.confirmarVenta(venta2);

        double comision = service.calcularComisionVendedor("V001");
        assertEquals(10750.0, comision, 0.01);
    }

    @Test
    void testComision_vendedorSinVentas_returns0() throws Exception {
        service.registrarVendedor("Ana", 300000);

        double comision = service.calcularComisionVendedor("V001");

        assertEquals(0.0, comision, 0.01);
    }

    @Test
    void testComision_vendedorNoEncontrado_lanzaExcepcion() {
        assertThrows(VendedorException.class, () -> service.calcularComisionVendedor("V999"));
    }

    @Test
    void testComision_vendedoresIndependientes() throws Exception {
        service.registrarProducto("Mouse", 100000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);
        service.registrarVendedor("Carlos", 320000);

        Venta ventaAna = service.crearVenta("V001");
        service.agregarDetalleAVenta(ventaAna, "P001", 1);
        service.confirmarVenta(ventaAna);

        Venta ventaCarlos = service.crearVenta("V002");
        service.agregarDetalleAVenta(ventaCarlos, "P001", 3);
        service.confirmarVenta(ventaCarlos);

        assertEquals(5000.0, service.calcularComisionVendedor("V001"), 0.01);
        assertEquals(30000.0, service.calcularComisionVendedor("V002"), 0.01);
    }

    @Test
    void testComision_conCantidadMayor_10porciento() throws Exception {
        service.registrarProducto("Mouse", 25000, "Tecnologia");
        service.registrarVendedor("Ana", 300000);

        Venta venta = service.crearVenta("V001");
        service.agregarDetalleAVenta(venta, "P001", 5);
        service.confirmarVenta(venta);

        double comision = service.calcularComisionVendedor("V001");
        assertEquals(12500.0, comision, 0.01);
    }
}
