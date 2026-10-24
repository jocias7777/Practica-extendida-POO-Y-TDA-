# Práctica 11 – Nómina mensual en Java

**Universidad Mariano Gálvez – Programación II (Segundo semestre 2026)**
Aplicación de consola en Java: arreglos simples, `ArrayList` y POO.

## Estructura

```
src/
├── modelo/
│   ├── Empleado.java          (clase abstracta)
│   ├── EmpleadoFijo.java      (hija: sueldo + bonificación)
│   └── EmpleadoPorHora.java   (hija: pago por hora x horas)
└── aplicacion/
    └── Main.java              (menús, captura, reporte)
```

## Cómo compilar y ejecutar

Desde la carpeta del proyecto:

```bash
mkdir out
javac -encoding UTF-8 -d out src/modelo/*.java src/aplicacion/Main.java
java -cp out aplicacion.Main
```

También se puede abrir la carpeta en IntelliJ / Eclipse / NetBeans / VS Code y ejecutar `aplicacion.Main`.
Si en Windows los acentos se ven raros, ejecutar antes `chcp 65001` o usar `java -Dstdout.encoding=UTF-8 -cp out aplicacion.Main`.

## Caso de comprobación (sección 11)

Ingresar mes `3` → muestra **MARZO**. Registrar:

- Ana López (fija): sueldo Q 4000.00 + bonificación Q 300.00 → Q 4300.00
- Luis Pérez (por hora): Q 25.00 x 120 h → Q 3000.00

Reporte: **2 trabajadores**, **total Q 7300.00**.

---

## Explicación de conceptos (evidencia, sección 15)

### Organización y creación de objetos

| Concepto | Dónde y por qué |
|---|---|
| **Proyecto, package, import** | `modelo` guarda los empleados y `aplicacion` guarda `Main`. Cada archivo declara su `package`; `Main` usa `import` para `ArrayList`, `Scanner`, `Locale` y las clases de `modelo`. |
| **Clase, objeto, instancia, instanciación** | Cada clase está en su propio `.java`. En `Main.capturarEmpleadoFijo()` se hace `new EmpleadoFijo(...)`: cada empleado es una instancia independiente que luego se agrega al `ArrayList`. |
| **Constructores, parámetros, argumentos, `this`** | `public EmpleadoFijo(String dpi, String nombre, double sueldoMensual, double bonificacion)`: `dpi`, `nombre`, `sueldoMensual` y `bonificacion` son los **parámetros** (se declaran en el constructor). En `Main` se llama `new EmpleadoFijo(dpi, nombre, sueldo, bono)`: `dpi`, `nombre`, `sueldo` y `bono` son los **argumentos** (los valores reales que se pasan). `this.sueldoMensual = sueldoMensual;` usa `this` para distinguir el atributo del parámetro. Los constructores no tienen tipo de retorno. |

### Encapsulación, abstracción y herencia

| Concepto | Dónde y por qué |
|---|---|
| **Atributos y modificadores de acceso** | Todos los atributos (`dpi`, `nombre`, `sueldoMensual`, `bonificacion`, `pagoPorHora`, `horasTrabajadas`, y en `Main` `meses`, `empleados`, `scanner`, `numeroMes`) son `private`. Se consultan con getters (`getDpi()`, `getNombre()`, ...). No hay setters porque los datos no se modifican después de registrar al empleado. |
| **Clase y método abstractos** | `Empleado` es `abstract` y declara `public abstract double calcularPago();`. No se puede hacer `new Empleado(...)`. Concentra lo común (DPI y nombre); las hijas definen el cálculo. |
| **Herencia y sobrescritura** | `EmpleadoFijo` y `EmpleadoPorHora` usan `extends Empleado`, llaman a `super(dpi, nombre)` para inicializar los datos comunes y sobrescriben `calcularPago()` y `obtenerTipo()` con `@Override`. No duplican DPI ni nombre. |
| **Polimorfismo** | `ArrayList<Empleado>` guarda ambos tipos. En `Main.imprimirNomina()` se recorre con `for (Empleado e : empleados)` y se llama `e.calcularPago()`: cada objeto ejecuta su propia fórmula. No hay `if`, `switch` ni `instanceof` para decidir la fórmula. |

### Métodos, ámbito y alcance

- **`main` y delegación:** `main` solo llama a `seleccionarMes()` y `menuPrincipal()`. El resto se reparte en métodos: `ingresarNomina()`, `capturarEmpleadoFijo()`, `capturarEmpleadoPorHora()`, `imprimirNomina()`, `leerDpi()`, `leerNombre()`, `leerDecimal()`, etc.
- **Métodos con parámetros:** `leerEntero(String mensaje, int minimo, int maximo)` y `leerDecimal(String mensaje)`.
- **Método que devuelve un resultado:** `leerEntero` devuelve `int`, `leerDpi` devuelve `String`, `capturarEmpleadoFijo` devuelve `Empleado`.
- **Atributo, parámetro y variable local (ejemplo en `leerEntero`):**
  - *Atributo:* `numeroMes` (de `Main`) → se puede usar en cualquier método de la clase.
  - *Parámetro:* `minimo` / `maximo` → solo dentro de `leerEntero`.
  - *Variable local:* `linea` y `valor` → solo dentro del bloque `while` / `try` donde se declaran.

### Arreglo simple y ArrayList

- **`String[] meses`**: 12 nombres predefinidos en `Main`; el tamaño es fijo.
- **`meses[numeroMes - 1]`**: se usa en `mostrarEncabezado()` (único lugar donde se muestra el mes). El `- 1` es porque los índices empiezan en 0: si el usuario ingresa 3, `meses[2]` es "Marzo". El número se valida entre 1 y 12 **antes** de usarlo como índice.
- **`ArrayList<Empleado> empleados`**: crece conforme se ingresan empleados.
  - `empleados.add(nuevo)` → se ejecuta solo cuando todos los datos son válidos.
  - `empleados.size()` → contador de "Trabajadores actuales" y "Total de trabajadores".
  - Recorrido con `for (Empleado e : empleados)` → imprime el reporte y suma el total.

### Validaciones

- Mes: entero entre 1 y 12.
- Menús: solo las opciones indicadas.
- DPI: `String` con exactamente 13 dígitos.
- Nombre: no vacío ni solo espacios.
- Sueldo, bonificación, pago por hora y horas: número finito `>= 0` (se aceptan decimales; texto, negativos y `NaN` se rechazan).
- Ante un dato inválido se muestra el error y se vuelve a pedir ese dato; el empleado no se guarda hasta tener todos sus datos correctos.
