package modelo;

/**
 * ProductoFisico extends Producto — Herencia + Polimorfismo
 */
public class ProductoFisico extends Producto {

    private static final long serialVersionUID = 1L;
    private double peso;     // en kg
    private String color;

    public ProductoFisico(String id, String nombre, String descripcion,
                          double precio, int stock, String categoria,
                          double peso, String color) {
        super(id, nombre, descripcion, precio, stock, categoria);
        this.peso = peso;
        this.color = color;
    }

    @Override
    public String getTipo() {
        return "FÍSICO";
    }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
