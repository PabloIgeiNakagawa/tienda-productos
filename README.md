# Tienda Productos

Sistema de gestión de tienda desarrollado en Java. Aplicación de consola con almacenamiento de datos en memoria.

## Características

- **Gestión de productos**: alta, búsqueda por categoría, nombre, rango de precio y código
- **Gestión de vendedores**: alta con sueldo base
- **Registro de ventas**: asociación producto-vendedor con detalle de cantidades
- **Cálculo de comisiones**: 5% por ventas de hasta 2 productos, 10% por más de 2 productos (evaluación individual por venta)
- **Manejo de excepciones**: jerarquía de excepciones personalizadas con separación por dominio
- **Carga de datos**: importación de productos y vendedores desde archivo CSV
- **Diagrama de Entidad Relación**: modelo de datos documentado en DBML

## Estructura del proyecto

```
tienda-productos/
├── data.csv                  # Archivo de datos (separado por ;)
├── src/
│   ├── app/                  # Interfaz de consola
│   │   ├── Main.java
│   │   ├── MenuProductos.java
│   │   ├── MenuVendedores.java
│   │   ├── VentaUI.java
│   │   └── UIHelper.java
│   ├── model/                # Modelos de dominio
│   │   ├── Producto.java
│   │   ├── Vendedor.java
│   │   ├── Venta.java
│   │   └── DetalleVenta.java
│   ├── service/              # Lógica de negocio
│   │   ├── Tienda.java       # Fachada
│   │   ├── ProductoService.java
│   │   ├── VendedorService.java
│   │   ├── VentaService.java
│   │   └── util/
│   │       └── CargadorDatos.java
│   └── exception/            # Excepciones personalizadas
│       ├── TiendaException.java
│       ├── ProductoException.java
│       └── VendedorException.java
├── test/
│   └── service/              # Tests unitarios (JUnit 5)
│       ├── TiendaServiceTest.java
│       ├── ProductoServiceTest.java
│       ├── VendedorServiceTest.java
│       └── VentaServiceTest.java
└── lib/                      # Dependencias (JUnit 5)
```

## Requisitos

- JDK 21 o superior

## Compilación y ejecución

```bash
# Compilar
javac -d bin -cp "src;lib/*" $(find src -name "*.java")

# Ejecutar
java -cp "bin;lib/*" app.Main
```

En Windows (PowerShell):

```powershell
# Compilar
$files = Get-ChildItem -Recurse -Include *.java src | ForEach-Object { $_.FullName }
javac -d bin -cp "src;lib/*" $files

# Ejecutar
java -cp "bin;lib/*" app.Main
```

## Ejecutar tests

```bash
java -jar lib/junit-platform-console-standalone-1.10.2.jar \
  --classpath "bin;lib/*" \
  --scan-classpath=bin
```

## Carga de datos

Desde el menú principal, seleccionar la opción **5. Cargar datos de prueba** para importar productos y vendedores desde `data.csv`.

Formato del CSV (separado por `;`):

```csv
TIPO;NOMBRE;PRECIO;CATEGORIA
PRODUCTO;Mouse Logitech;25000;Tecnologia
VENDEDOR;Ana Gomez;300000;
```

## Diagrama de Entidad Relación
<img width="1094" height="539" alt="DER" src="https://github.com/user-attachments/assets/62ac3787-5a7c-4ca4-a0dd-d32de0c5629b" />
