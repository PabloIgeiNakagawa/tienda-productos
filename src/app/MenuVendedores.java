package app;

import java.util.Scanner;

import exception.TiendaException;
import service.Tienda;

public class MenuVendedores {
    private Tienda tienda;
    private Scanner scanner;
    private UIHelper helper;

    public MenuVendedores(Tienda tienda, Scanner scanner) {
        this.tienda = tienda;
        this.scanner = scanner;
        this.helper = new UIHelper(scanner);
    }

    public void mostrar() {
        int opcion = 0;
        do {
            System.out.println("\n--- GESTIONAR VENDEDORES ---");
            System.out.println("1. Listar Vendedores");
            System.out.println("2. Registrar Vendedor");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        listarVendedores();
                        break;
                    case 2:
                        registrarVendedor();
                        break;
                    case 0:
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

    private void listarVendedores() {
        System.out.println("\n--- Lista de Vendedores ---");
        tienda.obtenerTodosLosVendedores().forEach(System.out::println);
    }

    private void registrarVendedor() {
        System.out.println("\n--- Registrar Vendedor ---");

        String nombre = helper.leerStringNoVacio("Nombre: ");
        double sueldo = helper.leerDoublePositivo("Sueldo Base: ");

        try {
            tienda.registrarVendedor(nombre, sueldo);
            System.out.println("Vendedor registrado con exito!");
        } catch (TiendaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
