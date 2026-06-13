package app;

import java.util.List;
import java.util.Scanner;

import exception.TiendaException;
import model.Producto;
import model.Vendedor;
import model.Venta;
import service.Tienda;

public class VentaUI {
    private Tienda tienda;
    private Scanner scanner;
    private UIHelper helper;

    public VentaUI(Tienda tienda, Scanner scanner) {
        this.tienda = tienda;
        this.scanner = scanner;
        this.helper = new UIHelper(scanner);
    }

    public void ejecutarVenta() {
        System.out.println("\n--- Registrar Venta ---");

        Vendedor vendedor = seleccionarVendedor();
        if (vendedor == null) {
            return;
        }

        try {
            Venta venta = tienda.crearVenta(vendedor.getCodigo());
            boolean agregandoProductos = true;

            while (agregandoProductos) {
                Producto producto = seleccionarProducto();
                if (producto == null) {
                    if (venta.getDetalles().isEmpty()) {
                        System.out.println("Venta cancelada.");
                        return;
                    }
                    break;
                }

                int cantidad = leerCantidad();
                tienda.agregarDetalleAVenta(venta, producto.getCodigo(), cantidad);
                System.out.println("Producto agregado: " + producto.getNombre() + " x" + cantidad);

                System.out.print("\nDesea agregar otro producto? (S/N): ");
                String respuesta = scanner.nextLine().trim().toUpperCase();
                if (!respuesta.equals("S")) {
                    agregandoProductos = false;
                }
            }

            mostrarResumen(venta);
            confirmarVenta(venta);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void mostrarComision() {
        List<Vendedor> vendedores = tienda.obtenerTodosLosVendedores();

        if (vendedores.isEmpty()) {
            System.out.println("\nNo hay vendedores disponibles.");
            return;
        }

        System.out.println("\n--- Calcular Comision ---");
        for (int i = 0; i < vendedores.size(); i++) {
            System.out.println((i + 1) + ". " + vendedores.get(i));
        }
        System.out.println("0. Volver");
        System.out.print("Seleccione un vendedor: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion == 0) {
                return;
            }
            if (opcion < 1 || opcion > vendedores.size()) {
                System.out.println("Opcion invalida.");
                return;
            }

            String codVend = vendedores.get(opcion - 1).getCodigo();
            double comision = tienda.calcularComisionVendedor(codVend);
            System.out.printf("La comision acumulada para %s es: $%,.0f\n",
                vendedores.get(opcion - 1).getNombre(), comision);
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un numero valido.");
        } catch (TiendaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Producto seleccionarProducto() {
        int opcion = 0;
        do {
            System.out.println("\n--- Seleccionar Producto ---");
            System.out.println("1. Listar todos");
            System.out.println("2. Buscar por Categoria");
            System.out.println("3. Buscar por Nombre");
            System.out.println("4. Buscar por Rango de Precio");
            System.out.println("5. Buscar por Codigo");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                Producto producto = null;

                switch (opcion) {
                    case 1:
                        producto = helper.seleccionarDeLista(tienda.obtenerTodosLosProductos(), "Productos");
                        break;
                    case 2:
                        producto = helper.seleccionarProductoPorCategoria(tienda);
                        break;
                    case 3:
                        producto = helper.seleccionarProductoPorNombre(tienda);
                        break;
                    case 4:
                        producto = helper.seleccionarProductoPorRangoPrecio(tienda);
                        break;
                    case 5:
                        producto = helper.seleccionarProductoPorCodigo(tienda);
                        break;
                    case 0:
                        return null;
                    default:
                        System.out.println("Opcion invalida. Intente de nuevo.");
                }

                if (producto != null) {
                    return producto;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un numero valido.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error inesperado: " + e.getMessage());
            }
        } while (opcion != 0);

        return null;
    }

    private Vendedor seleccionarVendedor() {
        List<Vendedor> vendedores = tienda.obtenerTodosLosVendedores();

        if (vendedores.isEmpty()) {
            System.out.println("\nNo hay vendedores disponibles.");
            return null;
        }

        System.out.println("\n--- Seleccionar Vendedor ---");
        for (int i = 0; i < vendedores.size(); i++) {
            System.out.println((i + 1) + ". " + vendedores.get(i));
        }
        System.out.println("0. Volver");
        System.out.print("Seleccione un vendedor: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion == 0) {
                return null;
            }
            if (opcion < 1 || opcion > vendedores.size()) {
                System.out.println("Opcion invalida.");
                return null;
            }
            return vendedores.get(opcion - 1);
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un numero valido.");
            return null;
        }
    }

    private int leerCantidad() {
        int cantidad = 0;
        boolean cantidadValida = false;
        while (!cantidadValida) {
            System.out.print("Cantidad: ");
            String cantidadStr = scanner.nextLine().trim();
            try {
                cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    System.out.println("Error: La cantidad debe ser mayor a 0.");
                } else {
                    cantidadValida = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un numero valido.");
            }
        }
        return cantidad;
    }

    private void mostrarResumen(Venta venta) {
        System.out.println("\n--- Resumen de la Venta ---");
        System.out.println("Vendedor: " + venta.getVendedor().getNombre());
        System.out.println("Productos:");
        venta.getDetalles().forEach(d -> System.out.println("  " + d));
        System.out.printf("Total: $%,.0f\n", venta.getTotal());
    }

    private void confirmarVenta(Venta venta) {
        System.out.print("\nConfirmar venta? (S/N): ");
        String confirmar = scanner.nextLine().trim().toUpperCase();
        if (confirmar.equals("S")) {
            tienda.confirmarVenta(venta);
            System.out.println("Venta registrada con exito!");
        } else {
            System.out.println("Venta cancelada.");
        }
    }
}
