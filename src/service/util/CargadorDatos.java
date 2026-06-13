package service.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import exception.TiendaException;
import service.Tienda;

public class CargadorDatos {

    public static void cargar(String rutaArchivo, Tienda service) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numLinea = 0;

            while ((linea = br.readLine()) != null) {
                numLinea++;
                linea = linea.trim();

                if (linea.isEmpty() || linea.startsWith("TIPO;")) {
                    continue;
                }

                try {
                    String[] partes = linea.split(";");
                    if (partes.length < 3) {
                        System.out.println("Linea " + numLinea + ": formato incompleto, se omite.");
                        continue;
                    }

                    String tipo = partes[0].trim().toUpperCase();
                    String nombre = partes[1].trim();
                    String precioStr = partes[2].trim();
                    String categoriaStr = partes.length > 3 ? partes[3].trim() : "";

                    switch (tipo) {
                        case "PRODUCTO":
                            double precio = Double.parseDouble(precioStr);
                            service.registrarProducto(nombre, precio, categoriaStr);
                            break;
                        case "VENDEDOR":
                            double sueldo = Double.parseDouble(precioStr);
                            service.registrarVendedor(nombre, sueldo);
                            break;
                        default:
                            System.out.println("Linea " + numLinea + ": tipo desconocido '" + tipo + "', se omite.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Linea " + numLinea + ": error en formato numerico, se omite.");
                } catch (TiendaException e) {
                    System.out.println("Linea " + numLinea + ": " + e.getMessage());
                }
            }

            System.out.println("Datos cargados correctamente desde " + rutaArchivo);

        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo '" + rutaArchivo + "': " + e.getMessage());
            System.out.println("Cargando datos por defecto...");
            cargarDatosPorDefecto(service);
        }
    }

    private static void cargarDatosPorDefecto(Tienda service) {
        try {
            service.registrarProducto("Notebook Asus", 850000, "Tecnologia");
            service.registrarProducto("Mouse Logitech", 25000, "Tecnologia");
            service.registrarProducto("Teclado Redragon", 45000, "Tecnologia");
            service.registrarProducto("Cafetera Moulinex", 120000, "Electro");

            service.registrarVendedor("Ana Gomez", 300000);
            service.registrarVendedor("Carlos Perez", 320000);
        } catch (TiendaException e) {
            System.out.println("Error cargando datos por defecto: " + e.getMessage());
        }
    }
}
