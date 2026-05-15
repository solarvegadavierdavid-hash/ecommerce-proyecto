package modelo;

/**
 * Administrador extends Usuario — Herencia
 */
public class Administrador extends Usuario {

    private static final long serialVersionUID = 1L;

    private String nivel; // "SUPERADMIN" o "ADMIN"

    public Administrador(String id, String nombre, String email, String password, String nivel) {
        super(id, nombre, email, password);
        this.nivel = nivel;
    }

    @Override
    public String getRol() {
        return "ADMIN";
    }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
}
