package service;

import java.util.ArrayList;
import java.util.List;

import exception.ProductoException;
import exception.VendedorException;
import model.DetalleVenta;
import model.Producto;
import model.Vendedor;
import model.Venta;

public class VentaService {
    private List<Venta> ventas;
    private int contadorVentas;
    private ProductoService productoService;
    private VendedorService vendedorService;

    public VentaService(List<Venta> ventas, ProductoService productoService, VendedorService vendedorService) {
        this.ventas = ventas;
        this.contadorVentas = 1;
        this.productoService = productoService;
        this.vendedorService = vendedorService;
    }

    public Venta crear(String codigoVendedor) throws VendedorException {
        Vendedor vendedor = vendedorService.buscarPorCodigo(codigoVendedor);
        String codigoVenta = "VENT-" + (contadorVentas++);
        return new Venta(codigoVenta, vendedor);
    }

    public void agregarDetalle(Venta venta, String codigoProducto, int cantidad) throws ProductoException {
        Producto producto = productoService.buscarPorCodigo(codigoProducto);
        DetalleVenta detalle = new DetalleVenta(producto, cantidad);
        venta.agregarDetalle(detalle);
    }

    public void confirmar(Venta venta) {
        ventas.add(venta);
        venta.getVendedor().agregarVenta(venta);
    }

    public List<Venta> obtenerTodas() {
        return new ArrayList<>(ventas);
    }
}
