# 🚚 SpeedFast App

Actividad formativa 5 (Semana 7)

---

## 📖 Descripción

Este proyecto es una aplicación de escritorio desarrollada en Java 25 y Maven que gestiona y simula el flujo logístico
de una empresa de despacho a domicilio en tiempo real. Combina un entorno gráfico en Java Swing, persistencia relacional
pura en MySQL y un motor de Simulación Multihilo Concurrente para procesar las rutas en paralelo de forma coordinada.

---

## 🚀 Cómo funciona el Entorno Concurrente y Transaccional  
La arquitectura del sistema resuelve la sincronización entre los hilos en memoria RAM y las tablas físicas en el disco
duro bajo tres reglas estrictas:

1. **Separación de Responsabilidades Estricta (MVC Pureza):** Las ventanas visuales (`View`) son pasivas y notifican
eventos mediante `ActionListener`. Los controladores (`Controller`) orquestan el flujo agnósticos a los componentes
gráficos. Toda la infraestructura JDBC e instrucciones SQL viven encapsuladas en la capa de datos (`DAO`).  


2. **Retiro Sincronizado Dirigido por Hilos:** Al iniciar la suite, el sistema despierta un hilo independiente (`Thread`)
en paralelo que implementa `Runnable` por cada repartidor real guardado en MySQL. Para evitar condiciones de carrera
(*Race Conditions*) o robos de tareas, el método `retirarPedidoPorRepartidor()` está blindado con **`synchronized`**,
cruzando los datos en memoria con la tabla intermedia para asegurar que cada trabajador extraiga únicamente los pedidos
asignados a su ID.


3. **Consistencia Transaccional Atómica:** El formulario de asignación manual ejecuta una transacción SQL unificada
dentro de `EntregaDAO`. Apaga el `setAutoCommit(false)` para garantizar que el `INSERT` de la entrega y el `UPDATE` del
estado del pedido ocurran bajo una misma unidad de trabajo. Si uno falla, se ejecuta un `rollback()` inmediato impidiendo
la corrupción de datos.

---

## 📁 Estructura del Proyecto

```text
speed-fast-s7-v2/
├── src/
│   └── main/
│       ├── java/
│       │   └── cl/
│       │       └── duoc/
│       │           └── speedfast/
│       │               ├── Main.java                              # Inicializa la GUI en el EDT y ejecuta diagnóstico JDBC
│       │               ├── database/
│       │               │   └── ConexionBD.java                    # Infraestructura de fábrica para conexiones MySQL
│       │               ├── controller/                            # Capa del Controlador (Orquestación de Negocio)
│       │               │   ├── ControladorPrincipal.java
│       │               │   ├── ControladorRepartoPedidos.java     # Motor de simulación y control de hilos
│       │               │   ├── ControladorRegistroPedido.java
│       │               │   ├── ControladorRegistroRepartidor.java
│       │               │   ├── ControladorRegistroEntrega.java
│       │               │   ├── ControladorListaPedidos.java
│       │               │   ├── ControladorListaRepartidores.java
│       │               │   └── ControladorListaEntregas.java
│       │               ├── model/
│       │               │   ├── dao/                               # Capa de Datos (Encapsulamiento de Sentencias JDBC)
│       │               │   │   ├── PedidoDAO.java
│       │               │   │   ├── RepartidorDAO.java
│       │               │   │   └── EntregaDAO.java
│       │               │   └── entity/                            # Capa del Modelo
│       │               │       ├── Pedido.java
│       │               │       ├── Repartidor.java                # Clase entidad con el ciclo de Runnable activo
│       │               │       ├── Entrega.java
│       │               │       ├── TipoPedido.java                # Enum
│       │               │       └── EstadoPedido.java              # Enum
│       │               ├── view/                                  # Capa de la Vista (Interfaces Gráficas Swing)
│       │               │   ├── VentanaPrincipal.java
│       │               │   ├── VentanaRegistroPedido.java
│       │               │   ├── VentanaRegistroRepartidor.java
│       │               │   ├── VentanaRegistroEntrega.java
│       │               │   ├── VentanaListaPedidos.java
│       │               │   ├── VentanaListaRepartidores.java
│       │               │   └── VentanaListaEntregas.java
│       │               └── event/
│       │                   └── LogListener.java                  # Interfaz funcional para desacoplamiento reactivo de bitácora
│       └── resources/
│           └── schema.sql                                        # Script de inicialización, llaves foráneas y restricciones
└── pom.xml                                                       # Archivo de configuración de dependencias de Maven
```

---

## 🛠️ Instrucciones para clonar y ejecutar

Requisitos del sistema:

* **JDK:** Java 25 (LTS) o superior

1. Clonar el repositorio desde la terminal de la computadora o IDE:  
   git clone https://github.com/alonsobonansco/speed-fast-s7-v2.git
2. Ir a File →️ Open y seleccionar la carpeta raíz del proyecto (la carpeta que contiene el archivo pom.xml).
3. Ejecutar el `Main` desde su clase en el paquete raíz `cl.duoc.speedfast`

---

## 👤 Autor

Alonso Bonansco Vergara  
Desarrollo Orientado a Objetos II - 004A  
Analista Programador Computacional
