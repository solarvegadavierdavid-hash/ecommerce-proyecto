package modelo;

import java.io.Serializable;

/**
 * Clase abstracta Usuario — Abstracción y base de herencia
 * Cliente extends Usuario / Administrador extends Usuario
 */
public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    // Encapsulación: atributos privados
    private String id;
    private String nombre;
    private String email;
    private String password;

    public Usuario(String id, String nombre, String email, String password) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    // Método abstracto — polimorfismo
    public abstract String getRol();

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "[" + getRol() + "] " + nombre + " (" + email + ")";
    }
}
