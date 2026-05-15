package datos;

import modelo.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Controlador — maneja toda la lógica del negocio
 */
public class Controlador {

    private ArrayList<Producto> productos;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Pedido> pedidos;
    private GestorDatos gestor;
    private Usuario usuarioActual;
    private Carrito carrito;

    public Controlador() {
        this.gestor = new GestorDatos();
        this.productos = gestor.cargarProductos();
        this.usuarios = gestor.cargarUsuarios();
        this.pedidos = gestor.cargarPedidos();
        this.carrito = new Carrito();
    }

    // ─── AUTENTICACIÓN ──────────────────────────────────────────────────────

    public Usuario login(String email, String password) throws Exception {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                this.usuarioActual = u;
                this.carrito = new Carrito(); // carrito nuevo al iniciar sesión
                return u;
            }
        }
        throw new Exception("Email o contraseña incorrectos.");
    }

    public void logout() {
        this.usuarioActual = null;
        this.carrito = new Carrito();
    }

    public Cliente registrarCliente(String nombre, String email, String password,
                                    String direccion, String telefono) throws Exception {
        // Verificar email único
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new Exception("Ya existe una cuenta con ese email.");
            }
        }
        if (nombre.isBlank() || email.isBlank() || password.length() < 4) {
            throw new Exception("Datos inválidos. La contraseña debe tener al menos 4 caracteres.");
        }
        String id = "U" + String.format("%03d", usuarios.size() + 1);
        Cliente nuevo = new Cliente(id, nombre, email, password, direccion, telefono);
        usuarios.add(nuevo);
        gestor.guardarUsuarios(usuarios);
        return nuevo;
    }

    // ─── PRODUCTOS ──────────────────────────────────────────────────────────

    public ArrayList<Producto> getProductos() { return productos; }

    public ArrayList<Producto> buscarProductos(String texto) {
        ArrayList<Producto> resultado = new ArrayList<>();
        String lower = texto.toLowerCase();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(lower)
                    || p.getCategoria().toLowerCase().contains(lower)
                    || p.getDescripcion().toLowerCase().contains(lower)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public void agregarProducto(Producto p) throws Exception {
        for (Producto existente : productos) {
            if (existente.getId().equals(p.getId())) {
                throw new Exception("Ya existe un producto con ese ID.");
            }
        }
        productos.add(p);
        gestor.guardarProductos(productos);
    }

    public void actualizarProducto(Producto p) throws Exception {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId().equals(p.getId())) {
                productos.set(i, p);
                gestor.guardarProductos(productos);
                return;
            }
        }
        throw new Exception("Producto no encontrado.");
    }

    public void eliminarProducto(String idProducto) throws Exception {
        Producto aEliminar = null;
        for (Producto p : productos) {
            if (p.getId().equals(idProducto)) {
                aEliminar = p;
                break;
            }
        }
        if (aEliminar == null) throw new Exception("Producto no encontrado.");
        productos.remove(aEliminar);
        gestor.guardarProductos(productos);
    }

    public Producto buscarProductoPorId(String id) {
        for (Producto p : productos) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    // ─── CARRITO ────────────────────────────────────────────────────────────

    public void agregarAlCarrito(String idProducto, int cantidad) throws Exception {
        Producto p = buscarProductoPorId(idProducto);
        if (p == null) throw new Exception("Producto no encontrado.");
        carrito.agregarProducto(p, cantidad);
    }

    public void eliminarDelCarrito(String idProducto) throws Exception {
        carrito.eliminarProducto(idProducto);
    }

    public Carrito getCarrito() { return carrito; }

    // ─── PEDIDOS ────────────────────────────────────────────────────────────

    public Pedido confirmarPedido(String metodoPago) throws Exception {
        if (usuarioActual == null) throw new Exception("Debe iniciar sesión.");
        if (carrito.estaVacio()) throw new Exception("El carrito está vacío.");

        // Reducir stock de cada producto
        for (ItemCarrito item : carrito.getItems()) {
            try {
                item.getProducto().reducirStock(item.getCantidad());
            } catch (Exception e) {
                throw new Exception("Error en producto '" + item.getProducto().getNombre()
                        + "': " + e.getMessage());
            }
        }

        String idPedido = "PED-" + System.currentTimeMillis();
        Pedido pedido = new Pedido(idPedido, usuarioActual.getId(),
                usuarioActual.getNombre(), carrito.getItems(),
                carrito.getTotal(), metodoPago);

        pedidos.add(pedido);
        gestor.guardarPedidos(pedidos);
        gestor.guardarProductos(productos);

        // Si es cliente, agregar al historial
        if (usuarioActual instanceof Cliente) {
            ((Cliente) usuarioActual).agregarPedido(pedido);
            gestor.guardarUsuarios(usuarios);
        }

        carrito.vaciar();
        return pedido;
    }

    public ArrayList<Pedido> getPedidosCliente(String idCliente) {
        ArrayList<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getIdCliente().equals(idCliente)) resultado.add(p);
        }
        return resultado;
    }

    public ArrayList<Pedido> getTodosPedidos() { return pedidos; }

    public void actualizarEstadoPedido(String idPedido, Pedido.Estado nuevoEstado) throws Exception {
        for (Pedido p : pedidos) {
            if (p.getIdPedido().equals(idPedido)) {
                p.setEstado(nuevoEstado);
                gestor.guardarPedidos(pedidos);
                return;
            }
        }
        throw new Exception("Pedido no encontrado.");
    }

    public ArrayList<Usuario> getUsuarios() { return usuarios; }
    public Usuario getUsuarioActual() { return usuarioActual; }

    public String generarIdProducto() {
        return "P" + String.format("%03d", productos.size() + 1);
    }
}
