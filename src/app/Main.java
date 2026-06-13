package app;

import java.util.Scanner;

import service.Tienda;
import service.util.CargadorDatos;

public class Main {
    private static Tienda tienda = new Tienda();
    private static Scanner scanner = new Scanner(System.in);
    private static boolean datosCargados = false;

    public static void main(String[] args) {
        MenuProductos menuProductos = new MenuProductos(tienda, scanner);
        MenuVendedores menuVendedores = new MenuVendedores(tienda, scanner);
        VentaUI ventaUI = new VentaUI(tienda, scanner);

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
        CargadorDatos.cargar("data.csv", tienda);
        datosCargados = true;
        System.out.println("Datos cargados: " +
            tienda.obtenerTodosLosProductos().size() + " productos, " +
            tienda.obtenerTodosLosVendedores().size() + " vendedores.");
    }
}
