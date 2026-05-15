package modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Carrito de compras — usa ArrayList (Colecciones)
 */
public class Carrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<ItemCarrito> items;

    public Carrito() {
        this.items = new ArrayList<>();
    }

    public void agregarProducto(Producto producto, int cantidad) throws Exception {
        // Verificar stock antes de agregar
        if (cantidad > producto.getStock()) {
            throw new Exception("Stock insuficiente. Disponible: " + producto.getStock());
        }

        // Si ya existe, sumar cantidad
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                int nuevaCantidad = item.getCantidad() + cantidad;
                if (nuevaCantidad > producto.getStock()) {
                    throw new Exception("No hay suficiente stock. Disponible: " + producto.getStock());
                }
                item.setCantidad(nuevaCantidad);
                return;
            }
        }
        items.add(new ItemCarrito(producto, cantidad));
    }

    public void eliminarProducto(String idProducto) throws Exception {
        ItemCarrito aEliminar = null;
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(idProducto)) {
                aEliminar = item;
                break;
            }
        }
        if (aEliminar == null) {
            throw new Exception("Producto no encontrado en el carrito.");
        }
        items.remove(aEliminar);
    }

    public double getTotal() {
        double total = 0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void vaciar() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public ArrayList<ItemCarrito> getItems() { return items; }

    public int getTotalItems() {
        int total = 0;
        for (ItemCarrito item : items) {
            total += item.getCantidad();
        }
        return total;
    }
}
