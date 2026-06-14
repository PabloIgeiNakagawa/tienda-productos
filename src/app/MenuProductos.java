package app;

import java.util.List;
import java.util.Scanner;

import exception.TiendaException;
import model.Producto;
import service.ProductoService;

public class MenuProductos {
    private ProductoService productoService;
    private Scanner scanner;
    private UIHelper helper;

    public MenuProductos(ProductoService productoService, Scanner scanner) {
        this.productoService = productoService;
        this.scanner = scanner;
        this.helper = new UIHelper(productoService, scanner);
    }

    public void mostrar() {
        int opcion = 0;
        do {
            System.out.println("\n--- GESTIONAR PRODUCTOS ---");
            System.out.println("1. Listar Productos");
            System.out.println("2. Buscar por Categoria");
            System.out.println("3. Buscar por Nombre");
            System.out.println("4. Buscar por Rango de Precio");
            System.out.println("5. Buscar por Codigo");
            System.out.println("6. Registrar Producto");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        listarProductos();
                        break;
                    case 2:
                        buscarPorCategoria();
                        break;
                    case 3:
                        buscarPorNombre();
                        break;
                    case 4:
                        buscarPorRangoPrecio();
                        break;
                    case 5:
                        buscarPorCodigo();
                        break;
                    case 6:
                        registrarProducto();
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

    private void listarProductos() {
        System.out.println("\n--- Lista de Productos ---");
        productoService.obtenerTodos().forEach(System.out::println);
    }

    private void registrarProducto() {
        System.out.println("\n--- Registrar Producto ---");

        String nombre = helper.leerStringNoVacio("Nombre: ");
        double precio = helper.leerDoublePositivo("Precio: ");
        String categoria = helper.leerStringNoVacio("Categoria: ");

        try {
            productoService.registrar(nombre, precio, categoria);
            System.out.println("Producto registrado con exito!");
        } catch (TiendaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void buscarPorCategoria() {
        List<String> categorias = productoService.obtenerCategorias();
        if (categorias.isEmpty()) {
            System.out.println("\nNo hay categorias disponibles.");
            return;
        }

        System.out.println("\nCategorias disponibles:");
        System.out.println("0. Volver");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ". " + categorias.get(i));
        }
        System.out.print("Seleccione una categoria: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion == 0) {
                return;
            }
            if (opcion < 1 || opcion > categorias.size()) {
                System.out.println("Opcion invalida.");
                return;
            }
            String cat = categorias.get(opcion - 1);
            List<Producto> resultado = productoService.buscarPorCategoria(cat);
            helper.mostrarResultadoBusqueda(resultado);
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un numero valido.");
        }
    }

    private void buscarPorNombre() {
        System.out.print("\nIngrese el termino a buscar en el nombre: ");
        String termino = scanner.nextLine();
        List<Producto> resultado = productoService.buscarPorNombre(termino);
        helper.mostrarResultadoBusqueda(resultado);
    }

    private void buscarPorRangoPrecio() {
        try {
            System.out.print("\nIngrese precio minimo: ");
            double min = Double.parseDouble(scanner.nextLine());
            System.out.print("Ingrese precio maximo: ");
            double max = Double.parseDouble(scanner.nextLine());

            List<Producto> resultado = productoService.buscarPorRangoPrecio(min, max);
            helper.mostrarResultadoBusqueda(resultado);
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese valores numericos validos.");
        }
    }

    private void buscarPorCodigo() {
        System.out.print("\nIngrese el codigo a buscar: ");
        String codigo = scanner.nextLine();
        List<Producto> resultado = productoService.buscarPorCodigoLista(codigo);
        helper.mostrarResultadoBusqueda(resultado);
    }
}
