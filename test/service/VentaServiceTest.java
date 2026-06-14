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

public class VentaServiceTest {
    private VentaService service;
    private ProductoService productoService;
    private VendedorService vendedorService;

    @BeforeEach
    void setUp() {
        productoService = new ProductoService();
        vendedorService = new VendedorService();
        service = new VentaService(productoService, vendedorService);
    }

    @Test
    void testCrear_ok() throws Exception {
        vendedorService.registrar("Ana", 300000);

        Venta venta = service.crear("V001");

        assertNotNull(venta);
        assertEquals("V001", venta.getVendedor().getCodigo());
        assertTrue(venta.getDetalles().isEmpty());
    }

    @Test
    void testCrear_vendedorNoEncontrado() {
        assertThrows(VendedorException.class, () -> service.crear("V999"));
    }

    @Test
    void testAgregarDetalle_ok() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = service.crear("V001");
        service.agregarDetalle(venta, "P001", 2);

        assertEquals(1, venta.getDetalles().size());
        assertEquals(2, venta.getDetalles().get(0).getCantidad());
        assertEquals(50000, venta.getTotal(), 0.01);
    }

    @Test
    void testAgregarDetalle_productoNoEncontrado() throws Exception {
        vendedorService.registrar("Ana", 300000);

        Venta venta = service.crear("V001");

        assertThrows(ProductoException.class,
            () -> service.agregarDetalle(venta, "P999", 1));
    }

    @Test
    void testConfirmar_ok() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = service.crear("V001");
        service.agregarDetalle(venta, "P001", 1);
        service.confirmar(venta);

        assertEquals(1, service.obtenerTodas().size());
        assertEquals(1, vendedorService.buscarPorCodigo("V001").getVentas().size());
    }

    @Test
    void testAgregarMultiplesDetalles_ok() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        productoService.registrar("Teclado", 45000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = service.crear("V001");
        service.agregarDetalle(venta, "P001", 2);
        service.agregarDetalle(venta, "P002", 1);

        assertEquals(2, venta.getDetalles().size());
        assertEquals(3, venta.getCantidadTotalProductos());
        assertEquals(95000, venta.getTotal(), 0.01);
    }

    @Test
    void testConfirmarVenta_vendedorTieneVentaEnSuLista() throws Exception {
        productoService.registrar("Mouse", 25000, "Tecnologia");
        vendedorService.registrar("Ana", 300000);

        Venta venta = service.crear("V001");
        service.agregarDetalle(venta, "P001", 1);
        service.confirmar(venta);

        List<Venta> ventasAna = vendedorService.buscarPorCodigo("V001").getVentas();
        assertEquals(1, ventasAna.size());
        assertEquals("VENT-1", ventasAna.get(0).getCodigoVenta());
    }
}
