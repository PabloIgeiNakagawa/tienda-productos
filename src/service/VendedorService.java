package service;

import java.util.ArrayList;
import java.util.List;

import exception.VendedorException;
import model.Vendedor;
import model.Venta;

public class VendedorService {
    private List<Vendedor> vendedores;
    private int contadorVendedores;

    public VendedorService(List<Vendedor> vendedores) {
        this.vendedores = vendedores;
        this.contadorVendedores = 1;
    }

    public void registrar(String nombre, double sueldo) throws VendedorException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new VendedorException("El nombre del vendedor no puede estar vacio.");
        }
        if (sueldo <= 0) {
            throw new VendedorException("El sueldo base del vendedor debe ser mayor a 0.");
        }

        String codigo = "V" + String.format("%03d", contadorVendedores++);
        Vendedor v = new Vendedor(codigo, nombre, sueldo);
        vendedores.add(v);
    }

    public Vendedor buscarPorCodigo(String codigo) throws VendedorException {
        return vendedores.stream()
                .filter(v -> v.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElseThrow(() -> new VendedorException("Vendedor con codigo " + codigo + " no encontrado."));
    }

    public List<Vendedor> obtenerTodos() {
        return new ArrayList<>(vendedores);
    }

    public double calcularComision(String codigoVendedor) throws VendedorException {
        Vendedor vendedor = buscarPorCodigo(codigoVendedor);

        List<Venta> ventasDelVendedor = vendedor.getVentas();

        if (ventasDelVendedor.isEmpty()) {
            return 0.0;
        }

        return ventasDelVendedor.stream()
                .mapToDouble(venta -> {
                    int productos = venta.getCantidadTotalProductos();
                    double porcentaje = (productos <= 2) ? 0.05 : 0.10;
                    return venta.getTotal() * porcentaje;
                })
                .sum();
    }
}
