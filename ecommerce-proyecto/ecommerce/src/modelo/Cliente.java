package modelo;

import java.util.ArrayList;

/**
 * Cliente extends Usuario — Herencia
 */
public class Cliente extends Usuario {

    private static final long serialVersionUID = 1L;

    private String direccion;
    private String telefono;
    private ArrayList<Pedido> historialPedidos;

    public Cliente(String id, String nombre, String email, String password,
                   String direccion, String telefono) {
        super(id, nombre, email, password);
        this.direccion = direccion;
        this.telefono = telefono;
        this.historialPedidos = new ArrayList<>();
    }

    @Override
    public String getRol() {
        return "CLIENTE";
    }

    public void agregarPedido(Pedido pedido) {
        historialPedidos.add(pedido);
    }

    public ArrayList<Pedido> getHistorialPedidos() { return historialPedidos; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
