package modelo;

import java.io.Serializable;

/**
 * Clase abstracta Producto — Abstracción
 * ProductoFisico extends Producto / ProductoDigital extends Producto
 */
public abstract class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String categoria;

    public Producto(String id, String nombre, String descripcion,
                    double precio, int stock, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    // Método abstracto — polimorfismo
    public abstract String getTipo();

    // Lógica de stock con excepción
    public void reducirStock(int cantidad) throws Exception {
        if (cantidad > stock) {
            throw new Exception("Stock insuficiente. Disponible: " + stock);
        }
        this.stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return "[" + getTipo() + "] " + nombre + " - $" + String.format("%.2f", precio)
                + " | Stock: " + stock;
    }
}
