package service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exception.ProductoException;
import model.Producto;

public class ProductoService {
    private List<Producto> productos;
    private int contadorProductos;

    public ProductoService() {
        this.productos = new ArrayList<>();
        this.contadorProductos = 1;
    }

    public void registrar(String nombre, double precio, String categoria) throws ProductoException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ProductoException("El nombre del producto no puede estar vacio.");
        }
        if (precio <= 0) {
            throw new ProductoException("El precio del producto debe ser mayor a 0.");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new ProductoException("La categoria del producto no puede estar vacia.");
        }

        String codigo = "P" + String.format("%03d", contadorProductos++);
        Producto p = new Producto(codigo, nombre, precio, categoria);
        productos.add(p);
    }

    public Producto buscarPorCodigo(String codigo) throws ProductoException {
        return productos.stream()
                .filter(p -> p.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElseThrow(() -> new ProductoException("Producto con codigo " + codigo + " no encontrado."));
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        return productos.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    public List<Producto> buscarPorNombre(String termino) {
        return productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(termino.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Producto> buscarPorRangoPrecio(double min, double max) {
        return productos.stream()
                .filter(p -> p.getPrecio() >= min && p.getPrecio() <= max)
                .collect(Collectors.toList());
    }

    public List<Producto> buscarPorCodigoLista(String codigo) {
        return productos.stream()
                .filter(p -> p.getCodigo().equalsIgnoreCase(codigo))
                .collect(Collectors.toList());
    }

    public List<Producto> obtenerTodos() {
        return new ArrayList<>(productos);
    }

    public List<String> obtenerCategorias() {
        return productos.stream()
                .map(Producto::getCategoria)
                .distinct()
                .collect(Collectors.toList());
    }
}
