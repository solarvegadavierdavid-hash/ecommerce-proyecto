package modelo;

import java.io.Serializable;

/**
 * Clase abstracta Pago — Abstracción
 * Implementa Pagable (interfaz)
 */
public abstract class Pago implements Pagable, Serializable {

    private static final long serialVersionUID = 1L;

    private String idPago;
    private double monto;
    private String estado; // "PENDIENTE", "APROBADO", "RECHAZADO"

    public Pago(String idPago, double monto) {
        this.idPago = idPago;
        this.monto = monto;
        this.estado = "PENDIENTE";
    }

    public String getIdPago() { return idPago; }
    public double getMonto() { return monto; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

// ─── PagoTarjeta ────────────────────────────────────────────────────────────
class PagoTarjeta extends Pago {

    private static final long serialVersionUID = 1L;

    private String numeroTarjeta; // últimos 4 dígitos
    private String titular;

    public PagoTarjeta(String idPago, double monto, String numeroTarjeta, String titular) {
        super(idPago, monto);
        this.numeroTarjeta = numeroTarjeta;
        this.titular = titular;
    }

    @Override
    public boolean procesarPago(double monto) {
        // Simulación de pago con tarjeta
        setEstado("APROBADO");
        return true;
    }

    @Override
    public String getDescripcionPago() {
        return "Tarjeta **** " + numeroTarjeta + " | Titular: " + titular;
    }
}

// ─── PagoEfectivo ────────────────────────────────────────────────────────────
class PagoEfectivo extends Pago {

    private static final long serialVersionUID = 1L;

    private double montoEntregado;

    public PagoEfectivo(String idPago, double monto, double montoEntregado) {
        super(idPago, monto);
        this.montoEntregado = montoEntregado;
    }

    @Override
    public boolean procesarPago(double monto) {
        if (montoEntregado >= monto) {
            setEstado("APROBADO");
            return true;
        }
        setEstado("RECHAZADO");
        return false;
    }

    @Override
    public String getDescripcionPago() {
        return "Efectivo | Entregado: $" + String.format("%.2f", montoEntregado)
                + " | Cambio: $" + String.format("%.2f", montoEntregado - getMonto());
    }
}

// ─── PagoTransferencia ───────────────────────────────────────────────────────
class PagoTransferencia extends Pago {

    private static final long serialVersionUID = 1L;

    private String banco;
    private String numeroReferencia;

    public PagoTransferencia(String idPago, double monto, String banco, String numeroReferencia) {
        super(idPago, monto);
        this.banco = banco;
        this.numeroReferencia = numeroReferencia;
    }

    @Override
    public boolean procesarPago(double monto) {
        setEstado("APROBADO");
        return true;
    }

    @Override
    public String getDescripcionPago() {
        return "Transferencia | Banco: " + banco + " | Ref: " + numeroReferencia;
    }
}
