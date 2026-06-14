package app;

import java.util.Scanner;

import service.ProductoService;
import service.Tienda;
import service.VentaService;
import service.VendedorService;
import service.util.CargadorDatos;

public class Main {
    private static ProductoService productoService;
    private static VendedorService vendedorService;
    private static VentaService ventaService;
    private static Scanner scanner = new Scanner(System.in);
    private static boolean datosCargados = false;

    public static void main(String[] args) {
        Tienda tienda = Tienda.crear();
        productoService = tienda.getProductoService();
        vendedorService = tienda.getVendedorService();
        ventaService = tienda.getVentaService();

        MenuProductos menuProductos = new MenuProductos(productoService, scanner);
        MenuVendedores menuVendedores = new MenuVendedores(vendedorService, scanner);
        VentaUI ventaUI = new VentaUI(ventaService, productoService, vendedorService, scanner);

        int opcion = 0;

        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gestionar Productos");
            System.out.println("2. Gestionar Vendedores");
            System.out.println("3. Registrar Venta");
            System.out.println("4. Ver Comisiones");
            System.out.println("5. Cargar datos de prueba");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        menuProductos.mostrar();
                        break;
                    case 2:
                        menuVendedores.mostrar();
                        break;
                    case 3:
                        ventaUI.ejecutarVenta();
                        break;
                    case 4:
                        ventaUI.mostrarComision();
                        break;
                    case 5:
                        cargarDatosPrueba();
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opcion invalida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un numero valido.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error inesperado: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void cargarDatosPrueba() {
        if (datosCargados) {
            System.out.println("Los datos ya fueron cargados anteriormente.");
            return;
        }
        CargadorDatos.cargar("data.csv", productoService, vendedorService);
        datosCargados = true;
        System.out.println("Datos cargados: " +
            productoService.obtenerTodos().size() + " productos, " +
            vendedorService.obtenerTodos().size() + " vendedores.");
    }
}
