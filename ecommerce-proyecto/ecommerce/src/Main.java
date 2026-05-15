import datos.Controlador;
import ui.VentanaLogin;

import javax.swing.*;

/**
 * Main — Punto de entrada del sistema ecommerce
 * Proyecto Final Java POO
 */
public class Main {

    public static void main(String[] args) {
        // Look and Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Usar L&F por defecto si falla
        }

        // Iniciar en el hilo de UI de Swing
        SwingUtilities.invokeLater(() -> {
            Controlador controlador = new Controlador();
            VentanaLogin login = new VentanaLogin(controlador);
            login.setVisible(true);
        });
    }
}
