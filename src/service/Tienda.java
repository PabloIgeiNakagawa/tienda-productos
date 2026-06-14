package service;

import java.util.ArrayList;
import java.util.List;

import model.Producto;
import model.Vendedor;
import model.Venta;

public class Tienda {
    private final ProductoService productoService;
    private final VendedorService vendedorService;
    private final VentaService ventaService;

    public static Tienda crear() {
        return new Tienda();
    }

    private Tienda() {
        List<Producto> productos = new ArrayList<>();
        List<Vendedor> vendedores = new ArrayList<>();
        List<Venta> ventas = new ArrayList<>();

        this.productoService = new ProductoService(productos);
        this.vendedorService = new VendedorService(vendedores);
        this.ventaService = new VentaService(ventas, productoService, vendedorService);
    }

    public ProductoService getProductoService() { return productoService; }
    public VendedorService getVendedorService() { return vendedorService; }
    public VentaService getVentaService() { return ventaService; }
}
