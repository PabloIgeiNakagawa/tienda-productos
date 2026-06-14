package service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exception.ProductoException;
import model.Producto;

public class ProductoServiceTest {
    private ProductoService service;

    @BeforeEach
    void setUp() {
        service = new ProductoService();
    }

    @Test
    void testRegistrar_ok() throws ProductoException {
        service.registrar("Mouse", 25000, "Tecnologia");

        assertEquals(1, service.obtenerTodos().size());
        assertEquals("P001", service.obtenerTodos().get(0).getCodigo());
    }

    @Test
    void testRegistrar_autoGeneraCodigo() throws ProductoException {
        service.registrar("Mouse", 25000, "Tecnologia");
        service.registrar("Teclado", 45000, "Tecnologia");

        assertEquals("P001", service.obtenerTodos().get(0).getCodigo());
        assertEquals("P002", service.obtenerTodos().get(1).getCodigo());
    }

    @Test
    void testRegistrar_nombreVacio_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrar("", 25000, "Tecnologia"));
    }

    @Test
    void testRegistrar_precioNegativo_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrar("Mouse", -100, "Tecnologia"));
    }

    @Test
    void testRegistrar_categoriaNula_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrar("Mouse", 25000, null));
    }

    @Test
    void testBuscarPorCodigo_ok() throws Exception {
        service.registrar("Mouse", 25000, "Tecnologia");
        service.registrar("Teclado", 45000, "Tecnologia");

        Producto resultado = service.buscarPorCodigo("P002");

        assertEquals("Teclado", resultado.getNombre());
    }

    @Test
    void testBuscarPorCodigo_noEncontrado() {
        assertThrows(ProductoException.class, () -> service.buscarPorCodigo("P999"));
    }

    @Test
    void testBuscarPorCategoria() throws ProductoException {
        service.registrar("Mouse", 25000, "Tecnologia");
        service.registrar("Cafetera", 120000, "Electro");

        List<Producto> resultado = service.buscarPorCategoria("Tecnologia");

        assertEquals(1, resultado.size());
        assertEquals("Mouse", resultado.get(0).getNombre());
    }

    @Test
    void testBuscarPorNombre() throws ProductoException {
        service.registrar("Mouse Logitech", 25000, "Tecnologia");
        service.registrar("Teclado", 45000, "Tecnologia");

        List<Producto> resultado = service.buscarPorNombre("mouse");

        assertEquals(1, resultado.size());
    }

    @Test
    void testBuscarPorRangoPrecio() throws ProductoException {
        service.registrar("Mouse", 25000, "Tecnologia");
        service.registrar("Notebook", 850000, "Tecnologia");

        List<Producto> resultado = service.buscarPorRangoPrecio(20000, 50000);

        assertEquals(1, resultado.size());
        assertEquals("Mouse", resultado.get(0).getNombre());
    }

    @Test
    void testObtenerCategorias() throws ProductoException {
        service.registrar("Mouse", 25000, "Tecnologia");
        service.registrar("Cafetera", 120000, "Electro");

        List<String> categorias = service.obtenerCategorias();

        assertEquals(2, categorias.size());
        assertTrue(categorias.contains("Tecnologia"));
        assertTrue(categorias.contains("Electro"));
    }

    @Test
    void testRegistrar_nombreNulo_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrar(null, 25000, "Tecnologia"));
    }

    @Test
    void testRegistrar_precioCero_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrar("Mouse", 0, "Tecnologia"));
    }

    @Test
    void testRegistrar_categoriaVacia_lanzaExcepcion() {
        assertThrows(ProductoException.class,
            () -> service.registrar("Mouse", 25000, ""));
    }

    @Test
    void testBuscarPorCategoria_caseInsensitive() throws ProductoException {
        service.registrar("Mouse", 25000, "Tecnologia");

        List<Producto> resultado = service.buscarPorCategoria("tecnologia");

        assertEquals(1, resultado.size());
    }

    @Test
    void testBuscarPorCategoria_sinResultados() throws ProductoException {
        service.registrar("Mouse", 25000, "Tecnologia");

        List<Producto> resultado = service.buscarPorCategoria("Hogar");

        assertTrue(resultado.isEmpty());
    }
}
