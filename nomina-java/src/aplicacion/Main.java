package aplicacion;

import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

import modelo.Empleado;
import modelo.EmpleadoFijo;
import modelo.EmpleadoPorHora;

public class Main {

    // Arreglo simple: la cantidad de meses es fija (12) y está predefinida.
    private static final String[] meses = {
        "Enero", "Febrero", "Marzo", "Abril",
        "Mayo", "Junio", "Julio", "Agosto",
        "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    // ArrayList: la cantidad de empleados crece conforme se ingresan.
    // Guarda ambos tipos de empleado en una sola colección (polimorfismo).
    private static final ArrayList<Empleado> empleados = new ArrayList<>();

    private static final Scanner scanner = new Scanner(System.in);

    // Atributo (de la clase): lo pueden usar todos los métodos de Main.
    private static int numeroMes;

    public static void main(String[] args) {
        try {
            seleccionarMes();
            menuPrincipal();
        } catch (NoSuchElementException e) {
            // Se cerró la entrada (Ctrl+D / Ctrl+Z): salir sin error feo.
            System.out.println();
            System.out.println("Entrada finalizada. Programa terminado.");
        }
    }

    // ------------------------------------------------------------------
    // Selección del mes
    // ------------------------------------------------------------------

    private static void seleccionarMes() {
        limpiarPantalla();
        System.out.println("CÁLCULO DE NÓMINA");
        // Se valida entre 1 y 12 ANTES de usar el número como índice.
        numeroMes = leerEntero("Ingrese el mes a trabajar (1-12): ", 1, 12);
    }

    // ------------------------------------------------------------------
    // Menú principal
    // ------------------------------------------------------------------

    private static void menuPrincipal() {
        boolean salir = false;
        while (!salir) {
            limpiarPantalla();
            mostrarEncabezado();
            System.out.println("1 - Ingreso de nómina");
            System.out.println("2 - Impresión de nómina");
            System.out.println("3 - Salir");
            int opcion = leerEntero("Seleccione una opción: ", 1, 3);

            switch (opcion) {
                case 1:
                    ingresarNomina();
                    break;
                case 2:
                    imprimirNomina();
                    break;
                default:
                    salir = true;
                    limpiarPantalla();
                    System.out.println("Gracias por usar el sistema de nómina. ¡Hasta pronto!");
                    break;
            }
        }
    }

    // ------------------------------------------------------------------
    // Opción 1: ingreso de nómina
    // ------------------------------------------------------------------

    private static void ingresarNomina() {
        boolean otro = true;
        while (otro) {
            limpiarPantalla();
            mostrarEncabezado();
            System.out.println("INGRESO DE EMPLEADO A NÓMINA");
            System.out.println("Trabajadores actuales = " + empleados.size());
            System.out.println("1 - Empleado fijo");
            System.out.println("2 - Empleado por hora");
            int tipo = leerEntero("Seleccione el tipo de empleado: ", 1, 2);

            limpiarPantalla();
            mostrarEncabezado();
            Empleado nuevo;
            if (tipo == 1) {
                nuevo = capturarEmpleadoFijo();
            } else {
                nuevo = capturarEmpleadoPorHora();
            }

            // Solo se agrega cuando TODOS los datos ya son válidos.
            empleados.add(nuevo);

            System.out.println();
            System.out.println("Empleado agregado correctamente.");
            System.out.println("Trabajadores actuales = " + empleados.size());
            System.out.println();
            System.out.println("¿Desea ingresar otro empleado?");
            System.out.println("1 - Sí, ingresar otro empleado");
            System.out.println("2 - Volver al menú principal");
            int respuesta = leerEntero("Seleccione una opción: ", 1, 2);
            otro = (respuesta == 1);
        }
    }

    private static Empleado capturarEmpleadoFijo() {
        System.out.println("INGRESO DE EMPLEADO FIJO");
        String dpi = leerDpi();
        String nombre = leerNombre();
        double sueldo = leerDecimal("Sueldo mensual (Q): ");
        double bono = leerDecimal("Bonificación de este mes (Q): ");

        // Los valores capturados se pasan como ARGUMENTOS al constructor.
        EmpleadoFijo fijo = new EmpleadoFijo(dpi, nombre, sueldo, bono);
        System.out.println();
        System.out.println("Pago del mes = sueldo mensual + bonificación");
        System.out.println("Pago de " + fijo.getNombre() + " = Q " + formatear(fijo.calcularPago()));
        return fijo;
    }

    private static Empleado capturarEmpleadoPorHora() {
        System.out.println("INGRESO DE EMPLEADO POR HORA");
        String dpi = leerDpi();
        String nombre = leerNombre();
        double pagoHora = leerDecimal("Pago por hora (Q): ");
        double horas = leerDecimal("Horas trabajadas este mes: ");

        EmpleadoPorHora porHora = new EmpleadoPorHora(dpi, nombre, pagoHora, horas);
        System.out.println();
        System.out.println("Pago del mes = pago por hora x horas trabajadas");
        System.out.println("Pago de " + porHora.getNombre() + " = Q " + formatear(porHora.calcularPago()));
        return porHora;
    }

    // ------------------------------------------------------------------
    // Opción 2: impresión de nómina
    // ------------------------------------------------------------------

    private static void imprimirNomina() {
        limpiarPantalla();
        mostrarEncabezado();

        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados en la nómina de este mes");
        } else {
            System.out.printf("%-15s %-25s %-10s %12s%n", "DPI", "Nombre", "Tipo", "Pago (Q)");
            System.out.println("-".repeat(65));
        }

        double total = 0;
        // Recorrido del ArrayList con referencias de tipo Empleado.
        // calcularPago() es polimórfico: cada objeto ejecuta SU fórmula,
        // aquí no se decide nada con if / switch / instanceof.
        for (Empleado e : empleados) {
            double pago = e.calcularPago();
            total += pago;
            System.out.printf("%-15s %-25s %-10s %12s%n",
                    e.getDpi(), e.getNombre(), e.obtenerTipo(), formatear(pago));
        }

        System.out.println();
        System.out.println("Total de trabajadores: " + empleados.size());
        System.out.println("Total de nómina: Q " + formatear(total));
        System.out.println();
        System.out.print("Presione Enter para volver al menú principal...");
        scanner.nextLine();
    }

    // ------------------------------------------------------------------
    // Métodos de apoyo: encabezado, lectura y validación
    // ------------------------------------------------------------------

    // Único lugar donde se muestra el nombre del mes: meses[numeroMes - 1]
    // (los índices empiezan en 0). No se usa if/switch ni otra variable.
    private static void mostrarEncabezado() {
        System.out.println("NÓMINA DEL MES: " + meses[numeroMes - 1].toUpperCase());
        System.out.println();
    }

    // Método con parámetros (mensaje, minimo, maximo) que DEVUELVE un resultado.
    // 'linea' y 'valor' son variables locales: solo existen dentro del while.
    private static int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // cae al mensaje de error de abajo
            }
            System.out.println("Opción inválida. Ingrese un número entero entre "
                    + minimo + " y " + maximo + ".");
        }
    }

    private static String leerDpi() {
        while (true) {
            System.out.print("DPI: ");
            String dpi = scanner.nextLine().trim();
            if (dpi.matches("\\d{13}")) {
                return dpi;
            }
            System.out.println("DPI inválido. Debe tener exactamente 13 dígitos.");
        }
    }

    private static String leerNombre() {
        while (true) {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) {
                return nombre;
            }
            System.out.println("El nombre no puede estar vacío.");
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim().replace(',', '.');
            try {
                double valor = Double.parseDouble(linea);
                if (Double.isFinite(valor) && valor >= 0) {
                    return valor;
                }
                System.out.println("Valor inválido. Debe ser un número mayor o igual a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Ingrese un número (por ejemplo 25 o 120.5).");
            }
        }
    }

    // Montos en quetzales con dos decimales (punto decimal).
    private static String formatear(double monto) {
        return String.format(Locale.US, "%.2f", monto);
    }

    // Limpia la consola con códigos ANSI; si la consola del IDE no los
    // soporta (System.console() == null) se simula con líneas en blanco.
    private static void limpiarPantalla() {
        if (System.console() != null) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } else {
            for (int i = 0; i < 40; i++) {
                System.out.println();
            }
        }
    }
}
