package model;

import java.util.ArrayList;
import java.util.List;

public class Vendedor {
    private String codigo;
    private String nombre;
    private double sueldoBase;
    private List<Venta> ventas;

    public Vendedor(String codigo, String nombre, double sueldoBase) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.sueldoBase = sueldoBase;
        this.ventas = new ArrayList<>();
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getSueldoBase() { return sueldoBase; }
    public List<Venta> getVentas() { return new ArrayList<>(ventas); }

    public void agregarVenta(Venta venta) {
        ventas.add(venta);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s | Sueldo: $%,.0f", codigo, nombre, sueldoBase);
    }
}
