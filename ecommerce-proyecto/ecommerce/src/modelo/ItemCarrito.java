package modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ItemCarrito — representa un producto en el carrito con su cantidad
 */
public class ItemCarrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private Producto producto;
    private int cantidad;

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return producto.getNombre() + " x" + cantidad
                + " = $" + String.format("%.2f", getSubtotal());
    }
}
