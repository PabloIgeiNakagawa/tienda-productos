package service;

public class Tienda {
    private final ProductoService productoService;
    private final VendedorService vendedorService;
    private final VentaService ventaService;

    public static Tienda crear() {
        return new Tienda();
    }

    private Tienda() {
        this.productoService = new ProductoService();
        this.vendedorService = new VendedorService();
        this.ventaService = new VentaService(productoService, vendedorService);
    }

    public ProductoService getProductoService() { return productoService; }
    public VendedorService getVendedorService() { return vendedorService; }
    public VentaService getVentaService() { return ventaService; }
}
