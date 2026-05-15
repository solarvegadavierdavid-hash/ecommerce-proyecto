# 🛒 Mi Tienda Online

> Proyecto Final — Java POO · 2026

## 👥 Integrantes

| Nombre | GitHub |
|--------|--------|
| [Nombre 1] | [@usuario](https://github.com/usuario) |
| [Nombre 2] | [@usuario](https://github.com/usuario) |
| [Nombre 3] | [@usuario](https://github.com/usuario) |
| [Nombre 4] | [@usuario](https://github.com/usuario) |

---

## 📋 Descripción

Sistema de comercio electrónico desarrollado en Java con interfaz gráfica Swing. Permite gestionar productos físicos y digitales, registrar clientes, manejar un carrito de compras y completar el flujo de pedido con persistencia de datos mediante serialización.

---

## 🚀 Cómo ejecutar

### Requisitos
- Java JDK 17+
- NetBeans IDE (recomendado) o cualquier IDE con soporte Java

### Pasos en NetBeans
1. Clonar el repositorio:
```bash
git clone https://github.com/Antomaker/competencia.git
```
2. Abrir NetBeans → **File → Open Project** → seleccionar la carpeta del proyecto
3. Clic derecho en el proyecto → **Run** (o `F6`)

> ⚠️ La carpeta `datos/` se crea automáticamente en la primera ejecución. No es necesario configurar ninguna base de datos.

### Credenciales de demo
| Rol | Email | Contraseña |
|-----|-------|------------|
| Admin | admin@tienda.com | admin123 |
| Cliente | maria@email.com | 123456 |
| Cliente | carlos@email.com | 123456 |

---

## 🏗️ Tecnologías usadas

| Categoría | Tecnología |
|-----------|-----------|
| Lenguaje | Java 17 |
| UI | Swing |
| Persistencia | Serialización Java (.dat) |
| IDE | NetBeans |
| Control de versiones | Git + GitHub |

---

## 🧩 Diagrama de clases UML

![Diagrama de clases](docs/uml/diagrama-clases.png)

---

## 📐 Diagrama de casos de uso

![Casos de uso](docs/uml/casos-de-uso.png)

---

## 🎯 Funcionalidades implementadas

- [x] Gestión de productos (CRUD con control de stock)
- [x] Gestión de usuarios (registro/login con roles Cliente y Admin)
- [x] Carrito de compras (agregar, eliminar, calcular total)
- [x] Flujo completo: carrito → método de pago → pedido generado → stock actualizado
- [x] Historial de pedidos del cliente
- [x] Interfaz gráfica Swing funcional
- [x] Persistencia de datos (archivos .dat, sobrevive al cierre)
- [x] Búsqueda de productos por nombre o categoría

---

## 📐 Conceptos POO aplicados

| Concepto | Clase / método donde se aplica |
|----------|-------------------------------|
| Herencia | `Cliente extends Usuario`, `Administrador extends Usuario`, `ProductoFisico extends Producto`, `ProductoDigital extends Producto`, `PagoTarjeta extends Pago` |
| Encapsulación | Todos los atributos `private` con getters/setters en `Usuario`, `Producto`, `Pedido`, `Carrito` |
| Polimorfismo | `getRol()` y `getTipo()` con `@Override` en subclases; `procesarPago()` en subclases de `Pago` |
| Abstracción | Clase abstracta `Usuario`, clase abstracta `Producto`, clase abstracta `Pago`, interface `Pagable` |
| Colecciones | `ArrayList<Producto>`, `ArrayList<Pedido>`, `ArrayList<ItemCarrito>`, `ArrayList<Usuario>` en `Controlador` y `Carrito` |
| Excepciones | `try/catch` en `Controlador.login()`, `Carrito.agregarProducto()`, `Producto.reducirStock()`, `GestorDatos` |

---

## 📁 Estructura del proyecto

```
ecommerce/
├── src/
│   ├── Main.java
│   ├── modelo/
│   │   ├── Usuario.java         ← abstract
│   │   ├── Cliente.java         ← extends Usuario
│   │   ├── Administrador.java   ← extends Usuario
│   │   ├── Producto.java        ← abstract
│   │   ├── ProductoFisico.java  ← extends Producto
│   │   ├── ProductoDigital.java ← extends Producto
│   │   ├── Pago.java            ← abstract + subclases
│   │   ├── Pagable.java         ← interface
│   │   ├── Carrito.java
│   │   ├── ItemCarrito.java
│   │   └── Pedido.java
│   ├── datos/
│   │   ├── GestorDatos.java     ← persistencia .dat
│   │   └── Controlador.java     ← lógica del negocio
│   ├── ui/
│   │   ├── VentanaLogin.java
│   │   ├── VentanaCliente.java
│   │   ├── VentanaAdmin.java
│   │   └── DialogoRegistro.java
│   └── excepciones/
│       └── StockInsuficienteException.java
├── docs/uml/
├── assets/
└── README.md
```
