package datos;

import modelo.*;
import java.io.*;
import java.util.ArrayList;

/**
 * GestorDatos — Persistencia mediante serialización Java (.dat)
 * Los datos sobreviven al cierre de la aplicación
 */
public class GestorDatos {

    private static final String RUTA_PRODUCTOS = "datos/productos.dat";
    private static final String RUTA_USUARIOS  = "datos/usuarios.dat";
    private static final String RUTA_PEDIDOS   = "datos/pedidos.dat";

    public GestorDatos() {
        // Crear carpeta datos si no existe
        new File("datos").mkdirs();
    }

    // ─── PRODUCTOS ──────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public ArrayList<Producto> cargarProductos() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(RUTA_PRODUCTOS))) {
            return (ArrayList<Producto>) ois.readObject();
        } catch (FileNotFoundException e) {
            return productosIniciales();
        } catch (Exception e) {
            System.err.println("Error cargando productos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardarProductos(ArrayList<Producto> productos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(RUTA_PRODUCTOS))) {
            oos.writeObject(productos);
        } catch (IOException e) {
            System.err.println("Error guardando productos: " + e.getMessage());
        }
    }

    // ─── USUARIOS ───────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public ArrayList<Usuario> cargarUsuarios() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(RUTA_USUARIOS))) {
            return (ArrayList<Usuario>) ois.readObject();
        } catch (FileNotFoundException e) {
            return usuariosIniciales();
        } catch (Exception e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardarUsuarios(ArrayList<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(RUTA_USUARIOS))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Error guardando usuarios: " + e.getMessage());
        }
    }

    // ─── PEDIDOS ────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public ArrayList<Pedido> cargarPedidos() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(RUTA_PEDIDOS))) {
            return (ArrayList<Pedido>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error cargando pedidos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardarPedidos(ArrayList<Pedido> pedidos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(RUTA_PEDIDOS))) {
            oos.writeObject(pedidos);
        } catch (IOException e) {
            System.err.println("Error guardando pedidos: " + e.getMessage());
        }
    }

    // ─── DATOS INICIALES (primera vez) ──────────────────────────────────────

    private ArrayList<Producto> productosIniciales() {
        ArrayList<Producto> lista = new ArrayList<>();
        lista.add(new ProductoFisico("P001", "Camiseta Básica",
                "Camiseta 100% algodón, tallas S-XL", 29990, 50, "Ropa", 0.3, "Blanco"));
        lista.add(new ProductoFisico("P002", "Zapatos Deportivos",
                "Tenis running con suela reforzada", 89990, 30, "Calzado", 0.8, "Negro"));
        lista.add(new ProductoFisico("P003", "Mochila Escolar",
                "Mochila 30L resistente al agua", 59990, 20, "Accesorios", 0.5, "Azul"));
        lista.add(new ProductoFisico("P004", "Audífonos Bluetooth",
                "Audífonos inalámbricos 20h batería", 119990, 15, "Electrónica", 0.2, "Gris"));
        lista.add(new ProductoDigital("P005", "Curso de Java",
                "Curso completo Java POO - 40 horas", 49990, 999, "Educación",
                "https://cursos.ejemplo.com/java", "MP4"));
        lista.add(new ProductoFisico("P006", "Teclado Mecánico",
                "Teclado gaming RGB switches blue", 149990, 10, "Electrónica", 0.9, "Negro"));
        guardarProductos(lista);
        return lista;
    }

    private ArrayList<Usuario> usuariosIniciales() {
        ArrayList<Usuario> lista = new ArrayList<>();
        lista.add(new Administrador("U001", "Admin Sistema", "admin@tienda.com", "admin123", "SUPERADMIN"));
        lista.add(new Cliente("U002", "María García", "maria@email.com", "123456",
                "Calle 45 #12-30", "3001234567"));
        lista.add(new Cliente("U003", "Carlos López", "carlos@email.com", "123456",
                "Av. Principal #8-15", "3109876543"));
        guardarUsuarios(lista);
        return lista;
    }
}
