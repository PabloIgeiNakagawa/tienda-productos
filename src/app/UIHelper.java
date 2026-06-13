package app;

import java.util.List;
import java.util.Scanner;

import exception.TiendaException;
import model.Producto;
import service.Tienda;

public class UIHelper {
    private Scanner scanner;

    public UIHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String leerStringNoVacio(String prompt) {
        String valor;
        do {
            System.out.print(prompt);
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("Error: El valor no puede estar vacio.");
            }
        } while (valor.isEmpty());
        return valor;
    }

    public double leerDoublePositivo(String prompt) {
        double valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(prompt);
            String str = scanner.nextLine().trim();
            try {
                valor = Double.parseDouble(str);
                if (valor <= 0) {
                    System.out.println("Error: El valor debe ser mayor a 0.");
                } else {
                    valido = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un numero valido.");
            }
        }
        return valor;
    }

    public int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public <T> T seleccionarDeLista(List<T> items, String titulo) {
        if (items.isEmpty()) {
            System.out.println("\nNo hay " + titulo.toLowerCase() + " disponibles.");
            return null;
        }

        System.out.println("\n" + titulo + " encontrados:");
        System.out.println("0. Volver");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }
        System.out.print("Seleccione una opcion: ");

        int opcion = leerEntero();
        if (opcion == 0) {
            return null;
        }
        if (opcion < 1 || opcion > items.size()) {
            System.out.println("Opcion invalida.");
            return null;
        }
        return items.get(opcion - 1);
    }

    public Producto seleccionarProductoPorCategoria(Tienda tienda) {
        List<String> categorias = tienda.obtenerCategorias();
        if (categorias.isEmpty()) {
            System.out.println("\nNo hay categorias disponibles.");
            return null;
        }

        System.out.println("\nCategorias disponibles:");
        System.out.println("0. Volver");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ". " + categorias.get(i));
        }
        System.out.print("Seleccione una categoria: ");

        int opcion = leerEntero();
        if (opcion == 0) {
            return null;
        }
        if (opcion < 1 || opcion > categorias.size()) {
            System.out.println("Opcion invalida.");
            return null;
        }
        String cat = categorias.get(opcion - 1);
        List<Producto> resultado = tienda.buscarProductosPorCategoria(cat);
        return seleccionarDeLista(resultado, "Productos");
    }

    public Producto seleccionarProductoPorNombre(Tienda tienda) {
        System.out.print("\nIngrese el termino a buscar en el nombre: ");
        String termino = scanner.nextLine();
        List<Producto> resultado = tienda.buscarProductosPorNombre(termino);
        return seleccionarDeLista(resultado, "Productos");
    }

    public Producto seleccionarProductoPorRangoPrecio(Tienda tienda) {
        try {
            System.out.print("\nIngrese precio minimo: ");
            double min = Double.parseDouble(scanner.nextLine());
            System.out.print("Ingrese precio maximo: ");
            double max = Double.parseDouble(scanner.nextLine());

            List<Producto> resultado = tienda.buscarProductosPorRangoPrecio(min, max);
            return seleccionarDeLista(resultado, "Productos");
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese valores numericos validos.");
            return null;
        }
    }

    public Producto seleccionarProductoPorCodigo(Tienda tienda) {
        System.out.print("\nIngrese el codigo a buscar: ");
        String codigo = scanner.nextLine();
        List<Producto> resultado = tienda.buscarProductosPorCodigo(codigo);
        return seleccionarDeLista(resultado, "Productos");
    }

    public void mostrarResultadoBusqueda(List<Producto> resultado) {
        if (resultado.isEmpty()) {
            System.out.println("No se encontraron productos.");
        } else {
            resultado.forEach(System.out::println);
        }
    }
}
