package app;

import java.util.List;
import java.util.Scanner;

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

    public <T> void mostrarResultado(List<T> items) {
        if (items.isEmpty()) {
            System.out.println("No se encontraron resultados.");
        } else {
            items.forEach(System.out::println);
        }
    }
}
