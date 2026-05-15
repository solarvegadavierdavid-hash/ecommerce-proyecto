package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Pedido — generado al confirmar el carrito
 */
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Estado { PENDIENTE, PROCESANDO, ENVIADO, ENTREGADO, CANCELADO }

    private String idPedido;
    private String idCliente;
    private String nombreCliente;
    private ArrayList<ItemCarrito> items;
    private double total;
    private Estado estado;
    private String fechaCreacion;
    private String metodoPago;

    public Pedido(String idPedido, String idCliente, String nombreCliente,
                  ArrayList<ItemCarrito> items, double total, String metodoPago) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.items = new ArrayList<>(items);
        this.total = total;
        this.estado = Estado.PENDIENTE;
        this.metodoPago = metodoPago;
        this.fechaCreacion = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // Getters y Setters
    public String getIdPedido() { return idPedido; }
    public String getIdCliente() { return idCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public ArrayList<ItemCarrito> getItems() { return items; }
    public double getTotal() { return total; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public String getFechaCreacion() { return fechaCreacion; }
    public String getMetodoPago() { return metodoPago; }

    @Override
    public String toString() {
        return "Pedido #" + idPedido + " | " + fechaCreacion
                + " | Total: $" + String.format("%.2f", total)
                + " | Estado: " + estado;
    }
}
