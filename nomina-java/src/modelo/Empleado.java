package modelo;

/**
 * Clase abstracta: concentra los datos comunes (DPI y nombre) y obliga a las
 * clases hijas a definir su propio cálculo de pago. No se puede hacer
 * new Empleado(...).
 */
public abstract class Empleado {

    // Atributos: estado que pertenece al objeto. Son private (encapsulación).
    private String dpi;
    private String nombre;

    // Constructor: 'dpi' y 'nombre' son PARÁMETROS; 'this' distingue el
    // atributo del parámetro. Los constructores no declaran tipo de retorno.
    public Empleado(String dpi, String nombre) {
        this.dpi = dpi;
        this.nombre = nombre;
    }

    // Método abstracto: no tiene cuerpo, cada hija lo implementa.
    public abstract double calcularPago();

    // Cada hija sobrescribe este método para indicar su tipo.
    public abstract String obtenerTipo();

    // Getters (métodos de acceso). No se crean setters porque los datos
    // no se modifican después de registrar al empleado.
    public String getDpi() {
        return dpi;
    }

    public String getNombre() {
        return nombre;
    }
}
