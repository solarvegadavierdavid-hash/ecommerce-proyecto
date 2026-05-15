package modelo;

import java.io.Serializable;

/**
 * Interface Pagable — Abstracción (interfaz)
 */
public interface Pagable {
    boolean procesarPago(double monto);
    String getDescripcionPago();
}
