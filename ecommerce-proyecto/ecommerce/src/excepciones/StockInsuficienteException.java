package excepciones;

/**
 * Excepción personalizada para stock insuficiente — Manejo de excepciones
 */
public class StockInsuficienteException extends Exception {

    private int stockDisponible;
    private int cantidadSolicitada;

    public StockInsuficienteException(int stockDisponible, int cantidadSolicitada) {
        super("Stock insuficiente. Solicitado: " + cantidadSolicitada
                + ", Disponible: " + stockDisponible);
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public int getStockDisponible() { return stockDisponible; }
    public int getCantidadSolicitada() { return cantidadSolicitada; }
}
