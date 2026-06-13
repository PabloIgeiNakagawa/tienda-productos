package model;

import java.util.ArrayList;
import java.util.List;

public class Venta {
    private String codigoVenta;
    private Vendedor vendedor;
    private List<DetalleVenta> detalles;
    private double total;

    public Venta(String codigoVenta, Vendedor vendedor) {
        this.codigoVenta = codigoVenta;
        this.vendedor = vendedor;
        this.detalles = new ArrayList<>();
        this.total = 0;
    }

    public String getCodigoVenta() { return codigoVenta; }
    public Vendedor getVendedor() { return vendedor; }
    public List<DetalleVenta> getDetalles() { return new ArrayList<>(detalles); }
    public double getTotal() { return total; }

    public int getCantidadTotalProductos() {
        return detalles.stream()
                .mapToInt(DetalleVenta::getCantidad)
                .sum();
    }
    
    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        total += detalle.getSubtotal();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Venta %s - Vendedor: %s\n", codigoVenta, vendedor.getNombre()));
        for (DetalleVenta d : detalles) {
            sb.append("  ").append(d).append("\n");
        }
        sb.append(String.format("  Total: $%,.0f", total));
        return sb.toString();
    }
}
