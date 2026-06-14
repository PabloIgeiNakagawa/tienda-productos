package service.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import exception.TiendaException;
import service.ProductoService;
import service.VendedorService;

public class CargadorDatos {

    public static void cargar(String rutaArchivo, ProductoService productoService, VendedorService vendedorService) {
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
                            productoService.registrar(nombre, precio, categoriaStr);
                            break;
                        case "VENDEDOR":
                            double sueldo = Double.parseDouble(precioStr);
                            vendedorService.registrar(nombre, sueldo);
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
            cargarDatosPorDefecto(productoService, vendedorService);
        }
    }

    private static void cargarDatosPorDefecto(ProductoService productoService, VendedorService vendedorService) {
        try {
            productoService.registrar("Notebook Asus", 850000, "Tecnologia");
            productoService.registrar("Mouse Logitech", 25000, "Tecnologia");
            productoService.registrar("Teclado Redragon", 45000, "Tecnologia");
            productoService.registrar("Cafetera Moulinex", 120000, "Electro");

            vendedorService.registrar("Ana Gomez", 300000);
            vendedorService.registrar("Carlos Perez", 320000);
        } catch (TiendaException e) {
            System.out.println("Error cargando datos por defecto: " + e.getMessage());
        }
    }
}
