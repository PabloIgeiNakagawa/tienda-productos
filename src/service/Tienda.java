package service;

import java.util.ArrayList;
import java.util.List;

import exception.ProductoException;
import exception.VendedorException;
import model.Producto;
import model.Vendedor;
import model.Venta;

public class Tienda {
    private final ProductoService productoService;
    private final VendedorService vendedorService;
    private final VentaService ventaService;

    public Tienda() {
        List<Producto> productos = new ArrayList<>();
        List<Vendedor> vendedores = new ArrayList<>();
        List<Venta> ventas = new ArrayList<>();

        this.productoService = new ProductoService(productos);
        this.vendedorService = new VendedorService(vendedores);
        this.ventaService = new VentaService(ventas, productoService, vendedorService);
    }

    // ========== PRODUCTOS ==========

    public void registrarProducto(String nombre, double precio, String categoria) throws ProductoException {
        productoService.registrar(nombre, precio, categoria);
    }

    public List<Producto> buscarProductosPorCategoria(String categoria) {
        return productoService.buscarPorCategoria(categoria);
    }

    public List<Producto> buscarProductosPorNombre(String termino) {
        return productoService.buscarPorNombre(termino);
    }

    public List<Producto> buscarProductosPorRangoPrecio(double min, double max) {
        return productoService.buscarPorRangoPrecio(min, max);
    }

    public List<Producto> buscarProductosPorCodigo(String codigo) {
        return productoService.buscarPorCodigoLista(codigo);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoService.obtenerTodos();
    }

    public List<String> obtenerCategorias() {
        return productoService.obtenerCategorias();
    }

    // ========== VENDEDORES ==========

    public void registrarVendedor(String nombre, double sueldo) throws VendedorException {
        vendedorService.registrar(nombre, sueldo);
    }

    public List<Vendedor> obtenerTodosLosVendedores() {
        return vendedorService.obtenerTodos();
    }

    public double calcularComisionVendedor(String codigoVendedor) throws VendedorException {
        return vendedorService.calcularComision(codigoVendedor);
    }

    // ========== VENTAS ==========

    public Venta crearVenta(String codigoVendedor) throws VendedorException {
        return ventaService.crear(codigoVendedor);
    }

    public void agregarDetalleAVenta(Venta venta, String codigoProducto, int cantidad) throws ProductoException {
        ventaService.agregarDetalle(venta, codigoProducto, cantidad);
    }

    public void confirmarVenta(Venta venta) {
        ventaService.confirmar(venta);
    }

    public List<Venta> obtenerTodasLasVentas() {
        return ventaService.obtenerTodas();
    }
}
