package modelo;

/**
 * Empleado con sueldo mensual fijo y una bonificación del mes.
 * Pago del mes = sueldo mensual + bonificación.
 */
public class EmpleadoFijo extends Empleado {

    private double sueldoMensual;
    private double bonificacion;

    // super(...) inicializa los datos comunes (DPI y nombre) en la clase padre.
    public EmpleadoFijo(String dpi, String nombre, double sueldoMensual, double bonificacion) {
        super(dpi, nombre);
        this.sueldoMensual = sueldoMensual;
        this.bonificacion = bonificacion;
    }

    @Override
    public double calcularPago() {
        return sueldoMensual + bonificacion;
    }

    @Override
    public String obtenerTipo() {
        return "Fijo";
    }

    public double getSueldoMensual() {
        return sueldoMensual;
    }

    public double getBonificacion() {
        return bonificacion;
    }
}
