package modelo;

/**
 * ProductoDigital extends Producto — Herencia + Polimorfismo
 */
public class ProductoDigital extends Producto {

    private static final long serialVersionUID = 1L;
    private String urlDescarga;
    private String formato; // PDF, MP3, ZIP, etc.

    public ProductoDigital(String id, String nombre, String descripcion,
                           double precio, int stock, String categoria,
                           String urlDescarga, String formato) {
        super(id, nombre, descripcion, precio, stock, categoria);
        this.urlDescarga = urlDescarga;
        this.formato = formato;
    }

    @Override
    public String getTipo() {
        return "DIGITAL";
    }

    public String getUrlDescarga() { return urlDescarga; }
    public void setUrlDescarga(String urlDescarga) { this.urlDescarga = urlDescarga; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }
}
